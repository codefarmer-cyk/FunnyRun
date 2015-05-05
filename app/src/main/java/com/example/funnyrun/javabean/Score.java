package com.example.funnyrun.javabean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class Score extends BmobObject{
	private String address;
	private BmobDate updateTime;
	private Integer score;
	private String costTime;
	private Integer distance;
	private MyUser author;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public BmobDate getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(BmobDate updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public String getCostTime() {
		return costTime;
	}
	public void setCostTime(String string) {
		this.costTime = string;
	}
	public Integer getDistance() {
		return distance;
	}
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	public MyUser getAuthor() {
		return author;
	}
	public void setAuthor(MyUser author) {
		this.author = author;
	}
	
}
