package com.slxk.hounddog.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.db.DeviceModel;
import com.slxk.hounddog.di.component.DaggerDeviceListComponent;
import com.slxk.hounddog.mvp.contract.DeviceListContract;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.AlertBean;
import com.slxk.hounddog.mvp.model.bean.DeviceBaseResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListForManagerResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceLocationInfoBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListForManagerPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListPutBean;
import com.slxk.hounddog.mvp.model.putbean.RemoveDevicePutBean;
import com.slxk.hounddog.mvp.presenter.DeviceListPresenter;
import com.slxk.hounddog.mvp.ui.activity.AddDeviceActivity;
import com.slxk.hounddog.mvp.ui.adapter.DeviceListAdapter;
import com.slxk.hounddog.mvp.ui.view.MyLoadMoreView;
import com.slxk.hounddog.mvp.utils.BroadcastReceiverUtil;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.DateUtils;
import com.slxk.hounddog.mvp.utils.DeviceUtils;
import com.slxk.hounddog.mvp.utils.ResultDataUtils;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.AlertAppDialog;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/21/2021 14:28
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class DeviceListFragment extends BaseFragment<DeviceListPresenter> implements DeviceListContract.View,
        DeviceListAdapter.onDeviceChange {

    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.view_all)
    View viewAll;
    @BindView(R.id.ll_all)
    LinearLayout llAll;
    @BindView(R.id.tv_on_line)
    TextView tvOnLine;
    @BindView(R.id.view_on_line)
    View viewOnLine;
    @BindView(R.id.ll_on_line)
    LinearLayout llOnLine;
    @BindView(R.id.tv_static)
    TextView tvStatic;
    @BindView(R.id.view_static)
    View viewStatic;
    @BindView(R.id.ll_static)
    LinearLayout llStatic;
    @BindView(R.id.tv_on_off)
    TextView tvOnOff;
    @BindView(R.id.view_on_off)
    View viewOnOff;
    @BindView(R.id.ll_on_off)
    LinearLayout llOnOff;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.srl_view)
    SwipeRefreshLayout srlView;

    private ArrayList<DeviceModel> mDeviceAllBeans; // 全部设备
    private ArrayList<DeviceModel> mDeviceOnLineBeans; // 在线设备
    private ArrayList<DeviceModel> mDeviceStaticBeans; // 静止设备
    private ArrayList<DeviceModel> mDeviceOnOffBeans; // 离线设备
    private ArrayList<DeviceModel> mDeviceBeans; // 展示的设备列表
    private ArrayList<DeviceModel> pubDeviceBeans; // 获取的数据
    private DeviceListAdapter mAdapter;
    private int mDeviceState = 0; // 0：全部，1：在线，2：静止，3：离线
    private int total; //设备总个数
    private static final int ADD_DEVICE = 10; // 添加设备
    private static final int mDeviceLimitSize = 1000; // 限定设备数量,默认100
    private static final int mGroupDeviceLimitSize = 100; // 限制分组下设备获取数量

    private int all_total = 0; // 设备总数,get_total值为true的时候返回
    private int down_total = 0; // 下线数,get_total值为true的时候返回
    private int line_total = 0; // 在线数,get_total值为true的时候返回
    private int sleep_total = 0; // 睡眠数,get_total值为true的时候返回

    private ArrayList<String> mSimeiBeans; // 选中的设备号,限制数量1000，simei,sfamilyid,sgid三选一

    private ChangePageReceiver receiver; // 注册广播接收器

    public static DeviceListFragment newInstance() {
        DeviceListFragment fragment = new DeviceListFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerDeviceListComponent //如找不到该类,请编译一下项�?
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_list, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mDeviceBeans = new ArrayList<>();
        mDeviceAllBeans = new ArrayList<>();
        mDeviceOnLineBeans = new ArrayList<>();
        mDeviceStaticBeans = new ArrayList<>();
        mDeviceOnOffBeans = new ArrayList<>();
        mSimeiBeans = new ArrayList<>();
        pubDeviceBeans = new ArrayList<>();

        ivAdd.setVisibility(ConstantValue.isAccountLogin() ? View.VISIBLE : View.GONE);

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        srlView.setColorSchemeResources(R.color.color_00C8F8, R.color.color_00C8F8);
        srlView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onDeviceListForService(true, false);
            }
        });


        mAdapter = new DeviceListAdapter(R.layout.item_device_list, mDeviceBeans);
        recyclerview.setAdapter(mAdapter);
        mAdapter.setDeviceChange(this);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (Utils.isButtonQuickClick()) {
                    BroadcastReceiverUtil.onDeviceOperate(getContext(), 3, 0, mDeviceBeans.get(position).getImei(), 0, null);
                    BroadcastReceiverUtil.showMainPage(getContext(), 0);
                }
            }
        });

        //加载更多
        LoadMoreView loadMoreView = new MyLoadMoreView();
        mAdapter.setLoadMoreView(loadMoreView);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                onDeviceListForService(false, true);
            }
        }, recyclerview);

        // 注册一个广播接收器，用于接收从首页跳转到报警消息页面的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("device_list");
        receiver = new ChangePageReceiver();
        //注册切换页面广播接收者
        LocalBroadcastManager.getInstance(MyApplication.getMyApp()).registerReceiver(receiver, filter);

        onDeviceListForService(false, false);
    }

    /**
     * 页面切换广播，广播接收器
     */
    private class ChangePageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            onDeviceListForService(false, false);
        }
    }

    /**
     * 向服务器获取设备列表
     */
    private void onDeviceListForService(boolean isRefresh , boolean isLoadmore) {
        // 判断是账号登录还是设备号登录，分开走不通的流程
        if (ConstantValue.isAccountLogin()) {
            getDeviceListForGroup(isRefresh, isLoadmore);
        } else {
            getDeviceList(isRefresh, isLoadmore);
        }
    }

    /**
     * 获取设备列表 - 设备号登录
     */
    private void getDeviceList(boolean isRefresh, boolean isLoadmore) {
        DeviceListPutBean.ParamsBean paramsBean = new DeviceListPutBean.ParamsBean();
        paramsBean.setLimit_size(mDeviceLimitSize);
        if(isLoadmore){
            if(mDeviceAllBeans.size() > 0){
                paramsBean.setLast_sgid(String.valueOf(mDeviceAllBeans.get(mDeviceAllBeans.size()-1).getSgid()));
                paramsBean.setLast_simei(String.valueOf(mDeviceAllBeans.get(mDeviceAllBeans.size()-1).getSimei()));
            }
        }

        DeviceListPutBean bean = new DeviceListPutBean();
        bean.setFunc(ModuleValueService.Fuc_For_Device_List);
        bean.setModule(ModuleValueService.Module_For_Device_List);
        bean.setParams(paramsBean);

        getPresenter().getDeviceList(bean, isRefresh, isLoadmore);
    }

    /**
     * 获取分组下的设备列表 - 账号登录
     */
    private void getDeviceListForGroup(boolean isRefresh, boolean isLoadmore) {
        DeviceListForManagerPutBean.ParamsBean paramsBean = new DeviceListForManagerPutBean.ParamsBean();
        paramsBean.setLimit_size(mGroupDeviceLimitSize);
        paramsBean.setFamilyid(ConstantValue.getFamilySid());
        paramsBean.setGet_total(true);

        if(isLoadmore){
            if(mDeviceAllBeans.size() > 0){
                paramsBean.setLast_sgid(String.valueOf(mDeviceAllBeans.get(mDeviceAllBeans.size()-1).getSgid()));
                paramsBean.setLast_simei(String.valueOf(mDeviceAllBeans.get(mDeviceAllBeans.size()-1).getSimei()));
            }
        }

        //传入设备状态
        switch (mDeviceState) {
            case 0:

                break;
            case 1:
                paramsBean.setState("e_line_on");
                break;
            case 2:
                paramsBean.setState("e_line_sleep");
                break;
            case 3:
                paramsBean.setState("e_line_down");
                break;
        }

        DeviceListForManagerPutBean bean = new DeviceListForManagerPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Device_List_For_Group);
        bean.setModule(ModuleValueService.Module_For_Device_List_For_Group);

        getPresenter().getDeviceListForGroup(bean, isRefresh, isLoadmore);
    }

    /**
     * 筛选设备列表，区分在线，离线，静止设备
     */
    private void onScreenDeviceList() {

        // 判断是账号登录还是设备号登录，分开走不通的流程
        if (ConstantValue.isAccountLogin()) {
            switch (mDeviceState) {
                case 0:
                    mDeviceAllBeans = pubDeviceBeans;
                    break;
                case 1:
                    mDeviceOnLineBeans = pubDeviceBeans;
                    break;
                case 2:
                    mDeviceStaticBeans = pubDeviceBeans;
                    break;
                case 3:
                    mDeviceOnOffBeans = pubDeviceBeans;
                    break;
            }
        } else {

            mDeviceOnLineBeans.clear();
            mDeviceStaticBeans.clear();
            mDeviceOnOffBeans.clear();
            for (DeviceModel model : mDeviceAllBeans) {
                switch (model.getState()) {
                    case ResultDataUtils.Device_State_Line_Down:
                        mDeviceOnOffBeans.add(model);
                        break;
                    case ResultDataUtils.Device_State_Line_On:
                        mDeviceOnLineBeans.add(model);
                        break;
                    case ResultDataUtils.Device_State_Line_Sleep:
                        mDeviceStaticBeans.add(model);
                        break;
                }
            }

            all_total = mDeviceAllBeans.size();
            line_total = mDeviceOnLineBeans.size();
            sleep_total = mDeviceStaticBeans.size();
            down_total = mDeviceOnOffBeans.size();
        }

        onNumberShow();
        onShowDeviceList();
    }

    /**
     * 显示设备数量
     */
    @SuppressLint("SetTextI18n")
    private void onNumberShow() {
        tvAll.setText(getString(R.string.all) + "(" + all_total + ")");
        tvOnLine.setText(getString(R.string.state_line_on) + "(" + line_total + ")");
        tvStatic.setText(getString(R.string.state_line_static) + "(" + sleep_total + ")");
        tvOnOff.setText(getString(R.string.state_line_down) + "(" + down_total + ")");
    }

    /**
     * 显示的设备列表，0：全部，1：在线，2：静止，3：离线
     */
    private void onShowDeviceList() {
        mDeviceBeans.clear();
        switch (mDeviceState) {
            case 0:
                mDeviceBeans.addAll(mDeviceAllBeans);
                break;
            case 1:
                mDeviceBeans.addAll(mDeviceOnLineBeans);
                break;
            case 2:
                mDeviceBeans.addAll(mDeviceStaticBeans);
                break;
            case 3:
                mDeviceBeans.addAll(mDeviceOnOffBeans);
                break;
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 显示的设备列表，0：全部，1：在线，2：静止，3：离线
     */
    private void onSelectDeviceState() {
        tvAll.setTextColor(getResources().getColor(R.color.color_999999));
        viewAll.setVisibility(View.INVISIBLE);
        tvOnLine.setTextColor(getResources().getColor(R.color.color_999999));
        viewOnLine.setVisibility(View.INVISIBLE);
        tvStatic.setTextColor(getResources().getColor(R.color.color_999999));
        viewStatic.setVisibility(View.INVISIBLE);
        tvOnOff.setTextColor(getResources().getColor(R.color.color_999999));
        viewOnOff.setVisibility(View.INVISIBLE);
        switch (mDeviceState) {
            case 0:
                tvAll.setTextColor(getResources().getColor(R.color.color_00C8F8));
                viewAll.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvOnLine.setTextColor(getResources().getColor(R.color.color_00C8F8));
                viewOnLine.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvStatic.setTextColor(getResources().getColor(R.color.color_00C8F8));
                viewStatic.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvOnOff.setTextColor(getResources().getColor(R.color.color_00C8F8));
                viewOnOff.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick({R.id.iv_add, R.id.ll_all, R.id.ll_on_line, R.id.ll_static, R.id.ll_on_off})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()) {
            switch (view.getId()) {
                case R.id.iv_add:
                    onAddDevice();
                    break;
                case R.id.ll_all:
                    onCheckDeviceList(0);
                    break;
                case R.id.ll_on_line:
                    onCheckDeviceList(1);
                    break;
                case R.id.ll_static:
                    onCheckDeviceList(2);
                    break;
                case R.id.ll_on_off:
                    onCheckDeviceList(3);
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_DEVICE) {
                onDeviceListForService(false, false);
                BroadcastReceiverUtil.onDeviceOperate(getContext(), 1, 1, "", 0, null);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 添加设备
     */
    private void onAddDevice() {
        Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
        startActivityForResult(intent, ADD_DEVICE);
    }

    /**
     * 切换设备列表
     *
     * @param state
     */
    private void onCheckDeviceList(int state) {
        if (mDeviceState == state) {
            return;
        }
        mDeviceState = state;
        onSelectDeviceState();
        onShowDeviceList();

        onDeviceListForService(false,false);
    }

    @Override
    public void onDeviceDelete(String imei, String simei, String name) {
        if (ConstantValue.isAccountLogin()) {
            AlertBean bean = new AlertBean();
            bean.setTitle(getString(R.string.tip));
            if (!TextUtils.isEmpty(name)) {
                bean.setAlert(getString(R.string.delete_device_hint) + "(" + name + ")");
            } else {
                bean.setAlert(getString(R.string.delete_device_hint) + "(" + imei + ")");
            }
            AlertAppDialog dialog = new AlertAppDialog();
            dialog.show(getFragmentManager(), bean, new AlertAppDialog.onAlertDialogChange() {
                @Override
                public void onConfirm() {
                    submitDeleteDevice(imei, simei);
                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            ToastUtils.show(getString(R.string.delete_for_device_login_error));
        }
    }

    /**
     * 删除设备
     *
     * @param imei
     */
    private void submitDeleteDevice(String imei, String simei) {
        mSimeiBeans.clear();
        mSimeiBeans.add(simei);
        DeviceModel mModel = null;
        for (DeviceModel model : mDeviceBeans) {
            if (model.getImei().equals(imei)) {
                mModel = model;
                break;
            }
        }

        RemoveDevicePutBean.ParamsBean paramsBean = new RemoveDevicePutBean.ParamsBean();
        paramsBean.setSimei(mSimeiBeans);
        paramsBean.setDel_type(ResultDataUtils.Device_Delete_Parent);

        RemoveDevicePutBean bean = new RemoveDevicePutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Device_Remove);
        bean.setModule(ModuleValueService.Module_For_Device_Remove);

        showProgressDialog();

        getPresenter().submitDeleteDevice(bean, mModel);
    }

    @Override
    public void onDestroyView() {
        LocalBroadcastManager.getInstance(MyApplication.getMyApp()).unregisterReceiver(receiver);
        super.onDestroyView();
    }

    @Override
    public void getDeviceListSuccess(DeviceListResultBean deviceListResultBean ,boolean isLoadmore) {
        if(!isLoadmore) {
            mDeviceAllBeans.clear();
        }

        if (deviceListResultBean.getItems() != null && deviceListResultBean.getItems().size() > 0) {
            for (DeviceListResultBean.ItemsBean itemsBean : deviceListResultBean.getItems()) {
                DeviceLocationInfoBean bean = DeviceUtils.parseDeviceData(itemsBean.getLast_pos());
                DeviceModel model = new DeviceModel();
                model.setImei(String.valueOf(itemsBean.getImei()));
                model.setSimei(itemsBean.getSimei());
                model.setSgid(itemsBean.getSgid());
                model.setLat(bean.getLat());
                model.setLon(bean.getLon());
                model.setSpeed(bean.getSpeed());
                model.setTime(DateUtils.timeConversionDate_two(String.valueOf(bean.getTime())));
                model.setPower(itemsBean.getPower());
                model.setDevice_name(itemsBean.getCar_number());
                model.setState(itemsBean.getState());
                mDeviceAllBeans.add(model);
            }
        }
        mAdapter.notifyDataSetChanged();
        onScreenDeviceList();
    }

    @Override
    public void getDeviceListForGroupSuccess(DeviceListForManagerResultBean deviceListForManagerResultBean,boolean isLoadmore) {
        if(!isLoadmore) {
            pubDeviceBeans.clear();
        }

        all_total = deviceListForManagerResultBean.getAll_total();
        line_total = deviceListForManagerResultBean.getLine_total();
        sleep_total = deviceListForManagerResultBean.getSleep_total();
        down_total = deviceListForManagerResultBean.getDown_total();

        if (deviceListForManagerResultBean.getItems() != null && deviceListForManagerResultBean.getItems().size() > 0) {
            for (DeviceListForManagerResultBean.ItemsBean itemsBean : deviceListForManagerResultBean.getItems()) {
                DeviceLocationInfoBean bean = DeviceUtils.parseDeviceData(itemsBean.getLast_pos());
                DeviceModel model = new DeviceModel();
                model.setImei(String.valueOf(itemsBean.getImei()));
                model.setSimei(itemsBean.getSimei());
                model.setSgid(itemsBean.getSgid());
                model.setLat(bean.getLat());
                model.setLon(bean.getLon());
                model.setSpeed(bean.getSpeed());
                model.setTime(DateUtils.timeConversionDate_two(String.valueOf(bean.getTime())));
                model.setPower(itemsBean.getPower());
                model.setDevice_name(itemsBean.getCar_number());
                model.setState(itemsBean.getState());
                pubDeviceBeans.add(model);
            }
        }
        mAdapter.notifyDataSetChanged();
        onScreenDeviceList();
    }

    @Override
    public void submitDeleteDeviceSuccess(DeviceBaseResultBean deviceBaseResultBean, DeviceModel model) {
        ToastUtils.show(getString(R.string.delete_success));
        if (model != null) {
            mDeviceAllBeans.remove(model);
            BroadcastReceiverUtil.onDeviceOperate(getContext(), 1, 0, model.getImei(), 0, null);
        }

        onScreenDeviceList();
    }

    @Override
    public void onDismissProgressDialog() {
        dismissProgressDialog();
    }

    @Override
    public void finishRefresh() {
        srlView.setRefreshing(false);
    }

    @Override
    public void endLoadMore() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void setNoMore() {
        mAdapter.loadMoreEnd();
    }

    @Override
    public void endLoadFail() {
        mAdapter.loadMoreFail();
    }

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某�?Fragment 对象执行一些方�?
     * 建议在有多个需要与外界交互的方法时, 统一�?{@link Message}, 通过 what 字段来区分不同的方法, �?{@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操�? 可以起到分发的作�?     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周�? 如果调用 {@link #setData(Object)} 方法�?{@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用�?Presenter 的方�? 是会报空�? 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调�?{@link #setData(Object)}, �?{@link #initData(Bundle)} 中初始化就可以了
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * @param data 当不需要参数时 {@code data} 可以�?{@code null}
     */
    @Override
    public void setData(@Nullable Object data) {

    }


    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

}
