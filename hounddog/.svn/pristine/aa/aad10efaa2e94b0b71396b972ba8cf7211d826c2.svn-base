package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.DeviceListWirelessModule;
import com.slxk.hounddog.mvp.contract.DeviceListWirelessContract;

import com.jess.arms.di.scope.FragmentScope;
import com.slxk.hounddog.mvp.ui.fragment.DeviceListWirelessFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/22/2021 09:21
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = DeviceListWirelessModule.class, dependencies = AppComponent.class)
public interface DeviceListWirelessComponent {
    void inject(DeviceListWirelessFragment fragment);
    @Component.Builder
    interface Builder {
        @BindsInstance
        DeviceListWirelessComponent.Builder view(DeviceListWirelessContract.View view);
        DeviceListWirelessComponent.Builder appComponent(AppComponent appComponent);
        DeviceListWirelessComponent build();
    }
}