package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfoBean implements Parcelable{
	private String uid;
	private String uname; //昵称
	private String email;
	private String sex;
	private String location;
	private String avatar_url; //头像url

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	public void setAvatar_url(String avatarUrl) {
		avatar_url = avatarUrl;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
