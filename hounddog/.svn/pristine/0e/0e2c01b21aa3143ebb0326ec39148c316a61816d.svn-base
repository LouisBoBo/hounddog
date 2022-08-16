package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.TrackAmapWirelessModule;
import com.slxk.hounddog.mvp.contract.TrackAmapWirelessContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.TrackAmapWirelessActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 01/05/2022 15:51
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = TrackAmapWirelessModule.class, dependencies = AppComponent.class)
public interface TrackAmapWirelessComponent {
    void inject(TrackAmapWirelessActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TrackAmapWirelessComponent.Builder view(TrackAmapWirelessContract.View view);

        TrackAmapWirelessComponent.Builder appComponent(AppComponent appComponent);

        TrackAmapWirelessComponent build();
    }
}