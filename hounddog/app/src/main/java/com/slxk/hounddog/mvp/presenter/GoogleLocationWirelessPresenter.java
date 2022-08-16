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
import com.slxk.hounddog.mvp.contract.GoogleLocationWirelessContract;
import com.slxk.hounddog.mvp.model.bean.BaseStationAddressBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListForManagerResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListForManagerPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListPutBean;
import com.slxk.hounddog.mvp.model.putbean.OnKeyFunctionPutBean;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/22/2021 09:12
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class GoogleLocationWirelessPresenter extends BasePresenter<GoogleLocationWirelessContract.Model, GoogleLocationWirelessContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public GoogleLocationWirelessPresenter(GoogleLocationWirelessContract.Model model, GoogleLocationWirelessContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取当前账号下设备列表，设备号登录同求 - 设备号登录
     *
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
                        }else{
                            mRootView.onError();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.onError();
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
                        }else{
                            mRootView.onError();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.onError();
                    }

                });
    }

    /**
     * 一键功能设置
     * @param bean
     */
    public void submitOneKeyFunction(OnKeyFunctionPutBean bean){
        mModel.submitOneKeyFunction(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
//                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
//                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseBean>(mErrorHandler) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        mRootView.onDismissProgress();
                        if (baseBean.isSuccess()){
                            mRootView.submitOneKeyFunctionSuccess(baseBean);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        mRootView.onDismissProgress();
                    }
                });
    }

    /**
     * 通过基站信息获取经纬度
     * @param bts
     * @param model
     */
    public void getBaseStationLocation(String bts, DeviceModel model){
        mModel.getBaseStationLocation(bts)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
//                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
//                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseStationAddressBean>(mErrorHandler) {
                    @Override
                    public void onNext(BaseStationAddressBean baseStationAddressBean) {
                        mRootView.getBaseStationLocationSuccess(baseStationAddressBean, model);
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
