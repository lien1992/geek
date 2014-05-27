package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户基本资料-统计信息
 * 
 */
public class UserInfoCountBean implements Parcelable {
	private String following_count;
	private String follower_count;
	private String weibo_count;

	public UserInfoCountBean() {
	}

	public UserInfoCountBean(String follower_count, String following_count,
			String weibo_count) {
		this.follower_count = follower_count;
		this.following_count = following_count;
		this.weibo_count = weibo_count;
	}

	public String getFollowing_count() {
		return following_count;
	}

	public void setFollowing_count(String following_count) {
		this.following_count = following_count;
	}

	public String getFollower_count() {
		return follower_count;
	}

	public void setFollower_count(String follower_count) {
		this.follower_count = follower_count;
	}

	public String getWeibo_count() {
		return weibo_count;
	}

	public void setWeibo_count(String weibo_count) {
		this.weibo_count = weibo_count;
	}

	@Override
	public String toString() {
		return "count_info =  " + this.follower_count + this.following_count
				+ this.weibo_count;
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
