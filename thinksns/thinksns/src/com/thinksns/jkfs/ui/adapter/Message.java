package com.thinksns.jkfs.ui.adapter;

public class Message {

	private int type;//指定是哪种类型
	private String value;//值
	private String face;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getFace() {
		return face;
	}
	public void setFace(String face) {
		this.face = face;
	}
}
