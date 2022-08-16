package com.slxk.hounddog.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.slxk.hounddog.mvp.model.bean.DeviceConfigResultBean;
import com.slxk.hounddog.mvp.model.bean.LocationModeResultBean;
import com.slxk.hounddog.mvp.model.bean.SetConfigResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceConfigPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceConfigSetPutBean;
import com.slxk.hounddog.mvp.model.putbean.LocationModePutBean;
import com.slxk.hounddog.mvp.model.putbean.LocationModeSetPutBean;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/24/2021 09:19
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface LocationModeContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void getLocationModeSuccess(LocationModeResultBean locationModeResultBean);

        void submitLocationModeSuccess(BaseBean baseBean);

        void getDeviceConfigSuccess(DeviceConfigResultBean deviceConfigResultBean);

        void setDeviceConfigSuccess(BaseBean baseBean);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        /**
         * 获取定位模式
         * @param bean
         * @return
         */
        Observable<LocationModeResultBean> getLocationMode(LocationModePutBean bean);

        /**
         * 设置定位模式
         * @param bean
         * @return
         */
        Observable<BaseBean> submitLocationMode(LocationModeSetPutBean bean);

        /**
         * 获取设备的配置信息，支持的功能等
         * @param bean
         * @return
         */
        Observable<DeviceConfigResultBean> getDeviceConfig(DeviceConfigPutBean bean);

        /**
         * 设置设备的配置信息
         * @param bean
         * @return
         */
        Observable<SetConfigResultBean> setDeviceConfig(DeviceConfigSetPutBean bean);

    }
}
