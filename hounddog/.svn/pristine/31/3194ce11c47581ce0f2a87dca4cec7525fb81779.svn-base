package com.slxk.hounddog.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.slxk.hounddog.db.DeviceModel;
import com.slxk.hounddog.mvp.model.bean.DeviceBaseResultBean;
import com.slxk.hounddog.mvp.model.putbean.RemoveDevicePutBean;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/22/2021 09:21
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface DeviceListWirelessContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void submitDeleteDeviceSuccess(DeviceBaseResultBean deviceBaseResultBean, DeviceModel model);

        void onDismissProgressDialog();

    }
    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel{

        /**
         * 删除设备
         * @param bean
         * @return
         */
        Observable<DeviceBaseResultBean> submitDeleteDevice(RemoveDevicePutBean bean);

    }
}
