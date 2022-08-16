package com.slxk.hounddog.mvp.model.putbean;

/**
 * 下发透传指令
 */
public class PenetratePutBean {

    /**
     * module : sim
     * func : SimCmd
     * params : {"content":"ab09f1e6-2ae5-42f5-80eb-20f67e20e375","only_sim":false,"simei":"c2c1bafd-1bf3-4a00-9306-a24e1a626363"}
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
         * content : ab09f1e6-2ae5-42f5-80eb-20f67e20e375
         * only_sim : false
         * simei : c2c1bafd-1bf3-4a00-9306-a24e1a626363
         */

        private String content; // 短信内容
        private Boolean only_sim; // 是否只通过给sim卡下发短信的方式，默认false，
        private String simei; // 设备号，如果是设备登入，也可以不用传

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isOnly_sim() {
            return only_sim;
        }

        public void setOnly_sim(boolean only_sim) {
            this.only_sim = only_sim;
        }

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
        }
    }
}
