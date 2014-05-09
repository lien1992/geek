package com.thinksns.jkfs.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户列表
 * 
 */
public class UserInfoListBean implements Parcelable {
	private List<UserInfoBean> users = new ArrayList<UserInfoBean>();

	public List<UserInfoBean> getUsers() {
		return users;
	}

	public void setUsers(List<UserInfoBean> users) {
		this.users = users;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeTypedList(users);
	}

	public static final Parcelable.Creator<UserInfoListBean> CREATOR = new Parcelable.Creator<UserInfoListBean>() {
		public UserInfoListBean createFromParcel(Parcel in) {
			UserInfoListBean ulb = new UserInfoListBean();
			in.readTypedList(ulb.users, UserInfoBean.CREATOR);
			return ulb;
		}

		public UserInfoListBean[] newArray(int size) {
			return new UserInfoListBean[size];
		}
	};

}
