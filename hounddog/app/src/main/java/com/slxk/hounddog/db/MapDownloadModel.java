package com.slxk.hounddog.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 离线地图，下载任务
 */

@DatabaseTable(tableName = "db_map_download_bean")
public class MapDownloadModel {

    /**
     * 时间戳，用来匹配
     */
    public static final String TIME = "time";

    @DatabaseField(generatedId = true)
    private int id;
    //时间戳，唯一性，用来标识查询条件
    @DatabaseField(columnName = "time", unique = true)
    private String time;
    //下载进度
    @DatabaseField(columnName = "progress")
    private int progress;
    //当前下载到的位置索引
    @DatabaseField(columnName = "position")
    private int position;
    //总图片张数
    @DatabaseField(columnName = "image_size")
    private long image_size;
    //所有图片总大小
    @DatabaseField(columnName = "total_size")
    private String total_size;
    //下载的地图自定义名称
    @DatabaseField(columnName = "map_name")
    private String map_name;

    private boolean isDownload = false; // 是否正在下载

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getImage_size() {
        return image_size;
    }

    public void setImage_size(long image_size) {
        this.image_size = image_size;
    }

    public String getTotal_size() {
        return total_size;
    }

    public void setTotal_size(String total_size) {
        this.total_size = total_size;
    }

    public String getMap_name() {
        return map_name;
    }

    public void setMap_name(String map_name) {
        this.map_name = map_name;
    }

    @Override
    public String toString() {
        return "MapDownloadModel{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", progress='" + progress + '\'' +
                ", position='" + position + '\'' +
                ", image_size='" + image_size + '\'' +
                ", total_size='" + total_size + '\'' +
                ", map_name='" + map_name + '\'' +
                '}';
    }
}
