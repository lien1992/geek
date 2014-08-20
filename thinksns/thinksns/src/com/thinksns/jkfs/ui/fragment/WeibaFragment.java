package com.thinksns.jkfs.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.WeibaBean;
import com.thinksns.jkfs.ui.MainFragmentActivity;
import com.thinksns.jkfs.ui.adapter.WeibaDropAdapter;
import com.thinksns.jkfs.ui.view.ArcMenu;
import com.thinksns.jkfs.ui.view.SatelliteMenu;
import com.thinksns.jkfs.ui.view.SatelliteMenu.SateliteClickedListener;
import com.thinksns.jkfs.ui.view.SatelliteMenuItem;
import com.thinksns.jkfs.util.WeibaBaseHelper;
import com.thinksns.jkfs.util.WeibaDataHelper;

/**
 * @author 杨智勇
 * @since 2014-5-29
 */
public class WeibaFragment extends Fragment {
	// Handler信息
	public static final int GET_WEIBA_LIST = 0;

	// 常量
	private static final String TAG = "WeibaFragment";
	private static final String APP = "api";
	private static final String MOD = "Weiba";
	private static final String ACT_GET_WEIBA = "get_weibas";
	private static final String ACT_GET_FOLLOWING_POST = "following_posts";
	private static final String ACT_GET_POSTS = "get_posts";
	private static String OAUTH_TOKEN;
	private static String OAUTH_TOKEN_SECRECT;
	// 变量

