package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.RegisterNextModule;
import com.slxk.hounddog.mvp.contract.RegisterNextContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.RegisterNextActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/16/2022 17:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = RegisterNextModule.class, dependencies = AppComponent.class)
public interface RegisterNextComponent {
    void inject(RegisterNextActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        RegisterNextComponent.Builder view(RegisterNextContract.View view);

        RegisterNextComponent.Builder appComponent(AppComponent appComponent);

        RegisterNextComponent build();
    }
}