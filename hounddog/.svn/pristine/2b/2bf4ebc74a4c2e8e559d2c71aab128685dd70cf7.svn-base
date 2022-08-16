package com.slxk.hounddog.mvp.contract;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.slxk.hounddog.mvp.model.bean.ForgetPasswordResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.model.putbean.EmailCodePutBean;
import com.slxk.hounddog.mvp.model.putbean.ForgetPasswordPutBean;

import io.reactivex.Observable;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 02/17/2022 16:44
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface ForgetPasswordForEmailContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void getEmailCodeSuccess(BaseBean baseBean);

        void submitForgetPasswordSuccess(ForgetPasswordResultBean forgetPasswordResultBean);

    }
    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel{

        /**
         * 获取邮箱验证码
         * @param bean
         * @return
         */
        Observable<BaseBean> getEmailCode(EmailCodePutBean bean);

        /**
         * 提交找回密码
         * @param bean
         * @return
         */
        Observable<ForgetPasswordResultBean> submitForgetPassword(ForgetPasswordPutBean bean);

    }
}
