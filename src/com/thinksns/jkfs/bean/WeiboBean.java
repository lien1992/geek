package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 微博
 * 
 */
public class WeiboBean implements Parcelable {
	private String id;
	private String content;
	private String time;
	private String from; // 0、1、2
	private String uid;
	private String uname;
	private int comment_count; // 评论数
	private int repost_count; // 转发数

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

	public static Parcelable.Creator<WeiboBean> getCreator() {
		return CREATOR;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(content);
		dest.writeString(time);
		dest.writeString(from);
		dest.writeString(uid);
		dest.writeString(uname);
		dest.writeInt(comment_count);
		dest.writeInt(repost_count);

	}

	public static final Parcelable.Creator<WeiboBean> CREATOR = new Parcelable.Creator<WeiboBean>() {
		public WeiboBean createFromParcel(Parcel in) {
			WeiboBean wb = new WeiboBean();
			wb.id = in.readString();
			wb.content = in.readString();
			wb.time = in.readString();
			wb.from = in.readString();
			wb.uid = in.readString();
			wb.uname = in.readString();
			wb.comment_count = in.readInt();
			wb.repost_count = in.readInt();
			return wb;
		}

		public WeiboBean[] newArray(int size) {
			return new WeiboBean[size];
		}
	};

}
