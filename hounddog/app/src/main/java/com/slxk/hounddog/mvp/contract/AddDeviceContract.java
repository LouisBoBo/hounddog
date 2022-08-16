package com.slxk.hounddog.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.slxk.hounddog.mvp.model.bean.DeviceBaseResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceGroupResultBean;
import com.slxk.hounddog.mvp.model.putbean.AddDevicePutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceGroupPutBean;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/25/2021 09:42
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface AddDeviceContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void getDeviceGroupListSuccess(DeviceGroupResultBean deviceGroupResultBean);

        void submitAddDeviceSuccess(DeviceBaseResultBean deviceBaseResultBean);

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        /**
         * 获取车组织列表和车组列表
         * @param bean
         * @return
         */
        Observable<DeviceGroupResultBean> getDeviceGroupList(DeviceGroupPutBean bean);

        /**
         * 添加设备
         * @param bean
         * @return
         */
        Observable<DeviceBaseResultBean> submitAddDevice(AddDevicePutBean bean);

    }
}
