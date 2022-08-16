package com.slxk.hounddog.mvp.model.bean;

import com.google.gson.annotations.SerializedName;
import com.slxk.hounddog.mvp.model.entity.BaseBean;

import java.util.List;

/**
 * 合并账号
 */
public class MergeAccountResultBean extends BaseBean {

    private List<FailItemBean> fail_item; // 失败的
    private List<SucItemBean> suc_item; // 成功的

    public List<FailItemBean> getFail_item() {
        return fail_item;
    }

    public void setFail_item(List<FailItemBean> fail_item) {
        this.fail_item = fail_item;
    }

    public List<SucItemBean> getSuc_item() {
        return suc_item;
    }

    public void setSuc_item(List<SucItemBean> suc_item) {
        this.suc_item = suc_item;
    }

    public static class FailItemBean {
        /**
         * error_message : 8caddd4f-9acc-4812-9ad2-67afc7521d68
         * imei : 1856736202
         */

        @SerializedName("error_message")
        private String error_messageX; // 失败原因
        private long imei; // 用于展示的imei号

        public String getError_messageX() {
            return error_messageX;
        }

        public void setError_messageX(String error_messageX) {
            this.error_messageX = error_messageX;
        }

        public long getImei() {
            return imei;
        }

        public void setImei(long imei) {
            this.imei = imei;
        }
    }

    public static class SucItemBean {
        /**
         * imei : 1856736202
         * simei : b74ed002-c79b-41e0-a0a8-9f52aecd4e60
         */

        private long imei; // 用于展示的imei号
        private String simei;

        public long getImei() {
            return imei;
        }

        public void setImei(long imei) {
            this.imei = imei;
        }

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
        }
    }
}
