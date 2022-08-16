package com.slxk.hounddog.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.slxk.hounddog.di.module.HMSScanQrCodeModule;
import com.slxk.hounddog.mvp.contract.HMSScanQrCodeContract;

import com.jess.arms.di.scope.ActivityScope;
import com.slxk.hounddog.mvp.ui.activity.HMSScanQrCodeActivity;   


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 02/17/2022 17:53
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = HMSScanQrCodeModule.class, dependencies = AppComponent.class)
public interface HMSScanQrCodeComponent {
    void inject(HMSScanQrCodeActivity activity);
    @Component.Builder
    interface Builder {
        @BindsInstance
        HMSScanQrCodeComponent.Builder view(HMSScanQrCodeContract.View view);
        HMSScanQrCodeComponent.Builder appComponent(AppComponent appComponent);
        HMSScanQrCodeComponent build();
    }
}