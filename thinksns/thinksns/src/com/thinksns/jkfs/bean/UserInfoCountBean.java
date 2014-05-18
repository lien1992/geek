package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户基本资料-统计信息
 * 
 */
public class UserInfoCountBean implements Parcelable {
	private int following_count;
	private int follower_count;
	private int feed_count;
	private int favorite_count;
	private int weibo_count;
	private int unread_atme;
	private int unread_comment;

	public int getFollowing_count() {
		return following_count;
	}

	public void setFollowing_count(int followingCount) {
		following_count = followingCount;
	}

	public int getFollower_count() {
		return follower_count;
	}

	public void setFollower_count(int followerCount) {
		follower_count = followerCount;
	}

	public int getFeed_count() {
		return feed_count;
	}

	public void setFeed_count(int feedCount) {
		feed_count = feedCount;
	}

	public int getFavorite_count() {
		return favorite_count;
	}

	public void setFavorite_count(int favoriteCount) {
		favorite_count = favoriteCount;
	}

	public int getWeibo_count() {
		return weibo_count;
	}

	public void setWeibo_count(int weiboCount) {
		weibo_count = weiboCount;
	}

	public int getUnread_atme() {
		return unread_atme;
	}

	public void setUnread_atme(int unreadAtme) {
		unread_atme = unreadAtme;
	}

	public int getUnread_comment() {
		return unread_comment;
	}

	public void setUnread_comment(int unreadComment) {
		unread_comment = unreadComment;
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
