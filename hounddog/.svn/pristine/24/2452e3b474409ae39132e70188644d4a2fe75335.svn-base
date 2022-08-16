package com.slxk.hounddog.mvp.model.putbean;

/**
 * 设备管理-设备列表
 */
public class DeviceListForManagerPutBean {

    /**
     * module : family
     * func : GetDeviceList
     * params : {"limit_size":1469678120,"last_simei":"b9c8abc6-dec9-4514-a7c6-d4fa268f3754","last_sgid":"0c9b67cc-cd1e-4c52-8688-908e9e5562d9","groupid":"30a979ac-8fcd-4045-a442-fafc1debb78b","familyid":"09631e60-b01b-4d38-9969-5aed3d7b783f"}
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
         * limit_size : 1469678120
         * last_simei : b9c8abc6-dec9-4514-a7c6-d4fa268f3754
         * last_sgid : 0c9b67cc-cd1e-4c52-8688-908e9e5562d9
         * groupid : 30a979ac-8fcd-4045-a442-fafc1debb78b
         * familyid : 09631e60-b01b-4d38-9969-5aed3d7b783f
         */

        private int limit_size; // 限定设备数量,默认100
        private String last_simei; // 最后获取到的simei
        private String last_sgid; // 上一次请求的最后一个sgidz
        private String groupid; // 车组id,,familyid和groupid二选一,如果设备登入，两个都不填
        private String familyid; // 用户组织id,familyid和groupid二选一,如果设备登入，两个都不填
        private boolean get_total; // true获取在线，离线，下线统计值
        private String state; // 查询在线状态，不传入，表示查询全部

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getLimit_size() {
            return limit_size;
        }

        public void setLimit_size(int limit_size) {
            this.limit_size = limit_size;
        }

        public String getLast_simei() {
            return last_simei;
        }

        public void setLast_simei(String last_simei) {
            this.last_simei = last_simei;
        }

        public String getLast_sgid() {
            return last_sgid;
        }

        public void setLast_sgid(String last_sgid) {
            this.last_sgid = last_sgid;
        }

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getFamilyid() {
            return familyid;
        }

        public void setFamilyid(String familyid) {
            this.familyid = familyid;
        }

        public boolean isGet_total() {
            return get_total;
        }

        public void setGet_total(boolean get_total) {
            this.get_total = get_total;
        }
    }
}
