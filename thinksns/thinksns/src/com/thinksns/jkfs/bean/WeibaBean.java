package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 杨智勇
 * @since 2014-5-29
 */
public class WeibaBean implements Parcelable {
	private String weiba_id;
	private String weiba_name;
	private String intro;
	private String follower_count;// 粉丝数
	private String thread_count;// 发帖数
	private String notify;// 公告
	private String logo_url;
	private int follow_state;// 是否关注
	private int post_status;// 发帖权限

	public WeibaBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WeibaBean(String weiba_id, String weiba_name, String intro,
			String follower_count, String thread_count, String notify,
			String logo_url, int follow_state, int post_status) {
		super();
		this.weiba_id = weiba_id;
		this.weiba_name = weiba_name;
		this.intro = intro;
		this.follower_count = follower_count;
		this.thread_count = thread_count;
		this.notify = notify;
		this.logo_url = logo_url;
		this.follow_state = follow_state;
		this.post_status = post_status;
	}

	public String getWeiba_id() {
		return weiba_id;
	}

	public void setWeiba_id(String weiba_id) {
		this.weiba_id = weiba_id;
	}

	public String getWeiba_name() {
		return weiba_name;
	}

	public void setWeiba_name(String weiba_name) {
		this.weiba_name = weiba_name;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getFollower_count() {
		return follower_count;
	}

	public void setFollower_count(String follower_count) {
		this.follower_count = follower_count;
	}

	public String getThread_count() {
		return thread_count;
	}

	public void setThread_count(String thread_count) {
		this.thread_count = thread_count;
	}

	public String getNotify() {
		return notify;
	}

	public void setNotify(String notify) {
		this.notify = notify;
	}

	public String getLogo_url() {
		return logo_url;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}

	public int getFollow_state() {
		return follow_state;
	}

	public void setFollow_state(int follow_state) {
		this.follow_state = follow_state;
	}

	public int getPost_status() {
		return post_status;
	}

	public void setPost_status(int post_status) {
		this.post_status = post_status;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(weiba_id);
		dest.writeString(weiba_name);
		dest.writeString(intro);
		dest.writeString(follower_count);
		dest.writeString(thread_count);
		dest.writeString(notify);
		dest.writeString(logo_url);
		dest.writeInt(follow_state);
		dest.writeInt(post_status);
	}

	public static final Parcelable.Creator<WeibaBean> CREATOR = new Parcelable.Creator<WeibaBean>() {
		public WeibaBean createFromParcel(Parcel in) {
			WeibaBean wb = new WeibaBean();
			wb.weiba_id = in.readString();
			wb.weiba_name = in.readString();
			wb.intro = in.readString();
			wb.follower_count = in.readString();
			wb.thread_count = in.readString();
			wb.notify = in.readString();
			wb.logo_url = in.readString();
			wb.follow_state = in.readInt();
			wb.post_status = in.readInt();
			return wb;
		}

		public WeibaBean[] newArray(int size) {
			return new WeibaBean[size];
		}
	};

}
