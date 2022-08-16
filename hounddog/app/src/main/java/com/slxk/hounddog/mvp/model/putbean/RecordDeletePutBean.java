package com.slxk.hounddog.mvp.model.putbean;

import java.util.List;

/**
 * 录音删除
 */
public class RecordDeletePutBean {

    /**
     * module : record
     * func : DelRecord
     * params : {"simei":"536a8284-b6a7-4133-affa-1f3112b61792","recordtime_nodel":[53334003],"recordtime":[53334003,53334003]}
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
         * simei : 536a8284-b6a7-4133-affa-1f3112b61792
         * recordtime_nodel : [53334003]
         * recordtime : [53334003,53334003]
         */

        private String simei; // 设备号,设备号登入，可以不用
        private List<Long> recordtime_nodel; // 不需要删除的录音时间戳,如果不填，表示删除全部
        private List<Long> recordtime; // 录音时间戳,如果不填，表示删除全部

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
        }

        public List<Long> getRecordtime_nodel() {
            return recordtime_nodel;
        }

        public void setRecordtime_nodel(List<Long> recordtime_nodel) {
            this.recordtime_nodel = recordtime_nodel;
        }

        public List<Long> getRecordtime() {
            return recordtime;
        }

        public void setRecordtime(List<Long> recordtime) {
            this.recordtime = recordtime;
        }
    }
}
