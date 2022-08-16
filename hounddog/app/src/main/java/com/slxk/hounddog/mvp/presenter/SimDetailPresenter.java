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
import com.slxk.hounddog.mvp.contract.SimDetailContract;
import com.slxk.hounddog.mvp.model.bean.DeviceDetailResultBean;
import com.slxk.hounddog.mvp.model.bean.SimDetailResultBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceDetailPutBean;
import com.slxk.hounddog.mvp.model.putbean.SimDetailPutBean;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/25/2021 15:38
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class SimDetailPresenter extends BasePresenter<SimDetailContract.Model, SimDetailContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public SimDetailPresenter (SimDetailContract.Model model, SimDetailContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取sim卡详细信息
     * @param bean
     */
    public void getSimDetail(SimDetailPutBean bean){
        mModel.getSimDetail(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<SimDetailResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(SimDetailResultBean simDetailResultBean) {
                        if (simDetailResultBean.isSuccess() || simDetailResultBean.isSuccessFor200()){
                            mRootView.getSimDetailSuccess(simDetailResultBean);
                        }
                    }
                });
    }

    /**
     * 获取设备详细信息
     *
     * @param bean
     */
    public void getDeviceDetail(DeviceDetailPutBean bean) {
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
                        if (deviceDetailResultBean.isSuccess()) {
                            mRootView.getDeviceDetailInfoSuccess(deviceDetailResultBean);
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
