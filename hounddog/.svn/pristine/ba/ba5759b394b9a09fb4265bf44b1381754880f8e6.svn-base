package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.NavigationGoogleModule;
import com.slxk.hounddog.mvp.contract.NavigationGoogleContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.NavigationGoogleActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/23/2021 14:43
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = NavigationGoogleModule.class, dependencies = AppComponent.class)
public interface NavigationGoogleComponent {
    void inject(NavigationGoogleActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        NavigationGoogleComponent.Builder view(NavigationGoogleContract.View view);

        NavigationGoogleComponent.Builder appComponent(AppComponent appComponent);

        NavigationGoogleComponent build();
    }
}