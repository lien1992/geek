package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 微博
 * 
 */
public class WeiboBean implements Parcelable {
	private String feed_id;
	private String type; // postimage、repost...
	private String content;
	private String ctime;
	private String from; // 0、1、2
	private String uid;
	private String uname;
	private String avatar_big;
	private String avatar_middle;
	private String avatar_small;
	private String has_attach; // 0、1

	private int comment_count; // 评论数
	private int repost_count; // 转发数

	private WeiboAttachBean attach;

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

	public String getHas_attach() {
		return has_attach;
	}

	public void setHas_attach(String hasAttach) {
		has_attach = hasAttach;
	}

	public String getId() {
		return feed_id;
	}

	public void setId(String id) {
		this.feed_id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return ctime;
	}

	public void setTime(String time) {
		this.ctime = time;
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

	public void setAvatar_big(String avatar_big) {
		this.avatar_big = avatar_big;
	}

	public String getAvatar_big() {
		return avatar_big;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
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

	public void setAttach(WeiboAttachBean attach) {
		this.attach = attach;
	}

	public WeiboAttachBean getAttach() {
		return attach;
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
		dest.writeString(feed_id);
		dest.writeString(type);
		dest.writeString(content);
		dest.writeString(ctime);
		dest.writeString(from);
		dest.writeString(uid);
		dest.writeString(uname);
		dest.writeString(avatar_big);
		dest.writeString(avatar_middle);
		dest.writeString(avatar_small);
		dest.writeString(has_attach);
		dest.writeInt(comment_count);
		dest.writeInt(repost_count);
		dest.writeParcelable(attach, flags);

	}

	public static final Parcelable.Creator<WeiboBean> CREATOR = new Parcelable.Creator<WeiboBean>() {
		public WeiboBean createFromParcel(Parcel in) {
			WeiboBean wb = new WeiboBean();
			wb.feed_id = in.readString();
			wb.type = in.readString();
			wb.content = in.readString();
			wb.ctime = in.readString();
			wb.from = in.readString();
			wb.uid = in.readString();
			wb.uname = in.readString();
			wb.avatar_big = in.readString();
			wb.avatar_middle = in.readString();
			wb.avatar_small = in.readString();
			wb.has_attach = in.readString();
			wb.comment_count = in.readInt();
			wb.repost_count = in.readInt();
			wb.attach = in.readParcelable(WeiboAttachBean.class
					.getClassLoader());
			return wb;
		}

		public WeiboBean[] newArray(int size) {
			return new WeiboBean[size];
		}
	};

}
