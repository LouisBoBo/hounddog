package com.slxk.hounddog.mvp.model.putbean;


import com.slxk.hounddog.mvp.utils.Utils;

/**
 * 版本更新
 */
public class CheckAppUpdatePutBean {

    /**
     * module : user
     * func : CheckVersion
     * params : {"version":1825469593}
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
         * version : 1825469593
         */

        private String app_type; // app类型
        private int version; // app版本号
        private String lang = Utils.getLocaleLanguageShorthand(); // 登入之后的语言，非必填，默认中文, 例如:ch 中文; en 英语

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getApp_type() {
            return app_type;
        }

        public void setApp_type(String app_type) {
            this.app_type = app_type;
        }
    }
}
