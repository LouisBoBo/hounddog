package com.slxk.hounddog.mvp.model.bean;


import com.slxk.hounddog.mvp.model.entity.BaseBean;
import com.slxk.hounddog.mvp.utils.ResultDataUtils;

import java.util.List;

/**
 * 设备管理-设备列表
 */
public class DeviceListForManagerResultBean extends BaseBean {

    private int all_total = 0; // 设备总数,get_total值为true的时候返回
    private int down_total = 0; // 下线数,get_total值为true的时候返回
    private int line_total = 0; // 在线数,get_total值为true的时候返回
    private int sleep_total = 0; // 睡眠数,get_total值为true的时候返回
    private int total = 0; // 当前查询条件满足的全部设备总数，每次都会返回
    private List<ItemsBean> items;

    public int getAll_total() {
        return all_total;
    }

    public void setAll_total(int all_total) {
        this.all_total = all_total;
    }

    public int getDown_total() {
        return down_total;
    }

    public void setDown_total(int down_total) {
        this.down_total = down_total;
    }

    public int getLine_total() {
        return line_total;
    }

    public void setLine_total(int line_total) {
        this.line_total = line_total;
    }

    public int getSleep_total() {
        return sleep_total;
    }

    public void setSleep_total(int sleep_total) {
        this.sleep_total = sleep_total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * simei : 4d783a94-3372-4400-ae6d-b56dee641d4a
         * sgid : 7463c60e-91ea-44ce-97f0-63a0ad413b8f
         * power : 249306460
         * version : 95393a14-250d-45bf-807f-78a11d11520d
         * state : e_line_down|e_line_on|e_line_sleep
         * start_dev_time : 249306460
         * car_number : f0f58b1f-69b4-4000-ae94-1d6cf0b91520
         * groupname : 6c40368c-f8c4-487a-906e-bbcaafd5f736
         * imei : 249306460
         * last_com_time : 249306460
         * last_pos : 50f1188b-f06d-4a4c-a263-7a952962f96f
         */

        private String simei;
        private String sgid; // 车组id
        private int power; // 设备电量
        private String version; // 设备类型
        private String state = ResultDataUtils.Device_State_Line_Down; // 设备状态
        private long start_dev_time = 0; // 设备开始使用时间
        private String car_number; // 车牌号
        private String groupname; // 车组名称
        private long imei; // 设备imei,仅用作展示
        private long last_com_time; // 通讯时间
        private String last_pos; // 位置信息
        private boolean isSelect = false; // 是否选中
        private double day_distance; // 日里程，单位米
        private long state_time = 0; // 当前状态的当前时间与起始时间差
        private boolean expire; // 设备是否过期，true过期，false没过期
        private long state_begin_time = 0; //设备状态起始时间，

        public long getState_begin_time() {
            return state_begin_time;
        }

        public void setState_begin_time(long state_begin_time) {
            this.state_begin_time = state_begin_time;
        }

        public boolean isExpire() {
            return expire;
        }

        public void setExpire(boolean expire) {
            this.expire = expire;
        }

        public long getState_time() {
            return state_time;
        }

        public void setState_time(long state_time) {
            this.state_time = state_time;
        }

        public double getDay_distance() {
            return day_distance;
        }

        public void setDay_distance(double day_distance) {
            this.day_distance = day_distance;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getSimei() {
            return simei;
        }

        public void setSimei(String simei) {
            this.simei = simei;
        }

        public String getSgid() {
            return sgid;
        }

        public void setSgid(String sgid) {
            this.sgid = sgid;
        }

        public int getPower() {
            return power;
        }

        public void setPower(int power) {
            this.power = power;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCar_number() {
            return car_number;
        }

        public void setCar_number(String car_number) {
            this.car_number = car_number;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public long getStart_dev_time() {
            return start_dev_time;
        }

        public void setStart_dev_time(long start_dev_time) {
            this.start_dev_time = start_dev_time;
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

        public String getLast_pos() {
            return last_pos;
        }

        public void setLast_pos(String last_pos) {
            this.last_pos = last_pos;
        }
    }
}
