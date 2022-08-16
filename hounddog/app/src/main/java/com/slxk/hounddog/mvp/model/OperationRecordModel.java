package com.slxk.hounddog.mvp.model;

import android.app.Application;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;
import javax.inject.Inject;

import com.slxk.hounddog.mvp.contract.OperationRecordContract;
import com.slxk.hounddog.mvp.model.api.service.DeviceService;
import com.slxk.hounddog.mvp.model.bean.OperationRecordResultBean;
import com.slxk.hounddog.mvp.model.putbean.OperationRecordPutBean;
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
 * Created by MVPArmsTemplate on 12/25/2021 15:08
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class OperationRecordModel extends BaseModel implements OperationRecordContract.Model{
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public OperationRecordModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<OperationRecordResultBean> getOperationRecord(OperationRecordPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(DeviceService.class).getOperationRecord(ConstantValue.getApiUrlSid(), requestBody))
                .flatMap(new Function<Observable<OperationRecordResultBean>, ObservableSource<OperationRecordResultBean>>() {
                    @Override
                    public ObservableSource<OperationRecordResultBean> apply(Observable<OperationRecordResultBean> operationRecordResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(DeviceService.class).getOperationRecord(ConstantValue.getApiUrlSid(), requestBody);
                    }
                });
    }
}