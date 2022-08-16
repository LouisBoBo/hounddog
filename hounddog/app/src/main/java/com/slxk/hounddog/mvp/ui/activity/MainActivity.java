package com.slxk.hounddog.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.heytap.msp.push.HeytapPushManager;
import com.heytap.msp.push.callback.ICallBackResultService;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.di.component.DaggerMainComponent;
import com.slxk.hounddog.mvp.contract.MainContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.CheckAppUpdateBean;
import com.slxk.hounddog.mvp.model.bean.DeviceGroupResultBean;
import com.slxk.hounddog.mvp.model.bean.MergeAccountResultBean;
import com.slxk.hounddog.mvp.model.bean.PhoneCodeResultBean;
import com.slxk.hounddog.mvp.model.putbean.CheckAppUpdatePutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceGroupPutBean;
import com.slxk.hounddog.mvp.model.putbean.MergeAccountPutBean;
import com.slxk.hounddog.mvp.model.putbean.PhoneCodePutBean;
import com.slxk.hounddog.mvp.presenter.MainPresenter;
import com.slxk.hounddog.mvp.receiver.oppo.AppParam;
import com.slxk.hounddog.mvp.ui.fragment.AmapLocationFragment;
import com.slxk.hounddog.mvp.ui.fragment.BaiduLocationFragment;
import com.slxk.hounddog.mvp.ui.fragment.CompassFragment;
import com.slxk.hounddog.mvp.ui.fragment.DeviceListFragment;
import com.slxk.hounddog.mvp.ui.fragment.GoogleLocationFragment;
import com.slxk.hounddog.mvp.utils.BroadcastReceiverUtil;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.ResultDataUtils;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.GroupSelectDialog;
import com.slxk.hounddog.mvp.weiget.MergeAccountDialog;
import com.slxk.hounddog.mvp.weiget.MergeAccountFailDialog;
import com.slxk.hounddog.mvp.weiget.UploadAppDialog;
import com.xiaomi.mipush.sdk.MiPushClient;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 在线模式首页
 * <p>
 * Created by MVPArmsTemplate on 12/20/2021 15:36
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.iv_more_function)
    ImageView ivMoreFunction;
    @BindView(R.id.btn_wireless)
    Button btnWireless;
    @BindView(R.id.btn_network)
    Button btnNetwork;
    @BindView(R.id.rl_location_mode)
    RelativeLayout rlLocationMode;

    private static final int PERMISSON_REQUESTCODE = 1; // 获取权限回调码
    // 判断是否需要检测权限，防止不停的弹框
    private boolean isNeedCheck = true;
    private boolean isAuth = true; // 权限不足弹窗只弹一次，防止重复弹出
    private long system_time; // 记录快速点击时间
    private Handler mHandler;

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTitles;
    private FragmentContainerHelper mFragmentContainerHelper;

    private int mapType = 0; // 地图类型,0：高德地图，1：百度地图，2：谷歌地图
    private int mIndexPage = -1; // 底部菜单栏索引
    private boolean isChina = true; // 是否在中国

    // 版本更新
    private boolean isGetUpdateApp = false; // 是否请求过版本更新
    // 版本更新
    private static final int INSTALL_PERMISS_CODE = 101; // 允许安装位置应用回调码
    private static final int INSTALL_APK_RESULT = 102; // apk安装情况，是否完成安装回调
    private String mFilePath; // 版本更新下载url地址
    private boolean isForceApp = false; // 是否需要强制更新App，false-不强制更新 true-强制更新

    // 广播服务
    private ChangePageReceiver receiver; // 注册广播接收器

    private String mPhoneZone = "86"; // 电话号码区号
    private int mLimitSize = 100; // 限制分组获取数量

    private ArrayList<DeviceGroupResultBean.GaragesBean> groupDataBeans; // 设备分组

    public static Intent newInstance() {
        return new Intent(MyApplication.getMyApp(), MainActivity.class);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;//setContentView(id);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // https://www.jianshu.com/p/287fa7da557f
        outState.putParcelable("android:support:fragments", null);
        outState.putParcelable("android:fragments", null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setNavigationBarShow();
        if (!ConstantValue.isRequestAuthority()){
            checkPermissions(needPermissions);
            SPUtils.getInstance().put(ConstantValue.Is_Request_Authority, true);
        }
        mTitles = new ArrayList<>();
        mFragments = new ArrayList<>();
        groupDataBeans = new ArrayList<>();
        mFragmentContainerHelper = new FragmentContainerHelper();
        mapType = ConstantValue.getMapType();
        isChina = ConstantValue.isInChina();
        isGetUpdateApp = SPUtils.getInstance().getBoolean(ConstantValue.Is_Get_Update_App, false);
        mHandler = new Handler();
        int zone = SPUtils.getInstance().getInt(ConstantValue.Phone_Zone, 86);
        mPhoneZone = String.valueOf(zone);

        mTitles.add(getString(R.string.map));
        mTitles.add(getString(R.string.compass));
        mTitles.add(getString(R.string.device_list));

        for (int i = 0; i < mTitles.size(); i++) {
            if (i == 0) {
                if (mapType == 2) {
                    if (isChina){
                        mFragments.add(AmapLocationFragment.newInstance());
                    }else{
                        mFragments.add(GoogleLocationFragment.newInstance());
                    }
                } else if (mapType == 1) {
                    mFragments.add(BaiduLocationFragment.newInstance());
                } else {
                    mFragments.add(AmapLocationFragment.newInstance());
                }
            } else if (i == 1) {
                mFragments.add(CompassFragment.newInstance());
            } else if (i == 2) {
                mFragments.add(DeviceListFragment.newInstance());
            }
        }

        initMagicIndicator();
        mFragmentContainerHelper.handlePageSelected(MyApplication.getMyApp().getCheck_fragment(), false);
        switchPages(MyApplication.getMyApp().getCheck_fragment());
        onNetWorkLocation();
        onSetMessagePush();
        onMergeAccount();
        if (!isGetUpdateApp) {
            checkAppUpdate();
        }

        // 注册一个广播接收器，用于接收从首页跳转到报警消息页面的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("main");
        receiver = new ChangePageReceiver();
        //注册切换页面广播接收者
        LocalBroadcastManager.getInstance(MyApplication.getMyApp()).registerReceiver(receiver, filter);
    }

    /**
     * 页面切换广播，广播接收器
     */
    private class ChangePageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.hasExtra("page")) {
                    int page = intent.getIntExtra("page", 0);
                    switchPages(page);
                    mFragmentContainerHelper.handlePageSelected(page);
                }
            }
        }
    }

    /**
     * 设置消息推送
     */
    private void onSetMessagePush(){
        switch (ConstantValue.getPushMpm()){
            case ResultDataUtils.Push_XiaoMi:
                initXiaoMiPushAlias();
                break;
            case ResultDataUtils.Push_OPPO:
                initOPPOPush();
                break;
        }
    }

    /**
     * 设置小米推送的alias
     */
    private void initXiaoMiPushAlias(){
        String familyid = ConstantValue.getPushFamily();
        if (!TextUtils.isEmpty(familyid)) {
            MiPushClient.setAlias(this, familyid, null);
        }
    }

    // --------------------------------------------- 初始化oppo推送 ------------------------------------------------
    /**
     * 初始化oppo推送
     */
    private void initOPPOPush(){
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
     * 判断是否合并账号
     */
    private void onMergeAccount() {
        if (MyApplication.getMyApp().isMergeAccount() && ConstantValue.isAccountLogin() && !ConstantValue.isNoMoreReminders()) {
            MergeAccountDialog dialog = new MergeAccountDialog();
            dialog.show(getSupportFragmentManager(), new MergeAccountDialog.onMergeAccountChange() {
                @Override
                public void onSendCode() {
                    onSendPhoneCode();
                }

                @Override
                public void onMergeAccount() {
                    if (groupDataBeans.size() > 0) {
                        ivMoreFunction.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onShowMergeGroupSelect();
                            }
                        }, 300);
                    } else {
                        getDeviceGroupList();
                    }
                }
            });
        }
    }

    /**
     * 合并账号，选择分组合并
     */
    private void onShowMergeGroupSelect() {
        GroupSelectDialog dialog = new GroupSelectDialog();
        dialog.show(getSupportFragmentManager(), groupDataBeans, new GroupSelectDialog.onGroupSelectferDeviceChange() {
            @Override
            public void onGroupSelect(String sid) {
                submitMergeAccount(sid);
            }

            @Override
            public void onCancel() {
                onShowMergeAccount();
            }
        });
    }

    /**
     * 发送验证码
     */
    private void onSendPhoneCode() {
        PhoneCodePutBean.ParamsBean paramsBean = new PhoneCodePutBean.ParamsBean();
        paramsBean.setCode(Api.Mob_Module_Code);
        paramsBean.setKey(Api.Mob_App_Key);
        paramsBean.setPhone(ConstantValue.getAccount());
        paramsBean.setZone(mPhoneZone);

        PhoneCodePutBean bean = new PhoneCodePutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Phone_Code);
        bean.setModule(ModuleValueService.Module_For_Phone_Code);

        if (getPresenter() != null){
            getPresenter().getPhoneCode(bean);
        }
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
            paramsBean.setGet_all(true);

            DeviceGroupPutBean bean = new DeviceGroupPutBean();
            bean.setParams(paramsBean);
            bean.setFunc(ModuleValueService.Fuc_For_Device_Group_List);
            bean.setModule(ModuleValueService.Module_For_Device_Group_List);

            if (getPresenter() != null){
                getPresenter().getDeviceGroupList(bean);
            }
        }
    }

    /**
     * 显示合并账号框
     */
    private void onShowMergeAccount() {
        ivMoreFunction.postDelayed(new Runnable() {
            @Override
            public void run() {
                onMergeAccount();
            }
        }, 500);
    }

    /**
     * 提交合并账号
     *
     * @param gid
     */
    private void submitMergeAccount(String gid) {
        MergeAccountPutBean.ParamsBean paramsBean = new MergeAccountPutBean.ParamsBean();
        paramsBean.setKey(Api.Mob_App_Key);
        paramsBean.setSgid(gid);

        MergeAccountPutBean bean = new MergeAccountPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Merge_Account);
        bean.setModule(ModuleValueService.Module_For_Merge_Account);

        if (getPresenter() != null){
            getPresenter().submitMergeAccount(bean);
        }
    }

    /**
     * 检测app版本更新
     */
    private void checkAppUpdate() {
        CheckAppUpdatePutBean.ParamsBean paramsBean = new CheckAppUpdatePutBean.ParamsBean();
        paramsBean.setVersion(AppUtils.getAppVersionCode());
        paramsBean.setApp_type(Api.Check_Version_Type);

        CheckAppUpdatePutBean bean = new CheckAppUpdatePutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_App_Version);
        bean.setModule(ModuleValueService.Module_For_App_Version);

        if (getPresenter() != null) {
            getPresenter().getAppUpdate(bean);
        }
    }

    private void switchPages(int index) {
        if (mIndexPage == index){
            return;
        }
        mIndexPage = index;
        MyApplication.getMyApp().setCheck_fragment(index);
        rlLocationMode.setVisibility(index == 2 ? View.GONE : View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        for (int i = 0, j = mFragments.size(); i < j; i++) {
            if (i == index) {
                continue;
            }
            fragment = mFragments.get(i);
            if (fragment.isAdded()) {
                fragmentTransaction.hide(fragment);
            }
        }
        fragment = mFragments.get(index);
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.frame_pager, fragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
        // 发送广播，刷新罗盘设备列表
        BroadcastReceiverUtil.showCompassDeviceList(this, index);
        if (index == 2){
            // 发送广播，刷新设备列表
            BroadcastReceiverUtil.showDeviceList(this);
        }
        // 发送广播，通知地图页，目前切换了页面了
        BroadcastReceiverUtil.onDeviceDetailRunStop(this, 0, index);
    }

    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTitles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);

                // load custom layout
                View customLayout = LayoutInflater.from(context).inflate(R.layout.layout_main_tab, null);
                TextView titleText = (TextView) customLayout.findViewById(R.id.tv_tab_title);
                titleText.setText(mTitles.get(index));
                commonPagerTitleView.setContentView(customLayout);

                commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

                    @Override
                    public void onSelected(int index, int totalCount) {
                        titleText.setTextColor(Color.WHITE);
                        titleText.setBackgroundColor(getResources().getColor(R.color.color_3D8AF7));
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        titleText.setTextColor(getResources().getColor(R.color.color_666666));
                        titleText.setBackgroundColor(getResources().getColor(R.color.color_FFFFFF));
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

                    }
                });

                commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFragmentContainerHelper.handlePageSelected(index);
                        switchPages(index);
                    }
                });

                return commonPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(magicIndicator);
    }

    @OnClick({R.id.iv_more_function, R.id.btn_wireless})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()) {
            switch (view.getId()) {
                case R.id.iv_more_function:
                    launchActivity(SettingActivity.newInstance());
                    break;
                case R.id.btn_wireless:
                    onCheckAppLocationMode();
                    break;
            }
        }
    }

    /**
     * 切换定位模式
     */
    private void onCheckAppLocationMode(){
        SPUtils.getInstance().put(ConstantValue.APP_Location_Mode, 0);
        ActivityUtils.finishAllActivities();
        launchActivity(MainWirelessActivity.newInstance());
        MyApplication.getMyApp().setDevice_imei("");
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void onNetWorkLocation() {
        btnWireless.setBackground(getResources().getDrawable(R.drawable.bg_ffffff_fillet_3));
        btnWireless.setTextColor(getResources().getColor(R.color.color_333333));
        btnNetwork.setBackground(getResources().getDrawable(R.drawable.bg_1464f6_fillet_3));
        btnNetwork.setTextColor(getResources().getColor(R.color.color_FFFFFF));
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
    public void onBackPressed() {
        if (System.currentTimeMillis() - system_time > 2000) {
            ArmsUtils.snackbarText(getString(R.string.exit_app));
            system_time = System.currentTimeMillis();
        } else {
            ActivityUtils.finishAllActivities();
            super.onBackPressed();
        }
    }

    // ------------------- 分割线：发起动态权限申请  ------------------------

    /**
     * @param permissions
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        ArrayList<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private ArrayList<String> findDeniedPermissions(String[] permissions) {
        ArrayList<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NotNull String[] permissions, @NotNull int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            isNeedCheck = !verifyPermissions(paramArrayOfInt);
            if (isAuth && isNeedCheck) {
                showMissingPermissionDialog();
            }
            isAuth = false;
            SPUtils.getInstance().put(ConstantValue.Is_Auth_agree, true);
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tip);
        builder.setMessage(R.string.not_permission);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.setPositiveButton(R.string.setting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                        dialog.dismiss();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == INSTALL_PERMISS_CODE) {
            // 发起安装应用
            mHandler.postDelayed(new Runnable() {
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
                        if (btnWireless != null) {
                            onBackPressed();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void getPhoneCodeSuccess(PhoneCodeResultBean phoneCodeResultBean) {
        ToastUtils.show(getString(R.string.send_success));
    }

    @Override
    public void getDeviceGroupListSuccess(DeviceGroupResultBean deviceGroupResultBean) {
        groupDataBeans.clear();
        if (deviceGroupResultBean.getGarages() != null && deviceGroupResultBean.getGarages().size() > 0) {
            groupDataBeans.addAll(deviceGroupResultBean.getGarages());
            ivMoreFunction.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onShowMergeGroupSelect();
                }
            }, 300);
        }
    }

    @Override
    public void submitMergeAccountSuccess(MergeAccountResultBean mergeAccountResultBean) {
        MyApplication.getMyApp().setBeforeAccount(false);
        MyApplication.getMyApp().setMergeAccount(false);
        if (mergeAccountResultBean.getErrcode() == Api.Mobile_Code_Error) {
            onShowMergeAccount();
        } else if (mergeAccountResultBean.isSuccess()) {
            if (mergeAccountResultBean.getFail_item() != null && mergeAccountResultBean.getFail_item().size() > 0) {
                MergeAccountFailDialog dialog = new MergeAccountFailDialog();
                dialog.show(getSupportFragmentManager(), mergeAccountResultBean.getFail_item());
            } else {
                ToastUtils.show(getString(R.string.merge_success));
            }
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

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        LocalBroadcastManager.getInstance(MyApplication.getMyApp()).unregisterReceiver(receiver);
        super.onDestroy();
    }
}
