package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.OfflineMapDownLoadModule;
import com.slxk.hounddog.mvp.contract.OfflineMapDownLoadContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.OfflineMapDownLoadActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/24/2021 15:04
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = OfflineMapDownLoadModule.class, dependencies = AppComponent.class)
public interface OfflineMapDownLoadComponent {
    void inject(OfflineMapDownLoadActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        OfflineMapDownLoadComponent.Builder view(OfflineMapDownLoadContract.View view);
        OfflineMapDownLoadComponent.Builder appComponent(AppComponent appComponent);
        OfflineMapDownLoadComponent build();
    }
}