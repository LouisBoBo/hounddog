package com.slxk.hounddog.mvp.model.api.service;


import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.bean.TrackHasDataResultBean;
import com.slxk.hounddog.mvp.model.bean.TrackListResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 轨迹管理
 */
public interface TrackService {

    /**
     * 获取有轨迹数据的日期
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<TrackHasDataResultBean> getTrackHasForData(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 获取轨迹列表数据
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<TrackListResultBean> getTrackList(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 实时追踪
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<BaseBean> submitTempTracking(@Query("sid") String sid, @Body RequestBody requestBody);

}
