package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 杨智勇
 * @since 2014-5-29
 */
public class PostBean implements Parcelable {
	private String post_id;
	private String weiba_id;
	private String post_uid;
	private String uname;
	private String title;
	private String content;
	private String post_time;
	private String reply_count;
	private String read_count;
	private String top;
	private String recommend;
	private String avatar_tiny;
	private int favorite;

	public PostBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PostBean(String post_id, String weiba_id, String post_uid,
			String uname, String title, String content, String post_time,
			String reply_count, String read_count, String top,
			String recommend, String avatar_tiny, int favorite) {
		super();
		this.post_id = post_id;
		this.weiba_id = weiba_id;
		this.post_uid = post_uid;
		this.uname = uname;
		this.title = title;
		this.content = content;
		this.post_time = post_time;
		this.reply_count = reply_count;
		this.read_count = read_count;
		this.top = top;
		this.recommend = recommend;
		this.avatar_tiny = avatar_tiny;
		this.favorite = favorite;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	public String getWeiba_id() {
		return weiba_id;
	}

	public void setWeiba_id(String weiba_id) {
		this.weiba_id = weiba_id;
	}

	public String getPost_uid() {
		return post_uid;
	}

	public void setPost_uid(String post_uid) {
		this.post_uid = post_uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPost_time() {
		return post_time;
	}

	public void setPost_time(String post_time) {
		this.post_time = post_time;
	}

	public String getReply_count() {
		return reply_count;
	}

	public void setReply_count(String reply_count) {
		this.reply_count = reply_count;
	}

	public String getRead_count() {
		return read_count;
	}

	public void setRead_count(String read_count) {
		this.read_count = read_count;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getAvatar_tiny() {
		return avatar_tiny;
	}

	public void setAvatar_tiny(String avatar_tiny) {
		this.avatar_tiny = avatar_tiny;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(post_id);
		dest.writeString(weiba_id);
		dest.writeString(post_uid);
		dest.writeString(uname);
		dest.writeString(title);
		dest.writeString(content);
		dest.writeString(post_time);
		dest.writeString(reply_count);
		dest.writeString(read_count);
		dest.writeString(top);
		dest.writeString(recommend);
		dest.writeString(avatar_tiny);
		dest.writeInt(favorite);
	}

	public static final Parcelable.Creator<PostBean> CREATOR = new Parcelable.Creator<PostBean>() {
		public PostBean createFromParcel(Parcel in) {
			PostBean ab = new PostBean();
			ab.post_id = in.readString();
			ab.weiba_id = in.readString();
			ab.post_uid = in.readString();
			ab.uname = in.readString();
			ab.title = in.readString();
			ab.content = in.readString();
			ab.post_time = in.readString();
			ab.reply_count = in.readString();
			ab.read_count = in.readString();
			ab.top = in.readString();
			ab.recommend = in.readString();
			ab.avatar_tiny = in.readString();
			ab.favorite = in.readInt();
			return ab;
		}

		public PostBean[] newArray(int size) {
			return new PostBean[size];
		}
	};

}
