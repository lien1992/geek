package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 评论
 * 
 */
public class CommentBean implements Parcelable {
	private String id;
	private String content;
	private String time;
	private UserInfoBean user;

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

	public UserInfoBean getUser() {
		return user;
	}

	public void setUser(UserInfoBean user) {
		this.user = user;
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
		dest.writeParcelable(user, flags);
	}

	public static final Parcelable.Creator<CommentBean> CREATOR = new Parcelable.Creator<CommentBean>() {
		public CommentBean createFromParcel(Parcel in) {
			CommentBean cb = new CommentBean();
			cb.id = in.readString();
			cb.content = in.readString();
			cb.time = in.readString();
			cb.user = in.readParcelable(UserInfoBean.class.getClassLoader());
			return cb;
		}

		public CommentBean[] newArray(int size) {
			return new CommentBean[size];
		}
	};

}
