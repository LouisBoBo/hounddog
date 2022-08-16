package com.slxk.hounddog.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.di.component.DaggerRegisterEmailComponent;
import com.slxk.hounddog.mvp.contract.RegisterEmailContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.RegisterResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.EmailCodePutBean;
import com.slxk.hounddog.mvp.model.putbean.RegisterPutBean;
import com.slxk.hounddog.mvp.presenter.RegisterEmailPresenter;
import com.slxk.hounddog.mvp.ui.view.ClearEditText;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;

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
 * Created by MVPArmsTemplate on 02/17/2022 16:30
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class RegisterEmailActivity extends BaseActivity<RegisterEmailPresenter> implements RegisterEmailContract.View {

    @BindView(R.id.edt_account)
    ClearEditText edtAccount;
    @BindView(R.id.edt_email)
    ClearEditText edtEmail;
    @BindView(R.id.edt_code)
    ClearEditText edtCode;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.edt_password)
    ClearEditText edtPassword;
    @BindView(R.id.edt_password_confirm)
    ClearEditText edtPasswordConfirm;
    @BindView(R.id.btn_register)
    Button btnRegister;

    private Disposable disposable; // 验证码倒计时
    private String mPassword; // 密码

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRegisterEmailComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_register_email;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.register_account));
        if (getIntent() != null){
            if (getIntent().hasExtra("password")){
                mPassword = getIntent().getStringExtra("password");
            }
        }
        if (!TextUtils.isEmpty(mPassword)){
            edtPassword.setText(mPassword);
            edtPasswordConfirm.setText(mPassword);
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
        if (Utils.isButtonQuickClick()){
            switch (view.getId()) {
                case R.id.tv_send_code:
                    onSendEmailCode();
                    break;
                case R.id.btn_register:
                    submitRegister();
                    break;
            }
        }
    }

    /**
     * 发送邮箱验证码
     */
    private void onSendEmailCode(){
        String email = edtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            ToastUtils.show(getString(R.string.input_email));
            return;
        }
        if (!TextUtils.isEmpty(email)) {
            if (!Utils.isEmail(email)) {
                ToastUtils.show(getString(R.string.email_error));
                return;
            }
        }

        EmailCodePutBean.ParamsBean paramsBean = new EmailCodePutBean.ParamsBean();
        paramsBean.setEmail(email);

        EmailCodePutBean bean = new EmailCodePutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Email_Code);
        bean.setModule(ModuleValueService.Module_For_Email_Code);

        if (getPresenter() != null){
            getPresenter().getEmailCode(bean);
        }
    }

    /**
     * 提交注册
     */
    private void submitRegister() {
        String account = edtAccount.getText().toString().trim().toLowerCase();
        String code = edtCode.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String passwordConfirm = edtPasswordConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(account)) {
            ToastUtils.show(getString(R.string.input_account_two_hint));
            return;
        }
        if (!TextUtils.isEmpty(email)) {
            if (!Utils.isEmail(email)) {
                ToastUtils.show(getString(R.string.email_error));
                return;
            }
            if (TextUtils.isEmpty(code)) {
                ToastUtils.show(getString(R.string.input_code_hint));
                return;
            }
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(getString(R.string.input_password));
            return;
        }
        if (TextUtils.isEmpty(passwordConfirm)) {
            ToastUtils.show(getString(R.string.input_password_confirm));
            return;
        }
        if (!passwordConfirm.equals(password)) {
            ToastUtils.show(getString(R.string.password_error_hint));
            return;
        }

        RegisterPutBean.ParamsBean.InfoBean infoBean = new RegisterPutBean.ParamsBean.InfoBean();
        infoBean.setAccount(account);
        if (!TextUtils.isEmpty(email)){
            infoBean.setEmail(email);
        }
        infoBean.setPwd(password);

        RegisterPutBean.ParamsBean paramsBean = new RegisterPutBean.ParamsBean();
        paramsBean.setInfo(infoBean);
        paramsBean.setCode(code);
        paramsBean.setType(Api.App_Type);
        paramsBean.setCheck_phone(false);

        RegisterPutBean bean = new RegisterPutBean();
        bean.setFunc(ModuleValueService.Fuc_For_Register);
        bean.setModule(ModuleValueService.Module_For_Register);
        bean.setParams(paramsBean);

        if (getPresenter() != null) {
            getPresenter().submitRegister(bean);
        }
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

    @Override
    public void submitRegisterSuccess(RegisterResultBean registerResultBean) {
        ToastUtils.show(getString(R.string.register_success));
        Intent intent = new Intent();
        intent.putExtra("account", edtAccount.getText().toString().trim());
        intent.putExtra("password", edtPassword.getText().toString().trim());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void getEmailCodeSuccess(BaseBean baseBean) {
        ToastUtils.show(getString(R.string.errcode_success));
        countDownTime();
    }

}
