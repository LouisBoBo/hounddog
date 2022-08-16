package com.slxk.hounddog.mvp.model.bean;


import com.slxk.hounddog.mvp.model.entity.BaseBean;

import java.util.List;

/**
 * 设备分组或账号组织列表
 */
public class DeviceGroupResultBean extends BaseBean {

    /**
     * familyid : ceaa4749-3b83-4f91-b0b3-5a07faed2295
     * familys : [{"sid":"db5da6df-b3ff-4d42-bb6f-56a57dfc3511","sname":"444595d3-fb21-4d97-9ca4-183dfd27904e"}]
     * familys_total : 1748708247
     * garages : [{"sid":"12143397-5433-47e4-beea-0e97c800f8c6","sname":"9253ab1b-6128-47eb-b2d5-f23d136f0491"},{"sid":"e16bd5e4-37a5-46b8-aac4-d8f3765bd208","sname":"0e04b0c7-3ae4-47ec-bed7-8ca1c08b4e57"},{"sid":"c2badee1-66f6-491c-86b8-8b9af836966c","sname":"da3f75ca-caef-460d-81f3-edf3c41a8258"}]
     * garages_total : 1748708247
     */

    private String familyid; // 当前请求的组织id,为空表示最上一级
    private int familys_total; // 符合条件的车组织总数
    private int garages_total; // 符合条件的车组总数
    private List<FamilysBean> familys; // 组织列表
    private List<GaragesBean> garages; // 分组列表
    private int imei_count = 0; // get_all为true时返回,当前车组的设备总数

    public int getImei_count() {
        return imei_count;
    }

    public void setImei_count(int imei_count) {
        this.imei_count = imei_count;
    }

    public String getFamilyid() {
        return familyid;
    }

    public void setFamilyid(String familyid) {
        this.familyid = familyid;
    }

    public int getFamilys_total() {
        return familys_total;
    }

    public void setFamilys_total(int familys_total) {
        this.familys_total = familys_total;
    }

    public int getGarages_total() {
        return garages_total;
    }

    public void setGarages_total(int garages_total) {
        this.garages_total = garages_total;
    }

    public List<FamilysBean> getFamilys() {
        return familys;
    }

    public void setFamilys(List<FamilysBean> familys) {
        this.familys = familys;
    }

    public List<GaragesBean> getGarages() {
        return garages;
    }

    public void setGarages(List<GaragesBean> garages) {
        this.garages = garages;
    }

    public static class FamilysBean {
        /**
         * sid : db5da6df-b3ff-4d42-bb6f-56a57dfc3511
         * sname : 444595d3-fb21-4d97-9ca4-183dfd27904e
         */

        private String sid;
        private String sname; // 名称
        private int imei_count; // 设备总数
        private int sub_count;// 下级个数

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public int getImei_count() {
            return imei_count;
        }

        public void setImei_count(int imei_count) {
            this.imei_count = imei_count;
        }

        public int getSub_count() {
            return sub_count;
        }

        public void setSub_count(int sub_count) {
            this.sub_count = sub_count;
        }
    }

    public static class GaragesBean {
        /**
         * sid : 12143397-5433-47e4-beea-0e97c800f8c6
         * sname : 9253ab1b-6128-47eb-b2d5-f23d136f0491
         */

        private String sid;
        private String sname; // 名称
        private int imei_count; // 设备总数
        private boolean isSelect = false; // 是否选中
        private String unit; // 单位

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public int getImei_count() {
            return imei_count;
        }

        public void setImei_count(int imei_count) {
            this.imei_count = imei_count;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}
