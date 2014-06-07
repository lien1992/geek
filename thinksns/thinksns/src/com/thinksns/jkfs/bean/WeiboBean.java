package com.thinksns.jkfs.bean;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;
import com.thinksns.jkfs.util.Utility;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.TextUtils;

/**
 * 微博
 * 
 */
@Table(name = "weibo")
public class WeiboBean implements Parcelable {
	@Id
	private int id;
	@Column(column = "feed_id")
	private String feed_id;
	@Column(column = "type")
	private String type; // postimage、repost...
	@Column(column = "content")
	private String content;
	@Column(column = "ctime")
	private String ctime;
	@Column(column = "from_source")
	private String from; // 0、1、2
	@Column(column = "uid")
	private String uid;
	@Column(column = "uname")
	private String uname;
	@Column(column = "avatar_big")
	private String avatar_big;
	@Column(column = "avatar_middle")
	private String avatar_middle;
	@Column(column = "avatar_small")
	private String avatar_small;
	@Column(column = "has_attach")
	private String has_attach; // 0、1
	@Column(column = "comment_count")
	private int comment_count; // 评论数
	@Column(column = "repost_count")
	private int repost_count; // 转发数
	@Column(column = "digg_count")
	private int digg_count; // 赞数
	@Finder(valueColumn = "id", targetColumn = "weiboId")
	private List<WeiboAttachBean> attach;
	@Transient//@Finder(valueColumn = "id", targetColumn = "originId")
	private WeiboRepostBean transpond_data; // 被转发微博
	@Transient
	private SpannableString listViewSpannableString;

	public WeiboBean(String feed_id, String type, String content, String ctime,
			String from, String uid, String uname, String avatar_big,
			String avatar_middle, String avatar_small, String has_attach,
			int comment_count, int repost_count,
			ArrayList<WeiboAttachBean> attachList) {
		super();
		this.feed_id = feed_id;
		this.type = type;
		this.content = content;
		this.ctime = ctime;
		this.from = from;
		this.uid = uid;
		this.uname = uname;
		this.avatar_big = avatar_big;
		this.avatar_middle = avatar_middle;
		this.avatar_small = avatar_small;
		this.has_attach = has_attach;
		this.comment_count = comment_count;
		this.repost_count = repost_count;
		// this.attachList = attachList;
	}

	public WeiboBean() {
		super();
	}

	public String getFeed_id() {
		return feed_id;
	}

	public WeiboRepostBean getTranspond_data() {
		return transpond_data;
	}

	public void setTranspond_data(WeiboRepostBean transpondData) {
		transpond_data = transpondData;
	}

	public void setFeed_id(String feed_id) {
		this.feed_id = feed_id;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
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

	public String getHas_attach() {
		return has_attach;
	}

	public void setHas_attach(String hasAttach) {
		has_attach = hasAttach;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return ctime;
	}

	public void setTime(String time) {
		this.ctime = time;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
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

	public void setAvatar_big(String avatar_big) {
		this.avatar_big = avatar_big;
	}

	public String getAvatar_big() {
		return avatar_big;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int commentCount) {
		comment_count = commentCount;
	}

	public int getRepost_count() {
		return repost_count;
	}

	public void setRepost_count(int repostCount) {
		repost_count = repostCount;
	}

	public SpannableString getListViewSpannableString() {
		if (!TextUtils.isEmpty(listViewSpannableString)) {
			return listViewSpannableString;
		} else {
			Utility.addHighLightLinks(this);
			return listViewSpannableString;
		}
	}

	public void setListViewSpannableString(
			SpannableString listViewSpannableString) {
		this.listViewSpannableString = listViewSpannableString;
	}

	public static Parcelable.Creator<WeiboBean> getCreator() {
		return CREATOR;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(feed_id);
		dest.writeString(type);
		dest.writeString(content);
		dest.writeString(ctime);
		dest.writeString(from);
		dest.writeString(uid);
		dest.writeString(uname);
		dest.writeString(avatar_big);
		dest.writeString(avatar_middle);
		dest.writeString(avatar_small);
		dest.writeString(has_attach);
		dest.writeInt(comment_count);
		dest.writeInt(repost_count);
		dest.writeInt(digg_count);
		dest.writeTypedList(attach);
		dest.writeParcelable(transpond_data, flags);

	}

	public void setDigg_count(int digg_count) {
		this.digg_count = digg_count;
	}

	public int getDigg_count() {
		return digg_count;
	}

	public void setAttach(List<WeiboAttachBean> attach) {
		this.attach = attach;
	}

	public List<WeiboAttachBean> getAttach() {
		return attach;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static final Parcelable.Creator<WeiboBean> CREATOR = new Parcelable.Creator<WeiboBean>() {
		public WeiboBean createFromParcel(Parcel in) {
			WeiboBean wb = new WeiboBean();
			wb.feed_id = in.readString();
			wb.type = in.readString();
			wb.content = in.readString();
			wb.ctime = in.readString();
			wb.from = in.readString();
			wb.uid = in.readString();
			wb.uname = in.readString();
			wb.avatar_big = in.readString();
			wb.avatar_middle = in.readString();
			wb.avatar_small = in.readString();
			wb.has_attach = in.readString();
			wb.comment_count = in.readInt();
			wb.repost_count = in.readInt();
			wb.digg_count = in.readInt();
			wb.attach = new ArrayList<WeiboAttachBean>();
			in.readTypedList(wb.attach, WeiboAttachBean.CREATOR);
			wb.transpond_data = in.readParcelable(WeiboBean.class
					.getClassLoader());

			return wb;
		}

		public WeiboBean[] newArray(int size) {
			return new WeiboBean[size];
		}
	};

}
