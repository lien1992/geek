package com.thinksns.jkfs.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import android.os.Parcel;
import android.os.Parcelable;

@Table(name = "user_info_medal")
public class UserInfoMedalBean implements Parcelable {
	@Id
	private int id;
	@Column
	private String small_src;
	@Column
	private String src;
	@Foreign(column = "medalUserId", foreign = "id")
	private UserInfoBean userinfo;

	public UserInfoMedalBean() {

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(small_src);
		dest.writeString(src);

	}

	public static final Parcelable.Creator<UserInfoMedalBean> CREATOR = new Parcelable.Creator<UserInfoMedalBean>() {
		public UserInfoMedalBean createFromParcel(Parcel in) {
			UserInfoMedalBean umb = new UserInfoMedalBean();
			umb.small_src = in.readString();
			umb.src = in.readString();
			return umb;
		}

		public UserInfoMedalBean[] newArray(int size) {
			return new UserInfoMedalBean[size];
		}
	};

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getSmall_src() {
		return small_src;
	}

	public void setSmall_src(String smallSrc) {
		small_src = smallSrc;
	}

	public void setUserinfo(UserInfoBean userinfo) {
		this.userinfo = userinfo;
	}

	public UserInfoBean getUserinfo() {
		return userinfo;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSrc() {
		return src;
	}

}
