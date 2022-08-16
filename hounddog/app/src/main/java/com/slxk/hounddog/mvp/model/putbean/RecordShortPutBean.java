package com.slxk.hounddog.mvp.model.putbean;

/**
 * 开始短录音
 */
public class RecordShortPutBean {

    /**
     * module : record
     * func : StartRecord
     * params : {"time_second":833541066,"simei":"9c554045-44c0-4437-919e-f25eb6d10c5c"}
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
         * time_second : 833541066
         * simei : 9c554045-44c0-4437-919e-f25eb6d10c5c
         */

        private int time_second; // 录音时间 单位秒
        private String simei;

        public int getTime_second() {
            return time_second;
        }

        public void setTime_second(int time_second) {
            this.time_second = time_second;
        }

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
        }
    }
}
