package com.slxk.hounddog.mvp.model.putbean;

/**
 * 找回密码
 */
public class ForgetPasswordPutBean {

    /**
     * module : user
     * func : FindPass
     * params : {"account":"80fcb1b4-2eb6-4291-b32b-97e50a32fd3c","code":"565ab84b-edcc-4b23-87ff-0f9a033db84d","key":"4c19b9ab-1014-44e1-bfe7-7e6f8bcdcc59","new_pwd":"6480ab6c-2b4d-486f-8c8e-06c3e630aaed","phone":"3a7c76aa-6370-41e0-8b25-dca698e867c3","zone":"f54a60af-ed03-48bb-b838-7485542734b4"}
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
         * account : 80fcb1b4-2eb6-4291-b32b-97e50a32fd3c
         * code : 565ab84b-edcc-4b23-87ff-0f9a033db84d
         * key : 4c19b9ab-1014-44e1-bfe7-7e6f8bcdcc59
         * new_pwd : 6480ab6c-2b4d-486f-8c8e-06c3e630aaed
         * phone : 3a7c76aa-6370-41e0-8b25-dca698e867c3
         * zone : f54a60af-ed03-48bb-b838-7485542734b4
         */

        private String account; // 账号或者设备号
        private String code; // 验证码
        private String key; // appkey
        private String new_pwd; // 新密码
        private String phone; // 绑定的手机号码
        private String zone; // 区号
        private int type; // 账户挂靠的APP类型;例如:2 Llink
        private String email; // 邮箱
        private String flag; // 如果不填写account,产生冲突的情况下，返回的flag选择一个值填进来
        private String lang ; // 登入之后的语言，非必填，默认中文, 例如:ch 中文; en 英语

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

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

        public String getNew_pwd() {
            return new_pwd;
        }

        public void setNew_pwd(String new_pwd) {
            this.new_pwd = new_pwd;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
