package com.slxk.hounddog.mvp.model.putbean;

import java.util.List;

/**
 * 透传-历史回复列表
 */
public class PenetrateHistoryPutBean {

    /**
     * module : cmd
     * func : UserCmdList
     * params : {"begin_time":1688290300,"end_time":1688290300,"is_summary":true,"last_imei":1688290300,"last_time":1688290300,"last_type":1688290300,"limit_size":1688290300,"simei_all":"e1888ace-3218-4f65-9c7c-d4db4d176a7f","simeis":["742207e9-15ed-4a4d-8205-1ec9cd5a6819"],"type":[1688290300,1688290300],"uid":"01b3c8ad-6b09-4fea-a9b0-ec1437330e55"}
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
         * begin_time : 1688290300
         * end_time : 1688290300
         * is_summary : true
         * last_imei : 1688290300
         * last_time : 1688290300
         * last_type : 1688290300
         * limit_size : 1688290300
         * simei_all : e1888ace-3218-4f65-9c7c-d4db4d176a7f
         * simeis : ["742207e9-15ed-4a4d-8205-1ec9cd5a6819"]
         * type : [1688290300,1688290300]
         * uid : 01b3c8ad-6b09-4fea-a9b0-ec1437330e55
         */

        private Long begin_time; // 拉取日志的起始时间,精确到ms,可以不用填写
        private Long end_time; // 拉取日志的结束时间,精确到ms,可以不用填写,默认当前时间
        private Boolean is_summary; // 是否需要统计数据，默认不需要
        private Long last_imei; // 获取到上一页日志的最后一条信息的imei号
        private Long last_time; // 获取到上一页日志的最后一条信息的时间戳
        private Integer last_type; // 获取到上一页日志的最后一条信息的类型
        private int limit_size; // 限制条数，不填默认20条
        private String simei_all; // 针对单个设备查询他全部的操作记录，只有销售商有这个权限,将忽略参数uid，simeis
        private String uid; // 筛选指定账号的操作日志，如果不填写，返回当前账号的操作日志
        private List<String> simeis; // 如果查询特定账号的日志，可以对特定的设备号进行查询
        private List<Integer> type; // 筛选指令类型，不填返回全部

        public Long getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(Long begin_time) {
            this.begin_time = begin_time;
        }

        public Long getEnd_time() {
            return end_time;
        }

        public void setEnd_time(Long end_time) {
            this.end_time = end_time;
        }

        public Boolean getIs_summary() {
            return is_summary;
        }

        public void setIs_summary(Boolean is_summary) {
            this.is_summary = is_summary;
        }

        public Long getLast_imei() {
            return last_imei;
        }

        public void setLast_imei(Long last_imei) {
            this.last_imei = last_imei;
        }

        public Long getLast_time() {
            return last_time;
        }

        public void setLast_time(Long last_time) {
            this.last_time = last_time;
        }

        public Integer getLast_type() {
            return last_type;
        }

        public void setLast_type(Integer last_type) {
            this.last_type = last_type;
        }

        public int getLimit_size() {
            return limit_size;
        }

        public void setLimit_size(int limit_size) {
            this.limit_size = limit_size;
        }

        public String getSimei_all() {
            return simei_all;
        }

        public void setSimei_all(String simei_all) {
            this.simei_all = simei_all;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public List<String> getSimeis() {
            return simeis;
        }

        public void setSimeis(List<String> simeis) {
            this.simeis = simeis;
        }

        public List<Integer> getType() {
            return type;
        }

        public void setType(List<Integer> type) {
            this.type = type;
        }
    }
}
