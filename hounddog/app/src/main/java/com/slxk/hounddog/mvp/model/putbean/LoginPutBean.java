package com.slxk.hounddog.mvp.model.putbean;

/**
 * 登录上传参数bean
 */
public class LoginPutBean {

    /**
     * module : user
     * func : Login
     * params : {"phone":"35e18315-f769-4225-8bc7-61b810914da9","mpm_identify":"96a36699-f9fe-44e3-baf4-87d30be00cb1","mpm":"e_jpush|e_hpush","info":"3ac9d716-aa0b-465f-a5c6-2795a52db13c","flag":"e_account_login|e_device_login","account":"40a14dca-d741-41c3-85f8-6afb678c594c","type":"e4e249a6-782e-4149-81b1-8ba3d2b3e774","pwd_md5":"53eb93b3-88bb-4da3-9070-f60396371d96"}
     */

    private String module; // 模块名,user
    private String func; // 接口名称,Login
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
         * phone : 35e18315-f769-4225-8bc7-61b810914da9
         * mpm_identify : 96a36699-f9fe-44e3-baf4-87d30be00cb1
         * mpm : e_jpush|e_hpush
         * info : 3ac9d716-aa0b-465f-a5c6-2795a52db13c
         * flag : e_account_login|e_device_login
         * account : 40a14dca-d741-41c3-85f8-6afb678c594c
         * type : e4e249a6-782e-4149-81b1-8ba3d2b3e774
         * pwd_md5 : 53eb93b3-88bb-4da3-9070-f60396371d96
         */

        private String phone; // 用户手机号
        private String mpm_identify; // 推送标志
        private String mpm; // 推送通道 e_jpush | e_hpush
        private String info; // 手机登录信息:IP;IMEI;CCID;ADDR等
        private String flag; // 登录标识
        private String account; // 帐号 (帐号手机号 二选一必填)
        private int type; // 账户挂靠的APP类型;例如:2 Llink
        private String pwd_md5; // 用户md5加密密码
        private String platform; // 登入平台,ios,android,winphone
        private String lang; // 登入之后的语言，非必填，默认中文, 例如:ch 中文; en 英语

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMpm_identify() {
            return mpm_identify;
        }

        public void setMpm_identify(String mpm_identify) {
            this.mpm_identify = mpm_identify;
        }

        public String getMpm() {
            return mpm;
        }

        public void setMpm(String mpm) {
            this.mpm = mpm;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getPwd_md5() {
            return pwd_md5;
        }

        public void setPwd_md5(String pwd_md5) {
            this.pwd_md5 = pwd_md5;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }
    }
}
