package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.SimDetailModule;
import com.slxk.hounddog.mvp.contract.SimDetailContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.SimDetailActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/25/2021 15:38
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = SimDetailModule.class, dependencies = AppComponent.class)
public interface SimDetailComponent {
    void inject(SimDetailActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        SimDetailComponent.Builder view(SimDetailContract.View view);
        SimDetailComponent.Builder appComponent(AppComponent appComponent);
        SimDetailComponent build();
    }
}