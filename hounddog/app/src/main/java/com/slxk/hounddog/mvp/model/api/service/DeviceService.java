package com.slxk.hounddog.mvp.model.api.service;

import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.bean.DeviceBaseResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceConfigResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceDetailResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceGroupResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListForManagerResultBean;
import com.slxk.hounddog.mvp.model.bean.DeviceListResultBean;
import com.slxk.hounddog.mvp.model.bean.OperationRecordResultBean;
import com.slxk.hounddog.mvp.model.bean.PenetrateHistoryResultBean;
import com.slxk.hounddog.mvp.model.bean.PenetrateResultBean;
import com.slxk.hounddog.mvp.model.bean.SetConfigResultBean;
import com.slxk.hounddog.mvp.model.bean.UnbindPhoneResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 设备相关请求
 */
public interface DeviceService {

    /**
     * 获取当前账号下设备列表，设备号登录同求
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<DeviceListResultBean> getDeviceList(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 修改设备详细信息
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<BaseBean> submitDetailModify(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 获取设备详情
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<DeviceDetailResultBean> getDeviceDetailInfo(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 透传-历史回复记录
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<PenetrateHistoryResultBean> getPenetrateHistory(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 透传-下发透传指令
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<PenetrateResultBean> submitPenetrateSend(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 获取操作记录数据
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<OperationRecordResultBean> getOperationRecord(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 一键功能设置
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<BaseBean> submitOneKeyFunction(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 获取车组织列表和车组列表
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<DeviceGroupResultBean> getDeviceGroupList(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 获取分组下的设备列表
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<DeviceListForManagerResultBean> getDeviceListForGroup(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 添加设备
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<DeviceBaseResultBean> submitAddDevice(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 移除设备
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<DeviceBaseResultBean> submitRemoveDelete(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 获取设备的配置信息，支持的功能等
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<DeviceConfigResultBean> getDeviceConfig(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 设置设备的配置信息
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<SetConfigResultBean> setDeviceConfig(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 解绑手机号码
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<UnbindPhoneResultBean> submitUnbindPhone(@Query("sid") String sid, @Body RequestBody requestBody);

}
