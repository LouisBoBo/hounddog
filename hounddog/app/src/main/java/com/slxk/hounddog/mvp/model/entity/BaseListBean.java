package com.slxk.hounddog.mvp.model.entity;

import com.google.gson.annotations.SerializedName;
import com.slxk.hounddog.mvp.model.api.Api;

import java.io.Serializable;

public class BaseListBean<T extends ListBean>  implements Serializable {

    private T data;
    @SerializedName("errcode")
    private int code;
    @SerializedName("error_message")
    private String msg;

    public T getData() {
        return data;
    }

    public long getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 请求是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        if (code == Api.SUCCESS) {
            return true;
        } else {
            return false;
        }
    }
}
