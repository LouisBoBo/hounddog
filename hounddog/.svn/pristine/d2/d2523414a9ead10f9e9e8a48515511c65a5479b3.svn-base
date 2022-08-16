package com.slxk.hounddog.mvp.model.api.service;

import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.bean.BaseStationAddressBean;
import com.slxk.hounddog.mvp.model.bean.BluetoothUpgradeVersionBean;
import com.slxk.hounddog.mvp.model.bean.CheckAppUpdateBean;
import com.slxk.hounddog.mvp.model.bean.ForgetPasswordResultBean;
import com.slxk.hounddog.mvp.model.bean.LoginResultBean;
import com.slxk.hounddog.mvp.model.bean.PhoneAreaResultBean;
import com.slxk.hounddog.mvp.model.bean.PhoneCodeResultBean;
import com.slxk.hounddog.mvp.model.bean.RegisterResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 公共请求服务类
 */
public interface PublicService {

    /**
     * 登录
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<LoginResultBean> submitLogin(@Body RequestBody requestBody);

    /**
     * 获取版本更新信息
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<CheckAppUpdateBean> getAppUpdate(@Body RequestBody requestBody);

    /**
     * 注册
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<RegisterResultBean> submitRegister(@Body RequestBody requestBody);

    /**
     * 获取手机号码国际区号
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<PhoneAreaResultBean> getPhoneArea(@Body RequestBody requestBody);

    /**
     * 获取手机验证码
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<PhoneCodeResultBean> getPhoneCode(@Body RequestBody requestBody);

    /**
     * 获取邮箱验证码
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<BaseBean> getEmailCode(@Body RequestBody requestBody);

    /**
     * 提交找回密码
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<ForgetPasswordResultBean> submitForgetPassword(@Body RequestBody requestBody);

    /**
     * 通过基站信息获取定位经纬度信息
     * @param url
     * @return
     */
    @GET
    Observable<BaseStationAddressBean> getBaseStationLocation(@Url String url);

    /**
     * 获取蓝牙升级版本信息
     * @param url
     * @return
     */
    @GET
    Observable<BluetoothUpgradeVersionBean> getBluetoothUpgradeVersion(@Url String url);

    /**
     * 上报蓝牙升级结果
     * @param url
     * @return
     */
    @GET
    Observable<BaseBean> submitUpgradeResult(@Url String url);

}
