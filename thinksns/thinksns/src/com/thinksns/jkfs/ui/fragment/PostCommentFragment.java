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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.PostCommentBean;
import com.thinksns.jkfs.ui.adapter.CommentListAdapter;
import com.thinksns.jkfs.ui.adapter.PostListAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.ui.view.PullToRefreshListView.RefreshAndLoadMoreListener;
import com.thinksns.jkfs.util.WeibaActionHelper;
import com.thinksns.jkfs.util.WeibaBaseHelper;
import com.thinksns.jkfs.util.WeibaCommentHelper;
import com.thinksns.jkfs.util.db.PostCommentOperator;

/**
 * @author 杨智勇
 * @since 2014-5-29
 */
public class PostCommentFragment extends Fragment implements
		RefreshAndLoadMoreListener {
	// 常量
	public static final int GET_COMMENT_LIST = 0;
	public static final int APPEND_COMMENT_LIST = 1;
	public static final int ADD_COMMENT_LIST = 2;
	public static final int ACT_DELETE = 3;
	public static final int ACT_REPLY = 4;
	public static final int ACT_DELETE_OK = 5;
	public static final int defaultCount = 20;
	// 参数
	private static final String TAG = "PostListFragment";
	private static final String APP = "api";
	private static final String MOD = "Weiba";
	private static final String DELETE_COMMENT = "delete_comment";
	private static String OAUTH_TOKEN;
	private static String OAUTH_TOKEN_SECRECT;

	// 组件
	private PullToRefreshListView postCommentList;
	private ImageView loadImage;
	private CommentListAdapter commentListAdapter;
	private EditText inputView;
	private Handler mHandler;
	private Context mContext;
	private Map<String, String> map;
	private Boolean cacheFlag;

	// 数据
	private LinkedList<PostCommentBean> commentList;
	private String[] operatStatus;

	public PostCommentFragment() {
		super();
		// TODO Auto-generated constructor stub
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case GET_COMMENT_LIST:
					loadImage.setAnimation(null);
					loadImage.setVisibility(View.GONE);
					commentList = (LinkedList<PostCommentBean>) msg.obj;	
					if (commentList.size() == 0) {
						Toast.makeText(mContext, "暂无评论", Toast.LENGTH_SHORT)
								.show();
					}
					if (commentList.size() < 20) {
						postCommentList.setLoadMoreEnable(false);
					}
					{
						commentListAdapter = new CommentListAdapter(mContext,
								commentList, mHandler);
						postCommentList.setAdapter(commentListAdapter);
					}
					
					break;
				case APPEND_COMMENT_LIST:
					postCommentList.onRefreshComplete();
					LinkedList<PostCommentBean> appendList = (LinkedList<PostCommentBean>) msg.obj;
					if (commentListAdapter == null) {
						commentListAdapter = new CommentListAdapter(mContext,
								appendList, mHandler);
						postCommentList.setAdapter(commentListAdapter);
					} else {
						int count = append(appendList);
						Toast.makeText(mContext, "加载" + count + "条评论",
								Toast.LENGTH_SHORT).show();
						commentListAdapter.notifyDataSetChanged();
					}
					break;
				case ADD_COMMENT_LIST:
					postCommentList.onLoadMoreComplete();
					LinkedList<PostCommentBean> addList = (LinkedList<PostCommentBean>) msg.obj;
					if ((commentList.size()+addList.size()) > 20) {
						postCommentList.setLoadMoreEnable(true);
					}
					if (commentListAdapter == null) {
						commentListAdapter = new CommentListAdapter(mContext,
								addList, mHandler);
						postCommentList.setAdapter(commentListAdapter);
					} else {
						int count = insertToHead(addList);
						Toast.makeText(mContext, "加载" + count + "条评论",
								Toast.LENGTH_SHORT).show();
						commentListAdapter.notifyDataSetChanged();
					}
					break;
				case ACT_REPLY:
					PostCommentBean item = (PostCommentBean) msg.obj;
					String content="回复@" + item.getUname() + "：";
					int index=content.length();
					inputView.setSelected(true);
					inputView.setText(content);
					inputView.setTag(item.getReply_id());
					inputView.setSelection(index);
					break;
				case ACT_DELETE:
					final Map<String, String> delete = new HashMap<String, String>();
					delete.put("app", APP);
					delete.put("mod", MOD);
					delete.put("act", DELETE_COMMENT);
					delete.put("id", (String) msg.obj);
					delete.put("oauth_token", OAUTH_TOKEN);
					delete.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
					new Thread(new WeibaActionHelper(mHandler, mContext,
							delete, ACT_DELETE_OK)).start();
					break;
				case ACT_DELETE_OK:
					operatStatus = (String[]) msg.obj;
					if ("1".equals(operatStatus[0])) {
						Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT)
								.show();
						PostCommentOperator.getInstance().deleteById(
								operatStatus[1]);
					} else {
						Toast.makeText(mContext, "操作失败", Toast.LENGTH_SHORT)
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
		View rootView = inflater.inflate(R.layout.post_comment_fragment_layout,
				container, false);
		loadImage=(ImageView) rootView
				.findViewById(R.id.load_img);
		postCommentList = (PullToRefreshListView) rootView
				.findViewById(R.id.post_comment_list);
		postCommentList.setLoadMoreEnable(true);
		postCommentList.setListener(this);
		RotateAnimation rotateAnimation=new RotateAnimation(0.0f, +360.0f,
	               Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
	    rotateAnimation.setRepeatCount(Animation.INFINITE);
	    rotateAnimation.setInterpolator(new LinearInterpolator());
	    rotateAnimation.setDuration(500);
	    loadImage.startAnimation(rotateAnimation);
		refresh();
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

	public void setInputView(EditText inputView) {
		this.inputView = inputView;
	}

	public void refresh() {
		new Thread(new WeibaCommentHelper(mHandler, mContext, map,
				GET_COMMENT_LIST, cacheFlag)).start();

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		final Map<String, String> append = new HashMap<String, String>(map);
		int page = commentList.size() / defaultCount + 1;
		Toast.makeText(mContext, "加载更多", Toast.LENGTH_SHORT).show();
		append.put("page", Integer.toString(page));
		new Thread(new WeibaCommentHelper(mHandler, mContext, append,
				APPEND_COMMENT_LIST, cacheFlag)).start();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		new Thread(new WeibaCommentHelper(mHandler, mContext, map,
				ADD_COMMENT_LIST, cacheFlag)).start();	
	}

	public int append(List<PostCommentBean> list) {
		if (list == null) {
			return 0;
		}
		int index = 0;
		int length = list.size();
		if(commentList.size()==0){
			index=length;
		}else{
			Iterator<PostCommentBean> it = list.iterator();
			PostCommentBean last = commentList.getLast();
			while (it.hasNext()) {
				PostCommentBean temp = it.next();
				if (!temp.getReply_id().equals(last.getReply_id())) {
					index++;
				} else {
					break;
				}
			}
		}	
		Log.w("向后添加", "索引是" + index);
		if (index == (length - 1)) {
			return 0;
		} else if (index == length) {
			commentList.addAll(list);
			return length;
		} else {
			commentList.addAll(list.subList(index + 1, length - 1));
			return length - index - 1;
		}

	}

	public int insertToHead(List<PostCommentBean> list) {
		if (list == null) {
			return 0;
		}
		int index = 0;
		int length=list.size();
		if(commentList.size()==0){
			index=length;
		}else{
			Iterator<PostCommentBean> it = list.iterator();
			PostCommentBean first = commentList.getFirst();
			while (it.hasNext()) {
				PostCommentBean temp = it.next();
				if (!temp.getReply_id().equals(first.getReply_id())) {
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
				commentList.addFirst(list.get(i));
			}
			return index;
		}
	}

	public void clear() {
		commentList.clear();
	}

}
