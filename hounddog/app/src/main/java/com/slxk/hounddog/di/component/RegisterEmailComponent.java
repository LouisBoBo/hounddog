package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.RegisterEmailModule;
import com.slxk.hounddog.mvp.contract.RegisterEmailContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.RegisterEmailActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 02/17/2022 16:30
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = RegisterEmailModule.class, dependencies = AppComponent.class)
public interface RegisterEmailComponent {
    void inject(RegisterEmailActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        RegisterEmailComponent.Builder view(RegisterEmailContract.View view);

        RegisterEmailComponent.Builder appComponent(AppComponent appComponent);

        RegisterEmailComponent build();
    }
}