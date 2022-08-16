package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.AmapLocationWirelessModule;
import com.slxk.hounddog.mvp.contract.AmapLocationWirelessContract;

import com.jess.arms.di.scope.FragmentScope;
import com.slxk.hounddog.mvp.ui.fragment.AmapLocationWirelessFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/28/2021 17:37
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = AmapLocationWirelessModule.class, dependencies = AppComponent.class)
public interface AmapLocationWirelessComponent {
    void inject(AmapLocationWirelessFragment fragment);
    @Component.Builder
    interface Builder {
        @BindsInstance
        AmapLocationWirelessComponent.Builder view(AmapLocationWirelessContract.View view);
        AmapLocationWirelessComponent.Builder appComponent(AppComponent appComponent);
        AmapLocationWirelessComponent build();
    }
}