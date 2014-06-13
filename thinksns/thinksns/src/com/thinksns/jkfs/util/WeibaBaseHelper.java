package com.thinksns.jkfs.util;

import java.util.Map;

import android.content.Context;
import android.os.Handler;

import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

public class WeibaBaseHelper implements Runnable {
	public static final int NET_ERROR = -1;
	public static final int DATA_ERROR = -2;

	// 变量
	public boolean netState;
	public Handler mHandler;
	public Context mContext;
	public Map<String, String> map;
	public int flag;// 标记msg.what
	public String jsonData;

	public WeibaBaseHelper(Handler mHandler, Context mContext,
			Map<String, String> map, int flag) {
		super();
		this.mHandler = mHandler;
		this.mContext = mContext;
		this.map = map;
		this.flag = flag;
	}

	public String getJSONData() {
		try {
			return HttpUtility.getInstance().executeNormalTask(HttpMethod.Get,
					HttpConstant.GET_MESSAGE_LIST, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String postJSONData() {
		try {
			return HttpUtility.getInstance().executeNormalTask(HttpMethod.Post,
					HttpConstant.GET_MESSAGE_LIST, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
}