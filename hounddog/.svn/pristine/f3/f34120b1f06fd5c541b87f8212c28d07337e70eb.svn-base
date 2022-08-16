package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.NavigationAmapModule;
import com.slxk.hounddog.mvp.contract.NavigationAmapContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.NavigationAmapActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 01/13/2022 11:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = NavigationAmapModule.class, dependencies = AppComponent.class)
public interface NavigationAmapComponent {
    void inject(NavigationAmapActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        NavigationAmapComponent.Builder view(NavigationAmapContract.View view);
        NavigationAmapComponent.Builder appComponent(AppComponent appComponent);
        NavigationAmapComponent build();
    }
}