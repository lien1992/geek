package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 邓思宇 微博或关注人BEAN里面用的判断关注与否用
 * 
 */
public class UserFollowStateBean implements Parcelable {
	private int following;
	private int follower;

	public UserFollowStateBean() {
	}

	// public UserFollowStateBean(int following, int follower) {
	// this.setFollowing(following);
	// this.setFollower(follower);
	// }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

	public int getFollowing() {
		return following;
	}

	public void setFollowing(int following) {
		this.following = following;
	}

	public int getFollower() {
		return follower;
	}

	public void setFollower(int follower) {
		this.follower = follower;
	}

}