package com.slxk.hounddog.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 离线地图，单张图片的xy坐标系
 */

@DatabaseTable(tableName = "db_map_xy_bean")
public class MapXYModel {

    /**
     * 时间戳，用来匹配
     */
    public static final String TIME = "time";

    @DatabaseField(generatedId = true)
    private int id;
    //时间戳，唯一性，用来标识查询条件
    @DatabaseField(columnName = "time")
    private String time;
    //下载进度
    @DatabaseField(columnName = "xtile")
    private long xtile;
    //当前下载到的位置索引
    @DatabaseField(columnName = "ytile")
    private long ytile;
    //当前层级
    @DatabaseField(columnName = "zoom")
    private int zoom;

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getXtile() {
        return xtile;
    }

    public void setXtile(long xtile) {
        this.xtile = xtile;
    }

    public long getYtile() {
        return ytile;
    }

    public void setYtile(long ytile) {
        this.ytile = ytile;
    }

    @Override
    public String toString() {
        return "MapXYModel{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", xtile='" + xtile + '\'' +
                ", ytile='" + ytile + '\'' +
                '}';
    }
}
