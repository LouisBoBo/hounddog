package com.slxk.hounddog.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.slxk.hounddog.mvp.contract.BaiduLocationWirelessContract;
import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.api.service.DeviceService;
import com.slxk.hounddog.mvp.model.api.service.PublicService;
import com.slxk.hounddog.mvp.model.bean.BaseStationAddressBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListForManagerResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListForManagerPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListPutBean;
import com.slxk.hounddog.mvp.model.putbean.OnKeyFunctionPutBean;
import com.slxk.hounddog.mvp.utils.ConstantValue;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/28/2021 17:36
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class BaiduLocationWirelessModel extends BaseModel implements BaiduLocationWirelessContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public BaiduLocationWirelessModel(IRepositoryManager repositoryManager) {
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

    @Override
    public Observable<BaseBean> submitOneKeyFunction(OnKeyFunctionPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(DeviceService.class).submitOneKeyFunction(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<BaseBean>, ObservableSource<BaseBean>>() {
                    @Override
                    public ObservableSource<BaseBean> apply(Observable<BaseBean> baseBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(DeviceService.class).submitOneKeyFunction(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

    @Override
    public Observable<BaseStationAddressBean> getBaseStationLocation(String bts) {
        String url = Api.BaseStation_Location_Url + "?accesstype=" + Api.BaseStation_Accesstype + "&bts=" + bts
                + "&output=" + Api.BaseStation_Output + "&key=" + Api.Amap_Key;
        return Observable.just(mRepositoryManager.obtainRetrofitService(PublicService.class)
                .getBaseStationLocation(url))
                .flatMap(new Function<Observable<BaseStationAddressBean>, ObservableSource<BaseStationAddressBean>>() {
                    @Override
                    public ObservableSource<BaseStationAddressBean> apply(Observable<BaseStationAddressBean> baseStationAddressBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(PublicService.class)
                                .getBaseStationLocation(url);
                    }
                });
    }

}