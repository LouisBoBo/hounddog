package com.slxk.hounddog.mvp.model.putbean;


import com.slxk.hounddog.mvp.utils.Utils;

/**
 * 获取手机号码国际区号
 */
public class PhoneAreaPutBean {

    /**
     * module : user
     * func : GetPhoneArea
     * params : {"last_country":"3fd2a299-5a6f-4c77-acca-bc85dc93604e"}
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
         * last_country : 3fd2a299-5a6f-4c77-acca-bc85dc93604e
         */

        private String last_country; // 上次获取到的最后的last_country
        private String lang = Utils.getLocaleLanguageShorthand(); // 登入之后的语言，非必填，默认中文, 例如:ch 中文; en 英语

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getLast_country() {
            return last_country;
        }

        public void setLast_country(String last_country) {
            this.last_country = last_country;
        }
    }
}
