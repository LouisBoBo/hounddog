package com.slxk.hounddog.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.slxk.hounddog.mvp.contract.PenetrateContract;
import com.slxk.hounddog.mvp.model.api.service.DeviceService;
import com.slxk.hounddog.mvp.model.bean.DeviceDetailResultBean;
import com.slxk.hounddog.mvp.model.bean.PenetrateHistoryResultBean;
import com.slxk.hounddog.mvp.model.bean.PenetrateResultBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceDetailPutBean;
import com.slxk.hounddog.mvp.model.putbean.PenetrateHistoryPutBean;
import com.slxk.hounddog.mvp.model.putbean.PenetratePutBean;
import com.slxk.hounddog.mvp.utils.ConstantValue;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/25/2021 14:15
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class PenetrateModel extends BaseModel implements PenetrateContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public PenetrateModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<PenetrateHistoryResultBean> getPenetrateHistory(PenetrateHistoryPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(DeviceService.class).getPenetrateHistory(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<PenetrateHistoryResultBean>, ObservableSource<PenetrateHistoryResultBean>>() {
                    @Override
                    public ObservableSource<PenetrateHistoryResultBean> apply(Observable<PenetrateHistoryResultBean> penetrateHistoryResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(DeviceService.class).getPenetrateHistory(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

    @Override
    public Observable<PenetrateResultBean> submitPenetrateSend(PenetratePutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(DeviceService.class).submitPenetrateSend(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<PenetrateResultBean>, ObservableSource<PenetrateResultBean>>() {
                    @Override
                    public ObservableSource<PenetrateResultBean> apply(Observable<PenetrateResultBean> penetrateResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(DeviceService.class).submitPenetrateSend(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

    @Override
    public Observable<DeviceDetailResultBean> getDeviceDetailInfo(DeviceDetailPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(DeviceService.class).getDeviceDetailInfo(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<DeviceDetailResultBean>, ObservableSource<DeviceDetailResultBean>>() {
                    @Override
                    public ObservableSource<DeviceDetailResultBean> apply(Observable<DeviceDetailResultBean> deviceDetailResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(DeviceService.class).getDeviceDetailInfo(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

}