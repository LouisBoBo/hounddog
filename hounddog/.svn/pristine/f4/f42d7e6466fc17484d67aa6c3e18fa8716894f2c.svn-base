package com.slxk.hounddog.mvp.model.bean;

import com.slxk.hounddog.mvp.model.entity.BaseBean;

import java.util.List;

/**
 * 录音相关设置，声控开关状态，增值服务等
 */
public class RecordConfigResultBean extends BaseBean {

    /**
     * data : [{"is_appreciation":false,"is_opt":false,"name":"767c85a2-9613-48c1-9012-766ab968dac2","time":486395797,"type":486395797},{"is_appreciation":false,"is_opt":false,"name":"1dc3e9ed-7f77-4b8c-a31c-6f3a34e4edff","time":486395797,"type":486395797},{"is_appreciation":false,"is_opt":false,"name":"444f5475-e856-4a5b-bd58-af01ba82fe9f","time":486395797,"type":486395797}]
     * expire : false
     * record : 486395797
     * vorswitch : 486395797
     */

    private boolean expire; // 是否过期
    private int record; // 短录音，实时录音是否支持，-1，不支持，1支持，2只读
    private int vorswitch; // 声控开关,0关,1开,-1不支持，2只读
    private List<DataBean> data;

    public boolean isExpire() {
        return expire;
    }

    public void setExpire(boolean expire) {
        this.expire = expire;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public int getVorswitch() {
        return vorswitch;
    }

    public void setVorswitch(int vorswitch) {
        this.vorswitch = vorswitch;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * is_appreciation : false
         * is_opt : false
         * name : 767c85a2-9613-48c1-9012-766ab968dac2
         * time : 486395797
         * type : 486395797
         */

        private boolean is_appreciation; // 是否是增值服务的时间
        private boolean is_opt; // 是否可操作
        private String name; // 可选的录音时长的名称
        private int time; // 可选的录音时常
        private int type; // 录音id，可用作购买增值服务id

        public boolean isIs_appreciation() {
            return is_appreciation;
        }

        public void setIs_appreciation(boolean is_appreciation) {
            this.is_appreciation = is_appreciation;
        }

        public boolean isIs_opt() {
            return is_opt;
        }

        public void setIs_opt(boolean is_opt) {
            this.is_opt = is_opt;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
