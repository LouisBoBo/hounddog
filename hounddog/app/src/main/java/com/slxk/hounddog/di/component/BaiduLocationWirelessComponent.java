package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.BaiduLocationWirelessModule;
import com.slxk.hounddog.mvp.contract.BaiduLocationWirelessContract;

import com.jess.arms.di.scope.FragmentScope;
import com.slxk.hounddog.mvp.ui.fragment.BaiduLocationWirelessFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/28/2021 17:36
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = BaiduLocationWirelessModule.class, dependencies = AppComponent.class)
public interface BaiduLocationWirelessComponent {
    void inject(BaiduLocationWirelessFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BaiduLocationWirelessComponent.Builder view(BaiduLocationWirelessContract.View view);

        BaiduLocationWirelessComponent.Builder appComponent(AppComponent appComponent);

        BaiduLocationWirelessComponent build();
    }
}