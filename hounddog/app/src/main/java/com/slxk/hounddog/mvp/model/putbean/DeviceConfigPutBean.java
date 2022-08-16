package com.slxk.hounddog.mvp.model.putbean;

/**
 * 获取设备的配置信息，支持的功能等
 */
public class DeviceConfigPutBean {

    /**
     * module : device
     * func : GetConfig
     * params : {"simei":"6f9a7285-a2da-4a8f-bf23-6b3005ba9ea7"}
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
         * simei : 6f9a7285-a2da-4a8f-bf23-6b3005ba9ea7
         */

        private String simei;
        private String type; // 拉取信息种类(必填)

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
