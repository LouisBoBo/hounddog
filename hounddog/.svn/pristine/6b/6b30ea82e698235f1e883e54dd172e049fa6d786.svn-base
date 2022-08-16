package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.ConnectDeviceModule;
import com.slxk.hounddog.mvp.contract.ConnectDeviceContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.ConnectDeviceActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/22/2021 17:08
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = ConnectDeviceModule.class, dependencies = AppComponent.class)
public interface ConnectDeviceComponent {
    void inject(ConnectDeviceActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        ConnectDeviceComponent.Builder view(ConnectDeviceContract.View view);
        ConnectDeviceComponent.Builder appComponent(AppComponent appComponent);
        ConnectDeviceComponent build();
    }
}