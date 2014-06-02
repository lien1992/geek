package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 邓思宇 关注人或粉丝BEAN
 */
public class UserFollowBean implements Parcelable {
	private String follow_id;
	private String uid;
	private String Remark;
	private String uname;
	private String space_url;
	public UserFollowStateBean follow_state;
	private String avatar_big;
	private String avatar_middle;
	private String avatar_small;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(follow_id);
		dest.writeString(uid);
		dest.writeString(Remark);
		dest.writeString(uname);
		dest.writeString(space_url);
		dest.writeString(avatar_big);
		dest.writeString(avatar_middle);
		dest.writeString(avatar_small);

	}

	public String getFollow_id() {
		return follow_id;
	}

	public void setFollow_id(String follow_id) {
		this.follow_id = follow_id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getSpace_url() {
		return space_url;
	}

	public void setSpace_url(String space_url) {
		this.space_url = space_url;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getAvatar_big() {
		return avatar_big;
	}

	public void setAvatar_big(String avatar_big) {
		this.avatar_big = avatar_big;
	}

	public String getAvatar_small() {
		return avatar_small;
	}

	public void setAvatar_small(String avatar_small) {
		this.avatar_small = avatar_small;
	}

	public String getAvatar_middle() {
		return avatar_middle;
	}

	public void setAvatar_middle(String avatar_middle) {
		this.avatar_middle = avatar_middle;
	}

	public static final Parcelable.Creator<UserFollowBean> CREATOR = new Parcelable.Creator<UserFollowBean>() {
		public UserFollowBean createFromParcel(Parcel in) {
			UserFollowBean ub = new UserFollowBean();

			ub.follow_id = in.readString();
			ub.uid = in.readString();
			ub.Remark = in.readString();
			ub.uname = in.readString();
			ub.space_url = in.readString();
			ub.avatar_big = in.readString();
			ub.avatar_middle = in.readString();
			ub.avatar_small = in.readString();
			return ub;

		}

		public UserFollowBean[] newArray(int size) {
			return new UserFollowBean[size];
		}
	};

}
