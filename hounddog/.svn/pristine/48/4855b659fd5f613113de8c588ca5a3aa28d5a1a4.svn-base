package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.CompassWirelessModule;
import com.slxk.hounddog.mvp.contract.CompassWirelessContract;

import com.jess.arms.di.scope.FragmentScope;
import com.slxk.hounddog.mvp.ui.fragment.CompassWirelessFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/22/2021 09:20
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = CompassWirelessModule.class, dependencies = AppComponent.class)
public interface CompassWirelessComponent {
    void inject(CompassWirelessFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        CompassWirelessComponent.Builder view(CompassWirelessContract.View view);

        CompassWirelessComponent.Builder appComponent(AppComponent appComponent);

        CompassWirelessComponent build();
    }
}