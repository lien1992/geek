package com.thinksns.jkfs.ui.fragment;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.ui.adapter.WeibaCenterAdapter;
import com.thinksns.jkfs.ui.adapter.WeibaSearchAdapter;
import com.thinksns.jkfs.ui.view.UnderlinePageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author 杨智勇
 * @since 2014-5-29
 */
public class PersonalCenterFragment extends Fragment {

	private TextView posteds;
	private TextView commenteds;
	private TextView favorite_list;
	private UnderlinePageIndicator weiba_center_indicator;
	private ViewPager weiba_center_pager;
	private WeibaCenterAdapter pagerAdapter;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(
				R.layout.personal_center_fragment_layout, container, false);
		posteds=(TextView) rootView.findViewById(R.id.posteds);
		commenteds=(TextView) rootView.findViewById(R.id.commenteds);
		favorite_list=(TextView) rootView.findViewById(R.id.favorite_list);
		weiba_center_indicator=(UnderlinePageIndicator)rootView.findViewById(R.id.weiba_center_indicator);
		weiba_center_pager=(ViewPager)rootView.findViewById(R.id.weiba_center_pager);
        
		FragmentManager fm=getChildFragmentManager();
		pagerAdapter=new WeibaCenterAdapter(fm, getActivity(), this);
		weiba_center_pager.setAdapter(pagerAdapter);
		weiba_center_indicator.setViewPager(weiba_center_pager);
		
		posteds.setTag(0);
		weiba_center_indicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				int index=(int) posteds.getTag();
				switch(arg0){
					case 0:posteds.setTextColor(getResources().getColor(R.color.green));break;
					case 1:commenteds.setTextColor(getResources().getColor(R.color.green));break;
					case 2:favorite_list.setTextColor(getResources().getColor(R.color.green));break;
				}
				switch(index){
					case 0:posteds.setTextColor(getResources().getColor(R.color.grey));break;
					case 1:commenteds.setTextColor(getResources().getColor(R.color.grey));break;
					case 2:favorite_list.setTextColor(getResources().getColor(R.color.grey));break;
			    }
				posteds.setTag(arg0);
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
		
		posteds.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				weiba_center_pager.setCurrentItem(0);
			}
		});
		commenteds.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						weiba_center_pager.setCurrentItem(1);
					}
				});
		favorite_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				weiba_center_pager.setCurrentItem(2);
			}
		});
		return rootView;

	}
}
