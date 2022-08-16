package com.slxk.hounddog.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.di.component.DaggerForgetPasswordComponent;
import com.slxk.hounddog.mvp.contract.ForgetPasswordContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.ForgetPasswordResultBean;
import com.slxk.hounddog.mvp.model.bean.PhoneAreaResultBean;
import com.slxk.hounddog.mvp.model.putbean.ForgetPasswordPutBean;
import com.slxk.hounddog.mvp.model.putbean.PhoneAreaPutBean;
import com.slxk.hounddog.mvp.presenter.ForgetPasswordPresenter;
import com.slxk.hounddog.mvp.ui.view.ClearEditText;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.ForgetPasswordAccountListDialog;
import com.slxk.hounddog.mvp.weiget.PhoneAreaDialog;

import java.util.ArrayList;
import java.util.List;

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
 * Created by MVPArmsTemplate on 02/17/2022 16:44
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class ForgetPasswordActivity extends BaseActivity<ForgetPasswordPresenter> implements ForgetPasswordContract.View {

    @BindView(R.id.tv_phone_area)
    TextView tvPhoneArea;
    @BindView(R.id.ll_mobile_area)
    LinearLayout llMobileArea;
    @BindView(R.id.edt_mobile)
    ClearEditText edtMobile;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private String mPhoneZone = "86"; // 电话号码区号
    private List<PhoneAreaResultBean.ItemsBean> phoneAreaBeans; // 手机号码国际区号

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerForgetPasswordComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_forget_password;//setContentView(id);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.retrieve_password));

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

    @OnClick({R.id.ll_mobile_area, R.id.btn_confirm})
    public void onViewClicked(View view) {
        if (Utils.isButtonQuickClick()){
            switch (view.getId()) {
                case R.id.ll_mobile_area: // 手机号码区号
                    onPhoneAreaSelect();
                    break;
                case R.id.btn_confirm: // 提交
                    hideKeyboard(edtMobile);
                    submitForgetPassword("");
                    break;
            }
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
     * 确认提交
     */
    private void submitForgetPassword(String flag) {
        String mobile = edtMobile.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.show(getString(R.string.input_phone));
            return;
        }
        ForgetPasswordPutBean.ParamsBean paramsBean = new ForgetPasswordPutBean.ParamsBean();
        paramsBean.setCode("000000");
        paramsBean.setKey(Api.Mob_App_Key);
        paramsBean.setNew_pwd("123456");
        paramsBean.setPhone(mobile);
        paramsBean.setZone(mPhoneZone);
        paramsBean.setType(Api.App_Type);
        if (!TextUtils.isEmpty(flag)) {
            paramsBean.setFlag(flag);
        }
        if (Utils.getLocaleLanguageShorthand().length() != 0) {
            paramsBean.setLang(Utils.getLocaleLanguageShorthand());
        }
        ForgetPasswordPutBean bean = new ForgetPasswordPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Forget_Password);
        bean.setModule(ModuleValueService.Module_For_Forget_Password);

        if (getPresenter() != null) {
            getPresenter().submitForgetPassword(bean);
        }
    }

    @Override
    public void getPhoneAreaSuccess(PhoneAreaResultBean phoneAreaResultBean) {
        phoneAreaBeans.clear();
        phoneAreaBeans.addAll(phoneAreaResultBean.getItems());
    }

    @Override
    public void submitForgetPasswordSuccess(ForgetPasswordResultBean forgetPasswordResultBean) {
        if (forgetPasswordResultBean.getFlag() != null && forgetPasswordResultBean.getFlag().size() > 1) {
            ForgetPasswordAccountListDialog dialog = new ForgetPasswordAccountListDialog();
            dialog.show(getSupportFragmentManager(), forgetPasswordResultBean.getFlag(), new ForgetPasswordAccountListDialog.onLoginAccountChange() {
                @Override
                public void onAccountInfo(String accountInfo) {
                    goToNext(accountInfo);
                }
            });
        } else {
            if (forgetPasswordResultBean.getErrcode() == Api.Mobile_Code_Error) {
                goToNext("");
            }
        }
    }

    private void goToNext(String flag) {
        Intent intent = new Intent(this, ForgetPasswordNextActivity.class);
        intent.putExtra("phone", edtMobile.getText().toString().trim());
        intent.putExtra("phoneZone", mPhoneZone);
        intent.putExtra("flag", flag);
        startActivity(intent);
    }

}
