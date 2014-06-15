package com.thinksns.jkfs.ui.fragment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.WeibaBean;
import com.thinksns.jkfs.ui.adapter.WeibaDropListAdapter;
import com.thinksns.jkfs.util.WeibaActionHelper;
import com.thinksns.jkfs.util.WeibaDataHelper;
import com.thinksns.jkfs.util.db.WeibaOperator;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

/**
 * @author 杨智勇
 * @since 2014-5-29
 */
public class CreatePostFragment extends Fragment {
	
	public static final int CREATE_POST_OK = 0;
	
	private static final String TAG = "CreatePostFragment";
	private static final String APP = "api";
	private static final String MOD = "Weiba";
	private static final String ACT_CREATE_POST = "create_post";
	private static String OAUTH_TOKEN;
	private static String OAUTH_TOKEN_SECRECT;
	
	private EditText title;
	private Spinner weiba_group;
	private EditText content;
	private View create_post_location;
	private View create_post_add_pic;
	private View create_post_add_topic;
	private View create_post_emotion;
	private View create_post_send;
	private Handler mHandler;
	private Activity mContext;
	
	private LinkedList<WeibaBean> followedWeiba;
	
	public CreatePostFragment() {
		super();
		// TODO Auto-generated constructor stub
		mHandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what){
				case CREATE_POST_OK:
					String[] operatStatus = (String[]) msg.obj;
					if ("1".equals(operatStatus[0])) {
						title.setText("");
						content.setText("");
						Toast.makeText(mContext,"发帖成功", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(mContext, "发帖失败", Toast.LENGTH_SHORT)
								.show();
					}
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
		View rootView = inflater.inflate(R.layout.create_post_fragment_layout,
				container, false);
		title=(EditText)rootView.findViewById(R.id.title);
		weiba_group=(Spinner)rootView.findViewById(R.id.weiba_group);
		content=(EditText)rootView.findViewById(R.id.content);
		create_post_location=rootView.findViewById(R.id.create_post_location);
		create_post_add_pic=rootView.findViewById(R.id.create_post_add_pic);
		create_post_add_topic=rootView.findViewById(R.id.create_post_add_topic);
		create_post_emotion=rootView.findViewById(R.id.create_post_emotion);
		create_post_send=rootView.findViewById(R.id.create_post_send);
		
		init();
		followedWeiba=WeibaOperator.getInstance().queryFollowedWeibaList();
		weiba_group.setAdapter(new WeibaDropListAdapter(mContext, followedWeiba));
		create_post_add_topic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String origin = content.getText().toString();
				String topicTag = "##";
				content.setText(origin + topicTag);
				content.setSelection(content.getText().toString().length() - 1);

			}
		});
		create_post_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String mTitle=title.getText().toString();
				String mContent=content.getText().toString();
				String weiba_id=((WeibaBean)weiba_group.getSelectedItem()).getWeiba_id();
				if(mTitle.equals("")){
					Toast.makeText(mContext, "标题不能为空", Toast.LENGTH_SHORT).show();
				}else if(mContent.equals("")){
					Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
				}else{
					final Map<String, String> post = new HashMap<String, String>();
					post.put("app", APP);
					post.put("mod", MOD);
					post.put("act", ACT_CREATE_POST);
					post.put("id", weiba_id);
					post.put("title", mTitle);
					post.put("content", mContent);
					post.put("oauth_token", OAUTH_TOKEN);
					post.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
					new Thread(new WeibaActionHelper(mHandler, mContext,
							post, CREATE_POST_OK)).start();
				}
			}
		});
		return rootView;
	}
	
	private void init() {
		mContext = getActivity();
		ThinkSNSApplication application = ThinkSNSApplication.getInstance();
		OAUTH_TOKEN = application.getOauth_token(mContext);
		OAUTH_TOKEN_SECRECT = application.getOauth_token_secret(mContext);
	}

}
