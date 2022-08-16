package com.slxk.hounddog.mvp.model.bean;

/**
 * 颜色池
 */
public class ColorBean {

    private int colorId; // 资源文件颜色id
    private String color; // 颜色代码

    public ColorBean(int id, String color){
        super();
        this.colorId = id;
        this.color = color;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
