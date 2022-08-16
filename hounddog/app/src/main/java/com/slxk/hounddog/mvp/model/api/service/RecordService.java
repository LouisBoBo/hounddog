package com.slxk.hounddog.mvp.model.api.service;


import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.bean.RecordConfigResultBean;
import com.slxk.hounddog.mvp.model.bean.RecordResultBean;
import com.slxk.hounddog.mvp.model.bean.RecordScheduleResultBean;
import com.slxk.hounddog.mvp.model.bean.RecordShortResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 录音相关操作
 */
public interface RecordService {

    /**
     * 获取录音配置参数
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<RecordConfigResultBean> getRecordConfig(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 获取录音数据
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<RecordResultBean> getRecordData(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 开启or关闭声控录音
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<BaseBean> submitAutoRecordSwitch(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 开始短录音
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<BaseBean> submitShortRecord(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 获取开始短录音指令下发结果
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<RecordShortResultBean> getShortRecordResult(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 获取短录音进度
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<RecordScheduleResultBean> getRecordSchedule(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 删除录音文件
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<BaseBean> submitDeleteRecord(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 停止录音
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<BaseBean> submitRecordStop(@Query("sid") String sid, @Body RequestBody requestBody);

}
