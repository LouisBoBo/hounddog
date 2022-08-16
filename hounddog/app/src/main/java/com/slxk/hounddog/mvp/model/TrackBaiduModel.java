package com.slxk.hounddog.mvp.model;

import android.app.Application;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;
import javax.inject.Inject;

import com.slxk.hounddog.mvp.contract.TrackBaiduContract;
import com.slxk.hounddog.mvp.model.api.service.TrackService;
import com.slxk.hounddog.mvp.model.bean.TrackHasDataResultBean;
import com.slxk.hounddog.mvp.model.bean.TrackListResultBean;
import com.slxk.hounddog.mvp.model.putbean.TrackHasDataPutBean;
import com.slxk.hounddog.mvp.model.putbean.TrackListPutBean;
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
 * Created by MVPArmsTemplate on 01/19/2022 16:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class TrackBaiduModel extends BaseModel implements TrackBaiduContract.Model{
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public TrackBaiduModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<TrackHasDataResultBean> getTrackHasForData(TrackHasDataPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(TrackService.class).getTrackHasForData(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<TrackHasDataResultBean>, ObservableSource<TrackHasDataResultBean>>() {
                    @Override
                    public ObservableSource<TrackHasDataResultBean> apply(Observable<TrackHasDataResultBean> trackHasDataResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(TrackService.class).getTrackHasForData(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

    @Override
    public Observable<TrackListResultBean> getTrackList(TrackListPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(TrackService.class).getTrackList(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<TrackListResultBean>, ObservableSource<TrackListResultBean>>() {
                    @Override
                    public ObservableSource<TrackListResultBean> apply(Observable<TrackListResultBean> trackListResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(TrackService.class).getTrackList(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

}