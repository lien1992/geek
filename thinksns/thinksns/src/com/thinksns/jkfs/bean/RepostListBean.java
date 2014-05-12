package com.thinksns.jkfs.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 转发列表
 * 
 */
public class RepostListBean implements Parcelable{
	private List<WeiboBean> reposts = new ArrayList<WeiboBean>();

	public List<WeiboBean> getReposts() {
		return reposts;
	}

	public void setReposts(List<WeiboBean> reposts) {
		this.reposts = reposts;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
