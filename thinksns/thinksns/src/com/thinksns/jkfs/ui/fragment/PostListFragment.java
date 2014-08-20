package com.thinksns.jkfs.ui.fragment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.R.interpolator;
import android.content.Context;
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
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.PostBean;
import com.thinksns.jkfs.ui.adapter.PostListAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.ui.view.PullToRefreshListView.RefreshAndLoadMoreListener;
import com.thinksns.jkfs.util.WeibaActionHelper;
import com.thinksns.jkfs.util.WeibaBaseHelper;
import com.thinksns.jkfs.util.WeibaPostHelper;
import com.thinksns.jkfs.util.db.PostOperator;

/**
 * @author 杨智勇
 * @since 2014-5-29
 */
public class PostListFragment extends Fragment implements
		RefreshAndLoadMoreListener {
	// Handler信息
	public static final int GET_POST_LIST = 0;
	public static final int APPEND_POST_LIST = 1;
	public static final int ADD_POST_LIST = 2;
	public static final int ACT_COLLECT = 3;
	public static final int ACT_CANCEL = 4;
	public static final int ACT_COLLECT_OK = 5;
	public static final int ACT_CANCEL_OK = 6;

	private static final String TAG = "PostListFragment";
	private static final String APP = "api";
	private static final String MOD = "Weiba";
	private static final String POST_FAVORITE = "post_favorite";
	private static final String POST_UNFAVORITE = "post_unfavorite";
	private static String OAUTH_TOKEN;
	private static String OAUTH_TOKEN_SECRECT;
	// 常量
	public static final int defaultCount = 20;
	// 变量
	private int postTableFlag;

	// 组件
	private PullToRefreshListView mListView;
	private ImageView loadImage;
	private PostListAdapter postListAdapter;
	private Handler mHandler;
	private Context mContext;
	private FragmentManager fm;
	private Fragment parentView;
	private boolean cacheFlag;
	private Map<String, String> map;

	// 资源
	private LinkedList<PostBean> postList;
	private String[] operatStatus;

	public PostListFragment() {
		super();
		// TODO Auto-generated constructor stub

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case GET_POST_LIST:// 加载帖子列表
					loadImage.setAnimation(null);
					loadImage.setVisibility(View.GONE);
					postList = (LinkedList<PostBean>) msg.obj;
					Log.w("l列表大小", postList.size() + "/" + postList.toString()
							+ "/" + map.toString());
					if (postList.size() == 0) {
						String info = "";
						String act = map.get("act");
						if (act.equals("following_posts")) {
							info = "你还没有关注任何微吧，快去看看吧！";
						} else if (act.equals("search_post")) {
							info = "未搜素到任何帖子，换换关键词试试！";
						}
						Toast.makeText(mContext, info, Toast.LENGTH_SHORT)
								.show();
					}

					if (postList.size() < 20) {
						mListView.setLoadMoreEnable(false);
					}
					{
						boolean pauseOnScroll = false;
						boolean pauseOnFling = true;
						ImageLoader imageLoader = ImageLoader.getInstance();
						PauseOnScrollListener listener = new PauseOnScrollListener(
								imageLoader, pauseOnScroll, pauseOnFling);
						postListAdapter = new PostListAdapter(mContext,
								postList, imageLoader, mHandler);
						mListView.setAdapter(postListAdapter);
						mListView.setOnScrollListener(listener);
					}
					break;

				case APPEND_POST_LIST:// 加载更多
					mListView.onLoadMoreComplete();
					LinkedList<PostBean> appendList = (LinkedList<PostBean>) msg.obj;
					if (postListAdapter == null) {
						postList = appendList;
						boolean pauseOnScroll = false;
						boolean pauseOnFling = true;
						ImageLoader imageLoader = ImageLoader.getInstance();
						PauseOnScrollListener listener = new PauseOnScrollListener(
								imageLoader, pauseOnScroll, pauseOnFling);
						postListAdapter = new PostListAdapter(mContext,
								postList, imageLoader, mHandler);
						mListView.setAdapter(postListAdapter);
						mListView.setOnScrollListener(listener);
					} else {
						int count = append(appendList);
						Toast.makeText(mContext, "加载" + count + "条帖子",
								Toast.LENGTH_SHORT).show();
						postListAdapter.notifyDataSetChanged();
					}
					break;
				case ADD_POST_LIST:// 刷新帖子列表
					mListView.onRefreshComplete();
					LinkedList<PostBean> addList = (LinkedList<PostBean>) msg.obj;
					if ((postList.size() + addList.size()) > 20) {
						mListView.setLoadMoreEnable(true);
					}
					if (postListAdapter == null) {
						postList = addList;
						boolean pauseOnScroll = false;
						boolean pauseOnFling = true;
						ImageLoader imageLoader = ImageLoader.getInstance();
						PauseOnScrollListener listener = new PauseOnScrollListener(
								imageLoader, pauseOnScroll, pauseOnFling);
						postListAdapter = new PostListAdapter(mContext,
								postList, imageLoader, mHandler);
						mListView.setAdapter(postListAdapter);
						mListView.setOnScrollListener(listener);
					} else {
						int count = insertToHead(addList);
						Toast.makeText(mContext, "加载" + count + "条帖子",
								Toast.LENGTH_SHORT).show();
						postListAdapter.notifyDataSetChanged();
					}
					break;
				case ACT_COLLECT:// 收藏操作
					final Map<String, String> collect = new HashMap<String, String>();
					collect.put("app", APP);
					collect.put("mod", MOD);
					collect.put("act", POST_FAVORITE);
					collect.put("id", (String) msg.obj);
					collect.put("oauth_token", OAUTH_TOKEN);
					collect.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
					new Thread(new WeibaActionHelper(mHandler, mContext,
							collect, ACT_COLLECT_OK)).start();
					Log.w("关注开始", collect.toString());
					break;
				case ACT_CANCEL:// 取消收藏操作
					final Map<String, String> cancel = new HashMap<String, String>();
					cancel.put("app", APP);
					cancel.put("mod", MOD);
					cancel.put("act", POST_UNFAVORITE);
					cancel.put("id", (String) msg.obj);
					cancel.put("oauth_token", OAUTH_TOKEN);
					cancel.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
					new Thread(new WeibaActionHelper(mHandler, mContext,
							cancel, ACT_CANCEL_OK)).start();
					break;
				case ACT_COLLECT_OK:// 收藏成功
					operatStatus = (String[]) msg.obj;
					Log.w("关注结束", operatStatus[0] + "/" + operatStatus[1]);
					if ("1".equals(operatStatus[0])) {
						Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT)
								.show();
						PostOperator.getInstance(postTableFlag)
								.updateFavoriteState(operatStatus[1], 1);
					} else {
						Toast.makeText(mContext, "收藏失败", Toast.LENGTH_SHORT)
								.show();
					}
					break;
				case ACT_CANCEL_OK:// 取消收藏成功
					operatStatus = (String[]) msg.obj;
					if ("1".equals(operatStatus[0])) {
						Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_SHORT)
								.show();
						PostOperator.getInstance(postTableFlag)
								.updateFavoriteState(operatStatus[1], 0);
					} else {
						Toast.makeText(mContext, "取消收藏失败", Toast.LENGTH_SHORT)
								.show();
					}
					break;
				case WeibaBaseHelper.DATA_ERROR:// 数据加载出错
					Toast.makeText(mContext, "数据加载失败", Toast.LENGTH_SHORT)
							.show();
					break;
				case WeibaBaseHelper.NET_ERROR:// 网络故障
					Toast.makeText(mContext, "网络故障", Toast.LENGTH_SHORT).show();
					break;
				}

			}

		};
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.post_list_fragment_layout,
				container, false);
		mListView = (PullToRefreshListView) rootView
				.findViewById(R.id.weiba_post_list);
		loadImage = (ImageView) rootView.findViewById(R.id.load_img);
		mListView.setSoundEffectsEnabled(true);
		mListView.setLoadMoreEnable(true);
		mListView.setListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				PostBean post = (PostBean) mListView.getItemAtPosition(arg2);
				PostDetailFragment pf = new PostDetailFragment();
				pf.setPost(post);
				pf.setHandler(mHandler);
				FragmentTransaction ft = fm.beginTransaction();
				ft.add(R.id.content_frame, pf);
				ft.hide(parentView);
				ft.addToBackStack(null);
				ft.commit();
			}
		});
		RotateAnimation rotateAnimation = new RotateAnimation(0.0f, +360.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		rotateAnimation.setInterpolator(new LinearInterpolator());
		rotateAnimation.setDuration(500);
		loadImage.startAnimation(rotateAnimation);
		return rootView;
	}

	public void setContext(Context mContext, Fragment parentView) {
		this.mContext = mContext;
		this.parentView = parentView;
		fm = ((FragmentActivity) mContext).getSupportFragmentManager();
		ThinkSNSApplication application = ThinkSNSApplication.getInstance();
		OAUTH_TOKEN = application.getOauth_token(mContext);
		OAUTH_TOKEN_SECRECT = application.getOauth_token_secret(mContext);
	}

	public void setAttribute(Map<String, String> map, boolean cacheFlag) {
		this.map = map;
		this.cacheFlag = cacheFlag;
	}

	public void refresh() {
		getTableNum();
		new Thread(new WeibaPostHelper(mHandler, mContext, map, GET_POST_LIST,
				cacheFlag, postTableFlag)).start();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Thread(new WeibaPostHelper(mHandler, mContext, map, ADD_POST_LIST,
				cacheFlag, postTableFlag)).start();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		final Map<String, String> append = new HashMap<String, String>(map);
		int page = postList.size() / defaultCount + 1;
		Toast.makeText(mContext, "加载更多", Toast.LENGTH_SHORT).show();
		append.put("page", Integer.toString(page));
		new Thread(new WeibaPostHelper(mHandler, mContext, append,
				APPEND_POST_LIST, cacheFlag, postTableFlag)).start();

	}

	/**
	 * 在postList后添加，返回值为新添加的元素数目；
	 * 
	 * @param list：需要添加的对象
	 */
	public int append(List<PostBean> list) {
		if (list == null) {
			return 0;
		}
		int index = 0;//index：从list的第几项开始往后添加
		int length = list.size();
		if (postList.size() == 0) {
			index = length;
		} else {
			Iterator<PostBean> it = list.iterator();
			PostBean last = postList.getLast();

			while (it.hasNext()) {
				PostBean temp = it.next();
				if (!temp.getPost_id().equals(last.getPost_id())) {
					index++;
				} else {
					break;
				}
			}
		}
		Log.w("向后添加", "索引是" + index);
		if (index == (length - 1)) {//此处分三种情况：1.中部相交；2.尾部相交；3.不相交
			return 0;
		} else if (index == length) {
			postList.addAll(list);
			return length;
		} else {
			postList.addAll(list.subList(index + 1, length - 1));
			return length - index - 1;
		}

	}

	/**
	 * 在postList前添加，返回值为新添加的元素数目
	 * 
	 * @param list：需要添加的对象
	 */
	public int insertToHead(List<PostBean> list) {
		if (list == null) {
			return 0;
		}
		int index = 0;
		if (postList.size() == 0) {
			index = list.size();
		} else {
			Iterator<PostBean> it = list.iterator();
			PostBean first = postList.getFirst();
			while (it.hasNext()) {
				PostBean temp = it.next();
				if (!temp.getPost_id().equals(first.getPost_id())) {
					index++;
				} else {
					break;
				}
			}
		}
		Log.w("向前添加", "索引是" + index);
		if (index == 0) {//此处分两种情况：1.头部相交，即代表完全相同；2.其他；
			return 0;
		} else {
			for (int i = index - 1; i >= 0; i--) {
				postList.addFirst(list.get(i));
			}
			return index;
		}
	}

	/**
	 * 清空postList；
	 * 
	 */
	public void clear() {
		postList.clear();
	}

	/**
	 * PostListFragment在帖子列表、我的个人中心、帖子搜索中都被重用，在帖子列表和我的个人中心均进行缓存，不同的项目下各建立了一个对应的表
	 * ，通过该方法获取相应表的序号；
	 * 
	 */
	public void getTableNum() {
		String act = map.get("act");
		Log.w("表序号前", act + "/" + postTableFlag);
		if (act.equals("following_posts")) {
			this.postTableFlag = 0;
		} else if (act.equals("posteds")) {
			this.postTableFlag = 1;
		} else if (act.equals("commenteds")) {
			this.postTableFlag = 2;
		} else if (act.equals("favorite_list")) {
			this.postTableFlag = 3;
		}
		Log.w("表序号后", act + "/" + postTableFlag);
	}
}
