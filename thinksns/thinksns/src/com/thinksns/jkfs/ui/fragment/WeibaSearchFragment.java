package com.thinksns.jkfs.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.ui.MainFragmentActivity;
import com.thinksns.jkfs.ui.adapter.WeibaSearchAdapter;
import com.thinksns.jkfs.ui.view.UnderlinePageIndicator;

/**
 * @author 杨智勇
 * @since 2014-5-29
 */
public class WeibaSearchFragment extends Fragment {
	
	private static final String TAG = "WeibaSearchFragment";
	private static final String APP = "api";
	private static final String MOD = "Weiba";
	private static final String SEARCH_WEIBA = "search_weiba";
	private static final String SEARCH_POST = "search_post";
	private static final String CREATE = "create";
	private static String OAUTH_TOKEN;
	private static String OAUTH_TOKEN_SECRECT;
	
	private Activity mContext;
	private Handler mHandler;
	private ImageView navigation;
	private EditText weiba_search_edit;
	private View weiba_search_button;
	private TextView weiba_search;
	private TextView post_search;
	private UnderlinePageIndicator weiba_search_indicator;
	private ViewPager weiba_search_pager;
	private WeibaSearchAdapter pagerAdapter;
	private FragmentManager fm;

	public WeibaSearchFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.weiba_search_fragment_layout,
				container, false);
		navigation = (ImageView) rootView.findViewById(R.id.weiba_fragment_title);
		weiba_search_edit=(EditText)rootView.findViewById(R.id.weiba_search_edit);
		weiba_search_button=rootView.findViewById(R.id.weiba_search_button);
		weiba_search_indicator=(UnderlinePageIndicator)rootView.findViewById(R.id.weiba_search_indicator);
		weiba_search_pager=(ViewPager)rootView.findViewById(R.id.weiba_search_pager);
		weiba_search=(TextView) rootView.findViewById(R.id.weiba_search);
        post_search=(TextView) rootView.findViewById(R.id.post_search);
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragmentActivity) getActivity()).getSlidingMenu()
                        .toggle();
            }
        }); 
        
        init();
		fm=getChildFragmentManager();
		pagerAdapter=new WeibaSearchAdapter(fm,mContext,this);
		weiba_search_pager.setAdapter(pagerAdapter);
		weiba_search_indicator.setViewPager(weiba_search_pager);
		weiba_search_indicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch(arg0){
				case 0:
					weiba_search.setTextColor(getResources().getColor(R.color.green));
					post_search.setTextColor(getResources().getColor(R.color.grey));
					break;
				case 1:
					weiba_search.setTextColor(getResources().getColor(R.color.grey));
					post_search.setTextColor(getResources().getColor(R.color.green));
					break;
				}	
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub			
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub		
			}
		});
		
		weiba_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				weiba_search_pager.setCurrentItem(0);
			}
		});
		
		post_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				weiba_search_pager.setCurrentItem(1);
			}
		});
		weiba_search_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String keyWord=weiba_search_edit.getText().toString();
				if(keyWord.equals("")){
					Toast.makeText(mContext, "请输入关键词", Toast.LENGTH_SHORT).show();
				}else{
					int index=weiba_search_pager.getCurrentItem();
					String act=index==0?SEARCH_WEIBA:SEARCH_POST;
					final Map<String, String> key = new HashMap<String, String>();
					key.put("app", APP);
					key.put("mod", MOD);
					key.put("act", act);
					key.put("keyword", keyWord);
					key.put("oauth_token", OAUTH_TOKEN);
					key.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
					if(index==0){
						WeibaSearchListFragment current=(WeibaSearchListFragment) pagerAdapter.instantiateItem(weiba_search_pager, 0);
						current.setAttribute(key, false);
						current.refresh();
						fm.beginTransaction().show(current).commit();
					}else{
						PostListFragment current=(PostListFragment) pagerAdapter.instantiateItem(weiba_search_pager, 1);
						current.setAttribute(key, false);
						current.refresh();  
						fm.beginTransaction().show(current).commit();
					}
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
