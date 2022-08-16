package com.slxk.hounddog.mvp.presenter;

import android.app.Application;

import com.blankj.utilcode.util.ToastUtils;
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
import com.slxk.hounddog.mvp.contract.ForgetPasswordNextContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.bean.ForgetPasswordResultBean;
import com.slxk.hounddog.mvp.model.bean.PhoneCodeResultBean;
import com.slxk.hounddog.mvp.model.putbean.ForgetPasswordPutBean;
import com.slxk.hounddog.mvp.model.putbean.PhoneCodePutBean;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/09/2022 11:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class ForgetPasswordNextPresenter extends BasePresenter<ForgetPasswordNextContract.Model, ForgetPasswordNextContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public ForgetPasswordNextPresenter (ForgetPasswordNextContract.Model model, ForgetPasswordNextContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取手机验证码
     * @param bean
     */
    public void getPhoneCode(PhoneCodePutBean bean){
        mModel.getPhoneCode(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<PhoneCodeResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(PhoneCodeResultBean phoneCodeResultBean) {
                        if (phoneCodeResultBean.isSuccess()) {
                            mRootView.getPhoneCodeSuccess(phoneCodeResultBean);
                        } else if (phoneCodeResultBean.getErrcode() == Api.Mobile_Code_Error) {
                            ToastUtils.showShort(phoneCodeResultBean.getError_message());
                        }
                    }
                });
    }

    /**
     * 提交找回密码
     * @param bean
     */
    public void submitForgetPassword(ForgetPasswordPutBean bean){
        mModel.submitForgetPassword(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<ForgetPasswordResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(ForgetPasswordResultBean baseBean) {
                        mRootView.submitForgetPasswordSuccess(baseBean);
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
