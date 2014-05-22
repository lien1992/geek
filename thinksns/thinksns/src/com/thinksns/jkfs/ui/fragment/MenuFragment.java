package com.thinksns.jkfs.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.ui.MainFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * SlidingMenu左侧菜单, 调试时自行将相关Fragment注释取消掉
 * 
 * @author wangjia
 * 
 */
public class MenuFragment extends Fragment implements OnClickListener {
	private ImageView avatar;
	private TextView nick;
	private LinearLayout home;
	private LinearLayout at;
	private LinearLayout favorite;
	private LinearLayout chat;
	private LinearLayout channel;
	private LinearLayout weiba;
	private LinearLayout setting;
	private LinearLayout logout;
	private Map<Integer, Fragment> fragments = new HashMap<Integer, Fragment>();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		fragments.put(R.id.sm_home,
				((MainFragmentActivity) getActivity()).getWeiboMainFragment());
		fragments.put(R.id.sm_at, ((MainFragmentActivity) getActivity())
				.getAtAndCommentFragment());
		// <<<<<<< HEAD
		// fragments.put(R.id.sm_favorite,
		// ((MainFragmentActivity) getActivity()).getCollectionFragment());
		// fragments.put(R.id.sm_chat,
		// ((MainFragmentActivity) getActivity()).getChatFragment());
		// fragments.put(R.id.sm_channel,
		// ((MainFragmentActivity) getActivity()).getChannelFragment());
		// fragments.put(R.id.sm_weiba,
		// ((MainFragmentActivity) getActivity()).getWeibaFragment());
		// fragments.put(R.id.sm_setting,
		// ((MainFragmentActivity) getActivity()).getSettingFragment());
		//
		// ((WeiboMainFragment) fragments.get(R.id.sm_home)).changeActionBar();
		// =======
		fragments.put(R.id.sm_favorite,
				((MainFragmentActivity) getActivity()).getCollectionFragment());
		fragments.put(R.id.sm_chat,
				((MainFragmentActivity) getActivity()).getChatFragment());
		fragments.put(R.id.sm_channel,
				((MainFragmentActivity) getActivity()).getChannelFragment());
		fragments.put(R.id.sm_weiba,
				((MainFragmentActivity) getActivity()).getWeibaFragment());
		fragments.put(R.id.sm_setting,
				((MainFragmentActivity) getActivity()).getSettingFragment());
		// */
		// >>>>>>> be3854490a74b7fe98b1c1cc96aae489361d9258
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final ScrollView view = (ScrollView) inflater.inflate(
				R.layout.slidingmenu_behind, container, false);
		avatar = (ImageView) view.findViewById(R.id.sm_behind_avatar);
		nick = (TextView) view.findViewById(R.id.sm_behind_nick);
		home = (LinearLayout) view.findViewById(R.id.sm_home);
		at = (LinearLayout) view.findViewById(R.id.sm_at);
		favorite = (LinearLayout) view.findViewById(R.id.sm_favorite);
		chat = (LinearLayout) view.findViewById(R.id.sm_chat);
		channel = (LinearLayout) view.findViewById(R.id.sm_channel);
		weiba = (LinearLayout) view.findViewById(R.id.sm_weiba);
		setting = (LinearLayout) view.findViewById(R.id.sm_setting);
		logout = (LinearLayout) view.findViewById(R.id.sm_logout);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		home.setOnClickListener(this);
		at.setOnClickListener(this);
		favorite.setOnClickListener(this);
		chat.setOnClickListener(this);
		channel.setOnClickListener(this);
		weiba.setOnClickListener(this);
		setting.setOnClickListener(this);
		logout.setOnClickListener(this);
		changeBackground(R.id.sm_home);

