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
import com.slxk.hounddog.mvp.contract.CompassContract;
import com.slxk.hounddog.mvp.model.bean.DeviceListForManagerResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListResultBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListForManagerPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListPutBean;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/21/2021 14:27
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class CompassPresenter extends BasePresenter<CompassContract.Model, CompassContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public CompassPresenter(CompassContract.Model model, CompassContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取当前账号下设备列表，设备号登录同求 - 设备号登录
     * @param bean
     */
    public void getDeviceList(DeviceListPutBean bean) {
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
                        if (deviceListResultBean.isSuccess()){
                            mRootView.getDeviceListSuccess(deviceListResultBean);
                        }
                    }
                });
    }

    /**
     * 获取分组下的设备列表 - 账号登录
     * @param bean
     */
    public void getDeviceListForGroup(DeviceListForManagerPutBean bean){
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
                        if (deviceListForManagerResultBean.isSuccess()){
                            mRootView.getDeviceListForGroupSuccess(deviceListForManagerResultBean);
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
