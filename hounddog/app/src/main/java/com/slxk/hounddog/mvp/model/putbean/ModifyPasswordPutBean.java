package com.slxk.hounddog.mvp.model.putbean;

/**
 * 修改密码
 */
public class ModifyPasswordPutBean {

    /**
     * module : user
     * func : SetAccount
     * params : {"relay_pwd":"14843545-0250-4c36-af7a-8f5d2fcfa3b6","pwd_md5":"d3527c89-f79a-4d98-80e9-a76bc153c209","push_switch":true,"key":"b64330b8-60b3-4483-802b-f106925befa6","info":{"uid":"c4dfb31c-325c-4bbf-8fe6-df52994be894","role":"e_role_manager|e_role_user","pwd":"224bda79-f4ce-4cbd-8c43-56819aa8ef9d","phone":"af0ef9cd-3edc-4131-9cd2-04dba508050d","nick_name":"97eaec86-4a3b-4229-8042-ce4a8e598ca8","email":"4d93b136-2ff5-439b-bd6f-9c0d282318ef","account":"039bf909-5756-4b9f-af1c-38d576ca9950"},"code":"4cdb0d4f-0461-4434-b770-69ab71e3e68d","zone":"95167759-905c-4b21-b9ce-6b9cae6a885e"}
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
         * relay_pwd : 14843545-0250-4c36-af7a-8f5d2fcfa3b6
         * pwd_md5 : d3527c89-f79a-4d98-80e9-a76bc153c209
         * push_switch : true
         * key : b64330b8-60b3-4483-802b-f106925befa6
         * info : {"uid":"c4dfb31c-325c-4bbf-8fe6-df52994be894","role":"e_role_manager|e_role_user","pwd":"224bda79-f4ce-4cbd-8c43-56819aa8ef9d","phone":"af0ef9cd-3edc-4131-9cd2-04dba508050d","nick_name":"97eaec86-4a3b-4229-8042-ce4a8e598ca8","email":"4d93b136-2ff5-439b-bd6f-9c0d282318ef","account":"039bf909-5756-4b9f-af1c-38d576ca9950"}
         * code : 4cdb0d4f-0461-4434-b770-69ab71e3e68d
         * zone : 95167759-905c-4b21-b9ce-6b9cae6a885e
         */

        private String relay_pwd;
        private String pwd_md5; // md5密码
        private Boolean push_switch;
        private String key;
        private InfoBean info;
        private String code;
        private String zone;
        private Boolean change_bind; // 是否更换手机号码绑定的imei号为当前imei号,默认false
        private Boolean check_phone; // 是否必须校验手机号码，默认是，如果全都不需要校验，那么需要通知平台配置该flag的属性,只有在设备号登录，并且置空了密码的情况下，设置该参数才生效

        public Boolean getCheck_phone() {
            return check_phone;
        }

        public void setCheck_phone(Boolean check_phone) {
            this.check_phone = check_phone;
        }

        public Boolean getChange_bind() {
            return change_bind;
        }

        public void setChange_bind(Boolean change_bind) {
            this.change_bind = change_bind;
        }

        public String getRelay_pwd() {
            return relay_pwd;
        }

        public void setRelay_pwd(String relay_pwd) {
            this.relay_pwd = relay_pwd;
        }

        public String getPwd_md5() {
            return pwd_md5;
        }

        public void setPwd_md5(String pwd_md5) {
            this.pwd_md5 = pwd_md5;
        }

        public Boolean getPush_switch() {
            return push_switch;
        }

        public void setPush_switch(Boolean push_switch) {
            this.push_switch = push_switch;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public static class InfoBean {
            /**
             * uid : c4dfb31c-325c-4bbf-8fe6-df52994be894
             * role : e_role_manager|e_role_user
             * pwd : 224bda79-f4ce-4cbd-8c43-56819aa8ef9d
             * phone : af0ef9cd-3edc-4131-9cd2-04dba508050d
             * nick_name : 97eaec86-4a3b-4229-8042-ce4a8e598ca8
             * email : 4d93b136-2ff5-439b-bd6f-9c0d282318ef
             * account : 039bf909-5756-4b9f-af1c-38d576ca9950
             */

            private String uid; // 用户加密后的uid,设备号登入可以不用填写,自己修改自己可以不用填
            private String role; // 用户类型,选填
            private String pwd; // 密码,修改时可以不用填写，添加注册必填
            private String phone;
            private String nick_name; // 昵称
            private String email; // 邮箱
            private String send_time; // 报警时间段 00:00:00
            private String sstart_time; // 报警时间段 00:00:00

            public String getSend_time() {
                return send_time;
            }

            public void setSend_time(String send_time) {
                this.send_time = send_time;
            }

            public String getSstart_time() {
                return sstart_time;
            }

            public void setSstart_time(String sstart_time) {
                this.sstart_time = sstart_time;
            }

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

        }
    }
}
