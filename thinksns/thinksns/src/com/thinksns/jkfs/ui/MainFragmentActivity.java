package com.thinksns.jkfs.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
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
import com.thinksns.jkfs.ui.fragment.MenuFragmentList;
import com.thinksns.jkfs.ui.fragment.WeibaFragment;
import com.thinksns.jkfs.ui.view.SlidingMenu;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MainFragmentActivity extends SlidingFragmentActivity {

    private Fragment mContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("sliding menu");

        setContentView(R.layout.responsive_content_frame);

        // check if the content frame contains the menu frame
        if (findViewById(R.id.menu_frame) == null) {
            setBehindContentView(R.layout.menu_frame);
            getSlidingMenu().setSlidingEnabled(true);
            getSlidingMenu().setTouchModeAbove(com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.TOUCHMODE_FULLSCREEN);
            // show home as up so we can toggle
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            // add a dummy view
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSlidingEnabled(false);
            getSlidingMenu().setTouchModeAbove(com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.TOUCHMODE_NONE);
        }

        // set the Above View Fragment
        if (savedInstanceState != null)
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        if (mContent == null)
            mContent = new HomeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, mContent)
                .commit();

        // set the Behind View Fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, new MenuFragmentList())
                .commit();

        // customize the SlidingMenu
        com.jeremyfeinstein.slidingmenu.lib.SlidingMenu sm = getSlidingMenu();
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);

        // show the explanation dialog
        if (savedInstanceState == null)
            new AlertDialog.Builder(this)
                    .setTitle(R.string.what_is_this)
                    .setMessage(R.string.responsive_explanation)
                    .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }

    public void switchContent(final Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                getSlidingMenu().showContent();
            }
        }, 50);
    }

//    public void onBirdPressed(int pos) {
//        Intent intent =new Intent(this,);
//        startActivity(intent);
//    }

    /*
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
        //final ActionBar actionBar = getActionBar();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        super.showMenu();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    */
}
