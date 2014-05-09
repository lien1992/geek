package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 收藏
 *
 */
public class FavoriteBean implements Parcelable{
	private String id;
	private String content;
	private String time;
	private String from;
	private String uid;
	private String uname;
	private int comment_count;
	private int repost_count;
	private String f_uid; // 收藏者
	private String f_time; // 收藏时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getF_uid() {
		return f_uid;
	}

	public void setF_uid(String fUid) {
		f_uid = fUid;
	}

	public String getF_time() {
		return f_time;
	}

	public void setF_time(String fTime) {
		f_time = fTime;
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
