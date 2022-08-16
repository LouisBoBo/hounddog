package com.slxk.hounddog.mvp.model.bean;

import com.slxk.hounddog.mvp.model.entity.BaseBean;

import java.util.List;

/**
 * 设备详细信息
 */
public class DeviceDetailResultBean extends BaseBean {

    /**
     * acc_power : e_acc_power_charge|e_acc_power_interuupt
     * acc_state : e_acc_close|e_acc_open|e_acc_empty
     * bd_satellite : 2052388107
     * bind_phone : 11029d04-246e-4336-9f35-48a8e04a8743
     * bind_seller_gname : a2948a55-44b6-4414-ac7b-7f814da752c0
     * bind_seller_name : ["9bd32258-32a8-4edf-ab01-bab45a72925d","074d3c71-9958-4c78-98c8-5a4285c2d547","8627b920-87ec-48a8-a054-65e510cb5ed6"]
     * bind_time : 2052388107
     * bind_user_name : ["18a08448-9465-4f8a-9e3c-d59a0b6fb489","79500f6d-c3d0-4df6-b94c-3538242b4d65","124c40c3-43bb-4767-830c-d5db008ea768","bded29fa-c9d8-4457-9069-118c7624ff81"]
     * center_phone : c2546c47-1d92-4f32-a79f-40283936651f
     * day_distance : 2052388107
     * detail : {"car_image":2052388107,"car_number":"2d56678b-b446-4aba-bdbb-660786e3034e","car_type":"555baf13-c74e-4624-b991-a044a7e97567","end_dev_time":2052388107,"engine_num":"df3f3a17-687c-4e1a-b9d5-a34e763828ce","frame_num":"c8bd39b8-ca02-4ee1-b50d-5c1bc48d13ae","mode":"e_mode_nomal|e_mode_looploc|e_mode_fly|e_mode_sup_save_power|e_mode_auto_save_power|e_mode_sleep|e_mode_save_power|e_mode_call_one|e_mode_sup_save_power_c2|e_mode_nomal_x7","scar_safe_time":"0aaa51df-a636-4990-9ea3-667df2949e5f","scar_year_check":"eee8fd23-49da-40ee-b182-42bade3c5a70","start_dev_time":2052388107,"user_addr":"baf27c48-2ca5-4d3d-b4d5-bc10b2368cac","user_cer":"fbe31d5b-9bd7-4f7d-ba57-6840aa9451d1","user_image":2052388107,"user_mail":"1ab79139-e3ff-4e05-85e9-b1f57ece7a58","user_name":"93d64015-071f-4089-8371-1503a86e2580","user_phone":"a2da6bca-a3b1-4949-a36a-0d9183598a53","user_sex":"e_sex_man|e_sex_woman","user_unit":"53673464-71ab-4bee-8cfd-a58633e550b6"}
     * down_line_time : 2052388107
     * enable_time : 2052388107
     * glonass_satellite : 2052388107
     * gps_satellite : 2052388107
     * history : 2052388107
     * iccid : d0ee75d8-ec45-4286-a3a3-edb6acf0ef3f
     * imei : 2052388107
     * last_com_time : 2052388107
     * mode_fun : e_mode_invalid|e_mode_loc|e_mode_rtls
     * record : 2052388107
     * signa_type : 2052388107
     * signa_val : 2052388107
     * signal_rate : 2052388107
     * state : e_line_down|e_line_on|e_line_sleep
     * state_begin_time : 2052388107
     * up_line_time : 2052388107
     * version : d74c63ae-a061-4f99-a9b2-7b42ffcd752e
     */

    private String acc_power; // 电源状态accele, e_acc_power_charge 充电， e_acc_power_interuupt 断电
    private String acc_state; // acc状态, e_acc_close 关闭，e_acc_open 打开，e_acc_empty 为空
    private int bd_satellite; // 北斗卫星个数
    private String bind_phone; // 绑定手机号,设置get_user时返回
    private String bind_seller_gname; // 绑定销售商账号分组名称
    private List<String> bind_seller_name; // 销售商账号名称,设置get_user时返回
    private List<String> bind_user_name; // 绑定账号名称,设置get_user时返回
    private long bind_time; // 绑定时间,设置get_user时返回
    private String center_phone; // 中心号码
    private double day_distance; // 日里程
    private DetailBean detail; // 详细信息
    private long down_line_time; // 最近下线时间
    private long enable_time; // 设备开通时间戳(第一次上线时间)
    private int glonass_satellite; // 格洛纳斯卫星个数
    private int gps_satellite; // gps卫星个数
    private int history; // 是否有历史轨迹的能力, 0不支持,1支持
    private String iccid; // 设备上报ICCID
    private long imei; // 车载设备IMEI ,用于展示
    private long last_com_time; // 最后通信时间
    private String mode_fun; // 支持的模式,e_mode_invalid 什么模式都不支持,e_mode_loc 支持多选的模式,e_mode_rtls 支持定时的模式
    private int record; // 是否有短录音或者实时录音或者声控录音的能力, 0不支持,1支持
    private int signa_type = -1; // 信号类型,2G信号0，4G信号1
    private int signa_val; // 信号强度,卫星数量
    private int signal_rate; // 信号强度百分比值
    private String state; // 设备状态,e_line_down 不在线,e_line_on 在线,e_line_sleep 睡眠
    private long state_begin_time; // 当前状态的起始时间
    private long up_line_time; // 最近上线时间
    private String ver; // 设备固件型号(不带版本信息)
    private String version; // 设备固件版本
    private int altitude; // 海拔
    private String external_voltage; // 外电电压
    private long state_time; // 当前状态的当前时间与起始时间差
    private String temper; // 冷链设备温度
    private String humility; // 冷链设备湿度

//    enum EProtocol {
//        e_protocol_gt808_2011 = 1;        //JT/T 808-2011协议
//        e_protocol_gt808_2013 = 2;        //JT/T 808-2013协议
//        e_protocol_gt808_2019 = 3;        //JT/T 808-2019协议
//        e_protocol_gt808_tt = 4;    //途途808协议
//        e_protocol_gt808_yw = 5;    //移文808协议
//        e_protocol_kks = 100;                //康凯斯ET130协议
//        e_protocol_kks_gt06e = 101; //康凯斯GT06E协议
//        e_protocol_tq = 110;                //天琴协议
//        e_protocol_th = 120;                //温湿度协议
//    }
    private int protocol = 1; // 设备协议

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getHumility() {
        return humility;
    }

