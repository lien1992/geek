package com.thinksns.jkfs.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户基本资料
 * 
 */
public class UserInfoBean implements Parcelable {
	private String uid;
	private String uname;
	private String email;
	private String sex;
	private String province;
	private String city;
	private String location;
	public UserInfoCountBean count_info;
	public UserFollowStateBean follow_state;

	// private String last_feed_id;// 最近一条微博ID
	private String avatar_original;

	private String avatar_big;
	private String avatar_middle;
	private String avatar_small;

	// private UserInfoCountBean count_info;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAvatar() {
		return avatar_original;
	}

	public void setAvatar(String avatar) {
		this.avatar_original = avatar;
	}

	// public void setCount_info(UserInfoCountBean count_info) {
	// this.count_info = count_info;
	// }
	//
	// public UserInfoCountBean getCount_info() {
	// return count_info;
	// }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(uid);
		dest.writeString(uname);
		dest.writeString(email);
		dest.writeString(sex);
		dest.writeString(province);
		dest.writeString(city);
		dest.writeString(avatar_original);
		dest.writeString(avatar_big);
		dest.writeString(avatar_middle);
		dest.writeString(avatar_small);
		dest.writeString(location);
		// dest.writeString(count_info);
		// dest.writeString(following_count);
		// dest.writeString(follower_count);
		// dest.writeString(weibo_count);
		// dest.writeString(last_feed_id);
		// dest.writeParcelable(count_info, flags);

	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	// public String getFollowing_count() {
	// return following_count;
	// }
	//
	// public void setFollowing_count(String following_count) {
	// this.following_count = following_count;
	// }
	//
	// public String getFollower_count() {
	// return follower_count;
	// }
	//
	// public void setFollower_count(String follower_count) {
	// this.follower_count = follower_count;
	// }
	//
	// public String getWeibo_count() {
	// return weibo_count;
	// }
	//
	// public void setWeibo_count(String weibo_count) {
	// this.weibo_count = weibo_count;
	// }
	//
	// public String getCount_info() {
	// return count_info;
	// }
	//
	// public void setCount_info(String count_info) {
	// this.count_info = count_info;
	// }

	public void setAvatar_big(String avatar_big) {
		this.avatar_big = avatar_big;
	}

	public String getAvatar_big() {
		return avatar_big;
	}

	public void setAvatar_middle(String avatar_middle) {
		this.avatar_middle = avatar_middle;
	}

	public String getAvatar_middle() {
		return avatar_middle;
	}

	public void setAvatar_small(String avatar_small) {
		this.avatar_small = avatar_small;
	}

	public String getAvatar_small() {
		return avatar_small;
	}

	public static final Parcelable.Creator<UserInfoBean> CREATOR = new Parcelable.Creator<UserInfoBean>() {
		public UserInfoBean createFromParcel(Parcel in) {
			UserInfoBean ub = new UserInfoBean();
			ub.uid = in.readString();
			ub.uname = in.readString();
			ub.email = in.readString();
			ub.sex = in.readString();
			ub.province = in.readString();
			ub.city = in.readString();
			ub.avatar_original = in.readString();
			ub.avatar_big = in.readString();
			ub.avatar_middle = in.readString();
			ub.avatar_small = in.readString();
			ub.location = in.readString();
			// ub.following_count = in.readString();
			// ub.follower_count = in.readString();
			// ub.weibo_count = in.readString();
			// ub.count_info = in.readString();
			// ub.last_feed_id = in.readString();
			// ub.count_info = in.readParcelable(UserInfoCountBean.class
			// .getClassLoader());
			return ub;

		}

		public UserInfoBean[] newArray(int size) {
			return new UserInfoBean[size];
		}
	};

}
