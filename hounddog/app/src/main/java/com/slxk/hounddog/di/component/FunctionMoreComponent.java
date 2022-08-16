package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.FunctionMoreModule;
import com.slxk.hounddog.mvp.contract.FunctionMoreContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.FunctionMoreActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/25/2021 11:44
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = FunctionMoreModule.class, dependencies = AppComponent.class)
public interface FunctionMoreComponent {
    void inject(FunctionMoreActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        FunctionMoreComponent.Builder view(FunctionMoreContract.View view);

        FunctionMoreComponent.Builder appComponent(AppComponent appComponent);

        FunctionMoreComponent build();
    }
}