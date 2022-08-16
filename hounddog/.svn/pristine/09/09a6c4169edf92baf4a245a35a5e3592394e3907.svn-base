package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.BindForeignModule;
import com.slxk.hounddog.mvp.contract.BindForeignContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.BindForeignActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/09/2022 18:11
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = BindForeignModule.class, dependencies = AppComponent.class)
public interface BindForeignComponent {
    void inject(BindForeignActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        BindForeignComponent.Builder view(BindForeignContract.View view);
        BindForeignComponent.Builder appComponent(AppComponent appComponent);
        BindForeignComponent build();
    }
}