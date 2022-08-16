package com.slxk.hounddog.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.slxk.hounddog.mvp.model.bean.CheckAppUpdateBean;
import com.slxk.hounddog.mvp.model.bean.DeviceGroupResultBean;
import com.slxk.hounddog.mvp.model.bean.MergeAccountResultBean;
import com.slxk.hounddog.mvp.model.bean.PhoneCodeResultBean;
import com.slxk.hounddog.mvp.model.putbean.CheckAppUpdatePutBean;
import com.slxk.hounddog.mvp.model.putbean.DeviceGroupPutBean;
import com.slxk.hounddog.mvp.model.putbean.MergeAccountPutBean;
import com.slxk.hounddog.mvp.model.putbean.PhoneCodePutBean;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/20/2021 15:36
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface MainContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void getAppUpdateSuccess(CheckAppUpdateBean checkAppUpdateBean);

        void getPhoneCodeSuccess(PhoneCodeResultBean phoneCodeResultBean);

        void getDeviceGroupListSuccess(DeviceGroupResultBean deviceGroupResultBean);

        void submitMergeAccountSuccess(MergeAccountResultBean mergeAccountResultBean);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        /**
         * 获取版本更新
         * @param bean
         * @return
         */
        Observable<CheckAppUpdateBean> getAppUpdate(CheckAppUpdatePutBean bean);

        /**
         * 获取手机验证码
         * @param bean
         * @return
         */
        Observable<PhoneCodeResultBean> getPhoneCode(PhoneCodePutBean bean);

        /**
         * 获取车组织列表和车组列表
         * @param bean
         * @return
         */
        Observable<DeviceGroupResultBean> getDeviceGroupList(DeviceGroupPutBean bean);

        /**
         * 合并账号
         * @param bean
         * @return
         */
        Observable<MergeAccountResultBean> submitMergeAccount(MergeAccountPutBean bean);

    }
}
