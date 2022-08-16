package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.MainWirelessModule;
import com.slxk.hounddog.mvp.contract.MainWirelessContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.MainWirelessActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/21/2021 14:17
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = MainWirelessModule.class, dependencies = AppComponent.class)
public interface MainWirelessComponent {
    void inject(MainWirelessActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MainWirelessComponent.Builder view(MainWirelessContract.View view);

        MainWirelessComponent.Builder appComponent(AppComponent appComponent);

        MainWirelessComponent build();
    }
}