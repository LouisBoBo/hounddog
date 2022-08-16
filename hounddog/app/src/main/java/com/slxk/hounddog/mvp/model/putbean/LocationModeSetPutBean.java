package com.slxk.hounddog.mvp.model.putbean;

/**
 * 设置定位模式
 */
public class LocationModeSetPutBean {

    /**
     * module : loc
     * func : SetLocMode
     * params : {"mode_id":1472107175,"mode_type":1472107175,"simei":"e7e3b4dd-8e17-41ea-a280-0aa9aff19e14","smode_value":"f891d1ba-ac85-4f94-b601-395cefe94dd4"}
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
         * mode_id : 1472107175
         * mode_type : 1472107175
         * simei : e7e3b4dd-8e17-41ea-a280-0aa9aff19e14
         * smode_value : f891d1ba-ac85-4f94-b601-395cefe94dd4
         */

        private int mode_id;
        private Integer mode_type;
        private String simei;
        private String smode_value;

        public int getMode_id() {
            return mode_id;
        }

        public void setMode_id(int mode_id) {
            this.mode_id = mode_id;
        }

        public Integer getMode_type() {
            return mode_type;
        }

        public void setMode_type(Integer mode_type) {
            this.mode_type = mode_type;
        }

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
        }

        public String getSmode_value() {
            return smode_value;
        }

        public void setSmode_value(String smode_value) {
            this.smode_value = smode_value;
        }
    }
}