    public void setHumility(String humility) {
        this.humility = humility;
    }

    public String getTemper() {
        return temper;
    }

    public void setTemper(String temper) {
        this.temper = temper;
    }

    public long getState_time() {
        return state_time;
    }

    public void setState_time(long state_time) {
        this.state_time = state_time;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public String getExternal_voltage() {
        return external_voltage;
    }

    public void setExternal_voltage(String external_voltage) {
        this.external_voltage = external_voltage;
    }

    public String getAcc_power() {
        return acc_power;
    }

    public void setAcc_power(String acc_power) {
        this.acc_power = acc_power;
    }

    public String getAcc_state() {
        return acc_state;
    }

    public void setAcc_state(String acc_state) {
        this.acc_state = acc_state;
    }

    public int getBd_satellite() {
        return bd_satellite;
    }

    public void setBd_satellite(int bd_satellite) {
        this.bd_satellite = bd_satellite;
    }

    public String getBind_phone() {
        return bind_phone;
    }

    public void setBind_phone(String bind_phone) {
        this.bind_phone = bind_phone;
    }

    public String getBind_seller_gname() {
        return bind_seller_gname;
    }

    public void setBind_seller_gname(String bind_seller_gname) {
        this.bind_seller_gname = bind_seller_gname;
    }

    public long getBind_time() {
        return bind_time;
    }

    public void setBind_time(long bind_time) {
        this.bind_time = bind_time;
    }

    public String getCenter_phone() {
        return center_phone;
    }

    public void setCenter_phone(String center_phone) {
        this.center_phone = center_phone;
    }

    public double getDay_distance() {
        return day_distance;
    }

    public void setDay_distance(double day_distance) {
        this.day_distance = day_distance;
    }

    public DetailBean getDetail() {
        return detail;
    }

    public void setDetail(DetailBean detail) {
        this.detail = detail;
    }

    public long getDown_line_time() {
        return down_line_time;
    }

    public void setDown_line_time(long down_line_time) {
        this.down_line_time = down_line_time;
    }

    public long getEnable_time() {
        return enable_time;
    }

    public void setEnable_time(long enable_time) {
        this.enable_time = enable_time;
    }

    public int getGlonass_satellite() {
        return glonass_satellite;
    }

    public void setGlonass_satellite(int glonass_satellite) {
        this.glonass_satellite = glonass_satellite;
    }

    public int getGps_satellite() {
        return gps_satellite;
    }

    public void setGps_satellite(int gps_satellite) {
        this.gps_satellite = gps_satellite;
    }

    public int getHistory() {
        return history;
    }

    public void setHistory(int history) {
        this.history = history;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public long getImei() {
        return imei;
    }

    public void setImei(long imei) {
        this.imei = imei;
    }

    public long getLast_com_time() {
        return last_com_time;
    }

    public void setLast_com_time(long last_com_time) {
        this.last_com_time = last_com_time;
    }

    public String getMode_fun() {
        return mode_fun;
    }

    public void setMode_fun(String mode_fun) {
        this.mode_fun = mode_fun;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public int getSigna_type() {
        return signa_type;
    }

    public void setSigna_type(int signa_type) {
        this.signa_type = signa_type;
    }

    public int getSigna_val() {
        return signa_val;
    }

    public void setSigna_val(int signa_val) {
        this.signa_val = signa_val;
    }

    public int getSignal_rate() {
        return signal_rate;
    }

    public void setSignal_rate(int signal_rate) {
        this.signal_rate = signal_rate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getState_begin_time() {
        return state_begin_time;
    }

    public void setState_begin_time(long state_begin_time) {
        this.state_begin_time = state_begin_time;
    }

    public long getUp_line_time() {
        return up_line_time;
    }

    public void setUp_line_time(long up_line_time) {
        this.up_line_time = up_line_time;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public List<String> getBind_seller_name() {
        return bind_seller_name;
    }

    public void setBind_seller_name(List<String> bind_seller_name) {
        this.bind_seller_name = bind_seller_name;
    }

    public List<String> getBind_user_name() {
        return bind_user_name;
    }

    public void setBind_user_name(List<String> bind_user_name) {
        this.bind_user_name = bind_user_name;
    }

    public static class DetailBean {
        /**
         * car_image : 2052388107
         * car_number : 2d56678b-b446-4aba-bdbb-660786e3034e
         * car_type : 555baf13-c74e-4624-b991-a044a7e97567
         * end_dev_time : 2052388107
         * engine_num : df3f3a17-687c-4e1a-b9d5-a34e763828ce
         * frame_num : c8bd39b8-ca02-4ee1-b50d-5c1bc48d13ae
         * mode : e_mode_nomal|e_mode_looploc|e_mode_fly|e_mode_sup_save_power|e_mode_auto_save_power|e_mode_sleep|e_mode_save_power|e_mode_call_one|e_mode_sup_save_power_c2|e_mode_nomal_x7
         * scar_safe_time : 0aaa51df-a636-4990-9ea3-667df2949e5f
         * scar_year_check : eee8fd23-49da-40ee-b182-42bade3c5a70
         * start_dev_time : 2052388107
         * user_addr : baf27c48-2ca5-4d3d-b4d5-bc10b2368cac
         * user_cer : fbe31d5b-9bd7-4f7d-ba57-6840aa9451d1
         * user_image : 2052388107
         * user_mail : 1ab79139-e3ff-4e05-85e9-b1f57ece7a58
         * user_name : 93d64015-071f-4089-8371-1503a86e2580
         * user_phone : a2da6bca-a3b1-4949-a36a-0d9183598a53
         * user_sex : e_sex_man|e_sex_woman
         * user_unit : 53673464-71ab-4bee-8cfd-a58633e550b6
         */

        private int car_image; // 车辆图标
        private String car_number; // 车牌号
        private String car_type; // 车辆类型
        private long end_dev_time; // 设备到期时间
        private String engine_num; // 发动机号
        private String frame_num; // 车架号车辆品牌
        private String mode; // 定位模式
        private String scar_safe_time; // 车辆保险 格式如:2018-10-19
        private String scar_year_check; // 车辆年检 格式如:2018-10-19
        private long start_dev_time; // 设备开通时间
        private String user_addr; // 车主地址
        private String user_cer; // 车主证件号码
        private int user_image; // 车主形象（图标）
        private String user_mail; // 车主e_mail
        private String user_name; // 车主姓名
        private String user_phone; // 用户手机号码
        private String user_sex; //车主性别，e_sex_man 男，e_sex_woman 女
        private String user_unit; // 车主工作单位
        private String bck_phone; // 车主电话（备用电话号码）
        private String mode_name; // 定位模式名称

        public String getMode_name() {
            return mode_name;
        }

        public void setMode_name(String mode_name) {
            this.mode_name = mode_name;
        }

        public String getBck_phone() {
            return bck_phone;
        }

        public void setBck_phone(String bck_phone) {
            this.bck_phone = bck_phone;
        }

        public int getCar_image() {
            return car_image;
        }

        public void setCar_image(int car_image) {
            this.car_image = car_image;
        }

        public String getCar_number() {
            return car_number;
        }

        public void setCar_number(String car_number) {
            this.car_number = car_number;
        }

        public String getCar_type() {
            return car_type;
        }

        public void setCar_type(String car_type) {
            this.car_type = car_type;
        }

        public long getEnd_dev_time() {
            return end_dev_time;
        }

        public void setEnd_dev_time(long end_dev_time) {
            this.end_dev_time = end_dev_time;
        }

        public String getEngine_num() {
            return engine_num;
        }

        public void setEngine_num(String engine_num) {
            this.engine_num = engine_num;
        }

        public String getFrame_num() {
            return frame_num;
        }

        public void setFrame_num(String frame_num) {
            this.frame_num = frame_num;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getScar_safe_time() {
            return scar_safe_time;
        }

        public void setScar_safe_time(String scar_safe_time) {
            this.scar_safe_time = scar_safe_time;
        }

        public String getScar_year_check() {
            return scar_year_check;
        }

        public void setScar_year_check(String scar_year_check) {
            this.scar_year_check = scar_year_check;
        }

        public long getStart_dev_time() {
            return start_dev_time;
        }

        public void setStart_dev_time(long start_dev_time) {
            this.start_dev_time = start_dev_time;
        }

        public String getUser_addr() {
            return user_addr;
        }

        public void setUser_addr(String user_addr) {
            this.user_addr = user_addr;
        }

        public String getUser_cer() {
            return user_cer;
        }

        public void setUser_cer(String user_cer) {
            this.user_cer = user_cer;
        }

        public int getUser_image() {
            return user_image;
        }

        public void setUser_image(int user_image) {
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

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
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
    }
}
