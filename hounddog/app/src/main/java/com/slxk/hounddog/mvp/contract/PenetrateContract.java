package com.slxk.hounddog.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.slxk.hounddog.mvp.model.bean.DeviceDetailResultBean;
import com.slxk.hounddog.mvp.model.bean.PenetrateHistoryResultBean;
import com.slxk.hounddog.mvp.model.bean.PenetrateResultBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceDetailPutBean;
import com.slxk.hounddog.mvp.model.putbean.PenetrateHistoryPutBean;
import com.slxk.hounddog.mvp.model.putbean.PenetratePutBean;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/25/2021 14:15
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface PenetrateContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void getPenetrateHistorySuccess(PenetrateHistoryResultBean bean, boolean isRefresh);

        void finishRefresh();

        void endLoadMore();

        void setNoMore();

        void endLoadFail();

        void submitPenetrateSendSuccess(PenetrateResultBean penetrateResultBean);

        void getDeviceDetailInfoSuccess(DeviceDetailResultBean deviceDetailResultBean);

        void getDeviceDetailInfoFail();

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        /**
         * 透传-历史回复记录
         * @param bean
         * @return
         */
        Observable<PenetrateHistoryResultBean> getPenetrateHistory(PenetrateHistoryPutBean bean);

        /**
         * 下发透传指令
         * @param bean
         * @return
         */
        Observable<PenetrateResultBean> submitPenetrateSend(PenetratePutBean bean);

        /**
         * 获取设备详情接口
         * @param bean
         * @return
         */
        Observable<DeviceDetailResultBean> getDeviceDetailInfo(DeviceDetailPutBean bean);

    }
}
