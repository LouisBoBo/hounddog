package com.slxk.hounddog.mvp.model.api.service;

import com.slxk.hounddog.mvp.model.api.Api;
import com.slxk.hounddog.mvp.model.bean.LogoutAccountResultBean;
import com.slxk.hounddog.mvp.model.bean.MergeAccountResultBean;
import com.slxk.hounddog.mvp.model.entity.BaseBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {

    /**
     * 退出登录
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<BaseBean> submitSignOut(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 注销账号
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<LogoutAccountResultBean> submitLogoutAccount(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 修改密码
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<BaseBean> submitModifyPassword(@Query("sid") String sid, @Body RequestBody requestBody);

    /**
     * 合并账号
     * @param sid 用户唯一id
     * @param requestBody
     * @return
     */
    @Headers({Api.HEADER_RELEASE_TYPE})
    @POST("/mapi")
    Observable<MergeAccountResultBean> submitMergeAccount(@Query("sid") String sid, @Body RequestBody requestBody);


}
