package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.GoogleLocationWirelessModule;
import com.slxk.hounddog.mvp.contract.GoogleLocationWirelessContract;

import com.jess.arms.di.scope.FragmentScope;
import com.slxk.hounddog.mvp.ui.fragment.GoogleLocationWirelessFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/22/2021 09:12
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = GoogleLocationWirelessModule.class, dependencies = AppComponent.class)
public interface GoogleLocationWirelessComponent {
    void inject(GoogleLocationWirelessFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        GoogleLocationWirelessComponent.Builder view(GoogleLocationWirelessContract.View view);

        GoogleLocationWirelessComponent.Builder appComponent(AppComponent appComponent);

        GoogleLocationWirelessComponent build();
    }
}