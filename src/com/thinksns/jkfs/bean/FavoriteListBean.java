package com.thinksns.jkfs.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 收藏列表
 * 
 */

public class FavoriteListBean implements Parcelable{
	private List<FavoriteListBean> favorites = new ArrayList<FavoriteListBean>();

	public List<FavoriteListBean> getFavorites() {
		return favorites;
	}

	public void setFavorites(List<FavoriteListBean> favorites) {
		this.favorites = favorites;
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
