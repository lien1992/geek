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
import com.thinksns.jkfs.util.db.PostOperator;

public class WeibaPostHelper extends WeibaBaseHelper {
	private static final String TAG = "WeibaPostHelper";
	private boolean cacheFlag;
	private int postTableFlag;
	private List<PostBean> postList;

	public WeibaPostHelper(Handler mHandler, Context mContext,
			Map<String, String> map, int flag, boolean cacheFlag,
			int postTableFlag) {
		super(mHandler, mContext, map, flag);
		this.cacheFlag = cacheFlag;
		this.postTableFlag = postTableFlag;
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
		postList = PostOperator.getInstance(postTableFlag).queryList();
		if (postList != null) {
			mHandler.obtainMessage(flag, postList).sendToTarget();
		} else {
			mHandler.obtainMessage(DATA_ERROR).sendToTarget();
		}
	}

	public void getDataViaNet() {
		jsonData = getJSONData();
		if (jsonData != null) {
			postList = jsonToPostList(jsonData);
			mHandler.obtainMessage(flag, postList).sendToTarget();
		} else {
			mHandler.obtainMessage(DATA_ERROR).sendToTarget();
		}
		if (cacheFlag) {
			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					PostOperator.getInstance(postTableFlag).addOrUpdateList(
							postList);
				}

			}.start();
		}
	}

	public List<PostBean> jsonToPostList(String jsonData) {
		List<PostBean> postList = new LinkedList<PostBean>();
		try {
			JSONArray obj = new JSONArray(jsonData);
			for (int i = 0; i < obj.length(); i++) {
				JSONObject item = obj.getJSONObject(i);
				JSONObject author = item.getJSONObject("author_info");
				postList.add(new PostBean(item.getString("post_id"), item
						.getString("weiba_id"), item.getString("post_uid"),
						author.getString("uname"), item.getString("title"),
						item.getString("content"), item.getString("post_time"),
						item.getString("reply_count"), item
								.getString("read_count"),
						item.getString("top"), item.getString("recommend"),
						author.getString("avatar_tiny"), item
								.optInt("favorite")));
			}
		} catch (JSONException e) {
			Log.i(TAG, "json出问题" + e.toString());
		}
		return postList;
	}
}