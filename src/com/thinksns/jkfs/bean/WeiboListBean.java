package com.thinksns.jkfs.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 微博列表
 * 
 */
public class WeiboListBean implements Parcelable {
	private List<WeiboBean> weibos = new ArrayList<WeiboBean>();

	public List<WeiboBean> getWeibos() {
		return weibos;
	}

	public void setWeibos(List<WeiboBean> weibos) {
		this.weibos = weibos;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeTypedList(weibos);
	}

	public static final Parcelable.Creator<WeiboListBean> CREATOR = new Parcelable.Creator<WeiboListBean>() {
		public WeiboListBean createFromParcel(Parcel in) {
			WeiboListBean wlb = new WeiboListBean();
			in.readTypedList(wlb.weibos, WeiboBean.CREATOR);
			return wlb;
		}

		public WeiboListBean[] newArray(int size) {
			return new WeiboListBean[size];
		}
	};
}
