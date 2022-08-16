package com.slxk.hounddog.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.slxk.hounddog.R;
import com.slxk.hounddog.app.MyApplication;
import com.slxk.hounddog.di.component.DaggerOperationRecordComponent;
import com.slxk.hounddog.mvp.contract.OperationRecordContract;
import com.slxk.hounddog.mvp.model.api.ModuleValueService;
import com.slxk.hounddog.mvp.model.bean.OperationRecordResultBean;
import com.slxk.hounddog.mvp.model.putbean.OperationRecordPutBean;
import com.slxk.hounddog.mvp.presenter.OperationRecordPresenter;
import com.slxk.hounddog.mvp.ui.adapter.OperationAdapter;
import com.slxk.hounddog.mvp.ui.view.MyLoadMoreView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/25/2021 15:08
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class OperationRecordActivity extends BaseActivity<OperationRecordPresenter> implements OperationRecordContract.View {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.srl_view)
    SwipeRefreshLayout srlView;

    private TextView tvPrompt; // 无数据布局
    private ArrayList<OperationRecordResultBean.ItemsBean> operationBeans;
    private OperationAdapter mAdapter;

    private long mLastTime = 0; // 获取到上一页日志的最后一条信息的时间戳
    private long mLastImei = 0; // 获取到上一页日志的最后一条信息的imei号
    private long mEndTime = 0; // 拉取日志的结束时间,精确到ms,可以不用填写,默认当前时间
    private long mBeginTime = 0; // 拉取日志的起始时间,精确到ms,可以不用填写
    private int limitSize = 20; // 每页最大加载条数
    private ArrayList<String> mSimeis;

    private String mImei;
    private String mSimei;
    private static final String Intent_Imei_Key = "imei_key";
    private static final String Intent_Simei_Key = "simei_key";

    public static Intent newInstance(String imei, String simei) {
        Intent intent = new Intent(MyApplication.getMyApp(), OperationRecordActivity.class);
        intent.putExtra(Intent_Imei_Key, imei);
        intent.putExtra(Intent_Simei_Key, simei);
        return intent;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerOperationRecordComponent.builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_operation_record;//setContentView(id);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle(getString(R.string.operation_record));
        operationBeans = new ArrayList<>();
        mSimeis = new ArrayList<>();
        mImei = getIntent().getStringExtra(Intent_Imei_Key);
        mSimei = getIntent().getStringExtra(Intent_Simei_Key);
        mSimeis.add(mSimei);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        srlView.setColorSchemeResources(R.color.color_00B4B7, R.color.color_00B4B7);
        srlView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLastImei = 0;
                mLastTime = 0;
                getOperationRecord(false, true);
            }
        });

        View statusView = LayoutInflater.from(this).inflate(R.layout.layout_status, recyclerView, false);
        tvPrompt = statusView.findViewById(R.id.txt_wrong_status);
        tvPrompt.setVisibility(View.GONE);

        mAdapter = new OperationAdapter(R.layout.item_operation_record, operationBeans);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setEmptyView(statusView);

        LoadMoreView loadMoreView = new MyLoadMoreView();
        mAdapter.setLoadMoreView(loadMoreView);
        mAdapter.setEnableLoadMore(true);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getOperationRecord(false, false);
            }
        }, recyclerView);

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                getOperationRecord(true, true);
            }
        }, 500);
    }

    /**
     * 获取操作记录数据
     *
     * @param isShow    是否弹出加载看
     * @param isRefresh 是否是刷新数据
     */
    private void getOperationRecord(boolean isShow, boolean isRefresh) {
        OperationRecordPutBean.ParamsBean paramsBean = new OperationRecordPutBean.ParamsBean();
        paramsBean.setLimit_size(limitSize);
        if (mLastTime != 0) {
            paramsBean.setLast_time(mLastTime);
        }
        if (mLastImei != 0) {
            paramsBean.setLast_imei(mLastImei);
        }
        if (mEndTime != 0) {
            paramsBean.setEnd_time(mEndTime);
        }
        if (mBeginTime != 0) {
            paramsBean.setBegin_time(mBeginTime);
        }
        if (mSimeis.size() > 0) {
            paramsBean.setSimeis(mSimeis);
        }
        OperationRecordPutBean bean = new OperationRecordPutBean();
        bean.setParams(paramsBean);
        bean.setFunc(ModuleValueService.Fuc_For_Operation_Record);
        bean.setModule(ModuleValueService.Module_For_Operation_Record);

        if (getPresenter() != null) {
            getPresenter().getOperationRecord(bean, isShow, isRefresh);
        }
    }

    @Override
    public void getOperationRecordSuccess(OperationRecordResultBean operationRecordResultBean, boolean isRefresh) {
        if (isRefresh) {
            operationBeans.clear();
        }
        if (operationRecordResultBean.getItems() != null && operationRecordResultBean.getItems().size() > 0) {
            mLastTime = operationRecordResultBean.getLast_time();
            mLastImei = operationRecordResultBean.getLast_imei();
            operationBeans.addAll(operationRecordResultBean.getItems());
        }
        mAdapter.notifyDataSetChanged();
        onShowNoData();
    }

    @Override
    public void finishRefresh() {
        srlView.setRefreshing(false);
    }

    @Override
    public void endLoadMore(int type) {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void setNoMore(int type) {
        mAdapter.loadMoreEnd();
    }

    @Override
    public void endLoadFail(int type) {
        mAdapter.loadMoreFail();
    }

    /**
     * 显示无数据提示
     */
    private void onShowNoData() {
        if (operationBeans.size() > 0) {
            tvPrompt.setVisibility(View.GONE);
        } else {
            tvPrompt.setVisibility(View.VISIBLE);
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
}
