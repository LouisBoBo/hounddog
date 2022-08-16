package com.slxk.hounddog.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.jess.arms.utils.RxLifecycleUtils;
import com.slxk.hounddog.mvp.contract.OperationRecordContract;
import com.slxk.hounddog.mvp.model.bean.OperationRecordResultBean;
import com.slxk.hounddog.mvp.model.putbean.OperationRecordPutBean;


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
@ActivityScope
public class OperationRecordPresenter extends BasePresenter<OperationRecordContract.Model, OperationRecordContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public OperationRecordPresenter (OperationRecordContract.Model model, OperationRecordContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取操作日志数据
     * @param bean
     * @param isShow
     * @param isRefresh
     */
    public void getOperationRecord(OperationRecordPutBean bean, boolean isShow, boolean isRefresh){
        mModel.getOperationRecord(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (isShow){
                        mRootView.showLoading();//显示下拉刷新的进度条
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (isShow){
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<OperationRecordResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(OperationRecordResultBean operationRecordResultBean) {
                        if (!isShow && isRefresh){
                            mRootView.finishRefresh();
                        }
                        if (operationRecordResultBean.isSuccess()){
                            mRootView.getOperationRecordSuccess(operationRecordResultBean, isRefresh);
                            if (operationRecordResultBean.getItems() != null){
                                if (operationRecordResultBean.getItems().size() == 0 || operationRecordResultBean.getItems().size() < bean.getParams().getLimit_size()) {
                                    mRootView.endLoadMore(0);//隐藏上拉加载更多的进度条
                                    mRootView.setNoMore(0);
                                }else{
                                    mRootView.endLoadMore(0);
                                }
                            }else{
                                mRootView.endLoadMore(0);//隐藏上拉加载更多的进度条
                                mRootView.setNoMore(0);
                            }
                        }else{
                            mRootView.endLoadFail(0);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.endLoadFail(0);
                    }

                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
