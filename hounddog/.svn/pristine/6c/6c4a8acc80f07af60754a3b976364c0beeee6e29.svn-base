package com.slxk.hounddog.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.slxk.hounddog.mvp.contract.RegisterNextContract;
import com.slxk.hounddog.mvp.model.api.service.PublicService;
import com.slxk.hounddog.mvp.model.bean.PhoneAreaResultBean;
import com.slxk.hounddog.mvp.model.bean.PhoneCodeResultBean;
import com.slxk.hounddog.mvp.model.bean.RegisterResultBean;
import com.slxk.hounddog.mvp.model.putbean.PhoneAreaPutBean;
import com.slxk.hounddog.mvp.model.putbean.PhoneCodePutBean;
import com.slxk.hounddog.mvp.model.putbean.RegisterPutBean;

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
 * Created by MVPArmsTemplate on 03/16/2022 17:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class RegisterNextModel extends BaseModel implements RegisterNextContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public RegisterNextModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<RegisterResultBean> submitRegister(RegisterPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(PublicService.class).submitRegister(requestBody))
                .flatMap(new Function<Observable<RegisterResultBean>, ObservableSource<RegisterResultBean>>() {
                    @Override
                    public ObservableSource<RegisterResultBean> apply(Observable<RegisterResultBean> baseBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(PublicService.class).submitRegister(requestBody);
                    }
                });
    }

    @Override
    public Observable<PhoneAreaResultBean> getPhoneArea(PhoneAreaPutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(PublicService.class).getPhoneArea(requestBody))
                .flatMap(new Function<Observable<PhoneAreaResultBean>, ObservableSource<PhoneAreaResultBean>>() {
                    @Override
                    public ObservableSource<PhoneAreaResultBean> apply(Observable<PhoneAreaResultBean> phoneAreaResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(PublicService.class).getPhoneArea(requestBody);
                    }
                });
    }

    @Override
    public Observable<PhoneCodeResultBean> getPhoneCode(PhoneCodePutBean bean) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(bean));
        return Observable.just(mRepositoryManager.obtainRetrofitService(PublicService.class).getPhoneCode(requestBody))
                .flatMap(new Function<Observable<PhoneCodeResultBean>, ObservableSource<PhoneCodeResultBean>>() {
                    @Override
                    public ObservableSource<PhoneCodeResultBean> apply(Observable<PhoneCodeResultBean> phoneCodeResultBeanObservable) throws Exception {
                        return mRepositoryManager.obtainRetrofitService(PublicService.class).getPhoneCode(requestBody);
                    }
                });
    }

}