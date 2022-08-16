package com.slxk.hounddog.mvp.model.bean;

import com.google.gson.annotations.SerializedName;
import com.slxk.hounddog.mvp.model.entity.BaseBean;

import java.util.List;

/**
 * 解绑手机号码
 */
public class UnbindPhoneResultBean extends BaseBean {

    private List<FailItemsBean> fail_items;

    public List<FailItemsBean> getFail_items() {
        return fail_items;
    }

    public void setFail_items(List<FailItemsBean> fail_items) {
        this.fail_items = fail_items;
    }

    public static class FailItemsBean {
        /**
         * error_message : 82829a2f-f5bd-40fc-9aec-90c688a37b21
         * imei : 743787879
         * simei : b1d7ed8e-f78f-4b85-97a3-3f9987c88c1a
         */

        @SerializedName("error_message")
        private String error_messageX;
        private int imei;
        private String simei;

        public String getError_messageX() {
            return error_messageX;
        }

        public void setError_messageX(String error_messageX) {
            this.error_messageX = error_messageX;
        }

        public int getImei() {
            return imei;
        }

        public void setImei(int imei) {
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
