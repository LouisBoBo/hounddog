package com.slxk.hounddog.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.di.component.DaggerSettingComponent;
import com.slxk.hounddog.mvp.contract.SettingContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.AlertBean;
import com.slxk.hounddog.mvp.model.bean.CheckAppUpdateBean;
import com.slxk.hounddog.mvp.model.bean.DeviceBaseResultBean;
import com.slxk.hounddog.mvp.model.bean.LogoutAccountResultBean;
import com.slxk.hounddog.mvp.model.bean.SettingFunctionBean;
import com.slxk.hounddog.mvp.model.bean.UnbindPhoneResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.CheckAppUpdatePutBean;
import com.slxk.hounddog.mvp.model.putbean.LogoutAccountPutBean;
import com.slxk.hounddog.mvp.model.putbean.SignOutPutBean;
import com.slxk.hounddog.mvp.model.putbean.UnbindPhonePutBean;
import com.slxk.hounddog.mvp.presenter.SettingPresenter;
import com.slxk.hounddog.mvp.ui.adapter.SettingAdapter;
import com.slxk.hounddog.mvp.utils.BluetoothManagerUtil;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.OfflineMapDownLoadUtil;
import com.slxk.hounddog.mvp.utils.ResultDataUtils;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.AlertAppDialog;
import com.slxk.hounddog.mvp.weiget.DeviceFailDialog;
import com.slxk.hounddog.mvp.weiget.UploadAppDialog;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/22/2021 15:11
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingContract.View {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_sign_out)
    TextView tvSignOut;

    private ArrayList<SettingFunctionBean> mFunctionBeans;
    private SettingAdapter mAdapter;

    private static final int INSTALL_PERMISS_CODE = 101; // 允许安装位置应用回调码
    private static final int INSTALL_APK_RESULT = 102; // apk安装情况，是否完成安装回调
    private String mFilePath; // 版本更新下载url地址
    private Handler mHandler;

    public static Intent newInstance() {
        return new Intent(MyApplication.getMyApp(), SettingActivity.class);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSettingComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_setting;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mFunctionBeans = new ArrayList<>();
        mHandler = new Handler();

        mFunctionBeans.add(new SettingFunctionBean(0, getString(R.string.connect_the_device), R.drawable.icon_setting_bluetooth));
//        mFunctionBeans.add(new SettingFunctionBean(1, getString(R.string.language_setting), R.drawable.icon_setting_language));
        mFunctionBeans.add(new SettingFunctionBean(5, getString(R.string.modify_password), R.drawable.icon_modify_password));
        if (ConstantValue.isCheckPhone()){
            mFunctionBeans.add(new SettingFunctionBean(6, getString(R.string.modify_mobile), R.drawable.icon_modify_mobile));
            if (!ConstantValue.isAccountLogin()) { //账号登录(包括手机号登录) 隐藏解绑手机号
                mFunctionBeans.add(new SettingFunctionBean(7, getString(R.string.unbind_mobile), R.drawable.icon_unbind_mobile));
            }
        }
        mFunctionBeans.add(new SettingFunctionBean(2, getString(R.string.user_manual), R.drawable.icon_setting_user_manual));
        mFunctionBeans.add(new SettingFunctionBean(8, getString(R.string.privacy_policy_user), R.drawable.icon_privacy_policy));
        mFunctionBeans.add(new SettingFunctionBean(3, getString(R.string.new_version_update), R.drawable.icon_setting_version));
        if (ConstantValue.isAccountLogin()){
            mFunctionBeans.add(new SettingFunctionBean(4, getString(R.string.logout_account), R.drawable.icon_setting_logout));
        }

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SettingAdapter(R.layout.item_setting, mFunctionBeans);
        recyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (Utils.isButtonQuickClick()) {
                    onFunctionClick(mFunctionBeans.get(position).getId());
                }
            }
        });
    }

    private void onFunctionClick(int id) {
        switch (id) {
            case 0:
                launchActivity(ConnectDeviceActivity.newInstance());
                break;
            case 1:
                break;
            case 2:
                launchActivity(WebViewActivity.newInstance(getString(R.string.user_manual), Api.User_Manual));
                break;
            case 3:
                checkAppUpdate();
                break;
            case 4:
                onLogoutAccount();
                break;
            case 5:
                launchActivity(ModifyPasswordActivity.newInstance());
                break;
            case 6:
                launchActivity(ModifyPhoneActivity.newInstance());
                break;
            case 7:
                onUnbindPhone();
                break;
            case 8:
                onSeePrivacyAgreement();
                break;
        }
    }

    /**
     * 查看用户隐私政策协议
     */
    private void onSeePrivacyAgreement() {
        launchActivity(WebViewActivity.newInstance(getString(R.string.privacy_policy_user), Api.Privacy_Policy));
    }

    /**
     * 解绑手机号码
     */
    private void onUnbindPhone(){
        AlertBean bean = new AlertBean();
        bean.setTitle(getString(R.string.tip));
        bean.setAlert(getString(R.string.unbind_phone_hint));
        bean.setConfirmTip(getString(R.string.confirm));
        bean.setCancelTip(getString(R.string.cancel));
        AlertAppDialog dialog = new AlertAppDialog();
        dialog.show(getSupportFragmentManager(), bean, new AlertAppDialog.onAlertDialogChange() {
            @Override
            public void onConfirm() {
                submitUnbindPhone();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 解绑手机号码
     */
    private void submitUnbindPhone(){
        UnbindPhonePutBean.ParamsBean paramsBean = new UnbindPhonePutBean.ParamsBean();

        UnbindPhonePutBean bean = new UnbindPhonePutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Unbind_Phone);
        if (ConstantValue.isAccountLogin()){
            bean.setModule(ModuleValueService.Module_For_Role_Transfer);
        }else{
            bean.setModule(ModuleValueService.Module_For_Unbind_Phone);
        }

        if (getPresenter() != null){
            getPresenter().submitUnbindPhone(bean);
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

        if (getPresenter() != null){
            getPresenter().getAppUpdate(bean);
        }
    }

    /**
     * 注销账号
     */
    private void onLogoutAccount(){
        AlertBean bean = new AlertBean();
        bean.setTitle(getString(R.string.tip));
        bean.setAlert(getString(R.string.logout_account_hint));
        bean.setConfirmTip(getString(R.string.confirm));
        bean.setCancelTip(getString(R.string.cancel));
        AlertAppDialog dialog = new AlertAppDialog();
        dialog.show(getSupportFragmentManager(), bean, new AlertAppDialog.onAlertDialogChange() {
            @Override
            public void onConfirm() {
                submitLogoutAccount();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 提交注销账号
     */
    private void submitLogoutAccount(){
        LogoutAccountPutBean bean = new LogoutAccountPutBean();
        bean.setFunc(ModuleValueService.Fuc_For_Logout_Account);
        bean.setModule(ModuleValueService.Module_For_Logout_Account);

        if (getPresenter() != null){
            getPresenter().submitLogoutAccount(bean);
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

    @OnClick({R.id.iv_close, R.id.tv_sign_out})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()){
            switch (view.getId()) {
                case R.id.iv_close:
                    finish();
                    break;
                case R.id.tv_sign_out:
                    onSignOut();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == INSTALL_PERMISS_CODE) {
                // 发起安装应用
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onInstallApkAuth(false);
                    }
                }, 1000);
            }
        }else if (requestCode == INSTALL_PERMISS_CODE) {
            // 发起安装应用
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onInstallApkAuth(false);
                }
            }, 1000);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 退出登录
     */
    private void onSignOut(){
        AlertBean bean = new AlertBean();
        bean.setTitle(getString(R.string.tip));
        bean.setAlert(getString(R.string.sign_out_hint));
        AlertAppDialog dialog = new AlertAppDialog();
        dialog.show(getSupportFragmentManager(), bean, new AlertAppDialog.onAlertDialogChange() {
            @Override
            public void onConfirm() {
                submitSignOut();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 提交退出登录
     */
    private void submitSignOut(){
        SignOutPutBean.ParamsBean paramsBean = new SignOutPutBean.ParamsBean();

        SignOutPutBean bean = new SignOutPutBean();
        bean.setFunc(ModuleValueService.Fuc_For_Sign_Out);
        bean.setModule(ModuleValueService.Module_For_Sign_Out);
        bean.setParams(paramsBean);

        if (getPresenter() != null){
            getPresenter().submitSignOut(bean);
        }
    }

    /**
     * 清除缓存信息
     */
    private void onClearData(){
        MyApplication.getMyApp().clearData();
        //清除缓存信息
        SPUtils.getInstance().remove(ConstantValue.USER_SID);
        SPUtils.getInstance().remove(ConstantValue.Push_Switch);
        SPUtils.getInstance().remove(ConstantValue.USER_LOGIN_TYPE);
        SPUtils.getInstance().remove(ConstantValue.Family_Sid);
        SPUtils.getInstance().remove(ConstantValue.Push_Family);
        SPUtils.getInstance().remove(ConstantValue.Family_Auth);
        SPUtils.getInstance().remove(ConstantValue.HUAWEI_TOKEN);

        BluetoothManagerUtil.getInstance().onDestroy();
        OfflineMapDownLoadUtil.getInstance().onDestroy();
        ActivityUtils.finishAllActivities();
        launchActivity(LoginActivity.newInstance(0, ""));
    }

    @Override
    public void submitSignOutSuccess(BaseBean baseBean) {
        if (ConstantValue.getPushMpm().equals(ResultDataUtils.Push_XiaoMi)){
            initDeleteXiaoMiPushAlias();
        }
        onClearData();
    }

    /**
     * 删除小米推送的Alias
     */
    private void initDeleteXiaoMiPushAlias(){
        String familyid = ConstantValue.getPushFamily();
        MiPushClient.unsetAlias(this, familyid, null);
    }

    @Override
    public void submitLogoutAccountSuccess(LogoutAccountResultBean logoutAccountResultBean) {
        ToastUtils.show(getString(R.string.logout_account_success));
        SPUtils.getInstance().remove(ConstantValue.ACCOUNT);
        SPUtils.getInstance().remove(ConstantValue.PASSWORD);
        SPUtils.getInstance().remove(ConstantValue.IS_SAVE_PASSWORD);
        onClearData();
    }

    @Override
    public void submitUnbindPhoneSuccess(UnbindPhoneResultBean unbindPhoneResultBean) {
        if (unbindPhoneResultBean.getFail_items() != null && unbindPhoneResultBean.getFail_items().size() > 0){
            List<DeviceBaseResultBean.FailItemsBean> failItemsBeans = new ArrayList<>();
            for (int i = 0; i < unbindPhoneResultBean.getFail_items().size(); i++){
                DeviceBaseResultBean.FailItemsBean bean = new DeviceBaseResultBean.FailItemsBean();
                bean.setImei(unbindPhoneResultBean.getFail_items().get(i).getImei());
                bean.setError_messageX(unbindPhoneResultBean.getFail_items().get(i).getError_messageX());
                failItemsBeans.add(bean);
            }
            DeviceFailDialog dialog = new DeviceFailDialog();
            dialog.show(getSupportFragmentManager(), failItemsBeans, 7);
        }else{
            ToastUtils.show(getString(R.string.operation_success));
            onClearData();
        }
    }

    @Override
    public void getAppUpdateSuccess(CheckAppUpdateBean checkAppUpdateBean) {
        SPUtils.getInstance().put(ConstantValue.Is_Get_Update_App, true);
        // 判断当前版本标识是不是小于服务器返回的版本标识
        if (!TextUtils.isEmpty(checkAppUpdateBean.getUrl())){
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

                }
            });
        }else{
            ToastUtils.show(getString(R.string.latest_version));
        }
    }
    
    // ------------------------------ 版本更新下载 --------------------------------------

    /**
     * 检测是否有安装权限，判断当前安卓版本是否大于等于8.0，8.0以上系统设置安装未知来源权限
     */
    private void applyInstallCheckApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 是否设置了安装权限
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
        Uri packageUrl = Uri.parse("package:" + MyApplication.getMyApp().getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUrl);
        startActivityForResult(intent, INSTALL_PERMISS_CODE);
    }

    /**
     * 检查安装权限，跳转权限设置页面
     * @param isIntentAuth true跳转，false不跳转
     */
    @SuppressLint("NewApi")
    private void onInstallApkAuth(boolean isIntentAuth){
        boolean isInstallPermission = MyApplication.getMyApp().getPackageManager().canRequestPackageInstalls();
        if (isIntentAuth){
            // 是否设置了安装权限
            if (isInstallPermission) {
                installApk();
            } else {
                installApkSetting();
            }
        }else{
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
                uri = FileProvider.getUriForFile(MyApplication.getMyApp(), "com.slxk.hounddog.fileprovider", file);
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
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        super.onDestroy();
    }
}
