package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户基本资料
 * 
 */
public class UserInfoBean implements Parcelable {
	private String uid;
	private String uname;
	private String email;
	private String sex;
	private String province;
	private String city;
	private String avatar_big;
	private String avatar_middle;
	private String avatar_small;
	private String last_feed_id;// 最近一条微博ID

	private UserInfoCountBean count_info;

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

	public void setCount_info(UserInfoCountBean count_info) {
		this.count_info = count_info;
	}

	public UserInfoCountBean getCount_info() {
		return count_info;
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
		dest.writeString(avatar_big);
		dest.writeString(avatar_middle);
		dest.writeString(avatar_small);
		dest.writeString(last_feed_id);
		dest.writeParcelable(count_info, flags);

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
			ub.avatar_big = in.readString();
			ub.avatar_middle = in.readString();
			ub.avatar_small = in.readString();
			ub.last_feed_id = in.readString();
			ub.count_info = in.readParcelable(UserInfoCountBean.class
					.getClassLoader());
			return ub;

		}

		public UserInfoBean[] newArray(int size) {
			return new UserInfoBean[size];
		}
	};

}
