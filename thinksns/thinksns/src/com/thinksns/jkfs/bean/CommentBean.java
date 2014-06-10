package com.thinksns.jkfs.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.thinksns.jkfs.util.Utility;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.TextUtils;

/**
 * 评论
 * 
 */
@Table(name = "comment")
public class CommentBean implements Parcelable {
	@Id
	private int id;
	@Column
	private String comment_id;
	@Column
	private String uid;
	@Column
	private String content;
	@Column
	private String to_comment_id;// 被评论微博ID
	@Column
	private String to_uid;// 被评论用户ID
	@Column
	private String ctime;
	@Column
	private String client_type;
	@Foreign(column = "userId", foreign = "id")
	private UserInfoBean user_info;
	@Column
	private String comment_type;// 标识评论我的、我评论的

	private transient SpannableString listViewSpannableString;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String commentId) {
		comment_id = commentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}

	public String getClient_type() {
		return client_type;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public UserInfoBean getUser_info() {
		return user_info;
	}

	public void setUser_info(UserInfoBean userInfo) {
		user_info = userInfo;
	}

	public static Parcelable.Creator<CommentBean> getCreator() {
		return CREATOR;
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

	public SpannableString getListViewSpannableString() {
		if (!TextUtils.isEmpty(listViewSpannableString)) {
			return listViewSpannableString;
		} else {
			Utility.addHighLightLinks(this);
			return listViewSpannableString;
		}
	}

	public void setListViewSpannableString(
			SpannableString listViewSpannableString) {
		this.listViewSpannableString = listViewSpannableString;
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
		dest.writeString(uid);
		dest.writeString(content);
		dest.writeString(to_comment_id);
		dest.writeString(to_uid);
		dest.writeString(ctime);
		dest.writeString(client_type);
		dest.writeParcelable(user_info, flags);
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUid() {
		return uid;
	}

	public void setComment_type(String comment_type) {
		this.comment_type = comment_type;
	}

	public String getComment_type() {
		return comment_type;
	}

	public static final Parcelable.Creator<CommentBean> CREATOR = new Parcelable.Creator<CommentBean>() {
		public CommentBean createFromParcel(Parcel in) {
			CommentBean cb = new CommentBean();
			cb.comment_id = in.readString();
			cb.uid = in.readString();
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
