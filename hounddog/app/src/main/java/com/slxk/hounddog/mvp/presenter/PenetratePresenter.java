package com.slxk.hounddog.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.slxk.hounddog.mvp.contract.PenetrateContract;
import com.slxk.hounddog.mvp.model.bean.DeviceDetailResultBean;
import com.slxk.hounddog.mvp.model.bean.PenetrateHistoryResultBean;
import com.slxk.hounddog.mvp.model.bean.PenetrateResultBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceDetailPutBean;
import com.slxk.hounddog.mvp.model.putbean.PenetrateHistoryPutBean;
import com.slxk.hounddog.mvp.model.putbean.PenetratePutBean;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/25/2021 14:15
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class PenetratePresenter extends BasePresenter<PenetrateContract.Model, PenetrateContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public PenetratePresenter(PenetrateContract.Model model, PenetrateContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取设备详情
     * @param bean
     */
    public void getDeviceDetailInfo(DeviceDetailPutBean bean){
        mModel.getDeviceDetailInfo(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<DeviceDetailResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(DeviceDetailResultBean deviceDetailResultBean) {
                        if (deviceDetailResultBean.isSuccess()){
                            mRootView.getDeviceDetailInfoSuccess(deviceDetailResultBean);
                        }else{
                            mRootView.getDeviceDetailInfoFail();
                        }
                    }
                });
    }

    public void getPenetrateHistory(PenetrateHistoryPutBean bean, boolean isShow, boolean isRefresh){
        mModel.getPenetrateHistory(bean)
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
                .subscribe(new ErrorHandleSubscriber<PenetrateHistoryResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(PenetrateHistoryResultBean penetrateHistoryResultBean) {
                        if (!isShow && isRefresh){
                            mRootView.finishRefresh();
                        }
                        if (penetrateHistoryResultBean.isSuccess()){
                            mRootView.getPenetrateHistorySuccess(penetrateHistoryResultBean, isRefresh);
                            if (penetrateHistoryResultBean.getItems() != null){
                                if (penetrateHistoryResultBean.getItems().size() == 0 || penetrateHistoryResultBean.getItems().size() < bean.getParams().getLimit_size()) {
                                    mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                                    mRootView.setNoMore();
                                }else{
                                    mRootView.endLoadMore();
                                }
                            }else{
                                mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                                mRootView.setNoMore();
                            }
                        }else{
                            mRootView.endLoadFail();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.endLoadFail();
                    }

                });
    }

    /**
     * 发送透传指令
     * @param bean
     */
    public void submitPenetrateSend(PenetratePutBean bean){
        mModel.submitPenetrateSend(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<PenetrateResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(PenetrateResultBean penetrateResultBean) {
                        if (penetrateResultBean.isSuccess()){
                            mRootView.submitPenetrateSendSuccess(penetrateResultBean);
                        }
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
