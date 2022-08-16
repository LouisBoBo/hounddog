package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.AmapLocationModule;
import com.slxk.hounddog.mvp.contract.AmapLocationContract;

import com.jess.arms.di.scope.FragmentScope;
import com.slxk.hounddog.mvp.ui.fragment.AmapLocationFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/21/2021 14:22
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = AmapLocationModule.class, dependencies = AppComponent.class)
public interface AmapLocationComponent {
    void inject(AmapLocationFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AmapLocationComponent.Builder view(AmapLocationContract.View view);

        AmapLocationComponent.Builder appComponent(AppComponent appComponent);

        AmapLocationComponent build();
    }
}