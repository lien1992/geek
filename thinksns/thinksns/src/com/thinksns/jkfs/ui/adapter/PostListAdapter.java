package com.thinksns.jkfs.ui.adapter;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.PostBean;
import com.thinksns.jkfs.ui.view.ScrollTextView;
import com.thinksns.jkfs.util.common.DateUtils;
import com.thinksns.jkfs.util.db.WeibaOperator;

public class PostListAdapter extends BaseAdapter {
	private Context mContext;
	private LinkedList<PostBean> dataList;
	private static DisplayImageOptions options;
	private ImageLoader imageLoader;
	private Handler mHandler;

	public PostListAdapter(Context mContext, LinkedList<PostBean> dataList,
			ImageLoader imageLoader, Handler mHandler) {
		super();
		this.mContext = mContext;
		this.dataList = dataList;
		this.imageLoader = imageLoader;
		this.mHandler = mHandler;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.no_picture)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheOnDisk(true)
				.build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public PostBean getItem(int position) {
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
					R.layout.post_item_layout, null);
			holder.weiba_name = (ScrollTextView) convertView
					.findViewById(R.id.weiba_name);
			holder.post_title = (TextView) convertView
					.findViewById(R.id.post_title);
			holder.post_content = (TextView) convertView
					.findViewById(R.id.post_content);
			holder.post_user = (TextView) convertView
					.findViewById(R.id.post_user);
			holder.post_date = (TextView) convertView
					.findViewById(R.id.post_date);
			holder.post_comment = (TextView) convertView
					.findViewById(R.id.post_comment);
			holder.post_view = (TextView) convertView
					.findViewById(R.id.post_view);
			holder.post_collect = (ImageView) convertView
					.findViewById(R.id.post_collect);
			holder.post_pic = (LinearLayout) convertView
					.findViewById(R.id.post_pic);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String content = getItem(position).getContent();
		holder.weiba_name.setText("["
				+ WeibaOperator.getInstance().queryWeibaNameByID(
						getItem(position).getWeiba_id()) + "]");
		holder.post_title.setText(getItem(position).getTitle());
		holder.post_content.setText(getAbstract(content));
		holder.post_user.setText(getItem(position).getUname());
		holder.post_date.setText(DateUtils.getTimeInString(getItem(position)
				.getPost_time()));
		holder.post_comment.setText("(" + getItem(position).getReply_count()
				+ ")");
		holder.post_view.setText("(" + getItem(position).getRead_count() + ")");
		final String post_id= getItem(position).getPost_id();
		final int pos=position;
		holder.post_collect.setImageResource(getItem(position).getFavorite() == 1 ? R.drawable.heart
				: R.drawable.favourite);
		holder.post_collect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean followState=getItem(pos).getFavorite() == 1;
				((ImageView) v)
						.setImageResource(!followState? R.drawable.heart
								: R.drawable.favourite);
				dataList.get(pos).setFavorite(!followState?1:0);
				mHandler.obtainMessage(!followState?3:4, post_id)
						.sendToTarget();
			}
		});
		List<String> url = parseIntoURL(content);
		if (url != null) {
			holder.post_pic.setVisibility(View.VISIBLE);
			holder.post_pic.removeAllViews();
			for (int i = 0; i < url.size(); i++) {
				ImageView imageView = new ImageView(mContext);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
				imageView.setAdjustViewBounds(true);
				imageView.setPadding(5, 5, 5, 5);
				imageLoader.displayImage(url.get(i), imageView, options);
				holder.post_pic.addView(imageView);
			}
		} else {
			holder.post_pic.setVisibility(View.GONE);
		}

		return convertView;
	}

	final class ViewHolder {
		ScrollTextView weiba_name;
		TextView post_title;
		TextView post_content;
		TextView post_user;
		TextView post_date;
		TextView post_comment;
		TextView post_view;
		ImageView post_collect;
		LinearLayout post_pic;
	}

	public String getAbstract(String content) {
		String text = content;
		if (text.length() > 250) {
			text = text.substring(0, 200);
		}
		return Jsoup.parse(text).text();
	}

	public List<String> parseIntoURL(String content) {
		Document doc = Jsoup.parse(content);
		Elements imgs = doc.getElementsByTag("img");
		List<String> url = new LinkedList<String>();
		for (Element img : imgs) {
			url.add(img.attr("abs:src"));
		}
		if (url.size() > 0) {
			return url;
		}
		return null;
	}
}
