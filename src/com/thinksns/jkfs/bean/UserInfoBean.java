package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户基本资料
 * 
 */
public class UserInfoBean implements Parcelable {
	private String uid;
	private String uname; // 昵称
	private String email;
	private String sex;
	private String province;
	private String city;
	private String avatar_url; // 头像url

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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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
		dest.writeString(uid);
		dest.writeString(uname);
		dest.writeString(email);
		dest.writeString(sex);
		dest.writeString(province);
		dest.writeString(city);
		dest.writeString(avatar_url);

	}

	public static final Parcelable.Creator<UserInfoBean> CREATOR = new Parcelable.Creator<UserInfoBean>() {
		public UserInfoBean createFromParcel(Parcel in) {
			UserInfoBean ub = new UserInfoBean();
			ub.uid = in.readString();
			ub.uname = in.readString();
			ub.email = in.readString();
			ub.sex = in.readString();
			ub.province = in.readString();
			ub.city = in.readString();
			ub.avatar_url = in.readString();
			return ub;

		}

		public UserInfoBean[] newArray(int size) {
			return new UserInfoBean[size];
		}
	};

}
