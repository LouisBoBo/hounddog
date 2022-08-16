package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.ModifyNameModule;
import com.slxk.hounddog.mvp.contract.ModifyNameContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.ModifyNameActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/23/2021 15:27
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = ModifyNameModule.class, dependencies = AppComponent.class)
public interface ModifyNameComponent {
    void inject(ModifyNameActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ModifyNameComponent.Builder view(ModifyNameContract.View view);

        ModifyNameComponent.Builder appComponent(AppComponent appComponent);

        ModifyNameComponent build();
    }
}