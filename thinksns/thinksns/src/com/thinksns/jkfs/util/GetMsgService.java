package com.thinksns.jkfs.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.NotificationBean;
import com.thinksns.jkfs.constant.BaseConstant;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class GetMsgService extends IntentService {
	private AccountBean account;
	private ThinkSNSApplication application;
	private List<NotificationBean> msgs = new ArrayList<NotificationBean>();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		application = (ThinkSNSApplication) getApplicationContext();
		account = application.getAccount(this);
	}

	public GetMsgService() {
		super("GetMsgService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		if (Utility.isConnected(this)) {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("app", "api");
			map.put("mod", "Notifytion");
			map.put("act", "get_notify_by_count");
			map.put("oauth_token", account.getOauth_token());
			map.put("oauth_token_secret", account.getOauth_token_secret());
			String json = HttpUtility.getInstance().executeNormalTask(
					HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
			if (json != null && json.startsWith("[")) {
				Type listType = new TypeToken<LinkedList<NotificationBean>>() {
				}.getType();
				msgs = gson.fromJson(json, listType);
				if (msgs == null || msgs.size() == 0) {
					return;
				} else {
					Intent in = new Intent();
					in.setAction(BaseConstant.UNREAD_MSG_BROADCAST);
					in.putExtra("system", msgs.get(0));
					in.putExtra("atme", msgs.get(1));
					in.putExtra("comment", msgs.get(2));
					in.putExtra("follower", msgs.get(3));
					in.putExtra("pm", msgs.get(4));
					sendBroadcast(in);
					Log.d("wj", "unread json sendBroadcast");
				}
			}
		} else {
			return;
		}
	}
}
