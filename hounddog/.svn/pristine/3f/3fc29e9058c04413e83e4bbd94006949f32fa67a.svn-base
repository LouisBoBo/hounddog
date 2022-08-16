package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.BindMobileModule;
import com.slxk.hounddog.mvp.contract.BindMobileContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.BindMobileActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/04/2022 11:34
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = BindMobileModule.class, dependencies = AppComponent.class)
public interface BindMobileComponent {
    void inject(BindMobileActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        BindMobileComponent.Builder view(BindMobileContract.View view);
        BindMobileComponent.Builder appComponent(AppComponent appComponent);
        BindMobileComponent build();
    }
}