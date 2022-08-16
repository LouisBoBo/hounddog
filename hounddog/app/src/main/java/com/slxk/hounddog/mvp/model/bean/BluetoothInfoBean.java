package com.slxk.hounddog.mvp.model.bean;

/**
 * 已连接的蓝牙信息bean
 */
public class BluetoothInfoBean {

    private String mac; //蓝牙设备的mac地址
    private String number; //接收机的号码
    private String name; //蓝牙设备的名称
    private int power; // 电量

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String toString(){
        return "mac=" + mac
                + ", number=" + number
                + ", name=" + name
                + ", power=" + power;
    }

}
