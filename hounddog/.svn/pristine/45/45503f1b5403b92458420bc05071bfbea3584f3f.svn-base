package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.OfflineMapModule;
import com.slxk.hounddog.mvp.contract.OfflineMapContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.OfflineMapActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/24/2021 09:53
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = OfflineMapModule.class, dependencies = AppComponent.class)
public interface OfflineMapComponent {
    void inject(OfflineMapActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        OfflineMapComponent.Builder view(OfflineMapContract.View view);

        OfflineMapComponent.Builder appComponent(AppComponent appComponent);

        OfflineMapComponent build();
    }
}