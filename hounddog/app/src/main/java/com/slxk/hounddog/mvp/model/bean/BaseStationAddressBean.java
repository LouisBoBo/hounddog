package com.slxk.hounddog.mvp.model.bean;

/**
 * 基站地址信息
 */
public class BaseStationAddressBean {

    /**
     * infocode : 10000
     * result : {"city":"沧州市","province":"河北省","poi":"肃宁县公安局城关派出所办证大厅","adcode":"130926","street":"聚贤街","desc":"河北省 沧州市 肃宁县 聚贤街 靠近肃宁县公安局城关派出所办证大厅","country":"中国","type":"4","location":"115.8541262,38.4183876","road":"聚贤街","radius":"550","citycode":"0317"}
     * info : OK
     * status : 1
     */

    private String infocode;
    private ResultBean result;
    private String info;
    private String status;

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class ResultBean {
        /**
         * city : 沧州市
         * province : 河北省
         * poi : 肃宁县公安局城关派出所办证大厅
         * adcode : 130926
         * street : 聚贤街
         * desc : 河北省 沧州市 肃宁县 聚贤街 靠近肃宁县公安局城关派出所办证大厅
         * country : 中国
         * type : 4
         * location : 115.8541262,38.4183876
         * road : 聚贤街
         * radius : 550
         * citycode : 0317
         */

        private String city;
        private String province;
        private String poi;
        private String adcode;
        private String street;
        private String desc;
        private String country;
        private String type;
        private String location; // 经纬度，逗号分割，  经度,纬度
        private String road;
        private String radius;
        private String citycode;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getPoi() {
            return poi;
        }

        public void setPoi(String poi) {
            this.poi = poi;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getRoad() {
            return road;
        }

        public void setRoad(String road) {
            this.road = road;
        }

        public String getRadius() {
            return radius;
        }

        public void setRadius(String radius) {
            this.radius = radius;
        }

        public String getCitycode() {
            return citycode;
        }

        public void setCitycode(String citycode) {
            this.citycode = citycode;
        }
    }
}
