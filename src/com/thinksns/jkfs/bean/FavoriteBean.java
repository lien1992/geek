package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 收藏
 * 
 */
public class FavoriteBean implements Parcelable {
	private String feed_id;
	private String uid;
	private String type;
	private String content;
	private String time;
	private String from;
	private String uname;
	private String avatar_big;
	private String avatar_middle;
	private String avatar_small;
	private int comment_count;
	private int repost_count;
	private String transpond_id;
	private WeiboBean transpond_data;

	public String getFeed_id() {
		return feed_id;
	}

	public void setFeed_id(String feedId) {
		feed_id = feedId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getAvatar_big() {
		return avatar_big;
	}

	public void setAvatar_big(String avatarBig) {
		avatar_big = avatarBig;
	}

	public String getAvatar_middle() {
		return avatar_middle;
	}

	public void setAvatar_middle(String avatarMiddle) {
		avatar_middle = avatarMiddle;
	}

	public String getAvatar_small() {
		return avatar_small;
	}

	public void setAvatar_small(String avatarSmall) {
		avatar_small = avatarSmall;
	}

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int commentCount) {
		comment_count = commentCount;
	}

	public int getRepost_count() {
		return repost_count;
	}

	public void setRepost_count(int repostCount) {
		repost_count = repostCount;
	}

	public String getTranspond_id() {
		return transpond_id;
	}

	public void setTranspond_id(String transpondId) {
		transpond_id = transpondId;
	}

	public WeiboBean getTranspond_data() {
		return transpond_data;
	}

	public void setTranspond_data(WeiboBean transpondData) {
		transpond_data = transpondData;
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
