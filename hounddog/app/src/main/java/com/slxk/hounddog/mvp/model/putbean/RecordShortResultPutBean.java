package com.slxk.hounddog.mvp.model.putbean;

/**
 * 开始短录音指令下发结果
 */
public class RecordShortResultPutBean {

    /**
     * module : record
     * func : GetStartRecordResult
     * params : {"simei":"18474479-b9dc-4a28-8747-1a14cb1b54ef"}
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
         * simei : 18474479-b9dc-4a28-8747-1a14cb1b54ef
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
