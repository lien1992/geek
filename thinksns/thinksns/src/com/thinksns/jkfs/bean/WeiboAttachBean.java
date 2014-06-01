package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 微博附件
 * 
 */
public class WeiboAttachBean implements Parcelable {
	private String attach_id;
	private String attach_name;
	private String attach_url;
	private String attach_small;
	private String attach_middle;

	public String getId() {
		return attach_id;
	}

	public void setId(String id) {
		this.attach_id = id;
	}

	public String getFile_name() {
		return attach_name;
	}

	public void setFile_name(String fileName) {
		attach_name = fileName;
	}

	public String getUrl() {
		return attach_url;
	}

	public void setUrl(String url) {
		this.attach_url = url;
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

	public static final Parcelable.Creator<WeiboAttachBean> CREATOR = new Parcelable.Creator<WeiboAttachBean>() {

		@Override
		public WeiboAttachBean createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			WeiboAttachBean wab = new WeiboAttachBean();
			wab.attach_id = in.readString();
			wab.attach_name = in.readString();
			wab.attach_url = in.readString();
			wab.attach_small = in.readString();
			wab.attach_middle = in.readString();

			return wab;
		}

		@Override
		public WeiboAttachBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new WeiboAttachBean[size];
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

}
