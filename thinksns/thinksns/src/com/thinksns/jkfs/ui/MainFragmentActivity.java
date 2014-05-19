package com.thinksns.jkfs.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseSlidingFragmentActivity;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.ui.adapter.MainFragmentPagerAdapter;
import com.thinksns.jkfs.ui.fragment.AboutMeFragment;
import com.thinksns.jkfs.ui.fragment.ChanelFragment;
import com.thinksns.jkfs.ui.fragment.ChatFragment;
import com.thinksns.jkfs.ui.fragment.CollectionFragment;
import com.thinksns.jkfs.ui.fragment.HomeFragment;
import com.thinksns.jkfs.ui.fragment.WeibaFragment;
import com.thinksns.jkfs.ui.view.SlidingMenu;

import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainFragmentActivity extends BaseSlidingFragmentActivity implements
		OnClickListener {
	private ViewPager viewPager;
	private ListView naviTitle;
	private SimpleAdapter naviAdapter;
	private SlidingMenu sm;
	private MainFragmentPagerAdapter pagerAdapter;

	private AccountBean account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        ThinkSNSApplication application=ThinkSNSApplication.getInstance();
        if(!application.isLogin(MainFragmentActivity.this)){
            return;
        }

		initSlidingMenu();
		setContentView(R.layout.slidingmenu_above);
		initViews();

		pagerAdapter = new MainFragmentPagerAdapter(MainFragmentActivity.this);
		viewPager.setOffscreenPageLimit(0);
		viewPager.setAdapter(pagerAdapter);
		naviAdapter = new SimpleAdapter(this, getData(),
				R.layout.slidingmenu_behind_listview_item, new String[] {
						"text", "icon" }, new int[] { R.id.sm_behind_title,
						R.id.sm_behind_icon });
		naviTitle.setAdapter(naviAdapter);
		naviTitle.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			switch (position){
                case 0:
                    HomeFragment home=new HomeFragment();
                    setFragment(home);
                    break;
                case 1:
                    ChatFragment chat=new ChatFragment();
                    setFragment(chat);
                    break;
                case 2:
                    break;
                case 3:
                    CollectionFragment collection =new CollectionFragment();
                    setFragment(collection);
                    break;
                case 4:
                    AboutMeFragment aboutMe=new AboutMeFragment();
                    setFragment(aboutMe);
                    break;
                case 5:
                    ChanelFragment chanel=new ChanelFragment();
                    setFragment(chanel);
                    break;
                case 6:
                    WeibaFragment weiba=new WeibaFragment();
                    setFragment(weiba);
                    break;

            }

			}
		});

	}

    private void setFragment(Fragment fragment){
        if(fragment==null)
            return;
        FragmentManager f=getSupportFragmentManager();
        FragmentTransaction ft=f.beginTransaction();
        ft.replace(R.id.above_pager,fragment);
        ft.commit();
    }
	private void initSlidingMenu() {
		setBehindContentView(R.layout.slidingmenu_behind);
		sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowDrawable(R.drawable.slidingmenu_shadow);
		sm.setShadowWidth(20);
	}

	private void initViews() {

		viewPager = (ViewPager) findViewById(R.id.above_pager);
		naviTitle = (ListView) findViewById(R.id.behind_listview);
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("text", "首页");
		map.put("icon", R.drawable.slide_ico_green1);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("text", "聊天");
		map.put("icon", R.drawable.slide_ico_green2);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("text", "评论");
		map.put("icon", R.drawable.slide_ico_green3);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("text", "收藏");
		map.put("icon", R.drawable.slide_ico_green4);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("text", "@我");
		map.put("icon", R.drawable.slide_ico_green5);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("text", "频道");
		map.put("icon", R.drawable.slide_ico_green6);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("text", "微吧");
		map.put("icon", R.drawable.slide_ico_green7);
		list.add(map);
		return list;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
