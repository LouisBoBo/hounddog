package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.WXEntryModule;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.contract.WXEntryContract;
import com.slxk.hounddog.wxapi.WXEntryActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 10/13/2020 16:37
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = WXEntryModule.class, dependencies = AppComponent.class)
public interface WXEntryComponent {
    void inject(WXEntryActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        WXEntryComponent.Builder view(WXEntryContract.View view);
        WXEntryComponent.Builder appComponent(AppComponent appComponent);
        WXEntryComponent build();
    }
}