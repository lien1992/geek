package com.thinksns.jkfs.ui.fragment;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.ui.MainFragmentActivity;
import com.thinksns.jkfs.ui.adapter.MainFragmentPagerAdapter;
import com.thinksns.jkfs.ui.view.UnderlinePageIndicator;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 主微博Fragment,承载WeiboListFragment和AboutMeFragment
 * 
 * @author wangjia
 * 
 */

public class WeiboMainFragment extends Fragment {

    private String TAG="WeiboMainFragment";
	private ViewPager pager;
	private UnderlinePageIndicator indicator;
	private MainFragmentPagerAdapter adapter;
	private View view;
	private LayoutInflater in;
	private TextView weiboList, aboutMe;
	private ImageView navi;

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
		indicator = (UnderlinePageIndicator) view
				.findViewById(R.id.main_weibo_indicator);
		pager = (ViewPager) view.findViewById(R.id.main_weibo_pager);

		return view;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		aboutMe.setTextColor(getResources().getColor(R.color.grey));
		adapter = new MainFragmentPagerAdapter(getActivity());
		pager.setOffscreenPageLimit(0);
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);
		indicator.setFades(false);
		indicator.setSelectedColor(getResources().getColor(R.color.green));
		indicator.setFadeLength(500);
		indicator.setOnPageChangeListener(new MyPageChangeListener());
		navi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				((MainFragmentActivity) getActivity()).getSlidingMenu()
						.toggle();
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
				((MainFragmentActivity) getActivity()).getSlidingMenu()
						.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			} else if (arg == 1) {
				aboutMe.setTextColor(getResources().getColor(R.color.green));
				weiboList.setTextColor(getResources().getColor(R.color.grey));
				((MainFragmentActivity) getActivity()).getSlidingMenu()
						.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			}
		}
	}

}
