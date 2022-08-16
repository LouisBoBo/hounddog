package com.slxk.hounddog.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 设备信息类-账号下的设备列表
 * Created by Administrator on 2017\6\29 0029.
 */


@DatabaseTable(tableName = "db_bluetooth_bean")
public class BluetoothModel {

    /**
     * 主键key，蓝牙设备的mac地址
     */
    public static final String MAC = "mac";

    /**
     * 主键key，关联的账号
     */
    public static final String ACCOUNT = "account";

    @DatabaseField(generatedId = true)
    private int id;
    //蓝牙设备的mac地址
    @DatabaseField(columnName = "mac", unique = true)
    private String mac;
    //关联的账号
    @DatabaseField(columnName = "account")
    private String account;
    //蓝牙设备的名称
    @DatabaseField(columnName = "name")
    private String name;
    //是否手动断开
    @DatabaseField(columnName = "disconnected")
    private boolean disconnected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    @Override
    public String toString() {
        return "BluetoothModel{" +
                "id=" + id +
                ", mac='" + mac + '\'' +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", disconnected=" + disconnected +
                '}';
    }
}
