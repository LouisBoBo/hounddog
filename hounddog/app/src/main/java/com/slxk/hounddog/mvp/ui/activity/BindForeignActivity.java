package com.slxk.hounddog.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.di.component.DaggerBindForeignComponent;
import com.slxk.hounddog.mvp.contract.BindForeignContract;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.ModifyPasswordPutBean;
import com.slxk.hounddog.mvp.presenter.BindForeignPresenter;
import com.slxk.hounddog.mvp.ui.view.ClearEditText;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.MD5Utils;
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
 * Created by MVPArmsTemplate on 03/09/2022 18:11
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class BindForeignActivity extends BaseActivity<BindForeignPresenter> implements BindForeignContract.View {

    @BindView(R.id.edt_password)
    ClearEditText edtPassword;
    @BindView(R.id.edt_password_confirm)
    ClearEditText edtPasswordConfirm;
    @BindView(R.id.btn_bind)
    Button btnBind;

    private String mSid;
    private String mPassword; // 密码
    private boolean isForeign;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBindForeignComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_bind_foreign;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.modify_password));
        mSid = getIntent().getStringExtra("sid");
        mPassword = getIntent().getStringExtra("password");
        isForeign = getIntent().getBooleanExtra("foreign", false);
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

    @OnClick(R.id.btn_bind)
    public void onViewClicked() {
        if (Utils.isButtonQuickClick()) {
            hideKeyboard(edtPassword);
            submitBindMobile(false);
        }
    }

    /**
     * 提交绑定
     *
     * @param isChangeBind 是否更换手机号码绑定的imei号为当前imei号,默认false
     */
    private void submitBindMobile(boolean isChangeBind) {
        String password = edtPassword.getText().toString().trim();
        String passwordConfirm = edtPasswordConfirm.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort(getString(R.string.input_new_password));
            return;
        }
        if (TextUtils.isEmpty(passwordConfirm)) {
            ToastUtils.showShort(getString(R.string.input_new_password_confirm));
            return;
        }
        if (!passwordConfirm.equals(password)) {
            ToastUtils.showShort(getString(R.string.password_error_hint));
            return;
        }

        ModifyPasswordPutBean.ParamsBean.InfoBean infoBean = new ModifyPasswordPutBean.ParamsBean.InfoBean();
        infoBean.setPwd(password);
        ModifyPasswordPutBean.ParamsBean paramsBean = new ModifyPasswordPutBean.ParamsBean();
        paramsBean.setInfo(infoBean);
        if (isChangeBind) {
            paramsBean.setChange_bind(true);
        }
        paramsBean.setPwd_md5(MD5Utils.getMD5Encryption(mPassword));
        if (isForeign)
            paramsBean.setCheck_phone(false); // 国外设备号 登入传 false
        ModifyPasswordPutBean bean = new ModifyPasswordPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Set_Account);
        bean.setModule(ModuleValueService.Module_For_Set_Account);

        if (getPresenter() != null) {
            getPresenter().submitBindMobile(bean, mSid, isChangeBind);
        }
    }

    @Override
    public void submitBindMobileSuccess(BaseBean baseBean, boolean isChangeBind) {
        if (baseBean.isSuccess()) {
            SPUtils.getInstance().put(ConstantValue.Is_Need_Check, false);
            if (ConstantValue.isSavePassword()) {
                SPUtils.getInstance().put(ConstantValue.PASSWORD, edtPassword.getText().toString().trim());
            }
            ToastUtils.showShort(getString(R.string.modify_success));
            Intent intent = new Intent(BindForeignActivity.this, LoginActivity.class);
            intent.putExtra("password", edtPassword.getText().toString().trim());
            startActivity(intent);
            finish();
        } else {
            ToastUtils.showShort(baseBean.getError_message());
        }
    }

}
