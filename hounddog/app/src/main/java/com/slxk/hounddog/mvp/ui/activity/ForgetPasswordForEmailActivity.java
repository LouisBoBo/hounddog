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
import com.slxk.hounddog.di.component.DaggerForgetPasswordForEmailComponent;
import com.slxk.hounddog.mvp.contract.ForgetPasswordForEmailContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.ForgetPasswordResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.EmailCodePutBean;
import com.slxk.hounddog.mvp.model.putbean.ForgetPasswordPutBean;
import com.slxk.hounddog.mvp.presenter.ForgetPasswordForEmailPresenter;
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
 * Created by MVPArmsTemplate on 02/17/2022 16:44
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class ForgetPasswordForEmailActivity extends BaseActivity<ForgetPasswordForEmailPresenter> implements ForgetPasswordForEmailContract.View {

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
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private Disposable disposable; // 验证码倒计时

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerForgetPasswordForEmailComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_forget_password_for_email;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.retrieve_password));
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

    @OnClick({R.id.tv_send_code, R.id.btn_confirm})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()){
            switch (view.getId()) {
                case R.id.tv_send_code:
                    onSendEmailCode();
                    break;
                case R.id.btn_confirm:
                    hideKeyboard(edtAccount);
                    submitForgetPassword("");
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
     * 确认提交
     */
    private void submitForgetPassword(String flag){
        String account = edtAccount.getText().toString().trim().toLowerCase();
        String email = edtEmail.getText().toString().trim();
        String code = edtCode.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String passwordConfirm = edtPasswordConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(account)) {
            ToastUtils.show(getString(R.string.input_account_two_hint));
            return;
        }
        if (TextUtils.isEmpty(email)) {
            ToastUtils.show(getString(R.string.input_email));
            return;
        }
        if (!Utils.isEmail(email)) {
            ToastUtils.show(getString(R.string.email_error));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtils.show(getString(R.string.input_code_hint));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(getString(R.string.input_new_password));
            return;
        }
        if (TextUtils.isEmpty(passwordConfirm)) {
            ToastUtils.show(getString(R.string.input_new_password_confirm));
            return;
        }
        if (!passwordConfirm.equals(password)) {
            ToastUtils.show(getString(R.string.password_error_hint));
            return;
        }

        ForgetPasswordPutBean.ParamsBean paramsBean = new ForgetPasswordPutBean.ParamsBean();
        paramsBean.setAccount(account);
        paramsBean.setCode(code);
        paramsBean.setNew_pwd(password);
        paramsBean.setEmail(email);
        paramsBean.setType(Api.App_Type);
        if (!TextUtils.isEmpty(flag)){
            paramsBean.setFlag(flag);
        }

        ForgetPasswordPutBean bean = new ForgetPasswordPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Forget_Password);
        bean.setModule(ModuleValueService.Module_For_Forget_Password);

        if (getPresenter() != null){
            getPresenter().submitForgetPassword(bean);
        }
    }

    @Override
    public void getEmailCodeSuccess(BaseBean baseBean) {
        ToastUtils.show(getString(R.string.errcode_success));
        countDownTime();
    }

    @Override
    public void submitForgetPasswordSuccess(ForgetPasswordResultBean forgetPasswordResultBean) {
        setResult(Activity.RESULT_OK);
        ToastUtils.show(getString(R.string.modify_success));
        finish();
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
