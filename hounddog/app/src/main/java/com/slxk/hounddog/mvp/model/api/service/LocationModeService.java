package com.slxk.hounddog.mvp.model.api.service;

import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.bean.LocationModeResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 定位模式
 */
public interface LocationModeService {

    /**
     * 获取定位模式
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<LocationModeResultBean> getLocationMode(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 设置定位模式
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<BaseBean> submitLocationMode(@Query("sid") String sid, @Body RequestBody requestBody);

}
