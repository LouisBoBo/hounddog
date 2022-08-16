package com.slxk.hounddog.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.mob.MobSDK;
import com.mob.tools.utils.DeviceHelper;
import com.slxk.hounddog.R;
import com.slxk.hounddog.di.component.DaggerRegisterNextComponent;
import com.slxk.hounddog.mvp.contract.RegisterNextContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.PhoneAreaResultBean;
import com.slxk.hounddog.mvp.model.bean.PhoneCodeResultBean;
import com.slxk.hounddog.mvp.model.bean.RegisterResultBean;
import com.slxk.hounddog.mvp.model.putbean.PhoneCodePutBean;
import com.slxk.hounddog.mvp.model.putbean.RegisterPutBean;
import com.slxk.hounddog.mvp.presenter.RegisterNextPresenter;
import com.slxk.hounddog.mvp.ui.view.ClearEditText;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.PhoneHasBindDialog;

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
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/16/2022 17:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class RegisterNextActivity extends BaseActivity<RegisterNextPresenter> implements RegisterNextContract.View {

    @BindView(R.id.edt_code)
    ClearEditText edtCode;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.ll_phone_code)
    LinearLayout llPhoneCode;
    @BindView(R.id.edt_password)
    ClearEditText edtPassword;
    @BindView(R.id.edt_password_confirm)
    ClearEditText edtPasswordConfirm;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.view_phone_code)
    View viewPhoneCode;

    private Disposable disposable; // 验证码倒计时
    private String opToken = "";
    private String operator = "";
    private String phoneOperator = "";
    private String token = "";
    private String md5 = "";
    private String mPhone = "";
    private String code;
    private String phoneZone;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRegisterNextComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_register_next;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.register));
        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().hasExtra("opToken")) {
                opToken = getIntent().getStringExtra("opToken");
            }
            if (getIntent().hasExtra("operator")) {
                operator = getIntent().getStringExtra("operator");
            }
            if (getIntent().hasExtra("phoneOperator")) {
                phoneOperator = getIntent().getStringExtra("phoneOperator");
            }
            if (getIntent().hasExtra("token")) {
                token = getIntent().getStringExtra("token");
            }
            if (getIntent().hasExtra("phone")) {
                mPhone = getIntent().getStringExtra("phone");
            }
            if (getIntent().hasExtra("phoneZone")) {
                phoneZone = getIntent().getStringExtra("phoneZone");
            }
        }
        md5 = DeviceHelper.getInstance(MobSDK.getContext()).getSignMD5();
        if (mPhone.length() == 0) {
            llPhoneCode.setVisibility(View.GONE);
            viewPhoneCode.setVisibility(View.GONE);
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

    @OnClick({R.id.tv_send_code, R.id.btn_register})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()) {
            switch (view.getId()) {
                case R.id.tv_send_code: // 发送验证码
                    onSendPhoneCode();
                    break;
                case R.id.btn_register: // 注册
                    submitRegister();
                    break;
            }
        }
    }

    /**
     * 发送验证码
     */
    private void onSendPhoneCode() {
        if (mPhone.length() == 0) return;
        PhoneCodePutBean.ParamsBean paramsBean = new PhoneCodePutBean.ParamsBean();
        paramsBean.setCode(Api.Mob_Module_Code);
        paramsBean.setKey(Api.Mob_App_Key);
        paramsBean.setPhone(mPhone);
        paramsBean.setZone(phoneZone);
        if(Utils.getLocaleLanguageShorthand().length()!=0){
            paramsBean.setLang(Utils.getLocaleLanguageShorthand());
        }

        PhoneCodePutBean bean = new PhoneCodePutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Phone_Code);
        bean.setModule(ModuleValueService.Module_For_Phone_Code);

        if (getPresenter() != null) {
            getPresenter().getPhoneCode(bean);
        }
    }

    /**
     * 提交注册
     */
    private void submitRegister() {
        String password = edtPassword.getText().toString().trim();
        String passwordConfirm = edtPasswordConfirm.getText().toString().trim();
        if (mPhone.length() != 0) {
            code = edtCode.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                ToastUtils.showShort(getString(R.string.input_code_hint));
                return;
            }
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort(getString(R.string.input_password));
            return;
        }
        if (TextUtils.isEmpty(passwordConfirm)) {
            ToastUtils.showShort(getString(R.string.input_password_confirm));
            return;
        }
        if (!passwordConfirm.equals(password)) {
            ToastUtils.showShort(getString(R.string.password_error_hint));
            return;
        }

        RegisterPutBean.ParamsBean.InfoBean infoBean = new RegisterPutBean.ParamsBean.InfoBean();
        if (mPhone.length() != 0) {
            infoBean.setPhone(mPhone);
        }
        infoBean.setPwd(password);

        RegisterPutBean.ParamsBean paramsBean = new RegisterPutBean.ParamsBean();
        paramsBean.setInfo(infoBean);
        if (mPhone.length() != 0) {
            paramsBean.setCode(code);
        }
        paramsBean.setKey(Api.Mob_App_Key);
        paramsBean.setType(Api.App_Type);
        paramsBean.setZone(phoneZone);

        RegisterPutBean bean = new RegisterPutBean();
        bean.setFunc(ModuleValueService.Fuc_For_Register);
        bean.setModule(ModuleValueService.Module_For_Register);
        bean.setParams(paramsBean);

        if (getPresenter() != null) {
            getPresenter().submitRegister(bean);
        }
    }

    @Override
    public void submitRegisterSuccess(RegisterResultBean baseBean) {
        try {
            if (baseBean.isSuccess()) { //成功
                ToastUtils.showShort(getString(R.string.register_success));
                String phoneResult = baseBean.getPhone();
                String account = mPhone;
                if (mPhone.length() == 0 && !TextUtils.isEmpty(phoneResult)) {
                    account = phoneResult;
                }
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("account", account);
                intent.putExtra("password", edtPassword.getText().toString().trim());
                startActivity(intent);
                finish();
            } else {
                if (baseBean.getErrcode() == Api.Mobile_Code_Error) { //验证码错误
                    ToastUtils.showShort(baseBean.getError_message());
                } else if (baseBean.getErrcode() == Api.Mobile_Bind_Used) { //手机号已绑定
                    onPhoneHasBinding();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该手机号码已经绑定了账号
     */
    private void onPhoneHasBinding() {
        PhoneHasBindDialog dialog = new PhoneHasBindDialog();
        dialog.show(getSupportFragmentManager(), new PhoneHasBindDialog.onPhoneHasBindChange() {
            @Override
            public void onRegister() {
                Intent intent = new Intent();
                intent.setClass(RegisterNextActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLogin() {
                launchActivity(LoginActivity.newInstance(0, ""));
                finish();
            }
        });
    }


    @Override
    public void getPhoneAreaSuccess(PhoneAreaResultBean phoneAreaResultBean) {

    }

    @Override
    public void getPhoneCodeSuccess(PhoneCodeResultBean phoneCodeResultBean) {
        ToastUtils.showShort(getString(R.string.send_success));
        countDownTime();
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
