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

import com.thinksns.jkfs.bean.WeibaBean;
import com.thinksns.jkfs.util.db.WeibaOperator;

public class WeibaDataHelper extends WeibaBaseHelper {
	private static final String TAG = "WeibaDataHelper";
	private List<WeibaBean> weibaList;
	private boolean cacheFlag;

	public WeibaDataHelper(Handler mHandler, Context mContext,
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
		weibaList = WeibaOperator.getInstance().queryList();
		if (weibaList != null) {
			mHandler.obtainMessage(flag, weibaList).sendToTarget();
		} else {
			mHandler.obtainMessage(DATA_ERROR).sendToTarget();
		}
	}

	/**
	 * 通过网络获取信息
	 * 
	 */
	public void getDataViaNet() {
		jsonData = getJSONData();
		Log.w(TAG, "jsonData大小"+jsonData);
		if (jsonData != null) {
			weibaList = jsonToWeibaList(jsonData);
			Log.w(TAG, "List大小"+weibaList.size());
			mHandler.obtainMessage(flag, weibaList).sendToTarget();
		} else {
			mHandler.obtainMessage(DATA_ERROR).sendToTarget();
		}
		if (cacheFlag) {
			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					WeibaOperator.getInstance().addOrUpdateList(weibaList);
				}
			}.start();
		}
	}

	public List<WeibaBean> jsonToWeibaList(String jsonData) {
		List<WeibaBean> weibaList = new LinkedList<WeibaBean>();
		try {
			JSONArray obj = new JSONArray(jsonData);
			for (int i = 0; i < obj.length(); i++) {
				JSONObject item = obj.getJSONObject(i);
				JSONObject post_status = item.optJSONObject("post_status");
				weibaList.add(new WeibaBean(item.getString("weiba_id"), item
						.getString("weiba_name"), item.getString("intro"), item
						.getString("follower_count"), item
						.getString("thread_count"), item.getString("notify"),
						item.getString("logo_url"), item.getInt("followstate"),
						post_status==null?-1:post_status.optInt("status")));
			}
		} catch (JSONException e) {
			Log.i(TAG, "json出问题" + e.toString());
		}
		return weibaList;
	}
}