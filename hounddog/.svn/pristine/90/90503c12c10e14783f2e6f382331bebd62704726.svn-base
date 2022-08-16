package com.slxk.hounddog.mvp.model.bean;


import com.slxk.hounddog.mvp.model.entity.BaseBean;

import java.util.List;

/**
 * 轨迹列表数据
 */
public class TrackListResultBean extends BaseBean {

    private boolean is_finish; // 是否加载完成，true 加载完毕， false未加载完
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public boolean isIs_finish() {
        return is_finish;
    }

    public void setIs_finish(boolean is_finish) {
        this.is_finish = is_finish;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * direction : 1289300838
         * lat : 1289300838
         * lon : 1289300838
         * speed : 1289300838
         * time : 1289300838
         * type : 1289300838
         */

        private float direction = 0; // 方向
        private long lat = 0; // 经度
        private long lon = 0; // 纬度
        private int speed = 0; // 速度
        private long time = 0; // 时间戳
        private int type = 0; // 所用的定位方式
        private int ptype = 0; // 是否为p点,1表示是p点
        private long start_time = 0; // 开始停留时间
        private int distance = 0; // 分段里程(当前p点跟上一个p点)
        private int duration = 0; // 停留时长
        private long end_time = 0; // 结束停留时间

        public float getDirection() {
            return direction;
        }

        public void setDirection(float direction) {
            this.direction = direction;
        }

        public long getLat() {
            return lat;
        }

        public void setLat(long lat) {
            this.lat = lat;
        }

        public long getLon() {
            return lon;
        }

        public void setLon(long lon) {
            this.lon = lon;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getPtype() {
            return ptype;
        }

        public void setPtype(int ptype) {
            this.ptype = ptype;
        }

        public long getStart_time() {
            return start_time;
        }

        public void setStart_time(long start_time) {
            this.start_time = start_time;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public long getEnd_time() {
            return end_time;
        }

        public void setEnd_time(long end_time) {
            this.end_time = end_time;
        }
    }
}
