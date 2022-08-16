package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.TrackBaiduWirelessModule;
import com.slxk.hounddog.mvp.contract.TrackBaiduWirelessContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.TrackBaiduWirelessActivity;


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
@ActivityScope
@Component(modules = TrackBaiduWirelessModule.class, dependencies = AppComponent.class)
public interface TrackBaiduWirelessComponent {
    void inject(TrackBaiduWirelessActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TrackBaiduWirelessComponent.Builder view(TrackBaiduWirelessContract.View view);

        TrackBaiduWirelessComponent.Builder appComponent(AppComponent appComponent);

        TrackBaiduWirelessComponent build();
    }
}