		// 替换头像、微博用户名..
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sm_home:
			changeBackground(R.id.sm_home);
			FragmentTransaction ft_home = getFragmentManager()
					.beginTransaction();
			ft_home.hide(fragments.get(R.id.sm_at));
			ft_home.hide(fragments.get(R.id.sm_favorite));
			ft_home.hide(fragments.get(R.id.sm_chat));
			ft_home.hide(fragments.get(R.id.sm_channel));
			ft_home.hide(fragments.get(R.id.sm_weiba));
			ft_home.hide(fragments.get(R.id.sm_setting));
			WeiboMainFragment wmfragment = (WeiboMainFragment) fragments
					.get(R.id.sm_home);
			ft_home.show(wmfragment);
			ft_home.commit();
			((MainFragmentActivity) getActivity()).getSlidingMenu()
					.showContent();
			break;
		case R.id.sm_at:
			changeBackground(R.id.sm_at);
			FragmentTransaction ft_at = getFragmentManager().beginTransaction();
			ft_at.hide(fragments.get(R.id.sm_home));
			ft_at.hide(fragments.get(R.id.sm_favorite));
			ft_at.hide(fragments.get(R.id.sm_chat));
			ft_at.hide(fragments.get(R.id.sm_channel));
			ft_at.hide(fragments.get(R.id.sm_weiba));
			ft_at.hide(fragments.get(R.id.sm_setting));
			AtAndCommentFragment acfragment = (AtAndCommentFragment) fragments
					.get(R.id.sm_at);
			ft_at.show(acfragment);
			ft_at.commit();
			((MainFragmentActivity) getActivity()).getSlidingMenu()
					.showContent();
			break;
		case R.id.sm_favorite:
			changeBackground(R.id.sm_favorite);
			FragmentTransaction ft_fav = getFragmentManager()
					.beginTransaction();
			ft_fav.hide(fragments.get(R.id.sm_home));
			ft_fav.hide(fragments.get(R.id.sm_at));
			ft_fav.hide(fragments.get(R.id.sm_chat));
			ft_fav.hide(fragments.get(R.id.sm_channel));
			ft_fav.hide(fragments.get(R.id.sm_weiba));
			ft_fav.hide(fragments.get(R.id.sm_setting));
			CollectionFragment favfragment = (CollectionFragment) fragments
					.get(R.id.sm_favorite);
			ft_fav.show(favfragment);
			ft_fav.commit();
			((MainFragmentActivity) getActivity()).getSlidingMenu()
					.showContent();
			break;
		case R.id.sm_chat:
			changeBackground(R.id.sm_chat);
			FragmentTransaction ft_ct = getFragmentManager().beginTransaction();
			ft_ct.hide(fragments.get(R.id.sm_home));
			ft_ct.hide(fragments.get(R.id.sm_favorite));
			ft_ct.hide(fragments.get(R.id.sm_at));
			ft_ct.hide(fragments.get(R.id.sm_channel));
			ft_ct.hide(fragments.get(R.id.sm_weiba));
			ft_ct.hide(fragments.get(R.id.sm_setting));
			ChatFragment ctfragment = (ChatFragment) fragments
					.get(R.id.sm_chat);
			ft_ct.show(ctfragment);
			ft_ct.commit();
			((MainFragmentActivity) getActivity()).getSlidingMenu()
					.showContent();
			break;
		case R.id.sm_channel:
			changeBackground(R.id.sm_channel);
			FragmentTransaction ft_chn = getFragmentManager()
					.beginTransaction();
			ft_chn.hide(fragments.get(R.id.sm_home));
			ft_chn.hide(fragments.get(R.id.sm_favorite));
			ft_chn.hide(fragments.get(R.id.sm_chat));
			ft_chn.hide(fragments.get(R.id.sm_at));
			ft_chn.hide(fragments.get(R.id.sm_weiba));
			ft_chn.hide(fragments.get(R.id.sm_setting));
			ChanelFragment chnfragment = (ChanelFragment) fragments
					.get(R.id.sm_channel);
			ft_chn.show(chnfragment);
			ft_chn.commit();
			((MainFragmentActivity) getActivity()).getSlidingMenu()
					.showContent();
			break;
		case R.id.sm_weiba:
			changeBackground(R.id.sm_weiba);
			FragmentTransaction ft_wb = getFragmentManager().beginTransaction();
			ft_wb.hide(fragments.get(R.id.sm_home));
			ft_wb.hide(fragments.get(R.id.sm_favorite));
			ft_wb.hide(fragments.get(R.id.sm_chat));
			ft_wb.hide(fragments.get(R.id.sm_channel));
			ft_wb.hide(fragments.get(R.id.sm_at));
			ft_wb.hide(fragments.get(R.id.sm_setting));
			WeibaFragment wbfragment = (WeibaFragment) fragments
					.get(R.id.sm_weiba);
			ft_wb.show(wbfragment);
			ft_wb.commit();
			((MainFragmentActivity) getActivity()).getSlidingMenu()
					.showContent();
			break;
		case R.id.sm_setting:
			changeBackground(R.id.sm_setting);
			FragmentTransaction ft_st = getFragmentManager().beginTransaction();
			ft_st.hide(fragments.get(R.id.sm_home));
			ft_st.hide(fragments.get(R.id.sm_favorite));
			ft_st.hide(fragments.get(R.id.sm_chat));
			ft_st.hide(fragments.get(R.id.sm_channel));
			ft_st.hide(fragments.get(R.id.sm_weiba));
			ft_st.hide(fragments.get(R.id.sm_at));
			SettingFragment stfragment = (SettingFragment) fragments
					.get(R.id.sm_setting);
			ft_st.show(stfragment);
			ft_st.commit();
			((MainFragmentActivity) getActivity()).getSlidingMenu()
					.showContent();
			break;
		case R.id.sm_logout:
			changeBackground(R.id.sm_logout);
			// 注销帐户..
			break;
		}
	}

	private void changeBackground(int id) {
		// TODO Auto-generated method stub
		home.setBackgroundResource(R.color.white);
		at.setBackgroundResource(R.color.white);
		favorite.setBackgroundResource(R.color.white);
		chat.setBackgroundResource(R.color.white);
		channel.setBackgroundResource(R.color.white);
		weiba.setBackgroundResource(R.color.white);
		setting.setBackgroundResource(R.color.white);
		logout.setBackgroundResource(R.color.white);
		switch (id) {
		case R.id.sm_home:
			home.setBackgroundResource(R.color.grey);
			break;
		case R.id.sm_at:
			at.setBackgroundResource(R.color.grey);
			break;
		case R.id.sm_favorite:
			favorite.setBackgroundResource(R.color.grey);
			break;
		case R.id.sm_chat:
			chat.setBackgroundResource(R.color.grey);
			break;
		case R.id.sm_channel:
			channel.setBackgroundResource(R.color.grey);
			break;
		case R.id.sm_weiba:
			weiba.setBackgroundResource(R.color.grey);
			break;
		case R.id.sm_setting:
			setting.setBackgroundResource(R.color.grey);
			break;
		case R.id.sm_logout:
			logout.setBackgroundResource(R.color.grey);
			break;
		}

	}

}
