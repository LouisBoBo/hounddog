package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.BaiduLocationModule;
import com.slxk.hounddog.mvp.contract.BaiduLocationContract;

import com.jess.arms.di.scope.FragmentScope;
import com.slxk.hounddog.mvp.ui.fragment.BaiduLocationFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/21/2021 14:23
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = BaiduLocationModule.class, dependencies = AppComponent.class)
public interface BaiduLocationComponent {
    void inject(BaiduLocationFragment fragment);
    @Component.Builder
    interface Builder {
        @BindsInstance
        BaiduLocationComponent.Builder view(BaiduLocationContract.View view);
        BaiduLocationComponent.Builder appComponent(AppComponent appComponent);
        BaiduLocationComponent build();
    }
}