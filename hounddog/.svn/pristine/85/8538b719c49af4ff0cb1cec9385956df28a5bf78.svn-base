package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.ModifyPasswordModule;
import com.slxk.hounddog.mvp.contract.ModifyPasswordContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.ModifyPasswordActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/04/2022 14:58
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = ModifyPasswordModule.class, dependencies = AppComponent.class)
public interface ModifyPasswordComponent {
    void inject(ModifyPasswordActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ModifyPasswordComponent.Builder view(ModifyPasswordContract.View view);

        ModifyPasswordComponent.Builder appComponent(AppComponent appComponent);

        ModifyPasswordComponent build();
    }
}