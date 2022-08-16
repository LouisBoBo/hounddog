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
import com.slxk.hounddog.mvp.contract.SettingContract;
import com.slxk.hounddog.mvp.model.bean.CheckAppUpdateBean;
import com.slxk.hounddog.mvp.model.bean.LogoutAccountResultBean;
import com.slxk.hounddog.mvp.model.bean.UnbindPhoneResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.CheckAppUpdatePutBean;
import com.slxk.hounddog.mvp.model.putbean.LogoutAccountPutBean;
import com.slxk.hounddog.mvp.model.putbean.SignOutPutBean;
import com.slxk.hounddog.mvp.model.putbean.UnbindPhonePutBean;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/22/2021 15:11
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class SettingPresenter extends BasePresenter<SettingContract.Model, SettingContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public SettingPresenter (SettingContract.Model model, SettingContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 退出登录
     * @param bean
     */
    public void submitSignOut(SignOutPutBean bean){
        mModel.submitSignOut(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseBean>(mErrorHandler) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        if (baseBean.isSuccess()){
                            mRootView.submitSignOutSuccess(baseBean);
                        }
                    }
                });
    }

    /**
     * 获取app版本更新信息
     */
    public void getAppUpdate(CheckAppUpdatePutBean bean){
        mModel.getAppUpdate(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
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
     * 注销账号
     * @param bean
     */
    public void submitLogoutAccount(LogoutAccountPutBean bean){
        mModel.submitLogoutAccount(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<LogoutAccountResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(LogoutAccountResultBean logoutAccountResultBean) {
                        if (logoutAccountResultBean.isSuccess()){
                            mRootView.submitLogoutAccountSuccess(logoutAccountResultBean);
                        }
                    }
                });
    }

    /**
     * 解绑手机号码
     * @param bean
     */
    public void submitUnbindPhone(UnbindPhonePutBean bean){
        mModel.submitUnbindPhone(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<UnbindPhoneResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(UnbindPhoneResultBean unbindPhoneResultBean) {
                        if (unbindPhoneResultBean.isSuccess()){
                            mRootView.submitUnbindPhoneSuccess(unbindPhoneResultBean);
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
