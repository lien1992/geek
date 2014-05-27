package com.thinksns.jkfs.ui.fragment;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseFragment;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.ChanelBean;
import com.thinksns.jkfs.bean.WeiboAttachBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.constant.CacheConstant;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.constant.SettingsUtil;
import com.thinksns.jkfs.ui.MainFragmentActivity;
import com.thinksns.jkfs.ui.adapter.ChanelFragmentListViewAdapter;
import com.thinksns.jkfs.ui.adapter.WeiboAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.ui.view.PullToRefreshListView.RefreshAndLoadMoreListener;
import com.thinksns.jkfs.util.DES;
import com.thinksns.jkfs.util.MD5;
import com.thinksns.jkfs.util.common.JSONUtils;
import com.thinksns.jkfs.util.db.AccountOperator;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

public class ChanelFragment extends Fragment {

    public static final String TAG = "ChanelFragment";
	// 获取微博时方法用的
	static private final int CHANEL_DIF = 0;
	static private final int CHANEL_SAME_REFRESH = 1;
	static private final int CHANEL_SAME_ON_LOAD_MORE = 2;
	// handler用的
	static private final int GETTED_CHANEL_LIST_WITHOUT_IMAGE = 0;
	static private final int CONNECT_WRONG = 1;
	static private final int DOWNLOAD_IMAGE_FINISHI = 2;
	static private final int GETTED_DIF_CHANEL_WEIBO_LIST = 3;
	static private final int GETTED_REFRESH_CHANEL_WEIBO_LIST = 4;
	static private final int GETTED_ON_LOAD_MORE_CHANEL_WEIBO_LIST = 5;
	// thinksnsAPI用的
	static private final String APP = "api";
	static private final String MOD = "Channel";
	static private final String ACT_GET_ALL_CHANEL = "get_all_channel";
	static private final String ACT_GET_WEIBO_BY_CHANEL_ID = "get_channel_feed";
	static private String OAUTH_TOKEN;
	static private String OAUTH_TOKEN_SECRECT;
	// 菜单用的
	static private final int MENU_ITEM_HEIGHT = 80;
	static private final int MENU_ITEM_COUNT = 4;
	// 只加载一次
	static private ArrayList<ChanelBean> chanelList;
	// 菜单的属性
	static private int MENU_HEIGHT = 0;
	static private int DOWNLOAD_IMAGE_COUNT = 0;
	// 是或否正在初始化，是的话置1，否则0
	static private int IS_INIT_CHANEL_IMG = 0;

