package com.slxk.hounddog.mvp.model.bean;

import com.google.gson.annotations.SerializedName;
import com.slxk.hounddog.mvp.model.entity.BaseBean;

import java.util.List;

/**
 * 添加设备，删除设备，冻结设备，回收站恢复设备统一返回类
 */
public class DeviceBaseResultBean extends BaseBean {

    /**
     * fail_items : [{"error_message":"c2c4f61c-b37e-46c8-b5bf-3bcb3ef8885a","imei":1598290937,"simei":"8199cfb8-5a8b-46b2-b2d8-34b377a01841"},{"error_message":"c3aec5b7-94bc-4170-9c3e-36970360259c","imei":1598290937,"simei":"1c2ec42f-c46b-49f0-b4ef-5911e90de879"},{"error_message":"9206c2ee-6de8-4c7e-9d0e-ea9bb582f847","imei":1598290937,"simei":"25a71f9e-1aaa-4a62-93fa-21c0cf73240b"},{"error_message":"d3935057-02c6-4a2b-93a3-04a3bc312b5b","imei":1598290937,"simei":"6ecf4c45-7b8a-44d4-b5da-9b30c307f691"}]
     * task_id : 547b25d8-5d2b-4c51-bae7-26a8559eba87
     */

    private String task_id;
    private List<FailItemsBean> fail_items; // 添加失败
    private List<SucItemsBean> suc_items; // 添加成功

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public List<FailItemsBean> getFail_items() {
        return fail_items;
    }

    public void setFail_items(List<FailItemsBean> fail_items) {
        this.fail_items = fail_items;
    }

    public List<SucItemsBean> getSuc_items() {
        return suc_items;
    }

    public void setSuc_items(List<SucItemsBean> suc_items) {
        this.suc_items = suc_items;
    }

    public static class FailItemsBean {
        /**
         * error_message : c2c4f61c-b37e-46c8-b5bf-3bcb3ef8885a
         * imei : 1598290937
         * simei : 8199cfb8-5a8b-46b2-b2d8-34b377a01841
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

    public static class SucItemsBean {

        private long imei;
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
