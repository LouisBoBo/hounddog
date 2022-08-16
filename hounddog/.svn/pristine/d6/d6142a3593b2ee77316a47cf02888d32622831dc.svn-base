package com.slxk.hounddog.mvp.model.putbean;

/**
 * 远程录音数据获取
 */
public class RecordPutBean {

    /**
     * module : record
     * func : GetRecord
     * params : {"limit_size":881964356,"imei":881964356,"endtime":881964356,"begintime":881964356}
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
         * limit_size : 881964356
         * imei : 881964356
         * endtime : 881964356
         * begintime : 881964356
         */

        private int limit_size; // 限制获取数量
        private String simei; // 设备号
        private Long endtime; // 结束时间
        private Long begintime; // 开始时间(不传开始时间表示获取结束时间之前的100条)

        public int getLimit_size() {
            return limit_size;
        }

        public void setLimit_size(int limit_size) {
            this.limit_size = limit_size;
        }

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
        }

        public Long getEndtime() {
            return endtime;
        }

        public void setEndtime(Long endtime) {
            this.endtime = endtime;
        }

        public Long getBegintime() {
            return begintime;
        }

        public void setBegintime(Long begintime) {
            this.begintime = begintime;
        }
    }
}
