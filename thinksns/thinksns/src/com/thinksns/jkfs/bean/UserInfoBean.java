package com.thinksns.jkfs.bean;

import java.util.List;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户基本资料
 * 
 */
@Table(name = "user_info")
public class UserInfoBean implements Parcelable {
	@Id
	private int id;
	@Column
	private String uid;
	@Column
	private String uname;
	@Column
	private String email;
	@Column
	private String sex;
	@Column
	private String province;
	@Column
	private String city;
	@Column
	private String location;
	@Finder(valueColumn = "id", targetColumn = "userInfoId")
	public UserInfoCountBean count_info;
	@Transient
	public UserFollowStateBean follow_state;
	@Column
	private String avatar_original;
	@Column
	private String avatar_big;
	@Column
	private String avatar_middle;
	@Column
	private String avatar_small;
	@Finder(valueColumn = "id", targetColumn = "userId")
	private CommentBean comment;
	@Finder(valueColumn = "id", targetColumn = "medalUserId")
	private List<UserInfoMedalBean> medals;

	public UserInfoBean() {

	}

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

	// public void setCount_info(UserInfoCountBean count_info) {
	// this.count_info = count_info;
	// }
	//
	// public UserInfoCountBean getCount_info() {
	// return count_info;
	// }

	public String getAvatar_original() {
		return avatar_original;
	}

	public void setAvatar_original(String avatarOriginal) {
		avatar_original = avatarOriginal;
	}

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

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setComment(CommentBean comment) {
		this.comment = comment;
	}

	public CommentBean getComment() {
		return comment;
	}

	public void setMedals(List<UserInfoMedalBean> medals) {
		this.medals = medals;
	}

	public List<UserInfoMedalBean> getMedals() {
		return medals;
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
