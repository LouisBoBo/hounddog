package com.slxk.hounddog.mvp.model.putbean;

/**
 * 合并账号
 */
public class MergeAccountPutBean {

    /**
     * module : user
     * func : AddBindPhoneDev
     * params : {"code":"6e2d7d3b-c1d6-471b-9b74-df6520e07d8a","key":"33162f75-d1b0-44e5-815a-8bedb0a3619a","sgid":"345d139c-1a51-437a-844c-6206b11e633a","zone":"6206394a-e6b8-4fa0-aba2-6d32e8cf0474"}
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
         * code : 6e2d7d3b-c1d6-471b-9b74-df6520e07d8a
         * key : 33162f75-d1b0-44e5-815a-8bedb0a3619a
         * sgid : 345d139c-1a51-437a-844c-6206b11e633a
         * zone : 6206394a-e6b8-4fa0-aba2-6d32e8cf0474
         */

        private String code; // 验证码
        private String key; // appkey
        private String sgid; // 需要合并的当前组织的分组id

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getSgid() {
            return sgid;
        }

        public void setSgid(String sgid) {
            this.sgid = sgid;
        }

    }
}
