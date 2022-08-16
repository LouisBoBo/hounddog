package com.slxk.hounddog.mvp.model.bean;

/**
 * 离线地图下载，记录每个层级的数据
 */
public class OfflineMapDownLoadBean {

    private String farLeftXY; // 左上角的xy轴坐标，用，号分割
    private String nearRightXY; // 右下角的xy轴坐标，用，号分割
    private long farLeftX; // 左上角
    private long farLeftY; // 左上角
    private long nearRightX; // 右下角
    private long nearRightY; // 右下角
    // 计算每次相加的阀值
    private long x_add_value;
    private long y_add_value;
    // 右下角的xy坐标 - 左上角的xy坐标，得出差值计算图片总数，计算每张图片经纬度需要从左上角往右下角加阀值
    private long x_difference;
    private long y_difference;
    // 取差值的绝对值，避免出现负数，方便计算
    private long x_absolute_value;
    private long y_absolute_value;
    private int zoom; // 层级

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public String getFarLeftXY() {
        return farLeftXY;
    }

    public void setFarLeftXY(String farLeftXY) {
        this.farLeftXY = farLeftXY;
    }

    public String getNearRightXY() {
        return nearRightXY;
    }

    public void setNearRightXY(String nearRightXY) {
        this.nearRightXY = nearRightXY;
    }

    public long getFarLeftX() {
        return farLeftX;
    }

    public void setFarLeftX(long farLeftX) {
        this.farLeftX = farLeftX;
    }

    public long getFarLeftY() {
        return farLeftY;
    }

    public void setFarLeftY(long farLeftY) {
        this.farLeftY = farLeftY;
    }

    public long getNearRightX() {
        return nearRightX;
    }

    public void setNearRightX(long nearRightX) {
        this.nearRightX = nearRightX;
    }

    public long getNearRightY() {
        return nearRightY;
    }

    public void setNearRightY(long nearRightY) {
        this.nearRightY = nearRightY;
    }

    public long getX_add_value() {
        return x_add_value;
    }

    public void setX_add_value(long x_add_value) {
        this.x_add_value = x_add_value;
    }

    public long getY_add_value() {
        return y_add_value;
    }

    public void setY_add_value(long y_add_value) {
        this.y_add_value = y_add_value;
    }

    public long getX_difference() {
        return x_difference;
    }

    public void setX_difference(long x_difference) {
        this.x_difference = x_difference;
    }

    public long getY_difference() {
        return y_difference;
    }

    public void setY_difference(long y_difference) {
        this.y_difference = y_difference;
    }

    public long getX_absolute_value() {
        return x_absolute_value;
    }

    public void setX_absolute_value(long x_absolute_value) {
        this.x_absolute_value = x_absolute_value;
    }

    public long getY_absolute_value() {
        return y_absolute_value;
    }

    public void setY_absolute_value(long y_absolute_value) {
        this.y_absolute_value = y_absolute_value;
    }
}
