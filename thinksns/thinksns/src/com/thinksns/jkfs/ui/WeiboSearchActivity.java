package com.thinksns.jkfs.ui;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseActivity;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.ui.adapter.WeiboAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class WeiboSearchActivity extends BaseActivity {

	public static final String TAG = "zcc";

	private Activity mContext;
	private ImageView searchImage;
	private ImageView settingImg;
	private TextView titleName;

	private PullToRefreshListView mListView;
	private LayoutInflater mInflater;
	private String jsonData;
	private Handler handler;
	private ArrayList<WeiboBean> weiboList;
	private WeiboAdapter listViewAdapter;

	private PopupWindow popupWindow;
	private View mPopupWindowView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		

	}
}
