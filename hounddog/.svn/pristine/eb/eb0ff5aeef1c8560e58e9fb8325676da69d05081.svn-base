package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.LocationModeModule;
import com.slxk.hounddog.mvp.contract.LocationModeContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.LocationModeActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/24/2021 09:19
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = LocationModeModule.class, dependencies = AppComponent.class)
public interface LocationModeComponent {
    void inject(LocationModeActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        LocationModeComponent.Builder view(LocationModeContract.View view);

        LocationModeComponent.Builder appComponent(AppComponent appComponent);

        LocationModeComponent build();
    }
}