package com.slxk.hounddog.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.FragmentScope;

import javax.inject.Inject;

import com.slxk.hounddog.mvp.contract.CompassContract;
import com.slxk.hounddog.mvp.model.api.service.DeviceService;
import com.slxk.hounddog.mvp.model.bean.DeviceListForManagerResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListResultBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListForManagerPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListPutBean;
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
 * Created by MVPArmsTemplate on 12/21/2021 14:27
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class CompassModel extends BaseModel implements CompassContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public CompassModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<DeviceListResultBean> getDeviceList(DeviceListPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(DeviceService.class).getDeviceList(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<DeviceListResultBean>, ObservableSource<DeviceListResultBean>>() {
                    @Override
                    public ObservableSource<DeviceListResultBean> apply(Observable<DeviceListResultBean> deviceListResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(DeviceService.class).getDeviceList(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

    @Override
    public Observable<DeviceListForManagerResultBean> getDeviceListForGroup(DeviceListForManagerPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(DeviceService.class).getDeviceListForGroup(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<DeviceListForManagerResultBean>, ObservableSource<DeviceListForManagerResultBean>>() {
                    @Override
                    public ObservableSource<DeviceListForManagerResultBean> apply(Observable<DeviceListForManagerResultBean> deviceListForManagerResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(DeviceService.class).getDeviceListForGroup(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

}