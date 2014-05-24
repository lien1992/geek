package com.thinksns.jkfs.ui.adapter;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.bean.WeiboListBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author zcc
 * @date 2014-05-19
 * 
 */
public class ChanelFragmentListViewAdapter extends BaseAdapter {
	ArrayList<WeiboBean> mlist;
	LayoutInflater mInflater;

	public ChanelFragmentListViewAdapter(ArrayList<WeiboBean> list,
			LayoutInflater inflater) {

		mlist = list;
		mInflater = inflater;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.main_weibo_listview_item,
					null);

			viewHolder = new ViewHolder();
			viewHolder.user_img = (ImageView) convertView
					.findViewById(R.id.wb_user_img);
			viewHolder.wb_u_name = (TextView) convertView
					.findViewById(R.id.wb_u_name);
			viewHolder.wb_time = (TextView) convertView
					.findViewById(R.id.wb_time);
			viewHolder.wb_text = (TextView) convertView
					.findViewById(R.id.wb_text);
			/*viewHolder.wb_pic = (ImageView) convertView
					.findViewById(R.id.wb_pic);*/

			viewHolder.re_user_name = (TextView) convertView
					.findViewById(R.id.re_user_name);
			viewHolder.re_wb_text = (TextView) convertView
					.findViewById(R.id.re_wb_text);
			viewHolder.wb_from = (TextView) convertView
					.findViewById(R.id.wb_from);
			viewHolder.like = (ImageView) convertView
					.findViewById(R.drawable.like);
			viewHolder.wb_like_count = (TextView) convertView
					.findViewById(R.id.wb_like_count);

			viewHolder.forward = (ImageView) convertView
					.findViewById(R.drawable.forward);
			viewHolder.wb_repost_count = (TextView) convertView
					.findViewById(R.id.wb_repost_count);
			viewHolder.comment = (ImageView) convertView
					.findViewById(R.drawable.comment);
			viewHolder.wb_comment_count = (TextView) convertView
					.findViewById(R.id.wb_comment_count);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 在这里给listview赋值
		// 还没抓数据,这是错的
		// viewHolder.user_img.setText(mlist.get(position).getUname());
		// viewHolder.user_name.setText(mlist.get(position).getContent());
		// viewHolder.time.setText(mlist.get(position).getUname());
		// viewHolder.wb_text.setText(mlist.get(position).getContent());
		// viewHolder.wb_pic.setText(mlist.get(position).getUname());
		// viewHolder.re_user_name.setText(mlist.get(position).getContent());
		// viewHolder.re_wb_text.setText(mlist.get(position).getUname());
		// viewHolder.from.setText(mlist.get(position).getContent());
		// viewHolder.repost_count.setText(mlist.get(position).getContent());
		// viewHolder.comment_count.setText(mlist.get(position).getContent());
		return convertView;
	}

	class ViewHolder {
		ImageView user_img;
		TextView wb_u_name;
		TextView wb_time;
		TextView wb_text;
		ImageView wb_pic;
		TextView re_user_name;
		TextView re_wb_text;
		TextView wb_from;
		ImageView like;
		TextView wb_like_count;
		ImageView forward;
		TextView wb_repost_count;
		ImageView comment;
		TextView wb_comment_count;
	}

}
