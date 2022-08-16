package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.TrackGoogleWirelessModule;
import com.slxk.hounddog.mvp.contract.TrackGoogleWirelessContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.TrackGoogleWirelessActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 01/05/2022 15:53
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = TrackGoogleWirelessModule.class, dependencies = AppComponent.class)
public interface TrackGoogleWirelessComponent {
    void inject(TrackGoogleWirelessActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TrackGoogleWirelessComponent.Builder view(TrackGoogleWirelessContract.View view);

        TrackGoogleWirelessComponent.Builder appComponent(AppComponent appComponent);

        TrackGoogleWirelessComponent build();
    }
}