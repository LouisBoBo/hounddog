package com.slxk.hounddog.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.slxk.hounddog.mvp.contract.RecordContract;
import com.slxk.hounddog.mvp.model.api.service.RecordService;
import com.slxk.hounddog.mvp.model.bean.RecordConfigResultBean;
import com.slxk.hounddog.mvp.model.bean.RecordResultBean;
import com.slxk.hounddog.mvp.model.bean.RecordScheduleResultBean;
import com.slxk.hounddog.mvp.model.bean.RecordShortResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.RecordConfigPutBean;
import com.slxk.hounddog.mvp.model.putbean.RecordDeletePutBean;
import com.slxk.hounddog.mvp.model.putbean.RecordPutBean;
import com.slxk.hounddog.mvp.model.putbean.RecordSchedulePutBean;
import com.slxk.hounddog.mvp.model.putbean.RecordShortPutBean;
import com.slxk.hounddog.mvp.model.putbean.RecordShortResultPutBean;
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
 * Created by MVPArmsTemplate on 12/23/2021 16:37
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class RecordModel extends BaseModel implements RecordContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public RecordModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<RecordConfigResultBean> getRecordConfig(RecordConfigPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(RecordService.class).getRecordConfig(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<RecordConfigResultBean>, ObservableSource<RecordConfigResultBean>>() {
                    @Override
                    public ObservableSource<RecordConfigResultBean> apply(Observable<RecordConfigResultBean> recordConfigResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(RecordService.class).getRecordConfig(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

    @Override
    public Observable<RecordResultBean> getRecordData(RecordPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(RecordService.class).getRecordData(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<RecordResultBean>, ObservableSource<RecordResultBean>>() {
                    @Override
                    public ObservableSource<RecordResultBean> apply(Observable<RecordResultBean> recordResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(RecordService.class).getRecordData(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

    @Override
    public Observable<BaseBean> submitShortRecord(RecordShortPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(RecordService.class).submitShortRecord(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<BaseBean>, ObservableSource<BaseBean>>() {
                    @Override
                    public ObservableSource<BaseBean> apply(Observable<BaseBean> baseBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(RecordService.class).submitShortRecord(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

    @Override
    public Observable<RecordShortResultBean> getShortRecordResult(RecordShortResultPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(RecordService.class).getShortRecordResult(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<RecordShortResultBean>, ObservableSource<RecordShortResultBean>>() {
                    @Override
                    public ObservableSource<RecordShortResultBean> apply(Observable<RecordShortResultBean> recordShortResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(RecordService.class).getShortRecordResult(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

    @Override
    public Observable<RecordScheduleResultBean> getRecordSchedule(RecordSchedulePutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(RecordService.class).getRecordSchedule(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<RecordScheduleResultBean>, ObservableSource<RecordScheduleResultBean>>() {
                    @Override
                    public ObservableSource<RecordScheduleResultBean> apply(Observable<RecordScheduleResultBean> recordScheduleResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(RecordService.class).getRecordSchedule(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

    @Override
    public Observable<BaseBean> submitDeleteRecord(RecordDeletePutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(RecordService.class).submitDeleteRecord(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<BaseBean>, ObservableSource<BaseBean>>() {
                    @Override
                    public ObservableSource<BaseBean> apply(Observable<BaseBean> baseBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(RecordService.class).submitDeleteRecord(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }

}