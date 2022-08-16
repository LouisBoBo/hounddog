package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.GoogleLocationModule;
import com.slxk.hounddog.mvp.contract.GoogleLocationContract;

import com.jess.arms.di.scope.FragmentScope;
import com.slxk.hounddog.mvp.ui.fragment.GoogleLocationFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/21/2021 14:26
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = GoogleLocationModule.class, dependencies = AppComponent.class)
public interface GoogleLocationComponent {
    void inject(GoogleLocationFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        GoogleLocationComponent.Builder view(GoogleLocationContract.View view);

        GoogleLocationComponent.Builder appComponent(AppComponent appComponent);

        GoogleLocationComponent build();
    }
}