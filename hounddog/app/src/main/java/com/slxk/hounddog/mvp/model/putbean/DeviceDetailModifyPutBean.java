package com.slxk.hounddog.mvp.model.putbean;

/**
 * 修改设备详细信息
 */
public class DeviceDetailModifyPutBean {

    /**
     * module : device
     * func : SetDetail
     * params : {"user_cer":"8087b6a7-437d-4840-a876-0173898c7cc8","scar_year_check":"aafa2560-732d-4e77-a1e1-d67ceefbc792","scar_safe_time":"3d8b2d74-44a8-49ca-88d2-9e1bc5df1d59","frame_num":"70a17805-7a5d-4c11-b25d-2d76082ca5b7","engine_num":"473230aa-2f4f-403a-a8d6-4e864ca0c974","center_phone":"e735140e-7ac2-46c9-a018-61dd2cf1f0f6","user_image":471031293,"user_mail":"50c0b211-294e-43c1-9d51-e64eb06bbd95","user_name":"cb7c6a0d-2a88-4738-90aa-aab996e9d967","user_phone":"0a3df170-b877-4173-8180-cfb781f6c36b","user_sex":"e_sex_man|e_sex_woman","user_unit":"f8f75941-9000-4455-828e-faeab4e5b574","car_type":"e913e77b-0247-4d20-9a15-dfd3fc904d73","car_number":"2f9f6108-7676-4c2f-adb1-ad27a42ea02f","car_image":471031293,"simei":"50745181-36e9-48b8-8f0c-210a570000ab","user_addr":"4f62639f-65ff-42d9-a531-dce697dd9ceb"}
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
         * user_cer : 8087b6a7-437d-4840-a876-0173898c7cc8
         * scar_year_check : aafa2560-732d-4e77-a1e1-d67ceefbc792
         * scar_safe_time : 3d8b2d74-44a8-49ca-88d2-9e1bc5df1d59
         * frame_num : 70a17805-7a5d-4c11-b25d-2d76082ca5b7
         * engine_num : 473230aa-2f4f-403a-a8d6-4e864ca0c974
         * center_phone : e735140e-7ac2-46c9-a018-61dd2cf1f0f6
         * user_image : 471031293
         * user_mail : 50c0b211-294e-43c1-9d51-e64eb06bbd95
         * user_name : cb7c6a0d-2a88-4738-90aa-aab996e9d967
         * user_phone : 0a3df170-b877-4173-8180-cfb781f6c36b
         * user_sex : e_sex_man|e_sex_woman
         * user_unit : f8f75941-9000-4455-828e-faeab4e5b574
         * car_type : e913e77b-0247-4d20-9a15-dfd3fc904d73
         * car_number : 2f9f6108-7676-4c2f-adb1-ad27a42ea02f
         * car_image : 471031293
         * simei : 50745181-36e9-48b8-8f0c-210a570000ab
         * user_addr : 4f62639f-65ff-42d9-a531-dce697dd9ceb
         */

        private String user_cer; // 车主证件号码
        private String scar_year_check; // 车辆年检 格式如:2018-10-19
        private String scar_safe_time; // 车辆保险 格式如:2018-10-19
        private String frame_num; // 车架号车辆品牌
        private String engine_num; // 发动机号
        private String center_phone; // 中心号码
        private Integer user_image; // 车主形象（图标）
        private String user_mail; // 车主e_mail
        private String user_name; // 车主姓名
        private String user_sex; // 车主性别 e_sex_man 男 , e_sex_woman 女
        private String user_unit; // 车主工作单位
        private String car_type; // 车辆类型
        private String car_number; // 车牌号
        private Integer car_image; // 车辆图标
        private String simei; // 需要修改的imei,如果设备登入，不用填
        private String user_addr; // 车主地址
        private String user_phone; // 车主电话

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getUser_cer() {
            return user_cer;
        }

        public void setUser_cer(String user_cer) {
            this.user_cer = user_cer;
        }

        public String getScar_year_check() {
            return scar_year_check;
        }

        public void setScar_year_check(String scar_year_check) {
            this.scar_year_check = scar_year_check;
        }

        public String getScar_safe_time() {
            return scar_safe_time;
        }

        public void setScar_safe_time(String scar_safe_time) {
            this.scar_safe_time = scar_safe_time;
        }

        public String getFrame_num() {
            return frame_num;
        }

        public void setFrame_num(String frame_num) {
            this.frame_num = frame_num;
        }

        public String getEngine_num() {
            return engine_num;
        }

        public void setEngine_num(String engine_num) {
            this.engine_num = engine_num;
        }

        public String getCenter_phone() {
            return center_phone;
        }

        public void setCenter_phone(String center_phone) {
            this.center_phone = center_phone;
        }

        public Integer getUser_image() {
            return user_image;
        }

        public void setUser_image(Integer user_image) {
            this.user_image = user_image;
        }

        public String getUser_mail() {
            return user_mail;
        }

        public void setUser_mail(String user_mail) {
            this.user_mail = user_mail;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_sex() {
            return user_sex;
        }

        public void setUser_sex(String user_sex) {
            this.user_sex = user_sex;
        }

        public String getUser_unit() {
            return user_unit;
        }

        public void setUser_unit(String user_unit) {
            this.user_unit = user_unit;
        }

        public String getCar_type() {
            return car_type;
        }

        public void setCar_type(String car_type) {
            this.car_type = car_type;
        }

        public String getCar_number() {
            return car_number;
        }

        public void setCar_number(String car_number) {
            this.car_number = car_number;
        }

        public Integer getCar_image() {
            return car_image;
        }

        public void setCar_image(Integer car_image) {
            this.car_image = car_image;
        }

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
        }

        public String getUser_addr() {
            return user_addr;
        }

        public void setUser_addr(String user_addr) {
            this.user_addr = user_addr;
        }
    }
}
