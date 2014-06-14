package com.thinksns.jkfs.ui.fragment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.WeibaBean;
import com.thinksns.jkfs.ui.adapter.WeibaSearchListAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.ui.view.PullToRefreshListView.RefreshAndLoadMoreListener;
import com.thinksns.jkfs.util.WeibaActionHelper;
import com.thinksns.jkfs.util.WeibaBaseHelper;
import com.thinksns.jkfs.util.WeibaDataHelper;
import com.thinksns.jkfs.util.db.WeibaOperator;

/**
 * @author 杨智勇
 * @since 2014-5-29
 */
public class WeibaSearchListFragment extends Fragment implements
		RefreshAndLoadMoreListener {
	// 常量
	public static final int SEARCH_WEIBA=0;
	public static final int ADD_WEIBA=1;
	public static final int APPEND_WEIBA=2;
	public static final int ACT_CREATE = 3;
	public static final int ACT_DESTROY = 4;
	public static final int ACT_CREATE_OK = 5;
	public static final int ACT_DESTROY_OK = 6;
	
	public static final int defaultCount = 20;
	
	// 参数
	private static final String TAG = "WeibaSearchListFragment";
	private static final String APP = "api";
	private static final String MOD = "Weiba";
	private static final String CREATE = "create";
	private static final String DESTROY = "destroy";
	private static String OAUTH_TOKEN;
	private static String OAUTH_TOKEN_SECRECT;

	// 组件
	private PullToRefreshListView weibaSearchListView;
	private WeibaSearchListAdapter weibaSearchListAdapter;
	private Handler mHandler;
	private Context mContext;
	private Map<String, String> map;
	private Boolean cacheFlag;

	// 数据
	private LinkedList<WeibaBean> weibaSearchList;
	private String[] operatStatus;

	public WeibaSearchListFragment() {
		super();
		// TODO Auto-generated constructor stub
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case SEARCH_WEIBA:
					weibaSearchList = (LinkedList<WeibaBean>) msg.obj;
					Log.w(TAG, "List大小"+weibaSearchList.size()+"/"+msg.obj.toString());
					if(weibaSearchList.size()==0){
						Toast.makeText(mContext, "没搜索到任何微吧 ，换换关键词试试！",
								Toast.LENGTH_SHORT).show();
					}
					if(weibaSearchList.size()<20){
						weibaSearchListView.setLoadMoreEnable(false);
					}
					weibaSearchListAdapter = new WeibaSearchListAdapter(mContext,
							weibaSearchList,mHandler);
					weibaSearchListView.setAdapter(weibaSearchListAdapter);
					break;
				case ADD_WEIBA:
					weibaSearchListView.onRefreshComplete();
					LinkedList<WeibaBean> addList = (LinkedList<WeibaBean>) msg.obj;
					if((weibaSearchList.size()+addList.size())>20){
						weibaSearchListView.setLoadMoreEnable(true);
					}
					if (weibaSearchListAdapter == null) {
						weibaSearchListAdapter = new WeibaSearchListAdapter(mContext,
								addList, mHandler);
						weibaSearchListView.setAdapter(weibaSearchListAdapter);
					} else {
						int count = insertToHead(addList);
						Toast.makeText(mContext, "加载" + count + "条信息",
								Toast.LENGTH_SHORT).show();
						weibaSearchListAdapter.notifyDataSetChanged();
					}
					break;
				case APPEND_WEIBA:
					weibaSearchListView.onLoadMoreComplete();
					LinkedList<WeibaBean> appendList = (LinkedList<WeibaBean>) msg.obj;
					if (weibaSearchListAdapter == null) {
						weibaSearchListAdapter = new WeibaSearchListAdapter(mContext,
								appendList, mHandler);
						weibaSearchListView.setAdapter(weibaSearchListAdapter);
					} else {
						int count = append(appendList);
						Toast.makeText(mContext, "加载" + count + "条信息",
								Toast.LENGTH_SHORT).show();
						weibaSearchListAdapter.notifyDataSetChanged();
					}
					break;
				case ACT_CREATE:
					final Map<String, String> create = new HashMap<String, String>();
					create.put("app", APP);
					create.put("mod", MOD);
					create.put("act", CREATE);
					create.put("id", (String)msg.obj);
					create.put("oauth_token", OAUTH_TOKEN);
					create.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
					new Thread(new WeibaActionHelper(mHandler, mContext,
							create, ACT_CREATE_OK)).start();
					break;
				case ACT_DESTROY:
					final Map<String, String> destroy = new HashMap<String, String>();
					destroy.put("app", APP);
					destroy.put("mod", MOD);
					destroy.put("act", DESTROY);
					destroy.put("id", (String)msg.obj);
					destroy.put("oauth_token", OAUTH_TOKEN);
					destroy.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
					new Thread(new WeibaActionHelper(mHandler, mContext,
							destroy, ACT_DESTROY_OK)).start();
					break;
				case ACT_CREATE_OK:
					operatStatus = (String[]) msg.obj;
					if ("1".equals(operatStatus[0])) {
						Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT)
								.show();
						WeibaOperator.getInstance().updateFollowState(operatStatus[1], 1);
					} else {
						Toast.makeText(mContext, "关注失败", Toast.LENGTH_SHORT)
								.show();
					}
					break;
				case ACT_DESTROY_OK:
					operatStatus = (String[]) msg.obj;
					if ("1".equals(operatStatus[0])) {
						Toast.makeText(mContext, "取消关注成功", Toast.LENGTH_SHORT)
								.show();
						WeibaOperator.getInstance().updateFollowState(operatStatus[1], 0);
					} else {
						Toast.makeText(mContext, "取消关注失败", Toast.LENGTH_SHORT)
								.show();
					}
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

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.weiba_search_list_fragment,
				container, false);
		weibaSearchListView = (PullToRefreshListView) rootView
				.findViewById(R.id.weiba_search_list);
		weibaSearchListView.setVisibility(View.INVISIBLE);
		weibaSearchListView.setLoadMoreEnable(true);
		weibaSearchListView.setListener(this);
		return rootView;

	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
		ThinkSNSApplication application = ThinkSNSApplication.getInstance();
		OAUTH_TOKEN = application.getOauth_token(mContext);
		OAUTH_TOKEN_SECRECT = application.getOauth_token_secret(mContext);
	}

	public void setAttribute(Map<String, String> map, boolean cacheFlag) {
		this.map = map;
		this.cacheFlag = cacheFlag;
	}

	public void refresh() {
		weibaSearchListView.setVisibility(View.VISIBLE);
		new Thread(new WeibaDataHelper(mHandler, mContext, map,
				SEARCH_WEIBA, cacheFlag)).start();
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Thread(new WeibaDataHelper(mHandler, mContext, map,
				ADD_WEIBA, cacheFlag)).start();	
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		final Map<String, String> append = new HashMap<String, String>(map);
		int page = weibaSearchList.size() / defaultCount + 1;
		Toast.makeText(mContext, "加载更多", Toast.LENGTH_SHORT).show();
		append.put("page", Integer.toString(page));
		new Thread(new WeibaDataHelper(mHandler, mContext, append,
				APPEND_WEIBA, cacheFlag)).start();
	}

	public int append(List<WeibaBean> list) {
		if (list == null) {
			return 0;
		}
		if(weibaSearchList.size()==0){
			weibaSearchList.addAll(list);
			return list.size();
		}
		Iterator<WeibaBean> it = list.iterator();
		WeibaBean last = weibaSearchList.getLast();
		int index = 0;
		while (it.hasNext()) {
			WeibaBean temp = it.next();
			if (!temp.getWeiba_id().equals(last.getWeiba_id())) {
				index++;
			} else {
				break;
			}
		}
		int length = list.size();
		Log.w("向后添加", "索引是" + index);
		if (index == (length - 1)) {
			return 0;
		} else if (index == length) {
			weibaSearchList.addAll(list);
			return length;
		} else {
			weibaSearchList.addAll(list.subList(index + 1, length - 1));
			return length - index - 1;
		}

	}

	public int insertToHead(List<WeibaBean> list) {
		if (list == null) {
			return 0;
		}
		int index = 0;
		if(weibaSearchList.size()==0){
			index=list.size();
		}else{
			Iterator<WeibaBean> it = list.iterator();
			WeibaBean first = weibaSearchList.getFirst();
			while (it.hasNext()) {
				WeibaBean temp = it.next();
				if (!temp.getWeiba_id().equals(first.getWeiba_id())) {
					index++;
				} else {
					break;
				}
			}	
		}
		Log.w("向前添加", "索引是" + index);
		if (index == 0) {
			return 0;
		} else {
			for (int i = index - 1; i >= 0; i--) {
				weibaSearchList.addFirst(list.get(i));
			}
			return index;
		}
	}

	public void clear() {
		weibaSearchList.clear();
	}

}
