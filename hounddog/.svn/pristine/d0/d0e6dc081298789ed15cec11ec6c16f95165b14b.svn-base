package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.PayWebViewModule;
import com.slxk.hounddog.mvp.contract.PayWebViewContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.PayWebViewActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/15/2022 18:11
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = PayWebViewModule.class, dependencies = AppComponent.class)
public interface PayWebViewComponent {
    void inject(PayWebViewActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PayWebViewComponent.Builder view(PayWebViewContract.View view);

        PayWebViewComponent.Builder appComponent(AppComponent appComponent);

        PayWebViewComponent build();
    }
}