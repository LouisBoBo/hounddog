package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.TrackAmapModule;
import com.slxk.hounddog.mvp.contract.TrackAmapContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.TrackAmapActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 01/19/2022 16:33
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = TrackAmapModule.class, dependencies = AppComponent.class)
public interface TrackAmapComponent {
    void inject(TrackAmapActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TrackAmapComponent.Builder view(TrackAmapContract.View view);

        TrackAmapComponent.Builder appComponent(AppComponent appComponent);

        TrackAmapComponent build();
    }
}