package com.thinksns.jkfs.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 微博附件
 * 
 */
@Table(name = "weibo_attach")
public class WeiboAttachBean implements Parcelable {
	@Id
	private int id;
	@Column(column = "attach_id")
	private String attach_id;
	@Column(column = "attach_name")
	private String attach_name;
	@Column(column = "attach_url")
	private String attach_url;
	@Column(column = "attach_small")
	private String attach_small;
	@Column(column = "attach_middle")
	private String attach_middle;
	@Foreign(column = "weiboId", foreign = "id")
	public WeiboBean weibo;

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

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getAttach_id() {
		return attach_id;
	}

	public void setAttach_id(String attachId) {
		attach_id = attachId;
	}

}
