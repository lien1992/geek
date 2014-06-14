package com.thinksns.jkfs.util;

import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class WeibaActionHelper extends WeibaBaseHelper {
	private static final String TAG = "WeibaActionHelper";

	public WeibaActionHelper(Handler mHandler, Context mContext,
			Map<String, String> map, int flag) {
		super(mHandler, mContext, map, flag);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		netState = Utility.isConnected(mContext);
		if (netState) {
			postViaNet();
		} else {
			mHandler.obtainMessage(NET_ERROR).sendToTarget();
		}
	}

	public void postViaNet() {
		jsonData = getJSONData();
		if (jsonData != null) {
			Log.w("JSON动作数据", map.toString()+"/"+jsonData);
			String[] obj = new String[] { jsonData, map.get("id") };
			mHandler.obtainMessage(flag, obj).sendToTarget();
		} else {
			mHandler.obtainMessage(DATA_ERROR).sendToTarget();
		}
	}
}