package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.DeviceListModule;
import com.slxk.hounddog.mvp.contract.DeviceListContract;

import com.jess.arms.di.scope.FragmentScope;
import com.slxk.hounddog.mvp.ui.fragment.DeviceListFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/21/2021 14:28
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = DeviceListModule.class, dependencies = AppComponent.class)
public interface DeviceListComponent {
    void inject(DeviceListFragment fragment);
    @Component.Builder
    interface Builder {
        @BindsInstance
        DeviceListComponent.Builder view(DeviceListContract.View view);
        DeviceListComponent.Builder appComponent(AppComponent appComponent);
        DeviceListComponent build();
    }
}