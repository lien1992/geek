package com.thinksns.jkfs.ui.fragment;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseFragment;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.ChannelBean;
import com.thinksns.jkfs.bean.WeiboAttachBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.constant.CacheConstant;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.constant.SettingsUtil;
import com.thinksns.jkfs.ui.MainFragmentActivity;
import com.thinksns.jkfs.ui.WeiboDetailActivity;
import com.thinksns.jkfs.ui.WeiboSearchActivity;
import com.thinksns.jkfs.ui.WriteWeiboActivity;
import com.thinksns.jkfs.ui.adapter.WeiboAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.ui.view.PullToRefreshListView.RefreshAndLoadMoreListener;
import com.thinksns.jkfs.util.DES;
import com.thinksns.jkfs.util.MD5;
import com.thinksns.jkfs.util.common.JSONUtils;
import com.thinksns.jkfs.util.common.PreferencesUtils;
import com.thinksns.jkfs.util.db.AccountOperator;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

/**
 * 初始化还是有点问题
 * 
 * @author zcc
 * 
 */
public class ChannelFragment extends Fragment {

	public static final String TAG = "zcc";
	// 获取微博时方法用的
	static private final int channel_DIF = 0;
	static private final int CHANNEL_SAME_REFRESH = 1;
	static private final int CHANNEL_SAME_ON_LOAD_MORE = 2;
	// handler用的
	static private final int GETTED_CHANNEL_LIST_WITHOUT_IMAGE = 0;
	static private final int CONNECT_WRONG = 1;
	static private final int DOWNLOAD_IMAGE_FINISHI = 2;
	static private final int GETTED_DIF_CHANNEL_WEIBO_LIST = 3;
	static private final int GETTED_REFRESH_CHANNEL_WEIBO_LIST = 4;
	static private final int GETTED_ON_LOAD_MORE_CHANNEL_WEIBO_LIST = 5;
	// thinksnsAPI用的
	static private final String APP = "api";
	static private final String MOD = "Channel";
	static private final String ACT_GET_ALL_CHANNEL = "get_all_channel";
	static private final String ACT_GET_WEIBO_BY_CHANNEL_ID = "get_channel_feed";
	static private String OAUTH_TOKEN;
	static private String OAUTH_TOKEN_SECRECT;
	// 菜单用的
	static private final int MENU_ITEM_HEIGHT = 80;
	static private final int MENU_ITEM_COUNT = 4;
	// 只加载一次
	static private ArrayList<ChannelBean> channelList;
	// 菜单的属性
	static private int MENU_HEIGHT = 0;
	static private int DOWNLOAD_IMAGE_COUNT = 0;
	// 是或否正在初始化，是的话置1，否则0
	static private int IS_INIT_CHANNEL_IMG = 0;

	private Activity mContext;
	private ImageView dropImage;
	private ImageView settingImg;
	private TextView titleName;

	private PullToRefreshListView mListView;
	private LayoutInflater mInflater;
	private String jsonData;
	private Handler handler;
	private ArrayList<WeiboBean> weiboList;
	private WeiboAdapter listViewAdapter;
	private String channelTitle = "官方发言";
	private String channel_category_id = "";
	private String weibo_max_id = "";
	private String weibo_since_id = "";

	private PopupWindow popupWindow;
	private View mPopupWindowView;

	/**
	 * 保存选中的频道信息
	 */
	private void saveTitle() {
		PreferencesUtils.putString(mContext, "channelTitle", channelTitle);
		PreferencesUtils.putString(mContext, "channel_category_id",
				channel_category_id);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.i(TAG, "onCreateView");

		final View view = inflater.inflate(R.layout.channel_fragment,
				container, false);

		dropImage = (ImageView) view
				.findViewById(R.id.channel_fragment_title_drop_img);
		settingImg = (ImageView) view
				.findViewById(R.id.channel_fragment_title_setting_img);
		titleName = (TextView) view
				.findViewById(R.id.channel_fragment_title_name);
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.channel_listview);
		mInflater = LayoutInflater.from(view.getContext());

