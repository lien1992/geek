package com.thinksns.think;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.PageIndicator;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Layout;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mListView;
	private String[] menu_item;
	private int[] menu_ico;
	private static FragmentManager fm;
	private PlaceholderFragment mainFragment;
	private ActionBarDrawerToggle mDrawerToggle; 
	private android.app.ActionBar mActionBar; 
	private CharSequence mTitle;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置主界面显示内容
		fm=getSupportFragmentManager();
		mainFragment=new PlaceholderFragment();
		mainFragment.setFragmentManager(fm);
		fm.beginTransaction().add(R.id.content_frame,mainFragment).commit();
		setContentView(R.layout.activity_main);
		
		mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
		mListView=(ListView)findViewById(R.id.listview_drawer);
		
		/*mTitle=getTitle();
		mDrawerToggle = new ActionBarDrawerToggle(  
                this,                     
                mDrawerLayout,         
                R.drawable.ic_launcher, 
                R.string.app_name,
                R.string.test
                ) {  
            public void onDrawerClosed(View view) {  
            	mActionBar.setTitle(mTitle+"打开了");  
                invalidateOptionsMenu();
            }  
            public void onDrawerOpened(View drawerView) {  
            	mActionBar.setTitle(mTitle+"关闭了");  
                invalidateOptionsMenu();
            }  
        };  
        mDrawerLayout.setDrawerListener(mDrawerToggle);  
       */
		
		menu_item= getResources().getStringArray(R.array.menu_item);
		menu_ico=new int[]{R.drawable.menu_ico_1,R.drawable.menu_ico_2,R.drawable.menu_ico_3,
				R.drawable.menu_ico_4,R.drawable.menu_ico_5,R.drawable.menu_ico_6,
				R.drawable.menu_ico_7,R.drawable.menu_ico_8};
		
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();  
	    for (int i = 0; i < menu_ico.length; i++) {  
	        HashMap<String, Object> map = new HashMap<String, Object>();  
	        map.put("ItemImage", menu_ico[i]);  
	        map.put("ItemTitle", menu_item[i]);   
	        data.add(map);  
	    }  		
	    String[] from = new String[] { "ItemImage", "ItemTitle"}; 
	    int[] to = new int[] { R.id.menu_ico ,R.id.menu_item};  
	    mListView.setAdapter(new SimpleAdapter(this, data, R.layout.menu_item, from, to));
	    
		mListView.setClickable(true);
		mListView.setOnItemClickListener(new ListView.OnItemClickListener (){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Number:"+arg2, Toast.LENGTH_SHORT).show();
				if(arg2<6){
					mainFragment.mIndicator.setCurrentItem(arg2);
				}
			}
			
		});  
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		private MainFragmentAdapter mAdapter;
		private ViewPager mPager;
		public PageIndicator mIndicator;
		private FragmentManager fm;
		
		public PlaceholderFragment() {
		}
		public void setFragmentManager(FragmentManager fm) {
			this.fm=fm;
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			mAdapter = new MainFragmentAdapter(fm);
			mPager = (ViewPager)rootView.findViewById(R.id.pager);
	        mPager.setAdapter(mAdapter);

	        mIndicator = (IconPageIndicator)rootView.findViewById(R.id.indicator);
	        mIndicator.setViewPager(mPager);
			
			return rootView;
		}
	}

}
