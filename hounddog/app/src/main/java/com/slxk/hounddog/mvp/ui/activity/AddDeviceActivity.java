package com.slxk.hounddog.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.huawei.hms.hmsscankit.OnResultCallback;
import com.huawei.hms.hmsscankit.RemoteView;
import com.huawei.hms.ml.scan.HmsScan;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.di.component.DaggerAddDeviceComponent;
import com.slxk.hounddog.mvp.contract.AddDeviceContract;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.AddDeviceInfoBean;
import com.slxk.hounddog.mvp.model.bean.DeviceBaseResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceGroupResultBean;
import com.slxk.hounddog.mvp.model.putbean.AddDevicePutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceGroupPutBean;
import com.slxk.hounddog.mvp.presenter.AddDevicePresenter;
import com.slxk.hounddog.mvp.ui.view.PasteEditText;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.DeviceFailDialog;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 添加设备
 * <p>
 * Created by MVPArmsTemplate on 12/25/2021 09:42
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class AddDeviceActivity extends BaseActivity<AddDevicePresenter> implements AddDeviceContract.View {

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.tv_sqr_number)
    TextView tvSqrNumber;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.edt_imei)
    PasteEditText edtImei;
    @BindView(R.id.btn_clear)
    Button btnClear;
    @BindView(R.id.tv_scan_tip)
    TextView tvScanTip;

    private ArrayList<AddDeviceInfoBean> infoBeans;
    private ArrayList<AddDevicePutBean.ParamsBean.InfoBean> submitBeans; // 上传的数据
    // 获取设备分组
    private ArrayList<DeviceGroupResultBean.GaragesBean> groupBeans; // 设备分组
    private int mLimitSize = 100; // 限制分组获取数量

    // 初始化扫码View
    private RemoteView remoteView;
    int mScreenWidth;
    int mScreenHeight;
    //The width and height of scan_view_finder is both 240 dp.
    final int SCAN_FRAME_SIZE = 240;
    private static final String TAG = "DefinedActivity";
    //Declare the key. It is used to obtain the value returned from Scan Kit.
    public static final String SCAN_RESULT = "scanResult";

    private boolean isAdd;  // 是否有相同的imei号
    private Vibrator vibrator; // 震动

    private static final int mSearchCountDownTime = 1; // 倒计时固定时长
    private int mSearchCountDown = mSearchCountDownTime; // 倒计时递减时长

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAddDeviceComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_add_device;//setContentView(id);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.add_device));

        submitBeans = new ArrayList<>();
        groupBeans = new ArrayList<>();
        infoBeans = new ArrayList<>();

        tvSqrNumber.setText(getString(R.string.sqr_number) + "：0");
        initScanView(savedInstanceState);
        initEditImei();
        getDeviceGroupList();
    }

    private void initEditImei() {
        edtImei.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchHandler.hasMessages(10)) {
                    searchHandler.removeMessages(10);
                    mSearchCountDown = mSearchCountDownTime;
                }
                searchHandler.sendEmptyMessageDelayed(10, 1000);
            }
        });
        // 监听粘贴动作
        edtImei.setOnPasteCallback(new PasteEditText.OnPasteCallback() {
            @Override
            public void onEditPaste() {
                if (searchHandler.hasMessages(10)) {
                    searchHandler.removeMessages(10);
                    mSearchCountDown = mSearchCountDownTime;
                }
                searchHandler.sendEmptyMessageDelayed(10, 1000);
            }
        });
    }

    /**
     * 获取车组织列表和车组列表
     */
    private void getDeviceGroupList() {
        if (ConstantValue.isAccountLogin()) {
            DeviceGroupPutBean.ParamsBean paramsBean = new DeviceGroupPutBean.ParamsBean();
            paramsBean.setF_limit_size(0);
            paramsBean.setG_limit_size(mLimitSize);
            paramsBean.setFamilyid(ConstantValue.getFamilySid());
            DeviceGroupPutBean bean = new DeviceGroupPutBean();
            bean.setParams(paramsBean);
            bean.setFunc(ModuleValueService.Fuc_For_Device_Group_List);
            bean.setModule(ModuleValueService.Module_For_Device_Group_List);

            if (getPresenter() != null) {
                getPresenter().getDeviceGroupList(bean);
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler searchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mSearchCountDown--;
            LogUtils.e("time=" + mSearchCountDown);
            if (mSearchCountDown <= 0) {
                mSearchCountDown = mSearchCountDownTime;
                onCopyPasteDevice();
            } else {
                searchHandler.sendEmptyMessageDelayed(10, 1000);
            }
        }
    };

    /**
     * 初始化扫码
     */
    private void initScanView(Bundle savedInstanceState) {
        //1. Obtain the screen density to calculate the viewfinder's rectangle.
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final float density = dm.density;
        //2. Obtain the screen size.
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;

        int scanFrameSize = (int) (SCAN_FRAME_SIZE * density);
        //3. Calculate the viewfinder's rectangle, which in the middle of the layout.
        //Set the scanning area. (Optional. Rect can be null. If no settings are specified, it will be located in the middle of the layout.)
        Rect rect = new Rect();
        rect.left = mScreenWidth / 2 - scanFrameSize / 2;
        rect.right = mScreenWidth / 2 + scanFrameSize / 2;
        rect.top = mScreenHeight / 2 - scanFrameSize / 2;
        rect.bottom = mScreenHeight / 2 + scanFrameSize / 2;

        //Initialize the RemoteView instance, and set callback for the scanning result.
        remoteView = new RemoteView.Builder().setContext(this).setBoundingBox(rect).setFormat(HmsScan.ALL_SCAN_TYPE).build();
        // Subscribe to the scanning result callback event.
        remoteView.setOnResultCallback(new OnResultCallback() {
            @Override
            public void onResult(HmsScan[] result) {
                //Check the result.
                if (result != null && result.length > 0 && result[0] != null && !TextUtils.isEmpty(result[0].getOriginalValue())) {
                    setResultStr(result[0].getOriginalValue());
                }
            }
        });
        // Load the customized view to the activity.
        remoteView.onCreate(savedInstanceState);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        frameLayout.addView(remoteView, params);
        // Set the back, photo scanning, and flashlight operations.

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
    }

    private void setResultStr(String result) {
        if (vibrator != null) vibrator.vibrate(300);
        if (result.matches("^[0-9]*$")) { // 扫描条形码或扫描出来的为全数字编号
            // 扫描争取，处理下一步操作
            if (infoBeans.size() >= 100) {
                ToastUtils.show(getString(R.string.scan_qr_code_number_error));
                return;
            }
            setDataForResult(result);
        } else if (result.startsWith("https://") || result.startsWith("http://")) { // 扫描内容是url地址
            ToastUtils.show(getString(R.string.scan_qr_error));
        } else { // 扫描剩余类型
            ToastUtils.show(getString(R.string.scan_qr_error));
        }
    }

    /**
     * 添加设备扫码处理结果
     *
     * @param result
     */
    @SuppressLint("SetTextI18n")
    private void setDataForResult(String result) {
        isAdd = false;
        for (AddDeviceInfoBean bean : infoBeans) {
            if (bean.getImei().equals(result)) {
                isAdd = true;
                break;
            }
        }
        if (!isAdd) {
            AddDeviceInfoBean bean = new AddDeviceInfoBean();
            bean.setImei(result);
            infoBeans.add(0, bean);
            String edtValue = edtImei.getText().toString();
            if (!TextUtils.isEmpty(edtValue)) {
                edtImei.setText(edtValue + "\n" + result);
            } else {
                edtImei.setText(result);
            }
        }

        onDeviceNumber();
    }

    /**
     * 显示添加设备的数量
     */
    @SuppressLint("SetTextI18n")
    private void onDeviceNumber() {
        if (infoBeans.size() > 0) {
            tvSqrNumber.setText(getString(R.string.sqr_number) + "：" + infoBeans.size());
        } else {
            tvSqrNumber.setText(getString(R.string.sqr_number) + "：0");
        }
    }

    /**
     * 智能解析设备号码
     */
    @SuppressLint("SetTextI18n")
    private void onCopyPasteDevice() {
        infoBeans.clear();
        String edtDevice = edtImei.getText().toString();
        if (TextUtils.isEmpty(edtDevice)) {
            tvSqrNumber.setText(getString(R.string.sqr_number) + "：0");
            return;
        }
        // 开始循环数据
        String[] deviceList;
        if (edtDevice.contains("\n")) {
            deviceList = edtDevice.split("\n");
        } else if (edtDevice.contains("\r")) {
            deviceList = edtDevice.split("\r");
        } else {
            deviceList = edtDevice.split("@");
        }
        for (String s : deviceList) {
            // 判断是否为全数字编号
            String imei = s.trim();
            imei = imei.replace("\n", "");
            imei = imei.replace("\r", "");
            imei = imei.replaceAll(" ", "");
            AddDeviceInfoBean bean = new AddDeviceInfoBean();
            if (imei.matches("^[0-9]*$")) {
                bean.setImei(imei);
            }
            if (!TextUtils.isEmpty(bean.getImei())) {
                boolean isHasDevice = false; // 是否已经包含了这个设备
                for (AddDeviceInfoBean infoBean : infoBeans) {
                    if (bean.getImei().equals(infoBean.getImei())) {
                        isHasDevice = true;
                        break;
                    }
                }
                if (!isHasDevice) {
                    infoBeans.add(bean);
                }
            }
        }
        tvSqrNumber.setText(getString(R.string.sqr_number) + "：" + infoBeans.size());
    }

    /**
     * Call the lifecycle management method of the remoteView activity.
     */
    @Override
    protected void onStart() {
        super.onStart();
        remoteView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        remoteView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        remoteView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        remoteView.onDestroy();
        searchHandler.removeCallbacksAndMessages(null);
        searchHandler = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        remoteView.onStop();
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

    @OnClick({R.id.btn_clear, R.id.btn_save})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()){
            switch (view.getId()) {
                case R.id.btn_clear:
                    onClear();
                    break;
                case R.id.btn_save:
                    onSaveAdd();
                    break;
            }
        }
    }

    /**
     * 清空
     */
    private void onClear() {
        hideKeyboard(edtImei);
        infoBeans.clear();
        edtImei.setText("");
        onDeviceNumber();
    }

    /**
     * 提交保存添加
     */
    private void onSaveAdd() {
        if (groupBeans.size() == 0) {
            getDeviceGroupList();
        } else {
            onAddDeviceSave(groupBeans.get(0).getSid());
        }
    }

    /**
     * 提交保存
     *
     * @param sid 分组id
     */
    private void onAddDeviceSave(String sid) {
        if (infoBeans.size() == 0) {
            ToastUtils.show(getString(R.string.device_add_hint));
            return;
        }

        submitBeans.clear();
        for (int i = 0; i < infoBeans.size(); i++) {
            if (!TextUtils.isEmpty(infoBeans.get(i).getImei())) {
                if (infoBeans.get(i).getImei().length() >= 19) {
                    ToastUtils.show(infoBeans.get(i).getImei() + "  " + getString(R.string.imei_length_error));
                    continue;
                }
                AddDevicePutBean.ParamsBean.InfoBean bean = new AddDevicePutBean.ParamsBean.InfoBean();
                bean.setImei(Long.parseLong(infoBeans.get(i).getImei()));
                submitBeans.add(bean);
            }
        }
        if (submitBeans.size() == 0) {
            ToastUtils.show(getString(R.string.device_add_input_hint));
            return;
        }
        AddDevicePutBean.ParamsBean paramsBean = new AddDevicePutBean.ParamsBean();
        paramsBean.setInfo(submitBeans);
        paramsBean.setSgid(sid);

        AddDevicePutBean bean = new AddDevicePutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Add_Device);
        bean.setModule(ModuleValueService.Module_For_Add_Device);

        if (getPresenter() != null) {
            getPresenter().submitAddDevice(bean);
        }
    }

    @Override
    public void getDeviceGroupListSuccess(DeviceGroupResultBean deviceGroupResultBean) {
        if (deviceGroupResultBean.getGarages() != null && deviceGroupResultBean.getGarages().size() > 0) {
            groupBeans.addAll(deviceGroupResultBean.getGarages());
        }
    }

    @Override
    public void submitAddDeviceSuccess(DeviceBaseResultBean deviceBaseResultBean) {
        setResult(Activity.RESULT_OK);
        edtImei.setText("");
        if (deviceBaseResultBean.getFail_items() != null && deviceBaseResultBean.getFail_items().size() > 0) {
            DeviceFailDialog dialog = new DeviceFailDialog();
            dialog.show(getSupportFragmentManager(), deviceBaseResultBean.getFail_items(), 0);
            if (submitBeans.size() > deviceBaseResultBean.getFail_items().size()) {
                ToastUtils.show(getString(R.string.add_success));
                setResult(Activity.RESULT_OK);
            }
        } else {
            ToastUtils.show(getString(R.string.add_success));
            setResult(Activity.RESULT_OK);
        }
    }

}
