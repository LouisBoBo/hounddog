package com.slxk.hounddog.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.di.component.DaggerModifyPhoneComponent;
import com.slxk.hounddog.mvp.contract.ModifyPhoneContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.PhoneAreaResultBean;
import com.slxk.hounddog.mvp.model.bean.PhoneCodeResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.ModifyPasswordPutBean;
import com.slxk.hounddog.mvp.model.putbean.PhoneAreaPutBean;
import com.slxk.hounddog.mvp.model.putbean.PhoneCodePutBean;
import com.slxk.hounddog.mvp.presenter.ModifyPhonePresenter;
import com.slxk.hounddog.mvp.ui.view.ClearEditText;
import com.slxk.hounddog.mvp.utils.BluetoothManagerUtil;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.OfflineMapDownLoadUtil;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.PhoneAreaDialog;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description: 修改绑定手机号码
 * <p>
 * Created by MVPArmsTemplate on 03/04/2022 14:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class ModifyPhoneActivity extends BaseActivity<ModifyPhonePresenter> implements ModifyPhoneContract.View {

    @BindView(R.id.tv_phone_area)
    TextView tvPhoneArea;
    @BindView(R.id.ll_mobile_area)
    LinearLayout llMobileArea;
    @BindView(R.id.edt_mobile)
    ClearEditText edtMobile;
    @BindView(R.id.edt_code)
    ClearEditText edtCode;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.btn_bind)
    Button btnBind;

    private String mPhoneZone = "86"; // 电话号码区号
    private ArrayList<PhoneAreaResultBean.ItemsBean> phoneAreaBeans; // 手机号码国际区号
    private Disposable disposable; // 验证码倒计时
    private String mBeforePhone; // 手机号码，用来判断上一个输入的手机号码跟当前手机号码是不是相同，当手机号码被注册或者绑定的时候

    public static Intent newInstance() {
        return new Intent(MyApplication.getMyApp(), ModifyPhoneActivity.class);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerModifyPhoneComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_modify_phone;//setContentView(id);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.modify_mobile));
        phoneAreaBeans = new ArrayList<>();
        tvPhoneArea.setText("+" + mPhoneZone);

        getPhoneArea();
    }

    /**
     * 获取手机号码国际区号
     */
    private void getPhoneArea() {
        PhoneAreaPutBean.ParamsBean paramsBean = new PhoneAreaPutBean.ParamsBean();
        PhoneAreaPutBean bean = new PhoneAreaPutBean();
        bean.setFunc(ModuleValueService.Fuc_For_Phone_Area);
        bean.setModule(ModuleValueService.Module_For_Phone_Area);
        bean.setParams(paramsBean);

        if (getPresenter() != null) {
            getPresenter().getPhoneArea(bean);
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

    @OnClick({R.id.ll_mobile_area, R.id.tv_send_code, R.id.btn_bind})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()) {
            hideKeyboard(edtCode);
            switch (view.getId()) {
                case R.id.ll_mobile_area:
                    onPhoneAreaSelect();
                    break;
                case R.id.tv_send_code:
                    onSendPhoneCode();
                    break;
                case R.id.btn_bind:
                    submitBindMobile();
                    break;
            }
        }
    }

    /**
     * 提交绑定
     */
    private void submitBindMobile() {
        String mobile = edtMobile.getText().toString().trim();
        String code = edtCode.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.show(getString(R.string.input_new_phone));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.show(getString(R.string.input_code_hint));
            return;
        }

        ModifyPasswordPutBean.ParamsBean.InfoBean infoBean = new ModifyPasswordPutBean.ParamsBean.InfoBean();
        infoBean.setPhone(mobile);

        ModifyPasswordPutBean.ParamsBean paramsBean = new ModifyPasswordPutBean.ParamsBean();
        paramsBean.setInfo(infoBean);
        paramsBean.setCode(code);
        paramsBean.setZone(mPhoneZone);
        paramsBean.setKey(Api.Mob_App_Key);

        ModifyPasswordPutBean bean = new ModifyPasswordPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Set_Account);
        bean.setModule(ModuleValueService.Module_For_Set_Account);

        if (getPresenter() != null) {
            getPresenter().submitBindMobile(bean);
        }
    }

    /**
     * 选择手机号码国际区号
     */
    private void onPhoneAreaSelect() {
        if (phoneAreaBeans.size() == 0) {
            getPhoneArea();
        } else {
            PhoneAreaDialog dialog = new PhoneAreaDialog();
            dialog.show(getSupportFragmentManager(), phoneAreaBeans, new PhoneAreaDialog.onPhoneAreaChange() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onPhoneArea(int zone) {
                    mPhoneZone = zone + "";
                    tvPhoneArea.setText("+" + mPhoneZone);
                }
            });
        }
    }

    /**
     * 发送验证码
     */
    private void onSendPhoneCode() {
        String mobile = edtMobile.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.show(getString(R.string.input_phone_hint));
            return;
        }
        if (!TextUtils.isEmpty(mBeforePhone) && mBeforePhone.equals(mobile)){
            ToastUtils.show(getString(R.string.mobile_register_error));
            return;
        }

        PhoneCodePutBean.ParamsBean paramsBean = new PhoneCodePutBean.ParamsBean();
        paramsBean.setCode(Api.Mob_Module_Code);
        paramsBean.setKey(Api.Mob_App_Key);
        paramsBean.setPhone(mobile);
        paramsBean.setZone(mPhoneZone);

        PhoneCodePutBean bean = new PhoneCodePutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Phone_Code);
        bean.setModule(ModuleValueService.Module_For_Phone_Code);

        if (getPresenter() != null) {
            getPresenter().getPhoneCode(bean);
        }
    }

    @Override
    public void getPhoneAreaSuccess(PhoneAreaResultBean phoneAreaResultBean) {
        phoneAreaBeans.clear();
        phoneAreaBeans.addAll(phoneAreaResultBean.getItems());
    }

    @Override
    public void getPhoneCodeSuccess(PhoneCodeResultBean phoneCodeResultBean) {
        ToastUtils.show(getString(R.string.errcode_success));
        countDownTime();
    }

    @Override
    public void submitBindMobileSuccess(BaseBean baseBean) {
        if (baseBean.isSuccess()){
            ToastUtils.show(getString(R.string.modify_success));
            if (ConstantValue.isLoginForBindMobile()){
                onClearData();
            }else{
                finish();
            }
        } else if (baseBean.getErrcode() == Api.Mobile_Bind_Used){
            mBeforePhone = edtMobile.getText().toString().trim();
        }else if (baseBean.getErrcode() == Api.Mobile_Code_Error){
            ToastUtils.show(baseBean.getError_message());
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
        launchActivity(LoginActivity.newInstance(1, edtMobile.getText().toString().trim()));
    }

    /**
     * 获取验证码倒计时
     */
    public void countDownTime() {
        final int count = 60;//倒计时10秒
        //ui线程中进行控件更新
        Observable<Long> countDownObservable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return count - aLong;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//ui线程中进行控件更新
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        tvSendCode.setEnabled(false);
                    }
                });

        //回复原来初始状态
        disposable = countDownObservable.subscribe(new Consumer<Long>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void accept(Long aLong) throws Exception {
                tvSendCode.setText(aLong + "s");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                //回复原来初始状态
                tvSendCode.setEnabled(true);
                tvSendCode.setText(getString(R.string.send_code));
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
        super.onDestroy();
    }
    
}
