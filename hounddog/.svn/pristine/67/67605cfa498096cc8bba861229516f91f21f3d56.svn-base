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
import com.slxk.hounddog.mvp.contract.MainWirelessContract;
import com.slxk.hounddog.mvp.model.bean.CheckAppUpdateBean;
import com.slxk.hounddog.mvp.model.bean.DeviceGroupResultBean;
import com.slxk.hounddog.mvp.model.bean.MergeAccountResultBean;
import com.slxk.hounddog.mvp.model.bean.PhoneCodeResultBean;
import com.slxk.hounddog.mvp.model.putbean.CheckAppUpdatePutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceGroupPutBean;
import com.slxk.hounddog.mvp.model.putbean.MergeAccountPutBean;
import com.slxk.hounddog.mvp.model.putbean.PhoneCodePutBean;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/21/2021 14:17
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class MainWirelessPresenter extends BasePresenter<MainWirelessContract.Model, MainWirelessContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public MainWirelessPresenter(MainWirelessContract.Model model, MainWirelessContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取app版本更新信息
     */
    public void getAppUpdate(CheckAppUpdatePutBean bean){
        mModel.getAppUpdate(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
//                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
//                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<CheckAppUpdateBean>(mErrorHandler) {
                    @Override
                    public void onNext(CheckAppUpdateBean checkAppUpdateBean) {
                        if (checkAppUpdateBean.isSuccess()){
                            mRootView.getAppUpdateSuccess(checkAppUpdateBean);
                        }
                    }
                });
    }

    /**
     * 获取手机验证码
     *
     * @param bean
     */
    public void getPhoneCode(PhoneCodePutBean bean) {
        mModel.getPhoneCode(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
//                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
//                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<PhoneCodeResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(PhoneCodeResultBean phoneCodeResultBean) {
                        if (phoneCodeResultBean.isSuccess()) {
                            mRootView.getPhoneCodeSuccess(phoneCodeResultBean);
                        }
                    }
                });
    }

    /**
     * 获取车组织列表和车组列表
     *
     * @param bean
     */
    public void getDeviceGroupList(DeviceGroupPutBean bean) {
        mModel.getDeviceGroupList(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
//                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
//                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<DeviceGroupResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(DeviceGroupResultBean deviceGroupResultBean) {
                        if (deviceGroupResultBean.isSuccess()) {
                            mRootView.getDeviceGroupListSuccess(deviceGroupResultBean);
                        }
                    }
                });
    }

    /**
     * 合并账号
     * @param bean
     */
    public void submitMergeAccount(MergeAccountPutBean bean){
        mModel.submitMergeAccount(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<MergeAccountResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(MergeAccountResultBean mergeAccountResultBean) {
                        mRootView.submitMergeAccountSuccess(mergeAccountResultBean);
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
