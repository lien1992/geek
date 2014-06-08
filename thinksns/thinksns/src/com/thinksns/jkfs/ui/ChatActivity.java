package com.thinksns.jkfs.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseActivity;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.MessageBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.adapter.ChatAdapter;
import com.thinksns.jkfs.ui.adapter.Message;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;
import com.thinksns.jkfs.util.http.HttpUtils;

/**
 * Created by mosl on 14-5-24.
 */
public class ChatActivity extends BaseActivity {

	public static final String TAG = "ChatActivity";

	private ListView listView;
	private ChatAdapter chatAdapter;
	private EditText sendEditText;

	private String mJsonData;
	private AccountBean mAccountBean;
	private ThinkSNSApplication mApplication;
	
	private String id; // 获得的对话list_id
	private List<MessageBean> mList = new LinkedList<MessageBean>();
	private String messageID;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity_layout);
		listView = (ListView) findViewById(R.id.chat_list);
		mApplication = (ThinkSNSApplication) this.getApplicationContext();
		mAccountBean = mApplication.getAccount(this);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			id = extras.getString("list_id");
		}
		getMessage(id);
		sendEditText = (EditText) findViewById(R.id.messageInput);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 1:
				try {
					JSONArray jsonObject = new JSONArray(mJsonData);
					if (jsonObject != null)
						for (int i = 0; i < jsonObject.length(); i++) {
							JSONObject obj = jsonObject.getJSONObject(i);
							mList.add(MessageBean.JsonToBean(obj));
						}

					Collections.reverse(mList);
					listView.setAdapter(getAdapter(mList));

					chatAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};

	private BaseAdapter getAdapter(List<MessageBean> mList) {
		chatAdapter = new ChatAdapter(this, getMyData(mList));
		return chatAdapter;
	}

	private List<Message> getMyData(List<MessageBean> mList) {

		List<Message> msgList = new ArrayList<Message>();
		Message msg;
		msg = new Message();

		for (int i = 0; i < mList.size(); i++) {
			msg = new Message();
			if (mList.get(i).from_uid.equals(mAccountBean.getUid())) {
				msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
			} else {
				msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
				msg.setFace(mList.get(i).from_face);
			}
			msg.setValue(mList.get(i).content);
			msgList.add(msg);
		}
		return msgList;
	}

	public void sendButton(View view) {
		String input = sendEditText.getText().toString();
		Message msg = new Message();
		msg.setValue(input);
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		sendMessage(id,input);
		chatAdapter.addMessage(msg);
		sendEditText.setText("");
	}

	// 点击ITEM列表 获得详细的的对话内容
	public void getMessage(final String id) {
		if (mApplication.isNewWork(this)) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					final Map<String, String> map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "Message");
					map.put("act", "get_message_detail");
					map.put("id", id);
					map.put("oauth_token", mAccountBean.getOauth_token());
					map.put("oauth_token_secret",
							mAccountBean.getOauth_token_secret());
					map.put("format", "json");
					mJsonData = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					mHandler.sendEmptyMessage(1);

				}
			}).start();
		} else {
			Toast.makeText(this, "没有联网", 1000).show();
		}
	}

	// 发送信息
	public void sendMessage(final String id, final String content) {
		if (mApplication.isNewWork(this)) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					final Map<String, String> map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "Message");
					map.put("act", "reply");
					map.put("id", id);
					map.put("content", content);
					map.put("oauth_token", mAccountBean.getOauth_token());
					map.put("oauth_token_secret",
							mAccountBean.getOauth_token_secret());
					messageID = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Post, HttpConstant.THINKSNS_URL, map);
				}
			}).start();
		} else {
			Toast.makeText(this, "没有联网", 1000).show();
		}
	}

}