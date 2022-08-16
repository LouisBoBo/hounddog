package com.slxk.hounddog.mvp.model.bean;

/**
 * 地图类型：高德地图，百度地图，谷歌地图
 */
public class MapOtherBean {

    private int id; // 0：路况，1：街景，街景选中不显示勾选图标
    private String name;
    private boolean isSelect; // 是否选中

    public MapOtherBean(int id, String name, boolean select){
        super();
        this.id = id;
        this.name = name;
        this.isSelect = select;
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

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
