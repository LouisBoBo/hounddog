package com.slxk.hounddog.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.slxk.hounddog.mvp.contract.TrackBaiduWirelessContract;
import com.slxk.hounddog.mvp.model.TrackBaiduWirelessModel;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 01/05/2022 15:50
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class TrackBaiduWirelessModule {

    @Binds
    abstract TrackBaiduWirelessContract.Model bindTrackBaiduWirelessModel(TrackBaiduWirelessModel model);
}