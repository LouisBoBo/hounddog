package com.slxk.hounddog.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.slxk.hounddog.mvp.contract.GoogleLocationWirelessContract;
import com.slxk.hounddog.mvp.model.GoogleLocationWirelessModel;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/22/2021 09:12
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class GoogleLocationWirelessModule {

    @Binds
    abstract GoogleLocationWirelessContract.Model bindGoogleLocationWirelessModel(GoogleLocationWirelessModel model);
}