package com.slxk.hounddog.mvp.model.bean;

import com.baidu.mapapi.model.LatLng;

public class RoutePointBaidu {

	public LatLng point;//经纬度
	public int type;//定位类型
	private float direction; // 方向
	private double lat; //经度
	private double lon; // 纬度
	private int speed; // 速度
	private long time; // 当前点的时间戳
	private int ptype; // 是否为p点,1表示是p点
	private long start_time; // 开始停留时间
	private int distance; // 分段里程(当前p点跟上一个p点)
	private int duration; // 停留时长
	private long end_time; // 结束停留时间

	public RoutePointBaidu(LatLng point, int type, long time, int speed, double lat, double lon, float direction, int ptype,
                           long start_time, int distance, int duration, long end_time) {
		this.point = point;
		this.type = type;
		this.time = time;
		this.speed=speed;
		this.lat = lat;
		this.lon = lon;
		this.direction = direction;
		this.ptype = ptype;
		this.start_time = start_time;
		this.distance = distance;
		this.duration = duration;
		this.end_time = end_time;
	}

	public LatLng getPoint() {
		return point;
	}

	public void setPoint(LatLng point) {
		this.point = point;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public float getDirection() {
		return direction;
	}

	public void setDirection(float direction) {
		this.direction = direction;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getPtype() {
		return ptype;
	}

	public void setPtype(int ptype) {
		this.ptype = ptype;
	}

	public long getStart_time() {
		return start_time;
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

	@Override
	public String toString() {
		return "RoutePoint{" +
				"point=" + point +
				", type=" + type +
				", time='" + time + '\'' +
				", speed=" + speed +'}';
	}
}
