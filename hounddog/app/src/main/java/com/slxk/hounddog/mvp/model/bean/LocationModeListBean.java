package com.slxk.hounddog.mvp.model.bean;

import java.util.List;

/**
 * 定位模式
 */
public class LocationModeListBean {

    /**
     * name : 无线定位模式
     * list : [{"v":5,"s":"5秒"},{"v":10,"s":"10秒"},{"v":30,"s":"30秒"},{"v":60,"s":"1分钟"},{"v":300,"s":"5分钟"}]
     */

    private String name;
    private List<ListBean> list;

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

        private int v;
        private String s;

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
    }

}
