package com.thinksns.jkfs.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.ui.MainFragmentActivity;
import com.thinksns.jkfs.ui.SearchActivity;
import com.thinksns.jkfs.ui.WriteWeiboActivity;
import com.thinksns.jkfs.ui.adapter.MainFragmentPagerAdapter;
import com.thinksns.jkfs.ui.adapter.MainFragmentPagerAdapter.SwitchGroupListener;
import com.thinksns.jkfs.ui.view.SatelliteMenu;
import com.thinksns.jkfs.ui.view.SatelliteMenuItem;
import com.thinksns.jkfs.ui.view.UnderlinePageIndicator;
import com.thinksns.jkfs.ui.view.SatelliteMenu.SateliteClickedListener;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 主微博Fragment,承载WeiboListFragment和AboutMeFragment
 * 
 * @author wangjia
 * 
 */

public class WeiboMainFragment extends Fragment {

	private String TAG = "WeiboMainFragment";
	private TextView group;
	private PopupWindow popupWindow;
	private ListView lv_group;
	private List<String> groups;
	private ViewPager pager;
	private UnderlinePageIndicator indicator;
	private MainFragmentPagerAdapter adapter;
	private View view;
	private LayoutInflater in;
	private TextView weiboList, aboutMe;
	private ImageView navi;
	private SatelliteMenu menu;
	private SwitchGroupListener switchListener;

	public void setSwitchGroupListener(SwitchGroupListener switchListener) {
		this.switchListener = switchListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		super.onCreateView(inflater, container, savedInstanceState);
		in = inflater;
		view = in.inflate(R.layout.main_weibo_fragment, null);
		weiboList = (TextView) view.findViewById(R.id.tab_main_weibo_fragment);
		aboutMe = (TextView) view.findViewById(R.id.tab_about_me_fragment);
		navi = (ImageView) view.findViewById(R.id.app_navi);
		group = (TextView) view.findViewById(R.id.weibo_main_group);
		indicator = (UnderlinePageIndicator) view
				.findViewById(R.id.main_weibo_indicator);
		pager = (ViewPager) view.findViewById(R.id.main_weibo_pager);
		menu = (SatelliteMenu) view.findViewById(R.id.sat_menu);
		return view;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		aboutMe.setTextColor(getResources().getColor(R.color.grey));
		adapter = new MainFragmentPagerAdapter(getActivity(), this);
		pager.setOffscreenPageLimit(0);
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);
		indicator.setFades(false);
		indicator.setSelectedColor(getResources().getColor(R.color.green));
		indicator.setFadeLength(500);
		indicator.setOnPageChangeListener(new MyPageChangeListener());
		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
		items.add(new SatelliteMenuItem(2, R.drawable.circle_ico_green_3_w));
		items.add(new SatelliteMenuItem(1, R.drawable.circle_ico_green_1_w));
		menu.addItems(items);
		menu.setOnItemClickedListener(new SateliteClickedListener() {
			public void eventOccured(int id) {
				switch (id) {
				case 1:
					startActivity(new Intent(getActivity(),
							WriteWeiboActivity.class));
					break;
				case 2:
					startActivity(new Intent(getActivity(),
							SearchActivity.class));
					break;
				}
			}
		});
		navi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				((MainFragmentActivity) getActivity()).getSlidingMenu()
						.toggle();
			}

		});
		group.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showWindow(v);
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	}

	class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int arg) {

			if (arg == 0) {
				weiboList.setTextColor(getResources().getColor(R.color.green));
				aboutMe.setTextColor(getResources().getColor(R.color.grey));
				group.setVisibility(View.VISIBLE);
				menu.setVisibility(View.VISIBLE);
				((MainFragmentActivity) getActivity()).getSlidingMenu()
						.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			} else if (arg == 1) {
				aboutMe.setTextColor(getResources().getColor(R.color.green));
				weiboList.setTextColor(getResources().getColor(R.color.grey));
				group.setVisibility(View.GONE);
				menu.setVisibility(View.GONE);
				((MainFragmentActivity) getActivity()).getSlidingMenu()
						.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			}
		}
	}

	private void showWindow(View parent) {
		if (popupWindow == null) {
			View view = in.inflate(R.layout.main_weibo_list_popup_listview,
					null);
			lv_group = (ListView) view.findViewById(R.id.main_popup_listview);
			groups = new ArrayList<String>();
			groups.add("公共微博");
			groups.add("我的关注");

			GroupAdapter groupAdapter = new GroupAdapter(groups);
			lv_group.setAdapter(groupAdapter);
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}

		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);

		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 显示位置
		popupWindow.showAsDropDown(parent, 10, 0);
		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				group.setText(groups.get(position));
				if (switchListener != null)
					switchListener.onSwitch();

				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
	}

	class GroupAdapter extends BaseAdapter {

		private List<String> list;

		public GroupAdapter(List<String> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = in.inflate(
						R.layout.main_weibo_list_popup_listview_item, null);
				holder = new ViewHolder();
				holder.groupItem = (TextView) convertView
						.findViewById(R.id.main_popup_listview_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.groupItem.setText(list.get(position));
			return convertView;
		}

		class ViewHolder {
			TextView groupItem;
		}

	}

}
