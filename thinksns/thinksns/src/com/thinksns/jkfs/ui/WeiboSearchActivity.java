package com.thinksns.jkfs.ui;

import java.util.ArrayList;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseActivity;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.ui.adapter.WeiboAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class WeiboSearchActivity extends BaseActivity {

	public static final String TAG = "zcc";

	private Activity mContext;
	private ImageView searchImage;
	private ImageView backImg;
	private TextView titleName;

	private PullToRefreshListView mListView;
	private LayoutInflater mInflater;
	private String jsonData;
	private Handler handler;
	private ArrayList<WeiboBean> weiboList;
	private WeiboAdapter weiboListViewAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weibo_search);
		mContext = this;
		searchImage = (ImageView) findViewById(R.id.search_weibo_title_search_img);
		backImg = (ImageView) findViewById(R.id.search_weibo_title_back_img);

		mListView = new PullToRefreshListView(mContext);
		mInflater = getLayoutInflater();
		weiboListViewAdapter = new WeiboAdapter(mContext, mInflater, mListView);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
			}
		};
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		initLayout();
	}

	/**
	 * 初始化布局
	 */
	private void initLayout() {
		// 屏幕宽度
//		Display display = mContext.getWindowManager().getDefaultDisplay();
//		int width = display.getWidth() / 2;
//		RelativeLayout view = (RelativeLayout) findViewById(R.id.search_weibo_choose_weibo);
//		LayoutParams lp = (LayoutParams) view.findViewById(
//				R.id.search_weibo_choose_weibo).getLayoutParams();
//		lp.width = width;
		// lp = (LayoutParams)
		// findViewById(R.id.search_weibo_choose_yonghu).getLayoutParams();
		// lp.width = width;
		// view.setLayoutParams(lp);
	}
}
