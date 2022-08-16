package com.slxk.hounddog.mvp.ui.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.db.DeviceModel;
import com.slxk.hounddog.di.component.DaggerDeviceDetailComponent;
import com.slxk.hounddog.mvp.contract.DeviceDetailContract;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.DeviceDetailResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceDetailModifyPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceDetailPutBean;
import com.slxk.hounddog.mvp.presenter.DeviceDetailPresenter;
import com.slxk.hounddog.mvp.utils.AddressParseUtil;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.DateUtils;
import com.slxk.hounddog.mvp.utils.DeviceUtils;
import com.slxk.hounddog.mvp.utils.ResultDataUtils;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/23/2021 17:38
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class DeviceDetailActivity extends BaseActivity<DeviceDetailPresenter> implements DeviceDetailContract.View {

    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.edt_device_name)
    EditText edtDeviceName;
    @BindView(R.id.tv_device_account)
    TextView tvDeviceAccount;
    @BindView(R.id.tv_device_type)
    TextView tvDeviceType;
    @BindView(R.id.tv_device_status)
    TextView tvDeviceStatus;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_device_iccid)
    TextView tvDeviceIccid;
    @BindView(R.id.tv_copy_iccid)
    TextView tvCopyIccid;
    @BindView(R.id.tv_opening_time)
    TextView tvOpeningTime;
    @BindView(R.id.tv_contact_person)
    TextView tvContactPerson;
    @BindView(R.id.edt_contact_person)
    EditText edtContactPerson;
    @BindView(R.id.tv_contact_mobile)
    TextView tvContactMobile;
    @BindView(R.id.edt_contact_mobile)
    EditText edtContactMobile;
    @BindView(R.id.tv_location_time)
    TextView tvLocationTime;
    @BindView(R.id.toolbar_right)
    TextView toolbarRight;

    private DeviceModel mModel;
    private boolean isModifySuccess = false; // 是否修改成功
    private boolean isEditState = false; // 是否是编辑状态
    private String mDeviceName; // 设备名称
    private String mContactPerson; // 联系人
    private String mContactMobile; // 备用号码
    private int mapType = 0; // 地图类型,0：高德地图，1：百度地图，2：谷歌地图
    private String mIccid;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDeviceDetailComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_device_detail;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.device_detail));
        toolbarRight.setVisibility(View.VISIBLE);
        mModel = (DeviceModel) getIntent().getSerializableExtra("bean");
        mapType = ConstantValue.getMapType();

        onEditStateShow();
        if (mModel != null) {
            if (!TextUtils.isEmpty(mModel.getDevice_name())) {
                tvDeviceName.setText(mModel.getDevice_name());
                edtDeviceName.setText(mModel.getDevice_name());
                mDeviceName = mModel.getDevice_name();
            }
            tvDeviceAccount.setText(mModel.getImei());

            if (ConstantValue.getAppLocationMode() == 0){
                if (!TextUtils.isEmpty(mModel.getDev_state())){
                    String strFormat = DeviceUtils.onLocationType(mModel.getDev_state());
                    char a6 = strFormat.charAt(1); // 设备状态
                    if ("1".equals(String.valueOf(a6))) {
                        tvDeviceStatus.setText(getString(R.string.state_line_on));
                        tvDeviceStatus.setTextColor(getResources().getColor(R.color.color_2ABA5A));
                    } else {
                        tvDeviceStatus.setText(getString(R.string.state_line_static));
                        tvDeviceStatus.setTextColor(getResources().getColor(R.color.color_FF2F25));
                    }
                    if (DeviceUtils.isDeviceOnOff(mModel.getCommunication_time())) {
                        tvDeviceStatus.setText(getString(R.string.state_line_down));
                        tvDeviceStatus.setTextColor(getResources().getColor(R.color.color_999999));
                    }
                }else{
                    tvDeviceStatus.setText(getString(R.string.state_line_down));
                    tvDeviceStatus.setTextColor(getResources().getColor(R.color.color_999999));
                }
            }else{
                switch (mModel.getState()) {
                    case ResultDataUtils.Device_State_Line_Sleep:
                        tvDeviceStatus.setText(getString(R.string.state_line_static));
                        tvDeviceStatus.setTextColor(getResources().getColor(R.color.color_FF2F25));
                        break;
                    case ResultDataUtils.Device_State_Line_On:
                        tvDeviceStatus.setText(getString(R.string.state_line_on));
                        tvDeviceStatus.setTextColor(getResources().getColor(R.color.color_2ABA5A));
                        break;
                    case ResultDataUtils.Device_State_Line_Down:
                        tvDeviceStatus.setText(getString(R.string.state_line_down));
                        tvDeviceStatus.setTextColor(getResources().getColor(R.color.color_999999));
                        break;
                }
            }

            tvLocationTime.setText(mModel.getTime());

            double lat = (double) mModel.getLat() / 1000000;
            double lon = (double) mModel.getLon() / 1000000;
            switch (mapType){
                case 0:
                case 1:
                    AddressParseUtil.getAmapAddress(this, tvAddress, lat, lon, "");
                    break;
                case 2:
                    AddressParseUtil.getGoogleAddress(this, tvAddress, lat, lon, "");
                    break;
            }
            if (Utils.isNetworkAvailable(this)) {
                getDeviceDetailInfo();
            }
        }
    }

    /**
     * 获取设备详细信息
     */
    private void getDeviceDetailInfo() {
        DeviceDetailPutBean.ParamsBean paramsBean = new DeviceDetailPutBean.ParamsBean();
        if (!TextUtils.isEmpty(mModel.getSimei())) {
            paramsBean.setSimei(mModel.getSimei());
        }

        DeviceDetailPutBean bean = new DeviceDetailPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Device_Detail);
        bean.setModule(ModuleValueService.Module_For_Device_Detail);

        if (getPresenter() != null) {
            getPresenter().getDeviceDetail(bean);
        }
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

    @OnClick({R.id.toolbar_right, R.id.tv_copy_iccid})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()) {
            switch (view.getId()) {
                case R.id.toolbar_right:
                    isEditState = !isEditState;
                    onEditDeviceInfo();
                    break;
                case R.id.tv_copy_iccid:
                    onCopyToClipboard(mIccid);
                    break;
            }
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

    /**
     * 是否是编辑状态
     */
    private void onEditStateShow(){
        toolbarRight.setText(isEditState ? getString(R.string.save) : getString(R.string.edit));
    }

    /**
     * 编辑设备信息
     */
    private void onEditDeviceInfo(){
        onEditStateShow();
        if (isEditState) {
            onDetailUIShow(false);
        } else {
            submitDetailModify();
        }
    }

    /**
     * UI显示
     *
     * @param isTextShow 是否显示TextView文字内容
     */
    private void onDetailUIShow(boolean isTextShow) {
        tvDeviceName.setVisibility(View.GONE);
        edtDeviceName.setVisibility(View.GONE);
        tvContactPerson.setVisibility(View.GONE);
        edtContactPerson.setVisibility(View.GONE);
        tvContactMobile.setVisibility(View.GONE);
        edtContactMobile.setVisibility(View.GONE);
        if (isTextShow) {
            tvDeviceName.setVisibility(View.VISIBLE);
            tvContactPerson.setVisibility(View.VISIBLE);
            tvContactMobile.setVisibility(View.VISIBLE);
        } else {
            edtDeviceName.setVisibility(View.VISIBLE);
            edtContactPerson.setVisibility(View.VISIBLE);
            edtContactMobile.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 提交修改信息
     */
    private void submitDetailModify() {
        boolean isModifyNumber = false; // 是否有修改设备名称
        boolean isModifyName = false; // 是否有修改联系人名称
        boolean isModifyMobile = false; // 是否有修改联系人电话
        String deviceName = edtDeviceName.getText().toString().trim();
        String contactPerson = edtContactPerson.getText().toString().trim();
        String contactMobile = edtContactMobile.getText().toString().trim();
        if (!TextUtils.isEmpty(deviceName) && deviceName.length() > 8){
            ToastUtils.show(getString(R.string.device_name_max_length));

            isEditState = true;
            onEditStateShow();
            onDetailUIShow(false);
            return;
        }
        // 判断备用手机号码是否有更改
        if (TextUtils.isEmpty(mContactMobile) && !TextUtils.isEmpty(contactMobile)) {
            isModifyMobile = true;
        }
        if (!TextUtils.isEmpty(mContactMobile) && TextUtils.isEmpty(contactMobile)) {
            isModifyMobile = true;
        }
        if (!TextUtils.isEmpty(mContactMobile) && !TextUtils.isEmpty(contactMobile)) {
            if (!mContactMobile.equals(contactMobile)) {
                isModifyMobile = true;
            }
        }
        // 判断设备名称是否有更改
        if (TextUtils.isEmpty(mDeviceName) && !TextUtils.isEmpty(deviceName)) {
            isModifyNumber = true;
        }
        if (!TextUtils.isEmpty(mDeviceName) && TextUtils.isEmpty(deviceName)) {
            isModifyNumber = true;
        }
        if (!TextUtils.isEmpty(mDeviceName) && !TextUtils.isEmpty(deviceName)) {
            if (!mDeviceName.equals(deviceName)) {
                isModifyNumber = true;
            }
        }
        // 判断联系人名称是否有更改
        if (TextUtils.isEmpty(mContactPerson) && !TextUtils.isEmpty(contactPerson)) {
            isModifyName = true;
        }
        if (!TextUtils.isEmpty(mContactPerson) && TextUtils.isEmpty(contactPerson)) {
            isModifyName = true;
        }
        if (!TextUtils.isEmpty(mContactPerson) && !TextUtils.isEmpty(contactPerson)) {
            if (!mContactPerson.equals(contactPerson)) {
                isModifyName = true;
            }
        }

        if (isModifyNumber || isModifyName || isModifyMobile) {
            DeviceDetailModifyPutBean.ParamsBean paramsBean = new DeviceDetailModifyPutBean.ParamsBean();
            if (isModifyNumber) {
                paramsBean.setCar_number(deviceName);
            }
            if (isModifyName) {
                paramsBean.setUser_name(contactPerson);
            }
            if (isModifyMobile) {
                paramsBean.setUser_phone(contactMobile);
            }
            if (!TextUtils.isEmpty(mModel.getSimei())) {
                paramsBean.setSimei(mModel.getSimei());
            }

            DeviceDetailModifyPutBean bean = new DeviceDetailModifyPutBean();
            bean.setFunc(ModuleValueService.Fuc_For_Modify_Device_Detail);
            bean.setModule(ModuleValueService.Module_For_Modify_Device_Detail);
            bean.setParams(paramsBean);

            if (getPresenter() != null) {
                getPresenter().submitDetailModify(bean);
            }
        } else {
            isEditState = false;
            onEditStateShow();
            onDetailUIShow(true);
        }
    }

    @Override
    public void getDeviceDetailInfoSuccess(DeviceDetailResultBean deviceDetailResultBean) {
        mIccid = deviceDetailResultBean.getIccid();
        if (!TextUtils.isEmpty(deviceDetailResultBean.getVer())){
            if (deviceDetailResultBean.getVer().toUpperCase().equals("D3_B")) {
                tvDeviceType.setText(getString(R.string.d3_version));
            } else if (deviceDetailResultBean.getVer().toUpperCase().contains("_P")) {
                tvDeviceType.setText(deviceDetailResultBean.getVer().toUpperCase().replace("_P", getString(R.string.update_version)));
            } else if (deviceDetailResultBean.getVer().toUpperCase().contains("_E")) {
                tvDeviceType.setText(deviceDetailResultBean.getVer().toUpperCase().replace("_E", getString(R.string.low_power_version)));
            } else {
                tvDeviceType.setText(deviceDetailResultBean.getVer().toUpperCase());
            }
        }
        if (!TextUtils.isEmpty(deviceDetailResultBean.getIccid())){
            tvDeviceIccid.setText(deviceDetailResultBean.getIccid());
        }
        if (deviceDetailResultBean.getDetail().getStart_dev_time() != 0){
            tvOpeningTime.setText(DateUtils.timedate_2(deviceDetailResultBean.getDetail().getStart_dev_time() + ""));
        }
        if (!TextUtils.isEmpty(deviceDetailResultBean.getDetail().getUser_name())) {
            tvContactPerson.setText(deviceDetailResultBean.getDetail().getUser_name());
            edtContactPerson.setText(deviceDetailResultBean.getDetail().getUser_name());
            mContactPerson = deviceDetailResultBean.getDetail().getUser_name();
        }
        if (!TextUtils.isEmpty(deviceDetailResultBean.getDetail().getBck_phone())) {
            tvContactMobile.setText(deviceDetailResultBean.getDetail().getBck_phone());
            edtContactMobile.setText(deviceDetailResultBean.getDetail().getBck_phone());
            mContactMobile = deviceDetailResultBean.getDetail().getBck_phone();
        }
    }

    @Override
    public void submitDetailModifySuccess(BaseBean baseBean) {
        if (baseBean.isSuccess()){
            ToastUtils.show(getString(R.string.modify_success));
            isModifySuccess = true;
            mDeviceName = edtDeviceName.getText().toString().trim();
            mContactPerson = edtContactPerson.getText().toString().trim();
            mContactMobile = edtContactMobile.getText().toString().trim();
            tvDeviceName.setText(mDeviceName);
            tvContactPerson.setText(mContactPerson);
            tvContactMobile.setText(mContactMobile);
            isEditState = false;
            onDetailUIShow(true);
        }else{
            isEditState = true;
            onDetailUIShow(false);
        }
        onEditStateShow();
    }

    @Override
    protected void onDestroy() {
        if (isModifySuccess){
            Intent intent = new Intent();
            intent.putExtra("name", mDeviceName);
            setResult(Activity.RESULT_OK, intent);
        }
        super.onDestroy();
    }

}
