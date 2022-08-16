package com.slxk.hounddog.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.slxk.hounddog.mvp.contract.ForgetPasswordNextContract;
import com.slxk.hounddog.mvp.model.ForgetPasswordNextModel;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/09/2022 11:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class ForgetPasswordNextModule {

    @Binds
    abstract ForgetPasswordNextContract.Model bindForgetPasswordNextModel(ForgetPasswordNextModel model);
}