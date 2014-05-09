package com.thinksns.jkfs.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 评论列表
 * 
 */
public class CommentListBean implements Parcelable {
	private List<CommentBean> comments = new ArrayList<CommentBean>();

	public List<CommentBean> getComments() {
		return comments;
	}

	public void setComments(List<CommentBean> comments) {
		this.comments = comments;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeTypedList(comments);
	}

	public static final Parcelable.Creator<CommentListBean> CREATOR = new Parcelable.Creator<CommentListBean>() {
		public CommentListBean createFromParcel(Parcel in) {
			CommentListBean clb = new CommentListBean();
			in.readTypedList(clb.comments, CommentBean.CREATOR);
			return clb;
		}

		public CommentListBean[] newArray(int size) {
			return new CommentListBean[size];
		}
	};

}