	// 组件
	private Activity mContext;
	private ImageView navigation;
	private ImageView drop_button;
	private ArcMenu arcMenu;
	private PostListFragment mPostList;
	private View rootView;
	private PopupWindow mPopupWindow;
	private View weiba_bar;
	private View popupView;
	private WeibaDropAdapter weibaDropAdapter;
	private GridView gridView;
	private Handler mHandler;
	// 资源
	private static final int[] ITEM_DRAWABLES = {
	    	R.drawable.circle_ico_green_compose,
			R.drawable.circle_ico_green_search,R.drawable.circle_ico_green_man };
	private LinkedList<WeibaBean> weibaList;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		rootView = inflater.inflate(R.layout.weiba_fragment_layout, container,
				false);
		weiba_bar = (View) rootView.findViewById(R.id.weiba_bar);
		navigation = (ImageView) rootView
				.findViewById(R.id.weiba_fragment_title);
		drop_button = (ImageView) rootView
				.findViewById(R.id.weiba_fragmente_drop_button);
		arcMenu = (ArcMenu) rootView.findViewById(R.id.weiba_arc_menu);
		navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragmentActivity) getActivity()).getSlidingMenu()
                        .toggle();
            }
        });

		View test = rootView.findViewById(R.id.weiba_main_page);
		Log.w(TAG, test.toString());
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case GET_WEIBA_LIST:
					weibaList = (LinkedList<WeibaBean>) msg.obj;
				    weibaDropAdapter = new WeibaDropAdapter(mContext,
								weibaList);
					gridView.setAdapter(weibaDropAdapter);
					break;
				case WeibaBaseHelper.DATA_ERROR:
					Toast.makeText(mContext, "数据加载失败", Toast.LENGTH_SHORT)
							.show();
					break;
				case WeibaBaseHelper.NET_ERROR:
					Toast.makeText(mContext, "网络故障", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};
		// 注意初始化顺序，顺序颠倒可能会出现空指针。
		init();
		initBar();
		refreshDropMenu();
		initPostList();
		initArcMenu(arcMenu,ITEM_DRAWABLES);
		return rootView;
	}

	private void init() {
		mContext = getActivity();
		ThinkSNSApplication application = ThinkSNSApplication.getInstance();
		OAUTH_TOKEN = application.getOauth_token(mContext);
		OAUTH_TOKEN_SECRECT = application.getOauth_token_secret(mContext);
	}

	private void initPostList() {
		// 数据
		final Map<String, String> map = new HashMap<String, String>();
		map.put("app", APP);
		map.put("mod", MOD);
		map.put("act", ACT_GET_FOLLOWING_POST);
		map.put("oauth_token", OAUTH_TOKEN);
		map.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
		mPostList = new PostListFragment();
		mPostList.setContext(mContext, this);
		mPostList.setAttribute(map, true);
		mPostList.refresh();
		getChildFragmentManager().beginTransaction()
				.replace(R.id.weiba_post_list_layout, mPostList).commit();
	}

	public void refreshDropMenu(){
		final Map<String, String> map = new HashMap<String, String>();
		map.put("app", APP);
		map.put("mod", MOD);
		map.put("act", ACT_GET_WEIBA);
		map.put("oauth_token", OAUTH_TOKEN);
		map.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
		new Thread(new WeibaDataHelper(mHandler, mContext, map, GET_WEIBA_LIST,
				true)).start();
	}
	// 初始化下拉菜单列表
	private void initBar() {
		// UI
		popupView = mContext.getLayoutInflater().inflate(R.layout.weiba_drop,
				null);
		gridView = (GridView) popupView.findViewById(R.id.gridview);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				mPopupWindow.dismiss();
				// TODO Auto-generated method stub
				WeibaBean item = weibaDropAdapter.getItem(position);
				final Map<String, String> map = new HashMap<String, String>();
				map.put("app", APP);
				map.put("mod", MOD);
				map.put("act", ACT_GET_POSTS);
				map.put("id", item.getWeiba_id());
				map.put("oauth_token", OAUTH_TOKEN);
				map.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
				mPostList.setAttribute(map, false);
				mPostList.refresh();
			}

		});
		mPopupWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
				(Bitmap) null));
		drop_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshDropMenu();
				mPopupWindow.showAsDropDown(weiba_bar);
			}
		});

	}

	// 初始化星型菜单
	private void initArcMenu(ArcMenu menu, int[] itemDrawables) {
		final int itemCount = itemDrawables.length;
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(mContext);
			item.setImageResource(itemDrawables[i]);

			final int position = i;
			menu.addItem(item, new OnClickListener() {
				FragmentManager fm = ((FragmentActivity) mContext)
						.getSupportFragmentManager();

				@Override
				public void onClick(View v) {
					FragmentTransaction ft = fm.beginTransaction();
					switch (position) {
					case 0:
						ft.replace(R.id.content_frame, new CreatePostFragment());
						ft.hide(WeibaFragment.this);
						ft.addToBackStack("create_post_fragment");
						ft.commit();
						break;
					case 1:
						ft.replace(R.id.content_frame, new WeibaSearchFragment());
						ft.hide(WeibaFragment.this);
						ft.addToBackStack("weiba_search_fragment");
						ft.commit();
						break;
					case 2:
						ft.replace(R.id.content_frame, new PersonalCenterFragment());
						ft.hide(WeibaFragment.this);
						ft.addToBackStack("personal_center_fragment");
						ft.commit();
						break;
					}
				}
			});
		}
	}
	
	/*private void initArcMenu(SatelliteMenu menu ,int[] itemDrawables) {
		List<SatelliteMenuItem> items=new ArrayList<SatelliteMenuItem>();
		for(int i=0;i<itemDrawables.length;i++){		
			items.add(new SatelliteMenuItem(i, itemDrawables[i]));
		}
		menu.addItems(items);  
		menu.setOnItemClickedListener(new SateliteClickedListener() {
			FragmentManager fm = ((FragmentActivity) mContext)
					.getSupportFragmentManager();
			@Override
			public void eventOccured(int id) {
				// TODO Auto-generated method stub
				FragmentTransaction ft = fm.beginTransaction();
				switch (id) {
				case 0:
					ft.replace(R.id.content_frame, new CreatePostFragment());
					ft.hide(WeibaFragment.this);
					ft.addToBackStack("create_post_fragment");
					ft.commit();
					break;
				case 1:
					ft.replace(R.id.content_frame, new WeibaSearchFragment());
					ft.hide(WeibaFragment.this);
					ft.addToBackStack("weiba_search_fragment");
					ft.commit();
					break;
				case 2:
					ft.replace(R.id.content_frame, new PersonalCenterFragment());
					ft.hide(WeibaFragment.this);
					ft.addToBackStack("personal_center_fragment");
					ft.commit();
					break;
				}
				
			}
		});
		
	}*/

	private void test() {
		Log.w("TemppjActivity",
				"cont.getCacheDir() = " + mContext.getCacheDir());
		Log.w("TemppjActivity",
				"cont.getDatabasePath(\"tencent_analysisi\") = "
						+ mContext.getDatabasePath("tencent_analysisi"));
		Log.w("TemppjActivity",
				"cont.getFilesDir() = " + mContext.getFilesDir());
	}

}
