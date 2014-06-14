package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 杨智勇
 * @since 2014-5-29
 */
public class PostCommentBean implements Parcelable {
	private String reply_id;
	private String post_id;
	private String uid;
	private String uname;
	private String avatar_tiny;
	private String to_reply_id;
	private String to_uid;
	private String ctime;// 发布时间
	private String content;
	private String storey;// 楼

	public PostCommentBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PostCommentBean(String reply_id, String post_id, String uid,
			String uname, String avatar_tiny, String to_reply_id,
			String to_uid, String ctime, String content, String storey) {
		super();
		this.reply_id = reply_id;
		this.post_id = post_id;
		this.uid = uid;
		this.uname = uname;
		this.avatar_tiny = avatar_tiny;
		this.to_reply_id = to_reply_id;
		this.to_uid = to_uid;
		this.ctime = ctime;
		this.content = content;
		this.storey = storey;
	}

	public String getReply_id() {
		return reply_id;
	}

	public void setReply_id(String reply_id) {
		this.reply_id = reply_id;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
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

	public String getAvatar_tiny() {
		return avatar_tiny;
	}

	public void setAvatar_tiny(String avatar_tiny) {
		this.avatar_tiny = avatar_tiny;
	}

	public String getTo_reply_id() {
		return to_reply_id;
	}

	public void setTo_reply_id(String to_reply_id) {
		this.to_reply_id = to_reply_id;
	}

	public String getTo_uid() {
		return to_uid;
	}

	public void setTo_uid(String to_uid) {
		this.to_uid = to_uid;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStorey() {
		return storey;
	}

	public void setStorey(String storey) {
		this.storey = storey;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(reply_id);
		dest.writeString(post_id);
		dest.writeString(uid);
		dest.writeString(uname);
		dest.writeString(avatar_tiny);
		dest.writeString(to_reply_id);
		dest.writeString(to_uid);
		dest.writeString(ctime);
		dest.writeString(content);
		dest.writeString(storey);
	}

	public static final Parcelable.Creator<PostCommentBean> CREATOR = new Parcelable.Creator<PostCommentBean>() {
		public PostCommentBean createFromParcel(Parcel in) {
			PostCommentBean ab = new PostCommentBean();
			ab.reply_id = in.readString();
			ab.post_id = in.readString();
			ab.uid = in.readString();
			ab.uname = in.readString();
			ab.avatar_tiny = in.readString();
			ab.to_reply_id = in.readString();
			ab.to_uid = in.readString();
			ab.ctime = in.readString();
			ab.content = in.readString();
			ab.storey = in.readString();
			return ab;
		}

		public PostCommentBean[] newArray(int size) {
			return new PostCommentBean[size];
		}
	};

}
