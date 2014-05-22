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
import com.thinksns.jkfs.ui.view.ChanelMenu;
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

			
			}
		});

		// 模拟数据
		ArrayList<WeiboBean> list = new ArrayList<WeiboBean>();
		for (int i = 0; i < 10; i++) {
			list.add(new WeiboBean());
		}

		mListView
				.setAdapter(new ChanelFragmentListViewAdapter(list, mInflater));
		return view;
	}
}
