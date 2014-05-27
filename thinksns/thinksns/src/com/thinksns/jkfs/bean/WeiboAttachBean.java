package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 微博附件
 *
 */
public class WeiboAttachBean implements Parcelable{
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
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

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
