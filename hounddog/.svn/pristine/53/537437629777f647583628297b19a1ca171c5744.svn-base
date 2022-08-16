package com.slxk.hounddog.mvp.model;

import android.app.Application;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;
import javax.inject.Inject;

import com.slxk.hounddog.mvp.contract.ConnectDeviceContract;
import com.slxk.hounddog.mvp.model.api.service.PublicService;
import com.slxk.hounddog.mvp.model.bean.BluetoothUpgradeVersionBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/22/2021 17:08
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class ConnectDeviceModel extends BaseModel implements ConnectDeviceContract.Model{
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public ConnectDeviceModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BluetoothUpgradeVersionBean> getBluetoothUpgradeVersion(String url) {
        return Observable.just(mRepositoryManager.obtainRetrofitService(PublicService.class).getBluetoothUpgradeVersion(url))
                .flatMap(new Function<Observable<BluetoothUpgradeVersionBean>, ObservableSource<BluetoothUpgradeVersionBean>>() {
                    @Override
                    public ObservableSource<BluetoothUpgradeVersionBean> apply(Observable<BluetoothUpgradeVersionBean> bluetoothUpgradeVersionBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(PublicService.class).getBluetoothUpgradeVersion(url);
                    }
                });
    }

    @Override
    public Observable<BaseBean> submitUpgradeResult(String url) {
        return Observable.just(mRepositoryManager.obtainRetrofitService(PublicService.class).submitUpgradeResult(url))
                .flatMap(new Function<Observable<BaseBean>, ObservableSource<BaseBean>>() {
                    @Override
                    public ObservableSource<BaseBean> apply(Observable<BaseBean> baseBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(PublicService.class).submitUpgradeResult(url);
                    }
                });
    }
}