package com.slxk.hounddog.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.slxk.hounddog.db.DeviceModel;
import com.slxk.hounddog.mvp.model.bean.BaseStationAddressBean;
import com.slxk.hounddog.mvp.model.bean.DeviceConfigResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceDetailResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListForManagerResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceConfigPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceDetailPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListForManagerPutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceListPutBean;
import com.slxk.hounddog.mvp.model.putbean.OnKeyFunctionPutBean;

import io.reactivex.Observable;


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
public interface BaiduLocationContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void getDeviceListSuccess(DeviceListResultBean deviceListResultBean, boolean isInitiativeRefresh);

        /**
         * 关闭加载框
         */
        void onDismissProgress();

        /**
         * 获取分组下的设备列表
         * @param deviceListForManagerResultBean
         */
        void getDeviceListForGroupSuccess(DeviceListForManagerResultBean deviceListForManagerResultBean, boolean isInitiativeRefresh);

        /**
         * 冻结设备，返回的是269877281
         * 数据已变更,请刷新 返回的是269877251，下发刷新接口
         */
        void getDeviceDetailError();

        void getDeviceDetailInfoSuccess(DeviceDetailResultBean deviceDetailResultBean);

        void submitOneKeyFunctionSuccess(BaseBean baseBean);

        void getDeviceConfigSuccess(DeviceConfigResultBean deviceConfigResultBean);

        /**
         * 获取经纬度信息成功
         * @param baseStationAddressBean
         */
        void getBaseStationLocationSuccess(BaseStationAddressBean baseStationAddressBean, DeviceModel model);
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
         * 获取设备详情接口
         * @param bean
         * @return
         */
        Observable<DeviceDetailResultBean> getDeviceDetailInfo(DeviceDetailPutBean bean);

        /**
         * 一键功能设置
         * @param bean
         * @return
         */
        Observable<BaseBean> submitOneKeyFunction(OnKeyFunctionPutBean bean);

        /**
         * 获取设备的配置信息，支持的功能等
         * @param bean
         * @return
         */
        Observable<DeviceConfigResultBean> getDeviceConfig(DeviceConfigPutBean bean);

        /**
         * 通过基站信息获取经纬度
         * @return
         */
        Observable<BaseStationAddressBean> getBaseStationLocation(String bts);

    }
}
