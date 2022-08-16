package com.slxk.hounddog.mvp.model.putbean;

/**
 * 获取录音进度
 */
public class RecordSchedulePutBean {

    /**
     * module : record
     * func : GetRecordSchedule
     * params : {"simei":"46e42ff7-d482-4e12-aad1-a6b0b1fe8e80"}
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
         * simei : 46e42ff7-d482-4e12-aad1-a6b0b1fe8e80
         */

        private String simei;

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
        }
    }
}
