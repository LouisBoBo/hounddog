package com.slxk.hounddog.mvp.model.bean;

import com.slxk.hounddog.mvp.model.entity.BaseBean;

import java.util.List;

/**
 * 定位模式
 */
public class LocationModeResultBean extends BaseBean {

    /**
     * mode_id : 1031937216
     * mode_list : [{"mid":1031937216,"msg":"db001789-897d-4284-8a6b-61737f23a04f","name":"28fdb2a0-e07a-461c-b303-08e0920fa1ef","param":"262a93e7-5e27-45dd-8a9e-a84950b7a8ca","uid":1031937216},{"mid":1031937216,"msg":"0ef77729-2336-490b-a348-1b8e45ba0ec5","name":"5ad7daf2-8c0c-4aa8-bd88-f9c34218ac99","param":"d8a33a0f-a081-4e3b-9dcf-f000d0b1c0a5","uid":1031937216},{"mid":1031937216,"msg":"de68d7fe-9010-47a8-8240-332f6918fa4a","name":"10412f1d-ef17-4711-8307-2e1725bc03eb","param":"609aaebe-f9c4-4634-bf49-f1ddf39c8a65","uid":1031937216}]
     * mode_type : 1031937216
     * smode_value : 7ba483cb-48d4-48fe-86f9-f1e2914b3169
     */

    private int mode_id; // 定位模式
    private int mode_type; // 值,例如点名模式 定位间隔 飞行模式开关 0-关闭 1-打开
    private String smode_value; // 待机模式设置的时间 格式 HH:MM 飞行模式定位间隔
    private List<ModeListBean> mode_list; // 支持的定位模式

    public int getMode_id() {
        return mode_id;
    }

    public void setMode_id(int mode_id) {
        this.mode_id = mode_id;
    }

    public int getMode_type() {
        return mode_type;
    }

    public void setMode_type(int mode_type) {
        this.mode_type = mode_type;
    }

    public String getSmode_value() {
        return smode_value;
    }

    public void setSmode_value(String smode_value) {
        this.smode_value = smode_value;
    }

    public List<ModeListBean> getMode_list() {
        return mode_list;
    }

    public void setMode_list(List<ModeListBean> mode_list) {
        this.mode_list = mode_list;
    }

    public static class ModeListBean {
        /**
         * mid : 1031937216
         * msg : db001789-897d-4284-8a6b-61737f23a04f
         * name : 28fdb2a0-e07a-461c-b303-08e0920fa1ef
         * param : 262a93e7-5e27-45dd-8a9e-a84950b7a8ca
         * uid : 1031937216
         */
        // 说明:uid的说明:
        // 1:界面没有任何处理
        // 2:弹出周期定位设置界面
        // 3:弹出飞行模式设置界面
        // 4:弹出提示框
        // 5:单下拉框
        // 6:弹出时间选择框
        // 7:多项下拉框

        private int mid; // 模式id
        private String msg; // 被前端展示的说明
        private String name; // 名称
        private String param; // 需要的附加信息，格式化的json字符串
        private int uid; // UI类型

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
