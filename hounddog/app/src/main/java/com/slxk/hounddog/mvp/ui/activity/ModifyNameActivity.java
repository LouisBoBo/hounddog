package com.slxk.hounddog.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.di.component.DaggerModifyNameComponent;
import com.slxk.hounddog.mvp.contract.ModifyNameContract;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceDetailModifyPutBean;
import com.slxk.hounddog.mvp.presenter.ModifyNamePresenter;
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
 * Description: 更名，修改设备名称
 * <p>
 * Created by MVPArmsTemplate on 12/23/2021 15:27
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class ModifyNameActivity extends BaseActivity<ModifyNamePresenter> implements ModifyNameContract.View {

    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    private String mName;
    private String mImei;
    private String mSimei;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerModifyNameComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_modify_name;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.menu_1));
        mName = getIntent().getStringExtra("name");
        mImei = getIntent().getStringExtra("imei");
        mSimei = getIntent().getStringExtra("simei");
        if (!TextUtils.isEmpty(mName)) {
            edtName.setText(mName);
            edtName.setSelection(edtName.length());
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

    @OnClick(R.id.btn_confirm)
    public void onViewClicked() {
        if (Utils.isButtonQuickClick()) {
            onConfirm();
        }
    }

    /**
     * 提交名称修改
     */
    private void onConfirm() {
        String name = edtName.getText().toString();
        if (!TextUtils.isEmpty(name) && name.length() > 8) {
            ToastUtils.show(getString(R.string.device_name_max_length));
            return;
        }

        DeviceDetailModifyPutBean.ParamsBean paramsBean = new DeviceDetailModifyPutBean.ParamsBean();
        paramsBean.setCar_number(name);
        if (!TextUtils.isEmpty(mSimei)) {
            paramsBean.setSimei(mSimei);
        }

        DeviceDetailModifyPutBean bean = new DeviceDetailModifyPutBean();
        bean.setFunc(ModuleValueService.Fuc_For_Modify_Device_Detail);
        bean.setModule(ModuleValueService.Module_For_Modify_Device_Detail);
        bean.setParams(paramsBean);

        if (getPresenter() != null) {
            getPresenter().submitDetailModify(bean);
        }
    }

    @Override
    public void submitDetailModifySuccess(BaseBean baseBean) {
        ToastUtils.show(getString(R.string.modify_success));
        mName = edtName.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("name", mName);
        setResult(Activity.RESULT_OK, intent);
    }

}
