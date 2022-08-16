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
import com.slxk.hounddog.mvp.contract.DeviceListWirelessContract;
import com.slxk.hounddog.mvp.model.bean.DeviceBaseResultBean;
import com.slxk.hounddog.mvp.model.putbean.RemoveDevicePutBean;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/22/2021 09:21
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class DeviceListWirelessPresenter extends BasePresenter<DeviceListWirelessContract.Model, DeviceListWirelessContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public DeviceListWirelessPresenter (DeviceListWirelessContract.Model model, DeviceListWirelessContract.View rootView) {
        super(model, rootView);
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
