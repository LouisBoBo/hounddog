package com.slxk.hounddog.mvp.model.putbean;

/**
 * 获取轨迹列表数据
 */
public class TrackListPutBean {

    /**
     * module : location
     * func : QueryLocation
     * params : {"time_end":530860572,"time_begin":530860572,"simei":"67f629b6-cdae-4edc-99d0-2e75c324aacb","limit_size":530860572,"last_time":530860572}
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
         * time_end : 530860572
         * time_begin : 530860572
         * simei : 67f629b6-cdae-4edc-99d0-2e75c324aacb
         * limit_size : 530860572
         * last_time : 530860572
         */

        private long time_end; //开始时间
        private long time_begin; // 结束时间
        private String simei;
        private int limit_size; // 限制获取数量
        private Long last_time; // 最后时间

        public long getTime_end() {
            return time_end;
        }

        public void setTime_end(long time_end) {
            this.time_end = time_end;
        }

        public long getTime_begin() {
            return time_begin;
        }

        public void setTime_begin(long time_begin) {
            this.time_begin = time_begin;
        }

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
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
    }
}
