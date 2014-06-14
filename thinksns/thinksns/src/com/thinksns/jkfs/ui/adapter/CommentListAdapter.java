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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.CommentBean;
import com.thinksns.jkfs.bean.PostBean;
import com.thinksns.jkfs.bean.PostCommentBean;
import com.thinksns.jkfs.util.common.DateUtils;

public class CommentListAdapter extends BaseAdapter {
	private Context mContext;
	private List<PostCommentBean> dataList;
	private Handler mHandler;
	private static DisplayImageOptions options;
	private String Uid;

	public CommentListAdapter(Context mContext, List<PostCommentBean> dataList,Handler mHandler) {
		super();
		this.mContext = mContext;
		this.dataList = dataList;
		this.mHandler=mHandler;
		Uid=ThinkSNSApplication.getInstance().getUser_Id(mContext);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.no_picture)
				.delayBeforeLoading(500)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).cacheOnDisk(true)
				.displayer(new RoundedBitmapDisplayer(10, 5))
				.build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public PostCommentBean getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(dataList.size()-1-position);
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
					R.layout.comment_item_layout, null);
			holder.comment_avatar = (ImageView) convertView
					.findViewById(R.id.comment_avatar);
			holder.comment_nick_name = (TextView) convertView
					.findViewById(R.id.comment_nick_name);
			holder.comment_date = (TextView) convertView
					.findViewById(R.id.comment_date);
			holder.comment_content = (TextView) convertView
					.findViewById(R.id.comment_content);
			holder.storey = (TextView) convertView.findViewById(R.id.storey);
			holder.reply_button = (ImageView) convertView
					.findViewById(R.id.reply_button);
            holder.delete_button=(ImageView) convertView
					.findViewById(R.id.delete_button);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(
				getItem(position).getAvatar_tiny(), holder.comment_avatar,
				options);
		holder.comment_nick_name.setText(getItem(position).getUname());
		holder.comment_date.setText(DateUtils.getTimeInString(getItem(position)
				.getCtime()));
		holder.comment_content.setText(getItem(position).getContent());
		holder.storey.setText(getItem(position).getStorey() + "楼");
		holder.delete_button.setVisibility(getItem(position).getUid().equals(Uid)?View.VISIBLE:View.GONE);
		final PostCommentBean item=getItem(position);
		final String reply_id=item.getReply_id();
		final int pos=dataList.size()-1-position;
		holder.delete_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHandler.obtainMessage(3,reply_id).sendToTarget();
				dataList.remove(pos);
				notifyDataSetChanged();
			}
		});
		
		holder.reply_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHandler.obtainMessage(4,item).sendToTarget();
			}
		});

		return convertView;
	}

	final class ViewHolder {
		ImageView comment_avatar;
		TextView comment_nick_name;
		TextView comment_date;
		TextView comment_content;
		TextView storey;
		ImageView reply_button;
		ImageView delete_button;
	}
}
