package com.slxk.hounddog.mvp.model.putbean;

/**
 * 查询有轨迹数据的日期
 */
public class TrackHasDataPutBean {

    /**
     * module : location
     * func : QueryTrackDate
     * params : {"simei":"442c1d93-56a5-40f3-bcc4-34da7fcfada7","date_end":1383478452,"date_begin":1383478452}
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
         * simei : 442c1d93-56a5-40f3-bcc4-34da7fcfada7
         * date_end : 1383478452
         * date_begin : 1383478452
         */

        private String simei;
        private Long date_end;
        private Long date_begin;

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
        }

        public Long getDate_end() {
            return date_end;
        }

        public void setDate_end(Long date_end) {
            this.date_end = date_end;
        }

        public Long getDate_begin() {
            return date_begin;
        }

        public void setDate_begin(Long date_begin) {
            this.date_begin = date_begin;
        }
    }
}
