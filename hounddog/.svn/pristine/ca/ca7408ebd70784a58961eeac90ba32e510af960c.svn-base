package com.slxk.hounddog.mvp.model.putbean;

import java.util.List;

/**
 * 获取设备列表上传参数
 */
public class DeviceListPutBean {

    /**
     * module : device
     * func : GetRunInfo
     * params : {"simei":["aa25f96b-e899-41a9-adad-688754dc8d86","a152fe4d-a816-4a3e-80ee-b9e9ada0f1ee","fc215b27-47ac-487f-a186-2190c59ba33c","863d8ff1-8e13-45e3-82e0-dc8d8e880c53"],"sfamily":"ffd9177b-4e94-49e6-a630-5e8fdcd873cc","limit_size":1039101628}
     */

    private String module;
    private String func;
    private ParamsBean params;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public static class ParamsBean {
        /**
         * simei : ["aa25f96b-e899-41a9-adad-688754dc8d86","a152fe4d-a816-4a3e-80ee-b9e9ada0f1ee","fc215b27-47ac-487f-a186-2190c59ba33c","863d8ff1-8e13-45e3-82e0-dc8d8e880c53"]
         * sfamily : ffd9177b-4e94-49e6-a630-5e8fdcd873cc
         * limit_size : 1039101628
         */

        private String sfamily; // 需要查询的车组织id,如果设备号登入，可以不用传
        private int limit_size; // 限制条数，默认200台,不能超过200
        private List<String> simei; // 需要查询的实时数据的设备列表,可以传入特别关心的设备号，否者服务器将返回family下的小于200台设备信息,如果设备号登入，可以不用传
        private List<String> call_simei; // 需要下发位置查询的设备列表，如果非点名模式，将忽略这个参数
        private Boolean call_all_dev; // 如果设备处于点名模式，是否针对返回的设备集体下发一次位置查询，默认false，不下发;设置true，下发
        private String last_sgid;//上一次请求的最后一个sgid
        private String last_simei;//最后获取到的simei

        public String getSfamily() {
            return sfamily;
        }

        public void setSfamily(String sfamily) {
            this.sfamily = sfamily;
        }

        public int getLimit_size() {
            return limit_size;
        }

        public void setLimit_size(int limit_size) {
            this.limit_size = limit_size;
        }

        public List<String> getSimei() {
            return simei;
        }

        public void setSimei(List<String> simei) {
            this.simei = simei;
        }

        public List<String> getCall_simei() {
            return call_simei;
        }

        public void setCall_simei(List<String> call_simei) {
            this.call_simei = call_simei;
        }

        public Boolean getCall_all_dev() {
            return call_all_dev;
        }

        public void setCall_all_dev(Boolean call_all_dev) {
            this.call_all_dev = call_all_dev;
        }

        public String getLast_sgid() {
            return last_sgid;
        }

        public void setLast_sgid(String last_sgid) {
            this.last_sgid = last_sgid;
        }

        public String getLast_simei() {
            return last_simei;
        }

        public void setLast_simei(String last_simei) {
            this.last_simei = last_simei;
        }
    }
}
