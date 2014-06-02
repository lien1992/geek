package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 新消息通知
 * 
 */
public class NotificationBean implements Parcelable {
	private String type;
	private String name;
	private String icon;
	private int count;
	private String data;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(type);
		dest.writeString(name);
		dest.writeString(icon);
		dest.writeInt(count);
		dest.writeString(data);
	}

	public static final Parcelable.Creator<NotificationBean> CREATOR = new Parcelable.Creator<NotificationBean>() {
		public NotificationBean createFromParcel(Parcel in) {
			NotificationBean nb = new NotificationBean();
			nb.type = in.readString();
			nb.name = in.readString();
			nb.icon = in.readString();
			nb.count = in.readInt();
			nb.data = in.readString();
			return nb;
		}

		public NotificationBean[] newArray(int size) {
			return new NotificationBean[size];
		}
	};
}
