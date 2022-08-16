package com.slxk.hounddog.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.slxk.hounddog.mvp.contract.LocationModeContract;
import com.slxk.hounddog.mvp.model.api.service.DeviceService;
import com.slxk.hounddog.mvp.model.api.service.LocationModeService;
import com.slxk.hounddog.mvp.model.bean.DeviceConfigResultBean;
import com.slxk.hounddog.mvp.model.bean.LocationModeResultBean;
import com.slxk.hounddog.mvp.model.bean.SetConfigResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceConfigPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceConfigSetPutBean;
import com.slxk.hounddog.mvp.model.putbean.LocationModePutBean;
import com.slxk.hounddog.mvp.model.putbean.LocationModeSetPutBean;
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
 * Created by MVPArmsTemplate on 12/24/2021 09:19
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class LocationModeModel extends BaseModel implements LocationModeContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public LocationModeModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<LocationModeResultBean> getLocationMode(LocationModePutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(LocationModeService.class).getLocationMode(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<LocationModeResultBean>, ObservableSource<LocationModeResultBean>>() {
                    @Override
                    public ObservableSource<LocationModeResultBean> apply(Observable<LocationModeResultBean> locationModeResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(LocationModeService.class).getLocationMode(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

    @Override
    public Observable<BaseBean> submitLocationMode(LocationModeSetPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(LocationModeService.class).submitLocationMode(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<BaseBean>, ObservableSource<BaseBean>>() {
                    @Override
                    public ObservableSource<BaseBean> apply(Observable<BaseBean> baseBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(LocationModeService.class).submitLocationMode(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

    @Override
    public Observable<DeviceConfigResultBean> getDeviceConfig(DeviceConfigPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(DeviceService.class).getDeviceConfig(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<DeviceConfigResultBean>, ObservableSource<DeviceConfigResultBean>>() {
                    @Override
                    public ObservableSource<DeviceConfigResultBean> apply(Observable<DeviceConfigResultBean> deviceConfigResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(DeviceService.class).getDeviceConfig(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

    @Override
    public Observable<SetConfigResultBean> setDeviceConfig(DeviceConfigSetPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(DeviceService.class).setDeviceConfig(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<SetConfigResultBean>, ObservableSource<SetConfigResultBean>>() {
                    @Override
                    public ObservableSource<SetConfigResultBean> apply(Observable<SetConfigResultBean> baseBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(DeviceService.class).setDeviceConfig(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }
}