		// 测试搜索++++++++++++++++++++++++++
		titleName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, WeiboSearchActivity.class);
				startActivity(intent);
			}
		});
		// 测试搜索++++++++++++++++++++++++++

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case GETTED_CHANNEL_LIST_WITHOUT_IMAGE:
					channelList = (ArrayList<ChannelBean>) msg.obj;
					// 初始化微博列表,""是默认id，这样代表没赋值过
					boolean isNull;
					if (isNull = "".equals(channel_category_id)) {
						initWeiboList(isNull);
					}
					// 下载图片
					downloadAllChannelImg();
					// 加载布局，这时候用的频道图片是默认图片
					initPopupWindow();
					Log.i(TAG, "执行了_____GETTED_CHANNEL_LIST_WITHOUT_IMAGE");
					dropImage.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (!popupWindow.isShowing()) {
								popupWindow.showAsDropDown(view
										.findViewById(R.id.channel_fragment_title));
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
					IS_INIT_CHANNEL_IMG = 0;
					break;
				case GETTED_DIF_CHANNEL_WEIBO_LIST:
					Log.i(TAG, "GETTED_DIF_CHANNEL_WEIBO_LIST");
					weiboList = (ArrayList<WeiboBean>) msg.obj;
					// 防止gson出问题，出问题时size=0
					if (weiboList.size() != 0) {
						listViewAdapter.update(weiboList);
						Log.i(TAG, weiboList.get(0).getUname());
						mListView.setSelection(0);
						// 显示下拉
						mListView.setVisibility(View.VISIBLE);
						// 保存标题，id
						saveTitle();
						// 标题
						Log.i(TAG, "即将更改的标题ahi" + channelTitle);
						titleName.setText(channelTitle);
					}
					break;
				case GETTED_REFRESH_CHANNEL_WEIBO_LIST:
					weiboList = (ArrayList<WeiboBean>) msg.obj;
					// 防止gson出问题，出问题时size=0
					if (weiboList.size() != 0) {
						Log.i(TAG, weiboList.size() + "微博个数验证");
						listViewAdapter.insertToHead(weiboList);
						mListView.setSelection(0);
						Log.i(TAG, "刷新完成 ");
						mListView.onRefreshComplete();
						Log.i(TAG, "下拉消失");
					} else {
						mListView.onRefreshComplete();
						Toast.makeText(mContext, "没有更多微博了亲:(",
								Toast.LENGTH_SHORT).show();
					}
					break;
				case GETTED_ON_LOAD_MORE_CHANNEL_WEIBO_LIST:
					weiboList = (ArrayList<WeiboBean>) msg.obj;
					// 防止gson出问题，出问题时size=0
					if (weiboList.size() != 0) {
						listViewAdapter.append(weiboList);
						mListView.onLoadMoreComplete();
					}
					break;
				}
			}
		};

		// 先初始化handler再初始化init
		init();
		// 菜单按钮默认
		setDropDefault();
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						WeiboDetailActivity.class);
				Log.d("listview item pos", position + "");
				intent.putExtra("weibo_detail", weiboList.get(position - 1));
				startActivity(intent);
			}

		});
		// 写微博长按
		// mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
		//
		// @Override
		// public boolean onItemLongClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(getActivity(),
		// WriteWeiboActivity.class);
		// startActivity(intent);
		// return true;
		// }
		//
		// });
		mListView.setListener(new RefreshAndLoadMoreListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				// 下拉刷新事件
				Log.i(TAG, channelTitle);
				getWeibosByChannelInThread(CHANNEL_SAME_REFRESH,
						weibo_since_id, "");

			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				// 加载更多
				getWeibosByChannelInThread(CHANNEL_SAME_ON_LOAD_MORE, "",
						weibo_max_id);
			}
		});
		settingImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				((MainFragmentActivity) getActivity()).getSlidingMenu()
						.toggle();
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
		listViewAdapter = new WeiboAdapter(mContext, mInflater, mListView);
		mListView.setAdapter(listViewAdapter);
		// 获取默认的频道信息
		getDefaultChannel();
		initWeiboList("".equals(channel_category_id));
		// 加载更多
		mListView.setLoadMoreEnable(true);
		// 在开始的时候初始化频道列表
		if (channelList == null) {
			getChannelsInThread();
		}
		// 检查缓存文件夹是否存在
		if (!isCacheFolderExist(CacheConstant.imageCachePath)) {
			createSDDir(CacheConstant.imageCachePath);
		}

	}

	/**
	 * 
	 * 获取用户设定的默认频道名称
	 */
	private void getDefaultChannel() {
		channelTitle = PreferencesUtils.getString(mContext, "channelTitle",
				channelTitle);
		channel_category_id = PreferencesUtils.getString(mContext,
				"channel_category_id", channel_category_id);
	}

	/**
	 * 初始化微博列表，默认官方发言,最好能让用户自己选择，存到本地，每次自动加载
	 * 
	 * @param isNull
	 *            本地加载失败，channel_category_id的值是 ""
	 */
	private void initWeiboList(boolean isNull) {
		Toast.makeText(mContext, "加载中，请稍后...", Toast.LENGTH_SHORT);
		// 初始化
		titleName.setText(channelTitle);
		if (isNull) {
			if (channelList != null) {
				Log.i(TAG, "本地加载失败");
				for (ChannelBean channel : channelList) {
					if (channelTitle.equals(channel.getTitle())) {
						channel_category_id = channel.getChannel_category_id();
					}
				}
			} else {
				return;
			}
		}
		Log.i(TAG, "初始化执行了");
		getWeibosByChannelInThread(channel_DIF, "", "");

	}

	/**
	 * 初始化popupwindow,中间会调用initPopupWindowLayout()来加载布局
	 */
	private void initPopupWindow() {

		mPopupWindowView = LayoutInflater.from(mContext).inflate(
				R.layout.channel_menu, null);
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
		int length = channelList.size();
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
				View view = inflater.inflate(R.layout.channel_menu_item, null);

				ImageView imageView = (ImageView) view
						.findViewById(R.id.channel_item_img);
				// 图片全下载完成后执行
				if (DOWNLOAD_IMAGE_COUNT == 0
						&& channelList.size() != 0
						&& channelList.get(position).getIcon_url() != null
						&& !"null".equals(channelList.get(position)
								.getIcon_url())) {
					String path = CacheConstant.imageCachePath + "/"
							+ channelList.get(position).getTitle() + ".png";
					Bitmap bitMap = BitmapFactory.decodeFile(path);
					imageView.setImageBitmap(bitMap);
				}
				TextView textView = (TextView) view
						.findViewById(R.id.channel_item_text);
				textView.setText(channelList.get(position).getTitle());

				tableRow.addView(view);

				ViewGroup.LayoutParams lp = view.getLayoutParams();
				lp.width = width / MENU_ITEM_COUNT;
				lp.height = MENU_ITEM_HEIGHT;
				view.setLayoutParams(lp);
				view.setFocusable(true);

				imageView.setLayoutParams(new RelativeLayout.LayoutParams(width
						/ MENU_ITEM_COUNT, MENU_ITEM_HEIGHT / 2));

				final int mPosition = position;
				// 菜单上每个频道对应的点击事件
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.i(TAG, "点击菜单了");
						// 点击频道的操作
						// 如果点击的频道不同，才刷新
						if (!titleName.getText().toString()
								.equals(channelList.get(mPosition).getTitle())) {
							channelTitle = channelList.get(mPosition)
									.getTitle();
							Log.i(TAG, channelTitle + "___点击的");
							channel_category_id = channelList.get(mPosition)
									.getChannel_category_id();
							getWeibosByChannelInThread(channel_DIF, "", "");
						}
						popupWindow.dismiss();
						// 菜单按钮不能再被点击，页面刷新完成后焦点true
						dropImage.setFocusable(false);
					}
				});
				// 最后再+1，否则上面view的点击事件会出错
				position += 1;

			}
			mytable.addView(tableRow, new LayoutParams(width, MENU_ITEM_HEIGHT));
			MENU_HEIGHT += MENU_ITEM_HEIGHT;

		}
		mytable.setFocusable(false);
	}

	// 下载全部频道图片
	private void downloadAllChannelImg() {
		for (ChannelBean channel : channelList) {
			String url = channel.getIcon_url();
			if (url != null && !"null".equals(url)) {
				DOWNLOAD_IMAGE_COUNT++;
				// 另起线程下载
				DownLoadChannelImageInThread(channel.getTitle(), url);
			}
		}
	}

	/**
	 * 线程中访问网络初始化频道列表
	 */
	private void getChannelsInThread() {
		new Thread() {
			public void run() {
				try {
					final Map<String, String> map = new HashMap<String, String>();
					map.put("app", APP);
					map.put("oauth_token", OAUTH_TOKEN);
					map.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
					map.put("mod", MOD);
					map.put("act", ACT_GET_ALL_CHANNEL);
					jsonData = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
					Log.i(TAG, jsonData);
					ArrayList<ChannelBean> channelList = JSONToChannels(jsonData);
					if (channelList.size() != 0) {
						handler.obtainMessage(
								ChannelFragment.GETTED_CHANNEL_LIST_WITHOUT_IMAGE,
								channelList).sendToTarget();
					}
				} catch (Exception e) {
					handler.obtainMessage(ChannelFragment.CONNECT_WRONG)
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
	ArrayList<ChannelBean> JSONToChannels(String jsonData) {
		ArrayList<ChannelBean> channelList = new ArrayList<ChannelBean>();
		try {
			JSONObject obj = new JSONObject(jsonData);
			Iterator<String> it = obj.keys();
			JSONObject item;

			while (it.hasNext()) {
				item = obj.getJSONObject(it.next());
				channelList.add(new ChannelBean(item
						.getString("channel_category_id"), item
						.getString("title"), item.getString("pid"), item
						.getString("sort"), item.getString("icon_url")));
			}
		} catch (JSONException e) {
			Log.i(TAG, "json出问题");
			handler.obtainMessage(CONNECT_WRONG).sendToTarget();
		}
		// 返回size==0说明错误
		return channelList;
	}

	// 下载频道图片
	private void DownLoadChannelImageInThread(final String name,
			final String imgUrl) {
		new Thread() {
			public void run() {
				try {
					String fileName = name + ".png";
					String path = CacheConstant.imageCachePath + "/" + fileName;
					// 如果文件已存在不继续
					if (!isCacheFolderExist(path)) {
						URL url = new URL(imgUrl);

						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						conn.setConnectTimeout(5000);
						// conn.setReadTimeout(50000);

						conn.setRequestMethod("GET");
						int code = conn.getResponseCode();
						if (code == 200) {
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
						}

					}
					DOWNLOAD_IMAGE_COUNT--;
					if (DOWNLOAD_IMAGE_COUNT == 0) {
						handler.obtainMessage(
								ChannelFragment.DOWNLOAD_IMAGE_FINISHI)
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
	 * 获取微博列表
	 * 
	 * @param category_id
	 *            channel的id
	 * @param ifchannelSameOfDif
	 *            channel是否是一样的。channel_DIF,channel_REFRESH,channel_ON_LOAD_MORE
	 * @param since_id
	 *            下拉刷新用,默认是空字符串""
	 * @param max_id
	 *            加载更多用，默认是空字符串""
	 * 
	 */
	private void getWeibosByChannelInThread(final int ifchannelSameOfDif,
			final String since_id, final String max_id) {

		Log.i(TAG, "获取微博中chaneltitle" + channelTitle);
		new Thread() {
			public void run() {
				final Map<String, String> map = new HashMap<String, String>();
				map.put("app", APP);
				map.put("oauth_token", OAUTH_TOKEN);
				map.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
				map.put("mod", MOD);
				map.put("act", ACT_GET_WEIBO_BY_CHANNEL_ID);
				map.put("category_id", channel_category_id);
				map.put("count", "20");
				if (!"".equals(max_id)) {
					map.put("max_id", max_id);
				}
				if (!"".equals(since_id)) {
					map.put("since_id", since_id);
				}
				Log.i(TAG, "获取微博");
				jsonData = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Get, HttpConstant.THINKSNS_URL, map);

				// 将json转化成bean列表，handler出去
				if (ifchannelSameOfDif == channel_DIF) {
					Log.i(TAG, "在这开始向handler传消息");

					handler.obtainMessage(
							ChannelFragment.GETTED_DIF_CHANNEL_WEIBO_LIST,
							JSONToWeibos(jsonData)).sendToTarget();
				} else if (ifchannelSameOfDif == CHANNEL_SAME_REFRESH) {
					Log.i(TAG, "同频道刷新");
					handler.obtainMessage(
							ChannelFragment.GETTED_REFRESH_CHANNEL_WEIBO_LIST,
							JSONToWeibos(jsonData)).sendToTarget();
				} else if (ifchannelSameOfDif == CHANNEL_SAME_ON_LOAD_MORE) {
					handler.obtainMessage(
							ChannelFragment.GETTED_ON_LOAD_MORE_CHANNEL_WEIBO_LIST,
							JSONToWeibos(jsonData)).sendToTarget();
				}

			};
		}.start();
	}

	private ArrayList<WeiboBean> JSONToWeibos(String jsonData) {

		ArrayList<WeiboBean> list = new ArrayList<WeiboBean>();
		try {
			Type listType = new TypeToken<ArrayList<WeiboBean>>() {
			}.getType();
			list = new Gson().fromJson(jsonData, listType);

			Log.i(TAG, "微博个数" + list.size());
			if (list.size() != 0) {
				Log.i(TAG, list.get(0).getUname());
				weibo_max_id = list.get(list.size() - 1).getId();
				weibo_since_id = list.get(0).getId();
			}
		} catch (JsonSyntaxException e) {
			Log.i(TAG, "json微博出问题");
			handler.obtainMessage(CONNECT_WRONG).sendToTarget();
		}
		Log.i(TAG, "微博个数" + list.size() + "yanzhwng");
		return list;
	}

	private void setDropDefault() {
		dropImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "获取列表中请稍后", Toast.LENGTH_SHORT).show();
				if (IS_INIT_CHANNEL_IMG == 0) {
					IS_INIT_CHANNEL_IMG = 1;
					if (channelList != null && channelList.size() != 0) {

						handler.obtainMessage(
								ChannelFragment.GETTED_CHANNEL_LIST_WITHOUT_IMAGE,
								channelList).sendToTarget();
					} else {
						Log.i(TAG, "图片没有而且微博列表是空的，具体的还没这种情况，还没写╮(╯_╰)╭");
					}
				}
			}
		});
	}
}
