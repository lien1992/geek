package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 评论
 * 
 */
public class CommentBean implements Parcelable {
	private String comment_id;
	private String content;
	private String to_comment_id;
	private String to_uid;
	private String ctime;
	private String client_type;
	private UserInfoBean user_info;
	// sourceInfo?

	public String getId() {
		return comment_id;
	}

	public void setId(String id) {
		this.comment_id = id;
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

	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}

	public String getClient_type() {
		return client_type;
	}

	public UserInfoBean getUser() {
		return user_info;
	}

	public void setUser(UserInfoBean user) {
		this.user_info = user;
	}

	public void setTo_comment_id(String to_comment_id) {
		this.to_comment_id = to_comment_id;
	}

	public String getTo_comment_id() {
		return to_comment_id;
	}

	public void setTo_uid(String to_uid) {
		this.to_uid = to_uid;
	}

	public String getTo_uid() {
		return to_uid;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(comment_id);
		dest.writeString(content);
		dest.writeString(to_comment_id);
		dest.writeString(to_uid);
		dest.writeString(ctime);
		dest.writeString(client_type);
		dest.writeParcelable(user_info, flags);
	}

	public static final Parcelable.Creator<CommentBean> CREATOR = new Parcelable.Creator<CommentBean>() {
		public CommentBean createFromParcel(Parcel in) {
			CommentBean cb = new CommentBean();
			cb.comment_id = in.readString();
			cb.content = in.readString();
			cb.to_comment_id = in.readString();
			cb.to_uid = in.readString();
			cb.ctime = in.readString();
			cb.client_type = in.readString();
			cb.user_info = in.readParcelable(UserInfoBean.class
					.getClassLoader());
			return cb;
		}

		public CommentBean[] newArray(int size) {
			return new CommentBean[size];
		}
	};

}
