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
import com.slxk.hounddog.mvp.contract.TrackAmapContract;
import com.slxk.hounddog.mvp.model.bean.TrackHasDataResultBean;
import com.slxk.hounddog.mvp.model.bean.TrackListResultBean;
import com.slxk.hounddog.mvp.model.putbean.TrackHasDataPutBean;
import com.slxk.hounddog.mvp.model.putbean.TrackListPutBean;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 01/19/2022 16:33
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class TrackAmapPresenter extends BasePresenter<TrackAmapContract.Model, TrackAmapContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public TrackAmapPresenter(TrackAmapContract.Model model, TrackAmapContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 获取有轨迹数据的日期
     * @param bean
     */
    public void getTrackHasForData(TrackHasDataPutBean bean){
        mModel.getTrackHasForData(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<TrackHasDataResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(TrackHasDataResultBean trackHasDataResultBean) {
                        if (trackHasDataResultBean.isSuccess()){
                            mRootView.getTrackHasForDataSuccess(trackHasDataResultBean);
                        }else{
                            mRootView.onEndMoreData();
                        }
                    }
                });
    }

    /**
     * 获取轨迹列表数据
     * @param bean
     * @param isShow 是否弹出加载看
     * @param isResetData 是否是请求新的一天轨迹数据，true:请求新的一天数据，false:请求同一天的轨迹数据，后续的数据
     */
    public void getTrackList(TrackListPutBean bean, boolean isShow, boolean isResetData){
        mModel.getTrackList(bean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (isShow){
                        mRootView.showLoading();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (isShow){
                        mRootView.hideLoading();
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<TrackListResultBean>(mErrorHandler) {
                    @Override
                    public void onNext(TrackListResultBean trackListResultBean) {
                        if (trackListResultBean.isSuccess()){
                            if (trackListResultBean.isIs_finish()
                                    || (trackListResultBean.getData() != null && trackListResultBean.getData().size() < bean.getParams().getLimit_size())){
                                mRootView.onEndMoreData();
                            }
                            mRootView.getTrackListSuccess(trackListResultBean, isResetData);
                        }else{
                            mRootView.onEndMoreData();
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
