package com.slxk.hounddog.mvp.model.bean;

/**
 * 蓝牙列表-设备列表
 */
public class BluetoothBean {

    private String mac; // 设备的mac地址
    private String name; // 蓝牙名称
    private boolean isConnect; // 当前是否正在连接
    private boolean isPair; // 是否配对过

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public boolean isPair() {
        return isPair;
    }

    public void setPair(boolean pair) {
        isPair = pair;
    }
}
