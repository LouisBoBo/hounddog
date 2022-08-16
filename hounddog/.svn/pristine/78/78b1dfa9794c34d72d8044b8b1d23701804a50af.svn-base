package com.slxk.hounddog.mvp.model.bean;


import com.google.gson.annotations.SerializedName;
import com.slxk.hounddog.mvp.model.entity.BaseBean;

import java.util.List;


/**
 *setConfig 返回
 */
public class SetConfigResultBean extends BaseBean {

    /**
     * "fail_items":[{"error_message":"设备不支持","imei":23777777069,"simei":"SyqPbQe7an9TIOMz-gl57O8KDU9sVjHPG3VKqjIGluY"}]}
     */

    private List<SucItemsBean> fail_items; // ，有操作失败的imei号，通过这个字段返回

    public List<SucItemsBean> getFail_items() {
        return fail_items;
    }

    public void setFail_items(List<SucItemsBean> fail_items) {
        this.fail_items = fail_items;
    }

    public static class SucItemsBean {
        /**
         * imei : 570512007
         * simei : 5fe1da04-35b1-4b60-8d79-9a75ac568a49
         */

        @SerializedName("error_message")
        private String error_messageX;
        private long imei;
        private String simei;

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

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
        }
    }
}
