package com.slxk.hounddog.mvp.model.putbean;

/**
 * 获取sim卡详情
 */
public class SimDetailPutBean {

    /**
     * params : {"simei":"2c2b043b-30a7-4ab9-93ca-99f017cf0a27"}
     * func : Get
     * module : sim
     */

    private ParamsBean params;
    private String func;
    private String module;

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public static class ParamsBean {
        /**
         * iccid : 2c2b043b-30a7-4ab9-93ca-99f017cf0a27
         */
        private String iccid; // 设备的iccid号
        private String simei; // 设备的simei号

        public String getIccid() {
            return iccid;
        }

        public void setIccid(String iccid) {
            this.iccid = iccid;
        }

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
        }
    }
}
