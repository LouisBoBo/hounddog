package com.slxk.hounddog.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.slxk.hounddog.db.DeviceModel;
import com.slxk.hounddog.mvp.model.bean.DeviceBaseResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListForManagerResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListResultBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListForManagerPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListPutBean;
import com.slxk.hounddog.mvp.model.putbean.RemoveDevicePutBean;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/21/2021 14:28
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface DeviceListContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void getDeviceListSuccess(DeviceListResultBean deviceListResultBean ,boolean isLoadmore);

        /**
         * 获取分组下的设备列表
         * @param deviceListForManagerResultBean
         */
        void getDeviceListForGroupSuccess(DeviceListForManagerResultBean deviceListForManagerResultBean ,boolean isLoadmore);

        void submitDeleteDeviceSuccess(DeviceBaseResultBean deviceBaseResultBean, DeviceModel model);

        void onDismissProgressDialog();

        void finishRefresh();

        void endLoadMore();

        void setNoMore();

        void endLoadFail();

    }
    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel{

        /**
         * 获取当前账号下设备列表，设备号登录同求
         * @param bean
         * @return
         */
        Observable<DeviceListResultBean> getDeviceList(DeviceListPutBean bean);

        /**
         * 获取分组下的设备列表
         * @param bean
         * @return
         */
        Observable<DeviceListForManagerResultBean> getDeviceListForGroup(DeviceListForManagerPutBean bean);

        /**
         * 删除设备
         * @param bean
         * @return
         */
        Observable<DeviceBaseResultBean> submitDeleteDevice(RemoveDevicePutBean bean);

    }
}
