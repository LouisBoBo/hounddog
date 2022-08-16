package com.slxk.hounddog.mvp.model.putbean;

import java.util.List;

/**
 * 获取操作记录数据
 */
public class OperationRecordPutBean {

    /**
     * module : device
     * func : GetLog
     * params : {"limit_size":935052433,"last_time":935052433,"last_imei":935052433,"end_time":935052433,"begin_time":935052433}
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
         * limit_size : 935052433
         * last_time : 935052433
         * last_imei : 935052433
         * end_time : 935052433
         * begin_time : 935052433
         */

        private int limit_size; // 限制条数，不填默认20条
        private Long last_time; // 获取到上一页日志的最后一条信息的时间戳
        private Long last_imei; // 获取到上一页日志的最后一条信息的imei号
        private Long end_time; // 拉取日志的结束时间,精确到ms,可以不用填写,默认当前时间
        private Long begin_time; // 拉取日志的起始时间,精确到ms,可以不用填写
        private List<String> simeis; // 设备号

        public List<String> getSimeis() {
            return simeis;
        }

        public void setSimeis(List<String> simeis) {
            this.simeis = simeis;
        }

        public int getLimit_size() {
            return limit_size;
        }

        public void setLimit_size(int limit_size) {
            this.limit_size = limit_size;
        }

        public Long getLast_time() {
            return last_time;
        }

        public void setLast_time(Long last_time) {
            this.last_time = last_time;
        }

        public Long getLast_imei() {
            return last_imei;
        }

        public void setLast_imei(Long last_imei) {
            this.last_imei = last_imei;
        }

        public Long getEnd_time() {
            return end_time;
        }

        public void setEnd_time(Long end_time) {
            this.end_time = end_time;
        }

        public Long getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(Long begin_time) {
            this.begin_time = begin_time;
        }
    }
}
