package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.NavigationBaiduModule;
import com.slxk.hounddog.mvp.contract.NavigationBaiduContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.NavigationBaiduActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 01/13/2022 11:38
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = NavigationBaiduModule.class, dependencies = AppComponent.class)
public interface NavigationBaiduComponent {
    void inject(NavigationBaiduActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        NavigationBaiduComponent.Builder view(NavigationBaiduContract.View view);

        NavigationBaiduComponent.Builder appComponent(AppComponent appComponent);

        NavigationBaiduComponent build();
    }
}