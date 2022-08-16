package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.TrackBaiduModule;
import com.slxk.hounddog.mvp.contract.TrackBaiduContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.TrackBaiduActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 01/19/2022 16:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = TrackBaiduModule.class, dependencies = AppComponent.class)
public interface TrackBaiduComponent {
    void inject(TrackBaiduActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        TrackBaiduComponent.Builder view(TrackBaiduContract.View view);
        TrackBaiduComponent.Builder appComponent(AppComponent appComponent);
        TrackBaiduComponent build();
    }
}