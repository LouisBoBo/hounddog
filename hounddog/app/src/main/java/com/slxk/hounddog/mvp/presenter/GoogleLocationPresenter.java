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
import com.slxk.hounddog.mvp.contract.GoogleLocationContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.bean.BaseStationAddressBean;
import com.slxk.hounddog.mvp.model.bean.DeviceConfigResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceDetailResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListForManagerResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceConfigPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceDetailPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListForManagerPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListPutBean;
import com.slxk.hounddog.mvp.model.putbean.OnKeyFunctionPutBean;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/21/2021 14:26
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class GoogleLocationPresenter extends BasePresenter<GoogleLocationContract.Model, GoogleLocationContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public GoogleLocationPresenter(GoogleLocationContract.Model model, GoogleLocationContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取当前账号下设备列表，设备号登录同求 - 设备号登录
     * @param bean
     * @param isInitiativeRefresh 是否需要查看全部设备
     */
    public void getDeviceList(DeviceListPutBean bean, boolean isInitiativeRefresh) {
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
                        mRootView.onDismissProgress();
                        if (deviceListResultBean.isSuccess()){
                            mRootView.getDeviceListSuccess(deviceListResultBean, isInitiativeRefresh);
                        }
                    }
                });
    }

    /**
     * 获取分组下的设备列表 - 账号登录
     * @param bean
     * @param isInitiativeRefresh 是否需要查看全部设备
     */
    public void getDeviceListForGroup(DeviceListForManagerPutBean bean, boolean isInitiativeRefresh){
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
                        mRootView.onDismissProgress();
                        if (deviceListForManagerResultBean.isSuccess()){
                            mRootView.getDeviceListForGroupSuccess(deviceListForManagerResultBean, isInitiativeRefresh);
                        }
                    }

                });
    }

    /**
     * 获取设备详情
     * @param bean
     */
    public void getDeviceDetailInfo(DeviceDetailPutBean bean){
        mModel.getDeviceDetailInfo(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
//                    mRootView.showLoading();//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
//                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<DeviceDetailResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(DeviceDetailResultBean deviceDetailResultBean) {
                        if (deviceDetailResultBean.isSuccess()){
                            mRootView.getDeviceDetailInfoSuccess(deviceDetailResultBean);
                        }else if (deviceDetailResultBean.getErrcode() == Api.Device_Freeze || deviceDetailResultBean.getErrcode() == Api.Data_Change){
                            mRootView.getDeviceDetailError();
                        }
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
     * 获取设备的配置信息，支持的功能等
     * @param bean
     */
    public void getDeviceConfig(DeviceConfigPutBean bean){
        mModel.getDeviceConfig(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
//                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
//                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<DeviceConfigResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(DeviceConfigResultBean deviceConfigResultBean) {
                        if (deviceConfigResultBean.isSuccess()){
                            mRootView.getDeviceConfigSuccess(deviceConfigResultBean);
                        }
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
