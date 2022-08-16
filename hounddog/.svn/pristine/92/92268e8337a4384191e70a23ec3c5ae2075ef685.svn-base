package com.slxk.hounddog.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;

import com.jess.arms.utils.RxLifecycleUtils;
import com.slxk.hounddog.db.DeviceModel;
import com.slxk.hounddog.mvp.contract.DeviceListContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.bean.DeviceBaseResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListForManagerResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListResultBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListForManagerPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListPutBean;
import com.slxk.hounddog.mvp.model.putbean.RemoveDevicePutBean;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/21/2021 14:28
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class DeviceListPresenter extends BasePresenter<DeviceListContract.Model, DeviceListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public DeviceListPresenter (DeviceListContract.Model model, DeviceListContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取当前账号下设备列表，设备号登录同求 - 设备号登录
     * @param bean
     */
    public void getDeviceList(DeviceListPutBean bean, boolean isRefresh, boolean isLoadmore) {
        mModel.getDeviceList(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
//                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
//                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<DeviceListResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(DeviceListResultBean deviceListResultBean) {
                        if (isRefresh){
                            mRootView.finishRefresh();
                        }
                        if (isLoadmore){
                            mRootView.endLoadMore();
                        }

                        if (deviceListResultBean.isSuccess()){
                            mRootView.getDeviceListSuccess(deviceListResultBean,isLoadmore);
                            if (deviceListResultBean.getItems() != null){
                                if (deviceListResultBean.getItems().size() == 0 || deviceListResultBean.getItems().size() < bean.getParams().getLimit_size()) {
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
                });
    }

    /**
     * 获取分组下的设备列表 - 账号登录
     * @param bean
     */
    public void getDeviceListForGroup(DeviceListForManagerPutBean bean, boolean isRefresh, boolean isLoadmore){
        mModel.getDeviceListForGroup(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
//                    mRootView.showLoading();//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
//                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<DeviceListForManagerResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(DeviceListForManagerResultBean deviceListForManagerResultBean) {
                        if (isRefresh){
                            mRootView.finishRefresh();
                        }
                        if (isLoadmore){
                            mRootView.endLoadMore();
                        }

                        if (deviceListForManagerResultBean.isSuccess()){
                            mRootView.getDeviceListForGroupSuccess(deviceListForManagerResultBean,isLoadmore);
                            if (deviceListForManagerResultBean.getItems() != null){
                                if (deviceListForManagerResultBean.getItems().size() == 0 || deviceListForManagerResultBean.getItems().size() < bean.getParams().getLimit_size()) {
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

                });
    }

    /**
     * 删除设备
     * @param bean
     */
    public void submitDeleteDevice(RemoveDevicePutBean bean, DeviceModel model){
        mModel.submitDeleteDevice(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
//                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
//                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<DeviceBaseResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(DeviceBaseResultBean deviceBaseResultBean) {
                        mRootView.onDismissProgressDialog();
                        if (deviceBaseResultBean.isSuccess()){
                            mRootView.submitDeleteDeviceSuccess(deviceBaseResultBean, model);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.onDismissProgressDialog();
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
