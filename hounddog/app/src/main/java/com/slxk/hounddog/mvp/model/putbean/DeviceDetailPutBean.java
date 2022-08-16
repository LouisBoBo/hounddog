package com.slxk.hounddog.mvp.model.putbean;

/**
 * 获取设备详细信息
 */
public class DeviceDetailPutBean {


    /**
     * module : device
     * func : GetDetail
     * params : {"get_user":true,"simei":"5715f5b1-c2ff-4231-b8f2-058e0a757074"}
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
         * get_user : true
         * simei : 5715f5b1-c2ff-4231-b8f2-058e0a757074
         */

        private Boolean get_user;
        private String simei;

        public Boolean getGet_user() {
            return get_user;
        }

        public void setGet_user(Boolean get_user) {
            this.get_user = get_user;
        }

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
        }
    }
}
