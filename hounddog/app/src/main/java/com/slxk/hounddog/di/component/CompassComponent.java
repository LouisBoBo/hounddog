package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.CompassModule;
import com.slxk.hounddog.mvp.contract.CompassContract;

import com.jess.arms.di.scope.FragmentScope;
import com.slxk.hounddog.mvp.ui.fragment.CompassFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/21/2021 14:27
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = CompassModule.class, dependencies = AppComponent.class)
public interface CompassComponent {
    void inject(CompassFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        CompassComponent.Builder view(CompassContract.View view);

        CompassComponent.Builder appComponent(AppComponent appComponent);

        CompassComponent build();
    }
}