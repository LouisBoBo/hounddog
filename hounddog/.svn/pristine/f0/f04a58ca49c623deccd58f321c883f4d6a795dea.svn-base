package com.slxk.hounddog.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.provider.Settings;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.heytap.msp.push.HeytapPushManager;
import com.heytap.msp.push.callback.ICallBackResultService;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.mob.MobSDK;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.db.DeviceModel;
import com.slxk.hounddog.db.MyDao;
import com.slxk.hounddog.di.component.DaggerLoginComponent;
import com.slxk.hounddog.mvp.contract.LoginContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.CheckAppUpdateBean;
import com.slxk.hounddog.mvp.model.bean.LoginResultBean;
import com.slxk.hounddog.mvp.model.putbean.CheckAppUpdatePutBean;
import com.slxk.hounddog.mvp.model.putbean.LoginPutBean;
import com.slxk.hounddog.mvp.presenter.LoginPresenter;
import com.slxk.hounddog.mvp.receiver.oppo.AppParam;
import com.slxk.hounddog.mvp.ui.view.ClearEditText;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.GpsUtils;
import com.slxk.hounddog.mvp.utils.IPUtils;
import com.slxk.hounddog.mvp.utils.MD5Utils;
import com.slxk.hounddog.mvp.utils.MacAddressUtils;
import com.slxk.hounddog.mvp.utils.ManufacturerUtil;
import com.slxk.hounddog.mvp.utils.PhoneSimInfoUtils;
import com.slxk.hounddog.mvp.utils.ResultDataUtils;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.BindMobileDialog;
import com.slxk.hounddog.mvp.weiget.LoginAccountListDialog;
import com.slxk.hounddog.mvp.weiget.PhoneModifyDialog;
import com.slxk.hounddog.mvp.weiget.PrivacyPolicyDialog;
import com.slxk.hounddog.mvp.weiget.ServiceIPCheckDialog;
import com.slxk.hounddog.mvp.weiget.UploadAppDialog;
import com.umeng.commonsdk.UMConfigure;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.vivo.push.util.VivoPushException;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/20/2021 15:36
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.iv_save_password)
    ImageView ivSavePassword;
    @BindView(R.id.tv_save_password)
    TextView tvSavePassword;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.iv_privacy_policy)
    ImageView ivPrivacyPolicy;
    @BindView(R.id.tv_privacy_policy)
    TextView tvPrivacyPolicy;
    @BindView(R.id.edt_account)
    ClearEditText edtAccount;
    @BindView(R.id.iv_sqr)
    ImageView ivSqr;
    @BindView(R.id.edt_password)
    ClearEditText edtPassword;
    @BindView(R.id.iv_see_password)
    ImageView ivSeePassword;
    @BindView(R.id.iv_check_ip)
    ImageView ivCheckIp;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    private boolean IsAuthAgree = false; // 是否授予手机授权
    private boolean isAgreePrivacy = false; // 是否同意了用户隐私协议
    private boolean isSavePassword = false; // 是否保存密码
    private boolean isAgreement = false; // 是否勾选了同意用户隐私政策协议
    private String mPassword = ""; // 输入的密码
    private int mAppLocationMode = 0; // APP定位模式，0:无线模式，1:网络模式

    private Double mLatitude = 0.0;
    private Double mLongitude = 0.0;
    private String mobileIpAddress; // 手机当前网络ip地址
    private String mobileImei = ""; // 手机的imei号，唯一编码
    private String mobileIccid = ""; // 获取手机的iccid
    private String mobileInfo = ""; // 手机信息，品牌，Android版本等等
    private String wifiMacAddress = ""; // 如果手机连接了wifi，获取路由器的mac地址

    private String mLoginInfo; // 登录信息，包含定位信息，手机信息，当前网络ip信息
    private String mLoginFlag = ""; // 登录标识:用户登录or设备号登录
    private String mPushMpm = ResultDataUtils.Push_XiaoMi; // 推送通道

    // 华为推送
    private String mHuaweiToken = ""; // 华为推送token
    private final static String CODELABS_ACTION = "com.slxk.hounddog.action";
    private MyReceiver receiver;
    // oppo推送
    private String mOppoRegId = ""; // oppo推送的regid
    // vivo推送
    private String mVivoRegId = ""; // vivo推送的regid

    // 1.判断SIM是否为国内运营商，国内的运营商，不管什么语言都走验证流程，没有SIM则进入第二步判断位置
    // 2.判断位置，国内则走验证流程，国外则不走验证流程，地址获取不到则进入第三步
    // 3.判断系统语言（繁体归国内），中文和繁体走验证流程，其他则不走验证流程
    private String localeLanguage; // 当前手机系统语言，用于判断用户是国内还是国外用户
    private int isOperator = -1; // ["中国电信CTCC":3]["中国联通CUCC:2]["中国移动CMCC":1]["other":0]["无sim卡":-1]
    private boolean isChinaOperator = true; // 是否需要走绑定流程，如果是中国运营商就走绑定流程

    private String mPhoneLanguage; // 当前手机系统语言，用来请求获取多语言文件
    // 百度定位
    private LocationClient mLocationClient;

    // 版本更新
    private boolean isGetUpdateApp = false; // 是否请求过版本更新
    private int versionCode; // 当前版本标识
    // 版本更新
    private static final int INSTALL_PERMISS_CODE = 101; // 允许安装位置应用回调码
    private static final int INSTALL_APK_RESULT = 102; // apk安装情况，是否完成安装回调
    private String mFilePath; // 版本更新下载url地址
    private boolean isForceApp = false; // 是否需要强制更新App，false-不强制更新 true-强制更新

    private static final int INTENT_SCAN_QR = 10; // 扫描二维码/条形码回调
    private static final int Intent_Register = 11; // 注册
    private static final int Intent_Bind_Mobile = 12; // 绑定手机
    private static final int Intent_Forget_Password = 13; // 修改密码

    private boolean isSeeLoginPassword = false; // 是否明文查看登录密码
    private static final String Intent_Type_Key = "intent_type_key";
    private static final String Intent_Mobile_Key = "intent_mobile_key";
    private int mIntentType = 0; // 跳转类型，0：普通跳转，1：修改绑定手机号跳转，需要弹出登录提示框
    private String mMobile = ""; // 绑定的电话号码，修改绑定手机号码传值，其他可不传
    private boolean isBeforeAccount = false; // 当前登录账号是否和上一个登录账号相同
    private boolean isMergeAccount = false; // 是否需要合并账号

    private boolean isVerifySupport = false;
    private boolean isPreVerifyDone = false; // 预登录
    private int verifyTo = 0; // 0 注册 1 一键绑定手机号
    private String mSid;
    private String opToken = "";
    private String operator = "";
    private String token = "";
    private String md5 = "";

    public static Intent newInstance(int type, String mobile) {
        Intent intent = new Intent(MyApplication.getMyApp(), LoginActivity.class);
        intent.putExtra(Intent_Type_Key, type);
        intent.putExtra(Intent_Mobile_Key, mobile);
        return intent;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login;//setContentView(id);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mIntentType = getIntent().getIntExtra(Intent_Type_Key, 0);
        mMobile = getIntent().getStringExtra(Intent_Mobile_Key);
        tvPrivacyPolicy.setText(Html.fromHtml(getString(R.string.consent_to_privacy_investigation)));
        tvVersion.setText("V" + AppUtils.getAppVersionName());

        mPhoneLanguage = Utils.getLocaleLanguageShorthand();
        localeLanguage = Utils.localeLanguage();

        mAppLocationMode = ConstantValue.getAppLocationMode();
        isAgreePrivacy = SPUtils.getInstance().getBoolean(ConstantValue.IS_AGREE_PRIVACY, false);
        isAgreement = SPUtils.getInstance().getBoolean(ConstantValue.IS_USER_AGREEMENT, false);
        IsAuthAgree = SPUtils.getInstance().getBoolean(ConstantValue.Is_Auth_agree, false);
        isGetUpdateApp = SPUtils.getInstance().getBoolean(ConstantValue.Is_Get_Update_App, false);
        isSavePassword = ConstantValue.isSavePassword();
        versionCode = AppUtils.getAppVersionCode();
        mobileInfo = Utils.getMobilePackageInfo(this);
        MyApplication.getMyApp().clearData();

        edtAccount.setText(ConstantValue.getAccount());
        if (isSavePassword) {
            edtPassword.setText(ConstantValue.getPassword());
        }
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().hasExtra("account")) {
                String account = getIntent().getStringExtra("account");
                edtAccount.setText(account);
            }
            if (getIntent().hasExtra("password")) {
                String password = getIntent().getStringExtra("password");
                edtPassword.setText(password);
            }
        }

        onSaveAgreementClick();
        onSavePasswordClick();

        if (isAgreePrivacy) {
            isOperator = PhoneSimInfoUtils.getSubscriptionOperatorType(this);
            if (isOperator == 0 || isOperator == -1) {
                isChinaOperator = false;
            }
            getPushRegisterId();
            initBaiduLbs();
            if (!isGetUpdateApp) {
                checkAppUpdate();
            }

            if (IsAuthAgree) {
                getIPAddress();
            }
        } else {
            onShowPrivacyPolicy();
        }

        if (mIntentType == 1) { //有修改手机号，弹框提示
            PhoneModifyDialog phoneModifyDialog = new PhoneModifyDialog();
            String phoneText = getString(R.string.modify_phone_txt) + ": " + mMobile + " " + getString(R.string.login);
            phoneModifyDialog.show(getSupportFragmentManager(), phoneText);
        }
        onDeleteDeviceListForDB();
    }

    /**
     * 初始化获取推送注册id
     */
    private void getPushRegisterId(){
        if (ManufacturerUtil.isHuawei() && ManufacturerUtil.getEMUI() && ManufacturerUtil.getHWID(this)) {
            receiver = new MyReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(CODELABS_ACTION);
            registerReceiver(receiver, filter);

            mPushMpm = ResultDataUtils.Push_HuaWei;
            getToken();
        } else if (ManufacturerUtil.isXiaomi()) {
            mPushMpm = ResultDataUtils.Push_XiaoMi;
        } else if (ManufacturerUtil.isOppo()) {
            mPushMpm = ResultDataUtils.Push_OPPO;
            initOPPOPush();
        } else if (ManufacturerUtil.isVivo()) {
            mPushMpm = ResultDataUtils.Push_VIVO;
            initVIVOPush();
        }
    }

    /**
     * 初始化vivo，获取vivo的regid
     */
    private void initVIVOPush() {
        mVivoRegId = PushClient.getInstance(this).getRegId();
    }

    /**
     * 获取华为推送token
     */
    private void getToken() {
        new Thread() {
            @SuppressLint("LogNotTimber")
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
//                    String appId = AGConnectServicesConfig.fromContext(LoginActivity.this).getString("client/app_id");
                    String appId = "105778529";
                    mHuaweiToken = HmsInstanceId.getInstance(LoginActivity.this).getToken(appId, "HCM");
                    if (!TextUtils.isEmpty(mHuaweiToken)) {
                        sendRegTokenToServer(mHuaweiToken);
                    }
                } catch (ApiException e) {
                    Log.e("kang", "get token failed, " + e);
                }
            }
        }.start();
    }

    private void sendRegTokenToServer(String token) {
//        Log.e("kang", "sending token to server. token:" + token);
    }

    /**
     * MyReceiver
     */
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null && bundle.getString("msg") != null) {
                mHuaweiToken = bundle.getString("msg");
            }
        }
    }

    /**
     * 初始化oppo推送
     */
    private void initOPPOPush() {
        try {
            HeytapPushManager.register(this, AppParam.appKey, AppParam.appSecret, mPushCallback);//setPushCallback接口也可设置callback
            HeytapPushManager.requestNotificationPermission();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("LogNotTimber")
    private ICallBackResultService mPushCallback = new ICallBackResultService() {
        @Override
        public void onRegister(int code, String s) {
            if (code == 0) {
                mOppoRegId = s;
                Log.e("注册成功", "registerId:" + s);
            } else {
                Log.e("注册失败", "code=" + code + ",msg=" + s);
            }
        }

        @Override
        public void onUnRegister(int code) {
            if (code == 0) {
                Log.e("注销成功", "code=" + code);
            } else {
                Log.e("注销失败", "code=" + code);
            }
        }

        @Override
        public void onGetPushStatus(final int code, int status) {
            if (code == 0 && status == 0) {
                Log.e("Push状态正常", "code=" + code + ",status=" + status);
            } else {
                Log.e("Push状态错误", "code=" + code + ",status=" + status);
            }
        }

        @Override
        public void onGetNotificationStatus(final int code, final int status) {
            if (code == 0 && status == 0) {
                Log.e("通知状态正常", "code=" + code + ",status=" + status);
            } else {
                Log.e("通知状态错误", "code=" + code + ",status=" + status);
            }
        }

        @Override
        public void onError(int i, String s) {
            Log.e("onError", "onError code : " + i + "   message : " + s);
        }

        @Override
        public void onSetPushTime(final int code, final String s) {
            Log.e("SetPushTime", "code=" + code + ",result:" + s);
        }

    };

    /**
     * 删除设备列表信息
     */
    public void onDeleteDeviceListForDB() {
        try {
            MyDao deviceBeanDao = new MyDao(DeviceModel.class);
            deviceBeanDao.deleteAll("db_device_bean");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐私政策协议告知
     */
    private void onShowPrivacyPolicy() {
        final PrivacyPolicyDialog dialog = new PrivacyPolicyDialog();
        dialog.show(getSupportFragmentManager(), new PrivacyPolicyDialog.onPrivacyPolicyChange() {
            @Override
            public void onPrivacyPolicy(boolean isPrivacy) {
                SPUtils.getInstance().put(ConstantValue.IS_AGREE_PRIVACY, isPrivacy);
                if (!isPrivacy) {
                    dialog.dismiss();
                    finish();
                } else {
                    initAppMapAndPush();
                    isOperator = PhoneSimInfoUtils.getSubscriptionOperatorType(LoginActivity.this);
                    if (isOperator == 0 || isOperator == -1) {
                        isChinaOperator = false;
                    }
                    btnLogin.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getPushRegisterId();
                        }
                    }, 1000);

                    SPUtils.getInstance().put(ConstantValue.Umeng_Init, true);
                    // 友盟隐私政策提交
                    UMConfigure.submitPolicyGrantResult(getApplicationContext(), true);
                    // Mob的隐私政策提交
                    MobSDK.submitPolicyGrantResult(true, null);
                    dialog.dismiss();
                }
            }

            @Override
            public void onSeePrivacyPolicy() {
                onSeePrivacyAgreement();
            }
        });
    }

    /**
     * 查看用户隐私政策协议
     */
    private void onSeePrivacyAgreement() {
        launchActivity(WebViewActivity.newInstance(getString(R.string.privacy_policy_user), Api.Privacy_Policy));
    }

    /**
     * 初始化百度地图，发起定位
     */
    private void initBaiduLbs() {
        //定位初始化
        mLocationClient = new LocationClient(this);

        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("gcj02"); // 设置坐标类型
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(10000);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(false);
        //可选，设置是否需要地址描述
        option.setIsNeedLocationDescribe(false);

        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);

        mLocationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            MyApplication.getMyApp().setLatitude(mLatitude);
            MyApplication.getMyApp().setLongitude(mLongitude);

            SPUtils.getInstance().put(ConstantValue.Is_In_China, GpsUtils.isChinaLocation(mLatitude, mLongitude));
            if (mLocationClient != null) {
                mLocationClient.stop();
            }
        }
    }

    /**
     * 检测app版本更新
     */
    private void checkAppUpdate() {
        CheckAppUpdatePutBean.ParamsBean paramsBean = new CheckAppUpdatePutBean.ParamsBean();
        paramsBean.setVersion(versionCode);
        paramsBean.setApp_type(Api.Check_Version_Type);

        CheckAppUpdatePutBean bean = new CheckAppUpdatePutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_App_Version);
        bean.setModule(ModuleValueService.Module_For_App_Version);

        if (getPresenter() != null) {
            getPresenter().getAppUpdate(bean);
        }
    }

    @Override
    public void onPause() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
        super.onDestroy();
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

    @OnClick({R.id.iv_save_password, R.id.tv_save_password, R.id.tv_forget_password, R.id.btn_login, R.id.tv_register, R.id.iv_privacy_policy,
            R.id.tv_privacy_policy, R.id.iv_sqr, R.id.iv_see_password, R.id.iv_check_ip})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()) {
            switch (view.getId()) {
                case R.id.iv_save_password:
                case R.id.tv_save_password:
                    isSavePassword = !isSavePassword;
                    onSavePasswordClick();
                    break;
                case R.id.tv_forget_password:
                    onForgetPassword();
                    break;
                case R.id.btn_login:
                    isMergeAccount = false;
                    isBeforeAccount = false;
                    hideKeyboard(edtAccount);
                    onLoginConfirm();
                    break;
                case R.id.tv_register:
                    onRegisterAccount();
                    break;
                case R.id.iv_privacy_policy:
                    isAgreement = !isAgreement;
                    onSaveAgreementClick();
                    break;
                case R.id.tv_privacy_policy:
                    launchActivity(WebViewActivity.newInstance(getString(R.string.privacy_policy_user), Api.Privacy_Policy));
                    break;
                case R.id.iv_sqr:
                    Intent intent_qr = new Intent(this, HMSScanQrCodeActivity.class);
                    startActivityForResult(intent_qr, INTENT_SCAN_QR);
                    break;
                case R.id.iv_see_password:
                    isSeeLoginPassword = !isSeeLoginPassword;
                    onSeeLoginPasswordType();
                    break;
                case R.id.iv_check_ip:
                    onCheckServiceIP();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Intent_Register) {
                if (data != null) {
                    String account = data.getStringExtra("account");
                    String password = data.getStringExtra("password");
                    edtAccount.setText(account);
                    edtPassword.setText(password);
                }
            } else if (requestCode == Intent_Forget_Password) {
                edtPassword.setText("");
            } else if (requestCode == INTENT_SCAN_QR) {
                if (data != null) {
                    String imei = data.getStringExtra("imei");
                    if (!TextUtils.isEmpty(imei)) {
                        edtAccount.setText(imei);
                        edtAccount.setSelection(edtAccount.getText().toString().length());
                    }
                }
            } else if (requestCode == Intent_Bind_Mobile) {
                String password = data.getStringExtra("password");
                if (!TextUtils.isEmpty(password)) {
                    edtPassword.setText(password);
                }
            }
        }
        if (requestCode == INSTALL_PERMISS_CODE) {
            // 发起安装应用
            btnLogin.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onInstallApkAuth(false);
                }
            }, 1000);
        } else if (requestCode == INSTALL_APK_RESULT) {
            if (isForceApp) {
                onBackPressed();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 切换域名或ip
     */
    private void onCheckServiceIP() {
        ServiceIPCheckDialog dialog = new ServiceIPCheckDialog();
        dialog.show(getSupportFragmentManager(), new ServiceIPCheckDialog.onServiceIPCheckChange() {
            @Override
            public void onCheck() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        restartApplication();
                    }
                }, 500);
            }
        });
    }

    //重启应用
    private void restartApplication() {
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //杀掉以前进程
        Process.killProcess(Process.myPid());
    }

    /**
     * 是否明文查看登录密码
     */
    private void onSeeLoginPasswordType() {
        if (isSeeLoginPassword) {
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT); //输入类型为普通文本
            ivSeePassword.setImageResource(R.drawable.icon_password_see);
        } else {
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); //输入类型为普通文本密码
            ivSeePassword.setImageResource(R.drawable.icon_password_hide);
        }
        edtPassword.setSelection(edtPassword.getText().toString().length());
    }

    /**
     * 注册账号
     */
    private void onRegisterAccount() {
        hideKeyboard(edtAccount);
        Intent intent = new Intent();
        if (isChinaOperator) {
            intent.setClass(this, RegisterActivity.class);
        } else {
            intent.setClass(this, RegisterEmailActivity.class);
        }
        startActivityForResult(intent, Intent_Register);
    }

    /**
     * 找回密码
     */
    private void onForgetPassword() {
        hideKeyboard(edtAccount);
        Intent intent = new Intent();
        if (isChinaOperator) {
            intent.setClass(this, ForgetPasswordActivity.class);
        } else {
            intent.setClass(this, ForgetPasswordForEmailActivity.class);
        }
        startActivityForResult(intent, Intent_Forget_Password);
    }

    /**
     * 保存密码操作
     */
    private void onSavePasswordClick() {
        ivSavePassword.setImageResource(isSavePassword ? R.drawable.icon_select : R.drawable.icon_unselected);
    }

    /**
     * 同意用户隐私政策协议
     */
    private void onSaveAgreementClick() {
        ivPrivacyPolicy.setImageResource(isAgreement ? R.drawable.icon_select : R.drawable.icon_unselected);
    }

    /**
     * 确认登录
     */
    private void onLoginConfirm() {
        String account = edtAccount.getText().toString().trim().toLowerCase();
        mPassword = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.show(getString(R.string.input_account));
            return;
        }
        if (TextUtils.isEmpty(mPassword)) {
            ToastUtils.show(getString(R.string.input_password));
            return;
        }
        if (!isAgreement) {
            ToastUtils.show(getString(R.string.agreement_hint));
            return;
        }

        mLoginInfo = "IP:" + mobileIpAddress + ";IMEI:" + mobileImei + ";IMSI:null" + ";ICCID:" + mobileIccid
                + ";Address:" + mLatitude + "," + mLongitude + mobileInfo + ";WifiMac:" + wifiMacAddress
                + ";Operator:" + PhoneSimInfoUtils.getSubscriptionSimType(this);

        if (mPushMpm.equals(ResultDataUtils.Push_HuaWei) && TextUtils.isEmpty(mHuaweiToken)) {
            mPushMpm = ResultDataUtils.Push_XiaoMi;
        } else if (mPushMpm.equals(ResultDataUtils.Push_OPPO) && TextUtils.isEmpty(mOppoRegId)) {
            mPushMpm = ResultDataUtils.Push_XiaoMi;
        } else if (mPushMpm.equals(ResultDataUtils.Push_VIVO) && TextUtils.isEmpty(mVivoRegId)) {
            mPushMpm = ResultDataUtils.Push_XiaoMi;
        }

        LoginPutBean.ParamsBean paramsBean = new LoginPutBean.ParamsBean();
        paramsBean.setAccount(account);
        paramsBean.setPwd_md5(MD5Utils.getMD5Encryption(mPassword));
        paramsBean.setMpm(mPushMpm);
        switch (mPushMpm) {
            case ResultDataUtils.Push_HuaWei:
                paramsBean.setMpm_identify(mHuaweiToken);
                break;
            case ResultDataUtils.Push_OPPO:
                paramsBean.setMpm_identify(mOppoRegId);
                break;
            case ResultDataUtils.Push_VIVO:
                paramsBean.setMpm_identify(mVivoRegId);
                break;
        }
        paramsBean.setPlatform(ModuleValueService.Login_Platform_Type);
        paramsBean.setInfo(mLoginInfo);
        if (!TextUtils.isEmpty(mLoginFlag)) {
            paramsBean.setFlag(mLoginFlag);
        }
        paramsBean.setType(Api.App_Type);
        paramsBean.setLang(mPhoneLanguage);

        LoginPutBean bean = new LoginPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Login);
        bean.setModule(ModuleValueService.Module_For_Login);

        if (getPresenter() != null) {
            getPresenter().submitLogin(bean);
        }
    }

    @Override
    public void onBackPressed() {
        ActivityUtils.finishAllActivities();
        super.onBackPressed();
    }

    @Override
    public void submitLoginSuccess(LoginResultBean loginResultBean) {
        if (loginResultBean.isSuccess()) {
            if (!TextUtils.isEmpty(loginResultBean.getSid())) {
                onLoginSuccess(loginResultBean);
            } else {
                onLoginAccountSelect(loginResultBean);
            }
        } else {
            mLoginFlag = "";
        }
    }

    /**
     * 多账号登录
     *
     * @param loginResultBean
     */
    private void onLoginAccountSelect(LoginResultBean loginResultBean) {
        if (loginResultBean.getFlag().size() >= 2) {
            int bindPhone = 0;
            for (String str : loginResultBean.getFlag()) {
                if (str.equals(ResultDataUtils.Login_type_Phone_Account)) {
                    bindPhone++;
                }
                if (str.equals(ResultDataUtils.Login_type_Phone_Device)) {
                    bindPhone++;
                }
            }
            isMergeAccount = bindPhone == 2;

            LoginAccountListDialog dialog = new LoginAccountListDialog();
            dialog.show(getSupportFragmentManager(), loginResultBean.getFlag(), new LoginAccountListDialog.onLoginAccountChange() {
                @Override
                public void onAccountInfo(String accountInfo) {
                    mLoginFlag = accountInfo;
                    onLoginConfirm();
                }
            });
        } else {
            isMergeAccount = false;
            mLoginFlag = "";
            ToastUtils.show(loginResultBean.getError_message());
        }
    }

    /**
     * 登录成功
     *
     * @param loginResultBean
     */
    private void onLoginSuccess(LoginResultBean loginResultBean) {
        mLoginFlag = loginResultBean.getFlag().get(0);
        SPUtils.getInstance().put(ConstantValue.USER_LOGIN_TYPE, mLoginFlag);
        SPUtils.getInstance().put(ConstantValue.HUAWEI_TOKEN, mHuaweiToken);
        SPUtils.getInstance().put(ConstantValue.Push_mpm, mPushMpm);

        if (loginResultBean.isIs_need_check()) {
            boolean isModifyPassword = false; // 是否需要修改密码
            boolean isBindMobile = false; // 是否需要绑定手机号码
            if (loginResultBean.getLack() != null) {
                for (String lack : loginResultBean.getLack()) {
                    if (lack.equals(ResultDataUtils.Lack_Password)) {
                        isModifyPassword = true;
                    }
                    if (lack.equals(ResultDataUtils.Lack_Phone)) {
                        isBindMobile = true;
                    }
                }
                SPUtils.getInstance().put(ConstantValue.Is_Modify_Password, isModifyPassword);
                SPUtils.getInstance().put(ConstantValue.Is_Bind_Mobile, isBindMobile);
            }
            // 是否是设备号登录
            boolean isDeviceLogin = false;
            for (String str : loginResultBean.getFlag()) {
                if (str.equals(ResultDataUtils.Login_type_Device)) {
                    isDeviceLogin = true;
                    break;
                }
            }
            // 是否是国外的设备号，国外的设备不需要绑定手机号
            boolean isForeignDevice = false;
            if (isDeviceLogin && !isChinaOperator && isModifyPassword) {
                isForeignDevice = true;
            }
            SPUtils.getInstance().put(ConstantValue.Is_Foreign_Device, isForeignDevice);
            BindMobileDialog dialog = new BindMobileDialog();
            dialog.show(getSupportFragmentManager(), isForeignDevice, isModifyPassword, isBindMobile, new BindMobileDialog.onBindMobileChange() {
                @Override
                public void onConfirm() {
                    mLoginFlag = "";
                    onBindMobile(loginResultBean.getSid());
                }

                @Override
                public void onCancel() {
                    mLoginFlag = "";
                }
            });
        } else {
            onSaveLoginInfo(loginResultBean);
        }
    }

    /**
     * 绑定手机号码
     *
     * @param sid
     */
    private void onBindMobile(String sid) {
        SPUtils.getInstance().put(ConstantValue.ACCOUNT, edtAccount.getText().toString().trim());
        if (isSavePassword) {
            SPUtils.getInstance().put(ConstantValue.PASSWORD, edtPassword.getText().toString().trim());
        }

        boolean isModifyPassword = SPUtils.getInstance().getBoolean(ConstantValue.Is_Modify_Password);
        boolean isBindMobile = SPUtils.getInstance().getBoolean(ConstantValue.Is_Bind_Mobile);
        boolean isForeignDevice = SPUtils.getInstance().getBoolean(ConstantValue.Is_Foreign_Device, false);
        mSid = sid;
        Intent intent;
        if (!isForeignDevice) {
            if (!isBindMobile && isModifyPassword) {
                intent = new Intent(this, BindForeignActivity.class); //国内登录修改初始密码
                intent.putExtra("foreign", false);
                toBindActivity(intent);
            } else {
                intent = new Intent(this, BindMobileActivity.class);
                toBindActivity(intent);

//                MobSDK.submitPolicyGrantResult(true, null);
//                isVerifySupport = SecVerify.isVerifySupport();
//                if (isVerifySupport) {
//                    verifyTo = 1;
//                    preVerify();
//                } else {
//                    LogUtils.e("当前环境不支持");
//                    intent = new Intent(this, BindMobileActivity.class);
//                    toBindActivity(intent);
//                }
            }
        } else { //国外设备，缺少密码
            intent = new Intent(this, BindForeignActivity.class); //设备号国外登录
            intent.putExtra("foreign", true);
            toBindActivity(intent);
        }
    }

    private void toBindActivity(Intent intent) {
        intent.putExtra("sid", mSid);
        intent.putExtra("password", mPassword);
        startActivity(intent);
    }

    /**
     * 保存登录信息，跳转主页
     */
    private void onSaveLoginInfo(LoginResultBean loginResultBean) {
        ToastUtils.show(getString(R.string.login_success));

        if (!TextUtils.isEmpty(ConstantValue.getAccount()) && ConstantValue.getAccount().equals(edtAccount.getText().toString().trim())) {
            isBeforeAccount = true;
        }
        // 如果达到合并账号的条件，再判断是不是账号登录，判断账号是否有e_ua_add_dev
        if (isMergeAccount) {
            if (mLoginFlag.equals(ResultDataUtils.Login_type_Account)
                    || mLoginFlag.equals(ResultDataUtils.Login_type_Phone_Account)) {
                if (loginResultBean.getFamilys() != null && loginResultBean.getFamilys().size() > 0) {
                    isMergeAccount = loginResultBean.getFamilys().get(0).getAuth().toString().contains(ResultDataUtils.Family_Auth_2);
                } else {
                    isMergeAccount = false;
                }
                if (loginResultBean.getFamilys() != null && loginResultBean.getFamilys().size() > 0) {
                    isMergeAccount = loginResultBean.getFamilys().get(0).getType().equals(ResultDataUtils.Account_User);
                }
            }
        }

        SPUtils.getInstance().put(ConstantValue.Is_Modify_Password, false);
        SPUtils.getInstance().put(ConstantValue.Phone_Zone, loginResultBean.getZone());
        SPUtils.getInstance().put(ConstantValue.IS_USER_AGREEMENT, isAgreement);
        SPUtils.getInstance().put(ConstantValue.USER_SID, loginResultBean.getSid());
        SPUtils.getInstance().put(ConstantValue.ACCOUNT, edtAccount.getText().toString().trim());
        SPUtils.getInstance().put(ConstantValue.IS_SAVE_PASSWORD, isSavePassword);
        SPUtils.getInstance().put(ConstantValue.Is_Need_Check, loginResultBean.isIs_need_check());
        SPUtils.getInstance().put(ConstantValue.Push_Family, loginResultBean.getJpush());
        SPUtils.getInstance().put(ConstantValue.Is_Check_Phone, loginResultBean.isIs_check_phone());
        if (loginResultBean.getFamilys() != null && loginResultBean.getFamilys().size() > 0) {
            SPUtils.getInstance().put(ConstantValue.Family_Sid, loginResultBean.getFamilys().get(0).getSid());
            SPUtils.getInstance().put(ConstantValue.Family_Sid_Login, loginResultBean.getFamilys().get(0).getSid());
            SPUtils.getInstance().put(ConstantValue.Family_Sname, loginResultBean.getFamilys().get(0).getSname());
            SPUtils.getInstance().put(ConstantValue.Family_Sname_Login, loginResultBean.getFamilys().get(0).getSname());
            if (loginResultBean.getFamilys().get(0).getAuth() != null && loginResultBean.getFamilys().get(0).getAuth().size() > 0) {
                SPUtils.getInstance().put(ConstantValue.Family_Auth, loginResultBean.getFamilys().get(0).getAuth().toString());
            }
        }
        if (isSavePassword) {
            SPUtils.getInstance().put(ConstantValue.PASSWORD, edtPassword.getText().toString().trim());
        }
        if (!isBeforeAccount) {
            SPUtils.getInstance().put(ConstantValue.Is_No_More_Reminders, false);
        }
        MyApplication.getMyApp().setBeforeAccount(isBeforeAccount);
        MyApplication.getMyApp().setMergeAccount(isMergeAccount);
        if (mAppLocationMode == 0) {
            launchActivity(MainWirelessActivity.newInstance());
        } else {
            launchActivity(MainActivity.newInstance());
        }
        finish();
    }

    @Override
    public void getAppUpdateSuccess(CheckAppUpdateBean checkAppUpdateBean) {
        SPUtils.getInstance().put(ConstantValue.Is_Get_Update_App, true);
        if (!TextUtils.isEmpty(checkAppUpdateBean.getUrl())) {
            isForceApp = checkAppUpdateBean.isIs_force();
            UploadAppDialog dialog = new UploadAppDialog();
            dialog.show(getSupportFragmentManager(), checkAppUpdateBean, new UploadAppDialog.onAlertDialogChange() {
                @Override
                public void onAppDownLoad(String path) {
                    mFilePath = path;
                    applyInstallCheckApp();
                    dialog.dismiss();
                }

                @Override
                public void onThreeDownLoad() {
                    if (!TextUtils.isEmpty(checkAppUpdateBean.getUrl())) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(checkAppUpdateBean.getUrl());
                        intent.setData(content_url);
                        startActivity(intent);
                    }
                    dialog.dismiss();
                }

                @Override
                public void onCancel() {
                    if (isForceApp) {
                        if (btnLogin != null) {
                            onBackPressed();
                        }
                    }
                }
            });
        }
    }

    // ------------------- 版本更新 -----------------------------

    /**
     * 检测是否有安装权限，判断当前安卓版本是否大于等于8.0，8.0以上系统设置安装未知来源权限
     */
    private void applyInstallCheckApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            onInstallApkAuth(true);
        } else {
            installApk();
        }
    }

    /**
     * 跳转应用详情页打开安装权限
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void installApkSetting() {
        Uri packageUrl = Uri.parse("package:" + getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUrl);
        startActivityForResult(intent, INSTALL_PERMISS_CODE);
    }

    /**
     * 检查安装权限，跳转权限设置页面
     *
     * @param isIntentAuth true跳转，false不跳转
     */
    @SuppressLint("NewApi")
    private void onInstallApkAuth(boolean isIntentAuth) {
        boolean isInstallPermission = getPackageManager().canRequestPackageInstalls();
        if (isIntentAuth) {
            // 是否设置了安装权限
            if (isInstallPermission) {
                installApk();
            } else {
                installApkSetting();
            }
        } else {
            if (isInstallPermission) {
                installApk();
            }
        }
    }

    /**
     * 下载完成，提示用户安装，获取安装App(支持7.0)的意图
     */
    private void installApk() {
        //apk文件的本地路径
        File file = new File(mFilePath);
        if (!file.exists()) {
            return;
        }
        try {
            Uri uri;
            //调用系统安装程序
            Intent intent = new Intent();
            intent.addCategory("android.intent.category.DEFAULT");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(this, "com.slxk.hounddog.fileprovider", file);
                intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//7.0以后，系统要求授予临时uri读取权限，安装完毕以后，系统会自动收回权限，该过程没有用户交互
            } else {
                uri = Uri.fromFile(file);
                intent.setAction("android.intent.action.VIEW");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            startActivityForResult(intent, INSTALL_APK_RESULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------------ 分割线：获取手机ip地址信息 ----------------------

    /**
     * 获取当前手机网络ip地址
     *
     * @return
     */
    private void getIPAddress() {
        new Thread(new Runnable() {
            @SuppressLint("ServiceCast")
            @Override
            public void run() {
                NetworkInfo info = ((ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                        try {
                            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                                NetworkInterface intf = en.nextElement();
                                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                                    InetAddress inetAddress = enumIpAddr.nextElement();
                                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                        mobileIpAddress = inetAddress.getHostAddress();
                                    }
                                }
                            }
                        } catch (SocketException e) {
                            e.printStackTrace();
                        }
                    } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                        wifiMacAddress = MacAddressUtils.getConnectedWifiMacAddress(MyApplication.getMyApp());
                        getMobileNetIp();
                    } else {
                        // 3.判断手机系统语言是否是中文，如果是，则走绑定流程
                        setChinaOperator();
                    }
                } else {
                    //当前无网络连接,请在设置中打开网络
                    // 3.判断手机系统语言是否是中文，如果是，则走绑定流程
                    setChinaOperator();
                }
            }
        }).start();
    }

    /**
     * 获取外网地址
     *
     * @return
     */
    private void getMobileNetIp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL infoUrl = null;
                InputStream inStream = null;
                try {
                    infoUrl = new URL(Api.App_GetIPUrl);
                    URLConnection connection = infoUrl.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    httpConnection.setReadTimeout(5000);//读取超时
                    httpConnection.setConnectTimeout(5000);//连接超时
                    httpConnection.setDoInput(true);
                    httpConnection.setUseCaches(false);

                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        inStream = httpConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                        StringBuilder strber = new StringBuilder();
                        while ((mobileIpAddress = reader.readLine()) != null)
                            strber.append(mobileIpAddress + "\n");
                        inStream.close();
                        // 从反馈的结果中提取出IP地址
                        if (!TextUtils.isEmpty(strber.toString()) && strber.toString().contains("{") && strber.toString().contains("}")) {
                            int start = strber.indexOf("{");
                            int end = strber.indexOf("}");
                            String json = strber.substring(start, end + 1);
                            if (json != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(json);
                                    mobileIpAddress = jsonObject.optString("cip");
                                    // 2.判断手机ip是否在国内，如果在国内，则走绑定流程
                                    // 3.判断手机系统语言是否是中文，如果是，则走绑定流程
                                    if (!isChinaOperator && isOperator == -1) {
                                        isChinaOperator = Utils.checkNameIsChina(jsonObject.optString("cname"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            // 3.判断手机系统语言是否是中文，如果是，则走绑定流程
                            setChinaOperator();
                        }
                        if (TextUtils.isEmpty(mobileIpAddress)) {
                            mobileIpAddress = IPUtils.getWifiIPAddress(LoginActivity.this);
                        }
                    } else {
                        // 3.判断手机系统语言是否是中文，如果是，则走绑定流程
                        setChinaOperator();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 3.判断手机系统语言是否是中文，如果是，则走绑定流程
     */
    private void setChinaOperator() {
        if (!isChinaOperator && isOperator == -1) {
            isChinaOperator = localeLanguage.equals("zh");
        }
    }


    // --------------------------------------- 初始化推送及地图问题 ---------------------------------------

    private void initAppMapAndPush(){
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(MyApplication.getMyApp());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.GCJ02);

        initForXiaoMiPush();
        initForVIVOPush();
        HeytapPushManager.init(MyApplication.getMyApp(), true);
    }

    private void initForVIVOPush(){
        //初始化push
        try {
            PushClient.getInstance(getApplicationContext()).initialize();
        } catch (VivoPushException e) {
            e.printStackTrace();
        }

        // 打开push开关, 关闭为turnOffPush，详见api接入文档
        PushClient.getInstance(getApplicationContext()).turnOnPush(new IPushActionListener() {
            @Override
            public void onStateChanged(int state) {
                // TODO: 开关状态处理， 0代表成功
            }
        });
    }

    /**
     * 初始化小米推送
     */
    private void initForXiaoMiPush(){
        //初始化push推送服务
        if(shouldInit()) {
            MiPushClient.registerPush(MyApplication.getMyApp(), MyApplication.getMyApp().getAppId(), MyApplication.getMyApp().getAppKey());
        }
        //打开Log
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @SuppressLint("LogNotTimber")
            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @SuppressLint("LogNotTimber")
            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getApplicationInfo().processName;
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

}
