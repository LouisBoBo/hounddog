package com.slxk.hounddog.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.slxk.hounddog.mvp.model.bean.PhoneAreaResultBean;
import com.slxk.hounddog.mvp.model.bean.PhoneCodeResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.ModifyPasswordPutBean;
import com.slxk.hounddog.mvp.model.putbean.PhoneAreaPutBean;
import com.slxk.hounddog.mvp.model.putbean.PhoneCodePutBean;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/04/2022 14:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface ModifyPhoneContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void getPhoneAreaSuccess(PhoneAreaResultBean phoneAreaResultBean);

        void getPhoneCodeSuccess(PhoneCodeResultBean phoneCodeResultBean);

        void submitBindMobileSuccess(BaseBean baseBean);

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        /**
         * 获取手机号码国际区号
         * @param bean
         * @return
         */
        Observable<PhoneAreaResultBean> getPhoneArea(PhoneAreaPutBean bean);

        /**
         * 获取手机验证码
         * @param bean
         * @return
         */
        Observable<PhoneCodeResultBean> getPhoneCode(PhoneCodePutBean bean);

        /**
         * 修改密码
         * @param bean
         * @return
         */
        Observable<BaseBean> submitBindMobile(ModifyPasswordPutBean bean);

    }
}
