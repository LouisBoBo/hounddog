package com.slxk.hounddog.mvp.model.bean;

import java.util.List;

/**
 * 定位模式选择参数
 */
public class LocationModeTime {

    /**
     * name : 无线定位模式
     * list : [{"v":5,"s":"5秒"},{"v":10,"s":"10秒"},{"v":30,"s":"30秒"},{"v":60,"s":"1分钟"},{"v":300,"s":"5分钟"}]
     */

    private int mid; // 定位模式id
    private String smode_value; // 设置的值
    private String name; // 名称
    private List<ListBean> list;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getSmode_value() {
        return smode_value;
    }

    public void setSmode_value(String smode_value) {
        this.smode_value = smode_value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * v : 5
         * s : 5秒
         */

        private int v; // 设置的值
        private String s; // 显示的字符串
        private boolean isEnd = false; // 是否是最后一个

        public int getV() {
            return v;
        }

        public void setV(int v) {
            this.v = v;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd(boolean end) {
            isEnd = end;
        }
    }
}
