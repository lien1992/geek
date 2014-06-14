package com.thinksns.jkfs.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.thinksns.jkfs.bean.PostBean;
import com.thinksns.jkfs.bean.PostCommentBean;
import com.thinksns.jkfs.util.db.PostCommentOperator;
import com.thinksns.jkfs.util.db.PostOperator;

public class WeibaCommentHelper extends WeibaBaseHelper {
	private static final String TAG = "WeibaCommentHelper";
	private boolean cacheFlag;
	private List<PostCommentBean> postCommentList;

	public WeibaCommentHelper(Handler mHandler, Context mContext,
			Map<String, String> map, int flag, boolean cacheFlag) {
		super(mHandler, mContext, map, flag);
		this.cacheFlag = cacheFlag;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		netState = Utility.isConnected(mContext);
		if (cacheFlag) {
			if (netState) {
				getDataViaNet();
			} else {
				getDataViaDB();
			}
		} else {
			if (netState) {
				getDataViaNet();
			} else {
				mHandler.obtainMessage(NET_ERROR).sendToTarget();
			}
		}
	}

	/**
	 * 通过本地数据库获取信息
	 * 
	 */
	public void getDataViaDB() {
		postCommentList = PostCommentOperator.getInstance().queryList();
		if (postCommentList != null) {
			mHandler.obtainMessage(flag, postCommentList).sendToTarget();
		} else {
			mHandler.obtainMessage(DATA_ERROR).sendToTarget();
		}
	}

	public void getDataViaNet() {
		jsonData = getJSONData();
		if (jsonData != null) {
			postCommentList = jsonToPosCommenttList(jsonData);
			mHandler.obtainMessage(flag, postCommentList).sendToTarget();
		} else {
			mHandler.obtainMessage(DATA_ERROR).sendToTarget();
		}
		if (cacheFlag) {
			new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					PostCommentOperator.getInstance().addOrUpdateList(postCommentList);
				}
			}.start();
		}
	}

	public List<PostCommentBean> jsonToPosCommenttList(String jsonData) {
		List<PostCommentBean> postCommentList = new LinkedList<PostCommentBean>();
		try {
			JSONArray obj = new JSONArray(jsonData);
			for (int i = 0; i < obj.length(); i++) {
				JSONObject item = obj.getJSONObject(i);
				JSONObject author = item.getJSONObject("author_info");
				postCommentList.add(new PostCommentBean(item.getString("reply_id"), item
						.getString("post_id"), item
						.getString("uid"), author
						.getString("uname"), author
						.getString("avatar_tiny"),item
						.getString("to_reply_id"), item
						.getString("to_uid"), item
						.getString("ctime"), item
						.getString("content"),item
						.getString("storey")));
			}
		} catch (JSONException e) {
			Log.i(TAG, "json出问题" + e.toString());
		}
		return postCommentList;
	}
}