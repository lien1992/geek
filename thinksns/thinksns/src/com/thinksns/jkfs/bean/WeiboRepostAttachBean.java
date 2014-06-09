package com.thinksns.jkfs.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 被转发微博附件
 * 
 */
@Table(name = "weibo_repost_attach")
public class WeiboRepostAttachBean implements Parcelable {
	@Id
	private int id;
	@Column
	private String attach_id;
	@Column
	private String attach_name;
	@Column
	private String attach_url;
	@Column
	private String attach_small;
	@Column
	private String attach_middle;
	@Foreign(column = "weiboRepostId", foreign = "id")
	public WeiboRepostBean repost;

	public WeiboRepostAttachBean() {

	}

	public String getAttach_id() {
		return attach_id;
	}

	public void setAttach_id(String attachId) {
		attach_id = attachId;
	}

	public String getAttach_name() {
		return attach_name;
	}

	public void setAttach_name(String attachName) {
		attach_name = attachName;
	}

	public String getAttach_url() {
		return attach_url;
	}

	public void setAttach_url(String attachUrl) {
		attach_url = attachUrl;
	}

	public String getFile_name() {
		return attach_name;
	}

	public void setFile_name(String fileName) {
		attach_name = fileName;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		dest.writeString(attach_id);
		dest.writeString(attach_name);
		dest.writeString(attach_url);
		dest.writeString(attach_small);
		dest.writeString(attach_middle);
	}

	public static final Parcelable.Creator<WeiboRepostAttachBean> CREATOR = new Parcelable.Creator<WeiboRepostAttachBean>() {

		@Override
		public WeiboRepostAttachBean createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			WeiboRepostAttachBean wrab = new WeiboRepostAttachBean();
			wrab.attach_id = in.readString();
			wrab.attach_name = in.readString();
			wrab.attach_url = in.readString();
			wrab.attach_small = in.readString();
			wrab.attach_middle = in.readString();

			return wrab;
		}

		@Override
		public WeiboRepostAttachBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new WeiboRepostAttachBean[size];
		}

	};

	public void setAttach_small(String attach_small) {
		this.attach_small = attach_small;
	}

	public String getAttach_small() {
		return attach_small;
	}

	public void setAttach_middle(String attach_middle) {
		this.attach_middle = attach_middle;
	}

	public String getAttach_middle() {
		return attach_middle;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
