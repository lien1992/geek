package com.thinksns.jkfs.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.PostBean;
import com.thinksns.jkfs.util.WeibaActionHelper;
import com.thinksns.jkfs.util.WeibaBaseHelper;
import com.thinksns.jkfs.util.common.DateUtils;
import com.thinksns.jkfs.util.db.WeibaOperator;

/**
 * @author 杨智勇
 * @since 2014-5-29
 */
public class PostDetailFragment extends Fragment {
	
	public static final int COMMENT_OK = 0;

	// 常量
	private static final String TAG = "WeibaFragment";
	private static final String APP = "api";
	private static final String MOD = "Weiba";
	private static final String GET_COMMENT_LIST = "comment_list";
	private static final String COMMENT_POST = "comment_post";
	private static final String REPLY_COMMENT = "reply_comment";
	private static String OAUTH_TOKEN;
	private static String OAUTH_TOKEN_SECRECT;
	// 组件
	private Activity mContext;
    private TextView post_title;
    private TextView weiba_name;
    private TextView post_user;
    private TextView post_date;
    private WebView post_content;
    private TextView post_comment;
    private TextView post_view;
    private ImageView post_collect;
    private PostCommentFragment post_comment_list;
    private Handler mHandler;
    private Handler myHandler;
    private View ScrollView;
    private EditText inputView;
    private View sendButton;
	// 数据
	private PostBean post;
	private static DisplayImageOptions options;
	private ImageLoader imageLoader;
    private boolean flag;
	
	public PostDetailFragment() {
		super();
		flag=true;
		imageLoader=ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.no_picture)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT).cacheOnDisk(true)
		.build();
		myHandler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what){
				case COMMENT_OK:
					String[] operatStatus = (String[]) msg.obj;
					if ("1".equals(operatStatus[0])) {
						inputView.setText("");
						Toast.makeText(mContext,"发布成功", Toast.LENGTH_SHORT)
								.show();
						if(post_comment_list!=null){
							post_comment_list.refresh();
						}
					} else {
						Toast.makeText(mContext, "发布失败", Toast.LENGTH_SHORT)
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
		View rootView = inflater.inflate(R.layout.post_detail_fragment_layout,
				container, false);
		post_title=(TextView)rootView.findViewById(R.id.post_title);
		weiba_name=(TextView)rootView.findViewById(R.id.weiba_name);
		post_user=(TextView)rootView.findViewById(R.id.post_user);
		post_date=(TextView)rootView.findViewById(R.id.post_date);
		post_content=(WebView)rootView.findViewById(R.id.post_content);
		post_comment=(TextView)rootView.findViewById(R.id.post_comment);
		post_view=(TextView)rootView.findViewById(R.id.post_view);
		post_collect=(ImageView)rootView.findViewById(R.id.post_collect);
		ScrollView=rootView.findViewById(R.id.scrollView);
		inputView=(EditText) rootView.findViewById(R.id.edit_comment);
		sendButton=rootView.findViewById(R.id.create_post_send);
		
		post_title.setText(post.getTitle());
		weiba_name.setText("["
				+ WeibaOperator.getInstance().queryWeibaNameByID(
						post.getWeiba_id()) + "]");
		post_user.setText(post.getUname());
		post_date.setText(DateUtils.getTimeInString(post.getPost_time()));
		
		WebSettings contentSetting= post_content.getSettings();
		contentSetting.setDefaultTextEncodingName("utf-8"); 
		contentSetting.setSupportZoom(true);
		contentSetting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		post_content.setBackgroundColor(0);
		post_content.loadDataWithBaseURL("http://tsimg.tsurl.cn", post.getContent(), "text/html", "utf-8", null);
		
		post_comment.setText("(" + post.getReply_count()+ ")");
		post_view.setText("(" + post.getRead_count() + ")");
		post_comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(flag){
					initCommentList();
					ScrollView.setVisibility(View.GONE);
					flag=!flag;
				}else{
					ScrollView.setVisibility(View.VISIBLE);
					getChildFragmentManager().beginTransaction().hide(post_comment_list).commit();
					flag=!flag;
				}
			}
		});
		
		sendButton.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String tag=(String)inputView.getTag();
				String act,id,content;
				content=inputView.getText().toString();
				if(!content.contains("@")){
					tag=null;
				}
				if(tag!=null){
					act=REPLY_COMMENT;
					id=tag;
				}else{
					act=COMMENT_POST;
					id=post.getPost_id();
				}
				final Map<String, String> reply = new HashMap<String, String>();
				reply.put("app", APP);
				reply.put("mod", MOD);
				reply.put("act", act);
				reply.put("id", id);
				reply.put("content", content);
				reply.put("oauth_token", OAUTH_TOKEN);
				reply.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
				new Thread(new WeibaActionHelper(myHandler, mContext,
						reply, COMMENT_OK)).start();
			}
		});
		final boolean followState=post.getFavorite() == 1;
		final String post_id=post.getPost_id();
		post_collect.setImageResource(followState?R.drawable.heart:R.drawable.favourite);
		post_collect.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((ImageView) v)
				.setImageResource(!followState ? R.drawable.heart
						: R.drawable.favourite);
				post.setFavorite(!followState?1:0);
				mHandler.obtainMessage(!followState?3:4, post_id)
				.sendToTarget();
			}
		});
		init();
		return rootView;

	}

	private void init() {
		mContext = getActivity();
		ThinkSNSApplication application = ThinkSNSApplication.getInstance();
		OAUTH_TOKEN = application.getOauth_token(mContext);
		OAUTH_TOKEN_SECRECT = application.getOauth_token_secret(mContext);
	}

	public void  initCommentList(){
		final Map<String, String> map = new HashMap<String, String>();
		map.put("app", APP);
		map.put("mod", MOD);
		map.put("act", GET_COMMENT_LIST);
		map.put("id", post.getPost_id());
		map.put("oauth_token", OAUTH_TOKEN);
		map.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
		post_comment_list = new PostCommentFragment();
		post_comment_list.setContext(mContext);
		post_comment_list.setAttribute(map, false);//Comment不缓存
		post_comment_list.setInputView(inputView);
		FragmentTransaction fm=getChildFragmentManager().beginTransaction();
		fm.add(R.id.detail_comment_list, post_comment_list);
		fm.addToBackStack(null);
		fm.commit();
	}
	
	public void setHandler(Handler mHandler){
		this.mHandler=mHandler;
	}
	
	public void setPost(PostBean post) {
		this.post = post;
	}
	
}
