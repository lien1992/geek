package com.thinksns.jkfs.bean;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;

/**
 * 被转发微博
 * 
 */
@Table(name = "weibo_repost")
public class WeiboRepostBean implements Parcelable {
	@Id
	private int id;
	@Column
	private String feed_id;
	@Column
	private String type;
	@Column
	private String content;
	@Column
	private String ctime;
	@Column
	private String uname;
	@Column
	private String avatar_big;
	@Column
	private String avatar_middle;
	@Column
	private String avatar_small;
	@Finder(valueColumn = "id", targetColumn = "weiboRepostId")
	private List<WeiboRepostAttachBean> attach;
	@Foreign(column = "originId", foreign = "id")
	public WeiboBean weibo;
	@Transient
	private SpannableString listViewSpannableString;

	public WeiboRepostBean() {

	}

	public String getFeed_id() {
		return feed_id;
	}

	public void setFeed_id(String feedId) {
		feed_id = feedId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
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

	public void setAvatar_big(String avatarBig) {
		avatar_big = avatarBig;
	}

	public String getAvatar_middle() {
		return avatar_middle;
	}

	public void setAvatar_middle(String avatarMiddle) {
		avatar_middle = avatarMiddle;
	}

	public String getAvatar_small() {
		return avatar_small;
	}

	public void setAvatar_small(String avatarSmall) {
		avatar_small = avatarSmall;
	}

	public SpannableString getListViewSpannableString() {
		return listViewSpannableString;

	}

	public void setListViewSpannableString(
			SpannableString listViewSpannableString) {
		this.listViewSpannableString = listViewSpannableString;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		dest.writeString(feed_id);
		dest.writeString(type);
		dest.writeString(content);
		dest.writeString(ctime);
		dest.writeString(uname);
		dest.writeString(avatar_big);
		dest.writeString(avatar_middle);
		dest.writeString(avatar_small);
		dest.writeTypedList(attach);

	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public List<WeiboRepostAttachBean> getAttach() {
		return attach;
	}

	public void setAttach(List<WeiboRepostAttachBean> attach) {
		this.attach = attach;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static final Parcelable.Creator<WeiboRepostBean> CREATOR = new Parcelable.Creator<WeiboRepostBean>() {

		@Override
		public WeiboRepostBean createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			WeiboRepostBean wrb = new WeiboRepostBean();
			wrb.feed_id = in.readString();
			wrb.type = in.readString();
			wrb.content = in.readString();
			wrb.ctime = in.readString();
			wrb.uname = in.readString();
			wrb.avatar_big = in.readString();
			wrb.avatar_middle = in.readString();
			wrb.avatar_small = in.readString();
			wrb.attach = new ArrayList<WeiboRepostAttachBean>();
			in.readTypedList(wrb.attach, WeiboRepostAttachBean.CREATOR);
			return wrb;
		}

		@Override
		public WeiboRepostBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new WeiboRepostBean[size];
		}

	};

}
