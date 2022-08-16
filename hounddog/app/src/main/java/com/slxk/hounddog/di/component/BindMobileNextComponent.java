package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.BindMobileNextModule;
import com.slxk.hounddog.mvp.contract.BindMobileNextContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.BindMobileNextActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/09/2022 11:36
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = BindMobileNextModule.class, dependencies = AppComponent.class)
public interface BindMobileNextComponent {
    void inject(BindMobileNextActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        BindMobileNextComponent.Builder view(BindMobileNextContract.View view);

        BindMobileNextComponent.Builder appComponent(AppComponent appComponent);

        BindMobileNextComponent build();
    }
}