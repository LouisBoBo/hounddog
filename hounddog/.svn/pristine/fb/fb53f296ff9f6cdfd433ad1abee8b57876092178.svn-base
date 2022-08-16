package com.slxk.hounddog.mvp.model.putbean;

/**
 * 注册接口传参
 */
public class RegisterPutBean {

    /**
     * module : user
     * func : Register
     * params : {"info":{"uid":"16e17095-e803-4029-aaeb-876aa7f51317","role":"e_role_manager|e_role_user","pwd":"eab6751c-aec8-452f-af07-55a1a23d33c6","phone":"9df6609f-da15-47b8-a7d0-4fa965c19c45","nick_name":"22f87893-5dce-4b58-870f-1833b91f960c","email":"36f3219a-5182-4d3e-8701-960ba9fd8232","account":"7cf4cbad-b101-4046-80f5-4caac0873210"}}
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
         * info : {"uid":"16e17095-e803-4029-aaeb-876aa7f51317","role":"e_role_manager|e_role_user","pwd":"eab6751c-aec8-452f-af07-55a1a23d33c6","phone":"9df6609f-da15-47b8-a7d0-4fa965c19c45","nick_name":"22f87893-5dce-4b58-870f-1833b91f960c","email":"36f3219a-5182-4d3e-8701-960ba9fd8232","account":"7cf4cbad-b101-4046-80f5-4caac0873210"}
         */

        private String code; // 验证码,用于短信校验手机号码
        private InfoBean info;
        private String key; // appkey,用于短信校验手机号码
        private int type; // 账户挂靠的APP类型;例如:2 Llink
        private String zone; // 区号,用于短信校验手机号码
        private Boolean check_phone; // 是否必须校验手机号码，默认是

        public Boolean getCheck_phone() {
            return check_phone;
        }

        public void setCheck_phone(Boolean check_phone) {
            this.check_phone = check_phone;
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * uid : 16e17095-e803-4029-aaeb-876aa7f51317
             * role : e_role_manager|e_role_user
             * pwd : eab6751c-aec8-452f-af07-55a1a23d33c6
             * phone : 9df6609f-da15-47b8-a7d0-4fa965c19c45
             * nick_name : 22f87893-5dce-4b58-870f-1833b91f960c
             * email : 36f3219a-5182-4d3e-8701-960ba9fd8232
             * account : 7cf4cbad-b101-4046-80f5-4caac0873210
             */

            private String uid; // 用户加密后的uid
            private String role; // 用户类型
            private String pwd; // 密码
            private String phone; // 手机号
            private String nick_name; // 名称
            private String email; // 邮箱
            private String account; // 账号

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getPwd() {
                return pwd;
            }

            public void setPwd(String pwd) {
                this.pwd = pwd;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
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
        }
    }
}