	private Activity mContext;
	private ImageView dropImage;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		final View view = inflater.inflate(R.layout.chanel_fragment, container,
				false);
		// 测试用button
		dropImage = (ImageView) view
				.findViewById(R.id.chanel_fragment_title_drop_img);
		// 默认是
		titleName = (TextView) view
				.findViewById(R.id.chanel_fragment_title_name);
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.chanel_listview);
		mInflater = LayoutInflater.from(view.getContext());

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case GETTED_CHANEL_LIST_WITHOUT_IMAGE:
					chanelList = (ArrayList<ChanelBean>) msg.obj;
					// 下载图片
					downloadAllChanelImg();
					// 加载布局，这时候用的频道图片是默认图片
					initPopupWindow();
					Log.i(TAG, "执行了_____GETTED_CHANEL_LIST_WITHOUT_IMAGE");
					dropImage.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Log.i(TAG, "菜单显示没" + popupWindow.isShowing());
							if (!popupWindow.isShowing()) {
								Log.i(TAG, "菜单显示没" + popupWindow.isShowing());
								popupWindow.showAsDropDown(view
										.findViewById(R.id.chanel_fragment_title));
								Log.i(TAG, "嗯。。我是萌萌的菜单。。我显示了");

							} else {
								popupWindow.dismiss();
								Log.i(TAG, "菜单消失啦");
							}
						}
					});

					break;
				case CONNECT_WRONG:
					Toast.makeText(mContext, "网络故障", Toast.LENGTH_SHORT).show();
					break;
				case DOWNLOAD_IMAGE_FINISHI:
					// 下载完频道图片了开始布局了，重新布局，把频道图片加上去
					initPopupWindow();
					// 初始化频道图片完成
					IS_INIT_CHANEL_IMG = 0;
					break;
				case GETTED_DIF_CHANEL_WEIBO_LIST:
					weiboList = (ArrayList<WeiboBean>) msg.obj;
					listViewAdapter.update(weiboList);
					break;
				case GETTED_REFRESH_CHANEL_WEIBO_LIST:
					weiboList = (ArrayList<WeiboBean>) msg.obj;
					listViewAdapter.insertToHead(weiboList);
					mListView.setSelection(0);
					break;
				case GETTED_ON_LOAD_MORE_CHANEL_WEIBO_LIST:
					weiboList = (ArrayList<WeiboBean>) msg.obj;
					listViewAdapter.append(weiboList);
					break;
				}
			}
		};

		// 先初始化handler再初始化init
		init();
		// 菜单按钮
		dropImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "获取列表中请稍后", Toast.LENGTH_SHORT).show();
				if (IS_INIT_CHANEL_IMG == 0) {
					IS_INIT_CHANEL_IMG = 1;
					if (chanelList != null || chanelList.size() != 0) {

						handler.obtainMessage(
								ChanelFragment.GETTED_CHANEL_LIST_WITHOUT_IMAGE,
								chanelList).sendToTarget();
					} else {
						Log.i(TAG, "图片没有而且微博列表是空的，具体的还没这种情况，还没写╮(╯_╰)╭");
					}
				}
			}
		});

		mListView.setListener(new RefreshAndLoadMoreListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				// 下拉刷新事件
				String chanelTitle = titleName.getText().toString();
				for (ChanelBean chanel : chanelList) {
					if (chanelTitle.equals(chanel.getTitle())) {
						getWeibosByChanel(chanel.getChannel_category_id(),
								CHANEL_SAME_REFRESH);
					}
				}
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				// 加载更多
				String chanelTitle = titleName.getText().toString();
				for (ChanelBean chanel : chanelList) {
					if (chanelTitle.equals(chanel.getTitle())) {
						getWeibosByChanel(chanel.getChannel_category_id(),
								CHANEL_SAME_ON_LOAD_MORE);
					}
				}
			}
		});

		return view;
	}

	/**
	 * 初始化操作
	 */
	private void init() {
		mContext = getActivity();
		ThinkSNSApplication application = ThinkSNSApplication.getInstance();
		OAUTH_TOKEN = application.getOauth_token(mContext);
		OAUTH_TOKEN_SECRECT = application.getOauth_token_secret(mContext);
		// 在开始的时候初始化频道列表
		if (chanelList == null) {
			getChanelsInThread();
		}
		// 检查缓存文件夹是否存在
		if (!isCacheFolderExist(CacheConstant.imageCachePath)) {
			createSDDir(CacheConstant.imageCachePath);
		}
		// 初始化微博列表
		initWeiboList();
	}

	private void initWeiboList() {

		String defaultChanelTitle = "官方发言";
		getWeibosByChanel(defaultChanelTitle, CHANEL_DIF);
		titleName.setText(defaultChanelTitle);

		listViewAdapter = new WeiboAdapter(mContext, mInflater, mListView);
		mListView.setAdapter(listViewAdapter);
	}

	/**
	 * 初始化popupwindow,中间会调用initPopupWindowLayout()来加载布局
	 */
	private void initPopupWindow() {

		mPopupWindowView = LayoutInflater.from(mContext).inflate(
				R.layout.chanel_menu, null);
		initPopupWindowLayout();

		// 初始化popupwindow，绑定显示view，设置该view的宽度/高度
		if (popupWindow == null) {
			popupWindow = new PopupWindow(mPopupWindowView,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景；使用该方法点击窗体之外，才可关闭窗体
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// Background不能设置为null，dismiss会失效
		// popupWindow.setBackgroundDrawable(null);
		popupWindow.update();
		// popupWindow调用dismiss时触发，设置了setOutsideTouchable(true)，点击view之外/按键back的地方也会触发
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				Log.i(TAG, "菜单消失了");
			}
		});
	}

	/**
	 * 对menu添加布局
	 */
	private void initPopupWindowLayout() {
		TableLayout mytable = (TableLayout) mPopupWindowView
				.findViewById(R.id.tableLayout);
		int length = chanelList.size();
		int rows = length / MENU_ITEM_COUNT + 1;
		int position = 0;
		// 屏幕宽度
		Display display = mContext.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		for (int row = 0; row < rows; row++) {
			TableRow tableRow = new TableRow(mContext);

			for (int column = 0; column < MENU_ITEM_COUNT; column++) {
				if (position >= length) {
					break;
				}

				LayoutInflater inflater = mContext.getLayoutInflater();
				View view = inflater.inflate(R.layout.chanel_menu_item, null);

				ImageView imageView = (ImageView) view
						.findViewById(R.id.chanel_item_img);
				// 图片全下载完成后执行
				if (DOWNLOAD_IMAGE_COUNT == 0
						&& chanelList.size() != 0
						&& chanelList.get(position).getIcon_url() != null
						&& !"null".equals(chanelList.get(position)
								.getIcon_url())) {
					String path = CacheConstant.imageCachePath + "/"
							+ chanelList.get(position).getTitle() + ".png";
					Bitmap bitMap = BitmapFactory.decodeFile(path);
					imageView.setImageBitmap(bitMap);
				}
				TextView textView = (TextView) view
						.findViewById(R.id.chanel_item_text);
				textView.setText(chanelList.get(position).getTitle());

				position += 1;

				tableRow.addView(view);

				ViewGroup.LayoutParams lp = view.getLayoutParams();
				lp.width = width / MENU_ITEM_COUNT;
				lp.height = MENU_ITEM_HEIGHT;
				view.setLayoutParams(lp);
				view.setFocusable(true);

				imageView.setLayoutParams(new RelativeLayout.LayoutParams(width
						/ MENU_ITEM_COUNT, MENU_ITEM_HEIGHT / 2));
				final int mPosition = position;
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 点击频道的操作
						// 如果点击的频道相同，没反应,不同则刷新
						if (titleName.getText().toString()
								.equals(chanelList.get(mPosition).getTitle())) {
							getWeibosByChanel(chanelList.get(mPosition)
									.getChannel_category_id(), CHANEL_DIF);
						}
						popupWindow.dismiss();
						// 菜单按钮不能再被点击，页面刷新完成后焦点true
						dropImage.setFocusable(false);
					}
				});
			}
			mytable.addView(tableRow, new LayoutParams(width, MENU_ITEM_HEIGHT));
			MENU_HEIGHT += MENU_ITEM_HEIGHT;

		}
		mytable.setFocusable(false);
	}

	// 下载全部频道图片
	private void downloadAllChanelImg() {
		for (ChanelBean chanel : chanelList) {
			String url = chanel.getIcon_url();
			if (url != null && !"null".equals(url)) {
				DOWNLOAD_IMAGE_COUNT++;
				Log.i(TAG, "查找json的时候准备开始下载");
				// 另起线程下载
				DownLoadChanelImageInThread(chanel.getTitle(), url);
			}
		}
	}

	/**
	 * 线程中访问网络初始化频道列表
	 */
	private void getChanelsInThread() {
		new Thread() {
			public void run() {
				// final Map<String, String> map = new HashMap<String,
				// String>();
				// map.put("app", APP);
				// map.put("oauth_token", OAUTH_TOKEN);
				// map.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
				// map.put("mod", MOD);
				// map.put("act", ACT_GET_ALL_CHANEL);
				// jsonData = HttpUtility.getInstance().executeNormalTask(
				// HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
				// Log.i(TAG, jsonData);
				// handler.obtainMessage(
				// ChanelFragment.GETTED_CHANEL_LIST_WITHOUT_IMAGE,
				// JSONToChanels(jsonData)).sendToTarget();
				try {
					final Map<String, String> map = new HashMap<String, String>();
					map.put("app", APP);
					map.put("oauth_token", OAUTH_TOKEN);
					map.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
					map.put("mod", MOD);
					map.put("act", ACT_GET_ALL_CHANEL);
					jsonData = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Log.i(TAG, jsonData);
					handler.obtainMessage(
							ChanelFragment.GETTED_CHANEL_LIST_WITHOUT_IMAGE,
							JSONToChanels(jsonData)).sendToTarget();
				} catch (Exception e) {
					handler.obtainMessage(ChanelFragment.CONNECT_WRONG)
							.sendToTarget();
				}
			};
		}.start();
	}

	/**
	 * @param JSONData
	 *            需要转换的json格式的String
	 * @return 数组size为0时，出错了
	 */
	ArrayList<ChanelBean> JSONToChanels(String jsonData) {
		ArrayList<ChanelBean> chanelList = new ArrayList<ChanelBean>();
		try {
			JSONObject obj = new JSONObject(jsonData);
			Iterator<String> it = obj.keys();
			JSONObject item;

			while (it.hasNext()) {
				item = obj.getJSONObject(it.next());
				chanelList.add(new ChanelBean(item
						.getString("channel_category_id"), item
						.getString("title"), item.getString("pid"), item
						.getString("sort"), item.getString("icon_url")));
			}
		} catch (JSONException e) {
			Log.i(TAG, "json出问题");
			handler.obtainMessage(CONNECT_WRONG).sendToTarget();
		}
		// 返回size==0说明错误
		return chanelList;
	}

	// 下载频道图片
	private void DownLoadChanelImageInThread(final String name,
			final String imgUrl) {
		new Thread() {
			public void run() {
				try {
					String fileName = name + ".png";
					String path = CacheConstant.imageCachePath + "/" + fileName;
					// 如果文件已存在不继续
					if (!isCacheFolderExist(path)) {
						Log.i(TAG, "准备下");
						Log.i(TAG, imgUrl);

						URL url = new URL(imgUrl);

						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						Log.i(TAG, "获得了连接");

						conn.setConnectTimeout(5000);
						// conn.setReadTimeout(50000);

						conn.setRequestMethod("GET");
						int code = conn.getResponseCode();
						Log.i(TAG, "200 快到了了");

						if (code == 200) {
							System.out.println("正确获得资源连接");
							Log.i(TAG, "200 着了");

							// 文件大小
							int fileLength = conn.getContentLength();
							System.out.println("fileLength:" + fileLength);

							// 创建文件
							RandomAccessFile raf;

							raf = new RandomAccessFile(path, "rwd");

							// 与原文件大小相同的的文件
							raf.setLength(fileLength);

							InputStream is = conn.getInputStream();

							int length = 0;
							byte[] buffer = new byte[1024];
							while ((length = is.read(buffer)) != -1) {
								raf.write(buffer, 0, length);
							}
							raf.close();
							Log.i(TAG, "下完一个");
						}

					}
					DOWNLOAD_IMAGE_COUNT--;
					if (DOWNLOAD_IMAGE_COUNT == 0) {
						handler.obtainMessage(
								ChanelFragment.DOWNLOAD_IMAGE_FINISHI)
								.sendToTarget();
						Log.i(TAG, "全下完");

					}
				} catch (Exception e) {

					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	// 建文件
	private void createSDDir(String path) {
		File dir = new File(path);
		dir.mkdir();
	}

	// 文件夹是否存在
	private boolean isCacheFolderExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 
	 * 获取微博列表，还没分页处理
	 * 
	 * @param category_id
	 *            chanel的id
	 * @param ifChanelSameOfDif
	 *            chanel是否是一样的。CHANEL_DIF,CHANEL_REFRESH,CHANEL_ON_LOAD_MORE
	 * 
	 */
	private void getWeibosByChanel(final String category_id,
			final int ifChanelSameOfDif) {
		new Thread() {
			public void run() {
				final Map<String, String> map = new HashMap<String, String>();
				map.put("app", APP);
				map.put("oauth_token", OAUTH_TOKEN);
				map.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
				map.put("mod", MOD);
				map.put("act", ACT_GET_WEIBO_BY_CHANEL_ID);
				map.put("category_id", category_id);

				jsonData = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
				Log.i(TAG, "这是微博______" + jsonData);
				// 将json转化成bean列表，handler出去
				if (ifChanelSameOfDif == CHANEL_DIF) {
					handler.obtainMessage(
							ChanelFragment.GETTED_DIF_CHANEL_WEIBO_LIST,
							JSONToWeibos(jsonData)).sendToTarget();
				} else if (ifChanelSameOfDif == CHANEL_SAME_REFRESH) {
					handler.obtainMessage(
							ChanelFragment.GETTED_REFRESH_CHANEL_WEIBO_LIST,
							JSONToWeibos(jsonData)).sendToTarget();
				} else if (ifChanelSameOfDif == CHANEL_SAME_ON_LOAD_MORE) {
					handler.obtainMessage(
							ChanelFragment.GETTED_ON_LOAD_MORE_CHANEL_WEIBO_LIST,
							JSONToWeibos(jsonData)).sendToTarget();
				}

			};
		}.start();
	}

	private ArrayList<WeiboBean> JSONToWeibos(String jsonData) {
		try {
			ArrayList<WeiboBean> list = JSONUtils.JSONToWeibos(jsonData);
			Log.i(TAG, "微博个数" + list.size());
			Log.i(TAG, list.get(0).getUname());

			return list;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<WeiboBean>();
	}

}
