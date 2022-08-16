package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.ForgetPasswordNextModule;
import com.slxk.hounddog.mvp.contract.ForgetPasswordNextContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.ForgetPasswordNextActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/09/2022 11:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = ForgetPasswordNextModule.class, dependencies = AppComponent.class)
public interface ForgetPasswordNextComponent {
    void inject(ForgetPasswordNextActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        ForgetPasswordNextComponent.Builder view(ForgetPasswordNextContract.View view);
        ForgetPasswordNextComponent.Builder appComponent(AppComponent appComponent);
        ForgetPasswordNextComponent build();
    }
}