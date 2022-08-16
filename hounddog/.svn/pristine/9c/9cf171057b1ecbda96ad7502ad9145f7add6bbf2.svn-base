package com.slxk.hounddog.mvp.model.putbean;


import com.slxk.hounddog.mvp.utils.Utils;

/**
 * 获取手机验证码
 */
public class PhoneCodePutBean {

    /**
     * module : user
     * func : MobVerifyCode
     * params : {"zone":"182bd42e-84d3-4753-9b3e-98855ce1ae77","phone":"40b0695b-a077-4906-b8a5-72bde6d17569","key":"f8bb401c-c648-4eac-a481-85db12949f57","code":"d1437eb4-8cff-48b9-a5c2-3d7dcf3f7b2d"}
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
         * zone : 182bd42e-84d3-4753-9b3e-98855ce1ae77
         * phone : 40b0695b-a077-4906-b8a5-72bde6d17569
         * key : f8bb401c-c648-4eac-a481-85db12949f57
         * code : d1437eb4-8cff-48b9-a5c2-3d7dcf3f7b2d
         */

        private String zone; // 区号
        private String phone; // 电话号码
        private String key; // appkey
        private String code; // 验证码模板
        private String lang = Utils.getLocaleLanguageShorthand(); // 登入之后的语言，非必填，默认中文, 例如:ch 中文; en 英语

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
