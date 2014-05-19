package com.thinksns.jkfs.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseFragment;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.ChanelBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.MainFragmentActivity;
import com.thinksns.jkfs.ui.adapter.ChanelFragmentListViewAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.DES;
import com.thinksns.jkfs.util.MD5;
import com.thinksns.jkfs.util.db.AccountOperator;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ChanelFragment extends Fragment {

	static private final String TAG = "ChanelFragment";
	static private final int GETTED_CHANEL_LIST = 0;
	static private ArrayList<ChanelBean> chanelList;

	private Button button;
	private PullToRefreshListView mListView;
	private LayoutInflater mInflater;
	private String jsonData;
	private Handler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater
				.inflate(R.layout.fragment_chanel, container, false);
		// 测试用button
		button = (Button) view.findViewById(R.id.zhankaicaidan_btn);
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.chanel_listview);
		mInflater = LayoutInflater.from(view.getContext());

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case GETTED_CHANEL_LIST:
					// 更新chanel列表ui
					break;
				}
			}
		};

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AccountBean accountBean = ThinkSNSApplication.getInstance()
						.getAccount();
				final Map<String, String> map = new HashMap<String, String>();
				map.put("app", "api");
				map.put("mod", "Channel");
				map.put("act", "get_all_channel");
				map.put("oauth_token", accountBean.getOauth_token());
				map.put("oauth_token_secret",
						accountBean.getOauth_token_secret());
				Log.i(TAG, accountBean.getOauth_token_secret());
				// 每次运行程序只获取一次chanel
				if (chanelList == null || chanelList.size() == 0) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							jsonData = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							Log.i(TAG, jsonData);
							try {
								JSONObject response = new JSONObject(jsonData);
								Iterator<String> it = response.keys();
								JSONObject obj;
								chanelList = new ArrayList<ChanelBean>();
								while (it.hasNext()) {
									obj = response.getJSONObject(it.next());
									chanelList.add(new ChanelBean(obj
											.getString("channel_category_id"),
											obj.getString("title"), obj
													.getString("pid"), obj
													.getString("sort"),
											obj.getString("icon_url")));
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							handler.sendEmptyMessage(GETTED_CHANEL_LIST);
						}
					}).start();
				}
			}
		});
		//测试用数据
		WeiboBean weibo = new WeiboBean();
		weibo.setContent("啦啦");
		weibo.setUname("凤飞飞");

		ArrayList<WeiboBean> list = new ArrayList<WeiboBean>();
		for (int i = 0; i < 30; i++) {
			list.add(weibo);
		}

		mListView
				.setAdapter(new ChanelFragmentListViewAdapter(list, mInflater));
		return view;
	}
}