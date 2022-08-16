package com.slxk.hounddog.mvp.model.bean;


import com.slxk.hounddog.mvp.model.entity.BaseBean;

import java.util.List;

/**
 * 录音数据结果返回
 */
public class RecordResultBean extends BaseBean {

    private List<DataBean> data;
    private int total; // 符合条件的总数
    private int state = -1; // 录音状态,0-成功 1-当前用户已开始录音 2-正在录音 但不是当前用户 3-该设备已开启声控录音 4-未执行

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * record_cos : 1675639476
         * record_time : 1675639476
         * url : 234bf93e-a083-495d-ad46-4d1456ae520f
         */

        private int record_cos; // 录音持续时长
        private long record_time; // 录音开始时间戳
        private String url; // 录音下载地址

        public int getRecord_cos() {
            return record_cos;
        }

        public void setRecord_cos(int record_cos) {
            this.record_cos = record_cos;
        }

        public long getRecord_time() {
            return record_time;
        }

        public void setRecord_time(long record_time) {
            this.record_time = record_time;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
