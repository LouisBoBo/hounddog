package com.slxk.hounddog.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.di.component.DaggerFunctionMoreComponent;
import com.slxk.hounddog.mvp.contract.FunctionMoreContract;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.AlertBean;
import com.slxk.hounddog.mvp.model.bean.FunctionMoreBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.OnKeyFunctionPutBean;
import com.slxk.hounddog.mvp.presenter.FunctionMorePresenter;
import com.slxk.hounddog.mvp.ui.adapter.FunctionMoreAdapter;
import com.slxk.hounddog.mvp.utils.ConstantValue;
import com.slxk.hounddog.mvp.utils.DeviceUtils;
import com.slxk.hounddog.mvp.utils.ResultDataUtils;
import com.slxk.hounddog.mvp.utils.ToastUtils;
import com.slxk.hounddog.mvp.utils.Utils;
import com.slxk.hounddog.mvp.weiget.AlertAppDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/25/2021 11:44
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class FunctionMoreActivity extends BaseActivity<FunctionMorePresenter> implements FunctionMoreContract.View {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private ArrayList<FunctionMoreBean> mFunctionMoreBeans;
    private FunctionMoreAdapter mAdapter;

    private String mSimei = "";
    private String mImei;
    private boolean isDeviceReset; // 是否支持恢复出厂设置
    private static final String Intent_Imei_Key = "imei_key";
    private static final String Intent_Simei_key = "simei_key";
    private static final String Intent_Device_Reset = "device_reset";

    public static Intent newInstance(String imei, String simei, boolean isReset) {
        Intent intent = new Intent(MyApplication.getMyApp(), FunctionMoreActivity.class);
        intent.putExtra(Intent_Imei_Key, imei);
        intent.putExtra(Intent_Simei_key, simei);
        intent.putExtra(Intent_Device_Reset, isReset);
        return intent;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerFunctionMoreComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_function_more;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.menu_7));
        mFunctionMoreBeans = new ArrayList<>();
        mImei = getIntent().getStringExtra(Intent_Imei_Key);
        mSimei = getIntent().getStringExtra(Intent_Simei_key);
        isDeviceReset = getIntent().getBooleanExtra(Intent_Device_Reset, false);

        mFunctionMoreBeans.add(new FunctionMoreBean(0, getString(R.string.penetrate), R.drawable.icon_penetrate));
        mFunctionMoreBeans.add(new FunctionMoreBean(1, getString(R.string.sim_info), R.drawable.icon_sim_info));
        mFunctionMoreBeans.add(new FunctionMoreBean(2, getString(R.string.operation_record), R.drawable.icon_operation_record));
        if (isDeviceReset){
            mFunctionMoreBeans.add(new FunctionMoreBean(3, getString(R.string.reset), R.drawable.icon_reset));
        }

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FunctionMoreAdapter(R.layout.item_function_more, mFunctionMoreBeans);
        recyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (Utils.isButtonQuickClick()){
                    onFunctionClick(mFunctionMoreBeans.get(position).getId());
                }
            }
        });
    }

    /**
     * 功能点击
     * @param id
     */
    private void onFunctionClick(int id){
        switch (id){
            case 0:
                launchActivity(PenetrateActivity.newInstance(mImei, mSimei));
                break;
            case 1:
                launchActivity(SimDetailActivity.newInstance(mImei, mSimei));
                break;
            case 2:
                launchActivity(OperationRecordActivity.newInstance(mImei, mSimei));
                break;
            case 3:
                onConfirmOneKeyFunction();
                break;
        }
    }

    /**
     * 一键功能提交
     */
    private void onConfirmOneKeyFunction(){
        AlertBean bean = new AlertBean();
        bean.setTitle(getString(R.string.tip));
        bean.setAlert(getString(R.string.reset_device_hint));
        bean.setConfirmTip(getString(R.string.confirm));
        bean.setCancelTip(getString(R.string.cancel));
        AlertAppDialog dialog = new AlertAppDialog();
        dialog.show(getSupportFragmentManager(), bean, new AlertAppDialog.onAlertDialogChange() {
            @Override
            public void onConfirm() {
                submitOneKeyFunction();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 一键功能提交
     */
    private void submitOneKeyFunction(){
        OnKeyFunctionPutBean.ParamsBean paramsBean = new OnKeyFunctionPutBean.ParamsBean();
        paramsBean.setType(ResultDataUtils.Function_Reset);
        paramsBean.setSimei(mSimei);

        OnKeyFunctionPutBean bean = new OnKeyFunctionPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_OnKey_Function);
        bean.setModule(ModuleValueService.Module_For_OnKey_Function);

        if (getPresenter() != null){
            getPresenter().submitOneKeyFunction(bean);
        }
    }

    @Override
    public void submitOneKeyFunctionSuccess(BaseBean baseBean) {
        ToastUtils.show(getString(R.string.operation_success));
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

}
