package com.slxk.hounddog.mvp.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.di.component.DaggerPenetrateComponent;
import com.slxk.hounddog.mvp.contract.PenetrateContract;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.DeviceDetailResultBean;
import com.slxk.hounddog.mvp.model.bean.PenetrateHistoryResultBean;
import com.slxk.hounddog.mvp.model.bean.PenetrateParamResultBean;
import com.slxk.hounddog.mvp.model.bean.PenetrateResultBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceDetailPutBean;
import com.slxk.hounddog.mvp.model.putbean.PenetrateHistoryPutBean;
import com.slxk.hounddog.mvp.model.putbean.PenetratePutBean;
import com.slxk.hounddog.mvp.presenter.PenetratePresenter;
import com.slxk.hounddog.mvp.ui.adapter.PenetrateHistoryAdapter;
import com.slxk.hounddog.mvp.ui.view.MyLoadMoreView;
import com.slxk.hounddog.mvp.utils.DateUtils;
import com.slxk.hounddog.mvp.utils.DeviceUtils;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.SMSListPopupwindow;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/25/2021 14:15
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class PenetrateActivity extends BaseActivity<PenetratePresenter> implements PenetrateContract.View, View.OnClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srl_view)
    SwipeRefreshLayout srlView;

    private View headerView;
    private LinearLayout llCommandOptions; // 指令选项
    private EditText edtValue; // 指令
    private TextView tvClear;
    private TextView tvConfirmSend;

    private PenetrateHistoryAdapter mAdapter;
    private ArrayList<PenetrateHistoryResultBean.ItemsBean> resultBeans;
    private SMSListPopupwindow smsListPopupwindow;

    private long lastImei = 0; // 获取到上一页日志的最后一条信息的imei号
    private long lastTime = 0; // 获取到上一页日志的最后一条信息的时间戳
    private int lastType = 0; // 获取到上一页日志的最后一条信息的类型
    private int limitSize = 20; // 限制条数，不填默认20条
    private ArrayList<String> mSimeis; // 如果查询特定账号的日志，可以对特定的设备号进行查询
    private ArrayList<Integer> mTypes; // 筛选指令类型，不填返回全部
    private int mProtocol = 1; // 设备协议

    private String mImei;
    private String mSimei;
    private static final String Intent_Imei_Key = "imei_key";
    private static final String Intent_Simei_Key = "simei_key";

    public static Intent newInstance(String imei, String simei) {
        Intent intent = new Intent(MyApplication.getMyApp(), PenetrateActivity.class);
        intent.putExtra(Intent_Imei_Key, imei);
        intent.putExtra(Intent_Simei_Key, simei);
        return intent;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPenetrateComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_penetrate;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.penetrate));
        resultBeans = new ArrayList<>();
        mSimeis = new ArrayList<>();
        mTypes = new ArrayList<>();
        mImei = getIntent().getStringExtra(Intent_Imei_Key);
        mSimei = getIntent().getStringExtra(Intent_Simei_Key);
        mSimeis.add(mSimei);

        headerView = LayoutInflater.from(this).inflate(R.layout.layout_penetrate_header, null, false);
        llCommandOptions = headerView.findViewById(R.id.ll_command_options);
        edtValue = headerView.findViewById(R.id.edt_value);
        tvClear = headerView.findViewById(R.id.tv_clear);
        tvConfirmSend = headerView.findViewById(R.id.tv_confirm_send);
        llCommandOptions.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        tvConfirmSend.setOnClickListener(this);

        mTypes.add(1089);
        mTypes.add(20000);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        srlView.setColorSchemeResources(R.color.color_00B4B7, R.color.color_00B4B7);
        srlView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lastTime = 0;
                lastImei = 0;
                lastType = 0;
                getPenetrateHistory(false, true);
            }
        });

        mAdapter = new PenetrateHistoryAdapter(R.layout.item_penetrate, resultBeans);
        recyclerView.setAdapter(mAdapter);
        mAdapter.addHeaderView(headerView);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                StringBuilder stringBuilder = new StringBuilder();
                PenetrateHistoryResultBean.ItemsBean itemsBean = resultBeans.get(position);
                if (itemsBean.getType() == 20000) {
                    PenetrateParamResultBean bean = DeviceUtils.paramPenetrateParamData(resultBeans.get(position).getParam());
                    stringBuilder.append(getString(R.string.instruction)).append(bean.getContent()).append("\n");
                } else {
                    stringBuilder.append(getString(R.string.instruction)).append(itemsBean.getParam()).append("\n");
                }
                stringBuilder.append(getString(R.string.reply)).append(itemsBean.getBack_result()).append("\n");
                stringBuilder.append(getString(R.string.time)).append(DateUtils.timeConversionDate_three(String.valueOf(itemsBean.getTime()))).append("\n");
                stringBuilder.append(getString(R.string.account)).append(itemsBean.getAccount());
                onCopyToClipboard(stringBuilder.toString());
            }
        });

        LoadMoreView loadMoreView = new MyLoadMoreView();
        mAdapter.setLoadMoreView(loadMoreView);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getPenetrateHistory(false, false);
            }
        }, recyclerView);

        getDeviceDetailInfo();
    }

    /**
     * 获取设备详细信息
     */
    private void getDeviceDetailInfo() {
        DeviceDetailPutBean.ParamsBean paramsBean = new DeviceDetailPutBean.ParamsBean();
        paramsBean.setSimei(mSimei);

        DeviceDetailPutBean bean = new DeviceDetailPutBean();
        bean.setFunc(ModuleValueService.Fuc_For_Device_Detail);
        bean.setModule(ModuleValueService.Module_For_Device_Detail);
        bean.setParams(paramsBean);

        if (getPresenter() != null){
            getPresenter().getDeviceDetailInfo(bean);
        }
    }

    /**
     * 获取历史回复记录数据
     *
     * @param isShow
     * @param isRefresh
     */
    private void getPenetrateHistory(boolean isShow, boolean isRefresh) {
        PenetrateHistoryPutBean.ParamsBean paramsBean = new PenetrateHistoryPutBean.ParamsBean();
        paramsBean.setLimit_size(limitSize);
        paramsBean.setType(mTypes);
        if (mSimeis.size() > 0){
            paramsBean.setSimeis(mSimeis);
        }
        if (lastImei != 0) {
            paramsBean.setLast_imei(lastImei);
        }
        if (lastTime != 0) {
            paramsBean.setLast_time(lastTime);
        }
        if (lastType != 0) {
            paramsBean.setLast_type(lastType);
        }

        PenetrateHistoryPutBean bean = new PenetrateHistoryPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Penetrate_History);
        bean.setModule(ModuleValueService.Module_For_Penetrate_History);

        if (getPresenter() != null) {
            getPresenter().getPenetrateHistory(bean, isShow, isRefresh);
        }
    }

    /**
     * 复制文本到粘贴管理器
     *
     * @param content 复制内容
     */
    private void onCopyToClipboard(String content) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）,其他的还有
        ClipData clipData = ClipData.newPlainText("zeroenterprise", content);

        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
        ToastUtils.show(getString(R.string.copy_success));
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
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View v) {
        if (Utils.isButtonQuickClick()){
            switch (v.getId()) {
                case R.id.ll_command_options:
                    onCommandOptionsSelect();
                    break;
                case R.id.tv_clear:
                    edtValue.setText("");
                    break;
                case R.id.tv_confirm_send:
                    onSendConfirm();
                    break;
            }
        }
    }

    /**
     * 透传指令选择
     */
    private void onCommandOptionsSelect() {
        if (smsListPopupwindow != null && smsListPopupwindow.isShowing()) {
            smsListPopupwindow.dismiss();
        } else {
            smsListPopupwindow = new SMSListPopupwindow(this);
            smsListPopupwindow.setOnSMSSelect(new SMSListPopupwindow.onSMSSelect() {
                @Override
                public void onSMSSelect(String content) {
                    edtValue.setText(content);
                }
            });
            smsListPopupwindow.showAsDropDown(llCommandOptions);
        }
    }

    /**
     * 确认发送透传指令
     */
    private void onSendConfirm(){
        String content = edtValue.getText().toString();
        if (TextUtils.isEmpty(content)){
            ToastUtils.show(getString(R.string.command_options_hint));
            return;
        }
        PenetratePutBean.ParamsBean paramsBean = new PenetratePutBean.ParamsBean();
        paramsBean.setContent(content);
        if (!TextUtils.isEmpty(mSimei)){
            paramsBean.setSimei(mSimei);
        }

        PenetratePutBean bean = new PenetratePutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Penetrate_Send);
        bean.setModule(ModuleValueService.Module_For_Penetrate_Send);

        if (getPresenter() != null){
            getPresenter().submitPenetrateSend(bean);
        }
    }

    @Override
    public void getPenetrateHistorySuccess(PenetrateHistoryResultBean bean, boolean isRefresh) {
        if (isRefresh) {
            resultBeans.clear();
        }
        if (bean.getItems() != null && bean.getItems().size() > 0) {
            resultBeans.addAll(bean.getItems());
            lastType = bean.getItems().get(bean.getItems().size() - 1).getType();
            lastTime = bean.getItems().get(bean.getItems().size() - 1).getTime();
            lastImei = bean.getItems().get(bean.getItems().size() - 1).getImei();
        }
        mAdapter.notifyDataSetChanged();
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

    @Override
    public void submitPenetrateSendSuccess(PenetrateResultBean penetrateResultBean) {
        ToastUtils.show(getString(R.string.send_success));
        tvConfirmSend.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastTime = 0;
                lastImei = 0;
                lastType = 0;
                getPenetrateHistory(true, true);
            }
        }, 2000);
    }

    @Override
    public void getDeviceDetailInfoSuccess(DeviceDetailResultBean deviceDetailResultBean) {
        mProtocol = deviceDetailResultBean.getProtocol();
        if (mProtocol == 110){
            llCommandOptions.setVisibility(View.GONE);
        }
        getPenetrateHistory(true, true);
    }

    @Override
    public void getDeviceDetailInfoFail() {
        getPenetrateHistory(true, true);
    }

}
