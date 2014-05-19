package com.thinksns.jkfs.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.UserInfoBean;



/**
 * @author 邓思宇
 * 关注或者粉丝列表的Adapter
 *
 */
public class PeopleAdapter extends BaseAdapter {
	
	private List<UserInfoBean> listPeople;
	private Context context;
	public PeopleAdapter(Context context,List<UserInfoBean> listPeople){
		this.context=context;
		this.listPeople=listPeople;
	}
	
	public void setListItem(List<UserInfoBean> listPeople){
		this.listPeople=listPeople;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listPeople.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listPeople.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) { // the most important part of an adapter
		// TODO Auto-generated method stub

		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.people_item, null);
		}
		
		//catch the info
		UserInfoBean m=listPeople.get(position);
		
		ImageView imgView = (ImageView) convertView.findViewById(R.id.item_head);

		TextView textMusicName = (TextView) convertView.findViewById(R.id.item_name);
		//textMusicName.setText(m.getUname());

		TextView textMusicSinger = (TextView) convertView.findViewById(R.id.item_weibo);
		//textMusicSinger.setText(m.getUid());
		Button button = (Button) convertView.findViewById(R.id.item_button);

		return convertView;
	}
	


}
