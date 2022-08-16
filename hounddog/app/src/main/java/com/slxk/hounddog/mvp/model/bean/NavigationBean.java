package com.slxk.hounddog.mvp.model.bean;

/**
 * 导航bean
 */
public class NavigationBean {

    private int id; // 0：高德地图，1：百度地图，2：腾讯地图
    private String name;
    private boolean isHasApp; // 是否安装了对应的app

    public NavigationBean(int id, String name, boolean isHasApp){
        super();
        this.id = id;
        this.name = name;
        this.isHasApp = isHasApp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasApp() {
        return isHasApp;
    }

    public void setHasApp(boolean hasApp) {
        isHasApp = hasApp;
    }
}
