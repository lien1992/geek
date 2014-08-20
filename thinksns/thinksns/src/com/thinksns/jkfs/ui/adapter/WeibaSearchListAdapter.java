package com.thinksns.jkfs.ui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.PostBean;
import com.thinksns.jkfs.bean.WeibaBean;

public class WeibaSearchListAdapter extends BaseAdapter {
	private Context mContext;
	private List<WeibaBean> dataList;
	private Handler mHandler;
	private static DisplayImageOptions options;

	public WeibaSearchListAdapter(Context mContext, List<WeibaBean> dataList,Handler mHandler) {
		super();
		this.mContext = mContext;
		this.dataList = dataList;
		this.mHandler=mHandler;
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.no_picture)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.cacheOnDisk(true)
				.displayer(new SimpleBitmapDisplayer())
				.build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public WeibaBean getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.weiba_search_item, null);
			holder.weiba_img = (ImageView) convertView
					.findViewById(R.id.weiba_img);
			holder.weiba_name = (TextView) convertView
					.findViewById(R.id.weiba_name);
			holder.weiba_intro = (TextView) convertView
					.findViewById(R.id.weiba_intro);
			holder.follower_count = (TextView) convertView
					.findViewById(R.id.follower_count);
			holder.thread_count = (TextView) convertView
					.findViewById(R.id.thread_count);
			holder.follow_weiba = (TextView) convertView
					.findViewById(R.id.follow_weiba);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(getItem(position).getLogo_url(),
				holder.weiba_img, options);
		holder.weiba_name.setText(getItem(position).getWeiba_name());
		holder.weiba_intro.setText(getItem(position).getIntro());
		holder.follower_count.setText(getFormalCount(getItem(position).getFollower_count()));
		holder.thread_count.setText(getFormalCount(getItem(position).getThread_count()));
		final int pos=position;
		final String weiba_id=getItem(position).getWeiba_id();
		holder.follow_weiba.setText(getItem(position).getFollow_state() == 1? "取消关注":"关    注");
		holder.follow_weiba.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean followState=getItem(pos).getFollow_state() == 1;
				//((TextView)v).setText(!followState? "取消关注":"关    注");
				mHandler.obtainMessage(followState?4:3,weiba_id).sendToTarget();
				getItem(pos).setFollow_state(followState?0:1);
				notifyDataSetChanged();
			}
		});
		

		return convertView;
	}

	private String getFormalCount(String arg){
		long count=Long.parseLong(arg);
		if(count<=10000){
			return arg;
		}else{
			return count/10000+"万";
		}
	}
	
	final class ViewHolder {
		ImageView weiba_img;
		TextView weiba_name;
		TextView weiba_intro;
		TextView follower_count;
		TextView thread_count;
		TextView follow_weiba;
	}
}
