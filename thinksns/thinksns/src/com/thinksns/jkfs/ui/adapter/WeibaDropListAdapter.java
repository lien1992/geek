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
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.PostBean;
import com.thinksns.jkfs.bean.WeibaBean;
import com.thinksns.jkfs.ui.view.ScrollTextView;
import com.thinksns.jkfs.util.common.DateUtils;
import com.thinksns.jkfs.util.db.WeibaOperator;

public class WeibaDropListAdapter extends BaseAdapter {
	private Context mContext;
	private LinkedList<WeibaBean> dataList;
	private static DisplayImageOptions options;
	private ImageLoader imageLoader;

	public WeibaDropListAdapter(Context mContext, LinkedList<WeibaBean> dataList) {
		super();
		this.mContext = mContext;
		this.dataList = dataList;
		this.imageLoader = ImageLoader.getInstance();	
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.no_picture)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).cacheOnDisk(true)
				.displayer(new RoundedBitmapDisplayer(5,5))
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
			LinearLayout  rootView = (LinearLayout) LayoutInflater.from(mContext).inflate(
					R.layout.weiba_drop_list_item, null);
			ImageView weiba_logo= (ImageView) rootView
					.findViewById(R.id.weiba_logo);
			TextView weiba_name = (TextView) rootView
					.findViewById(R.id.weiba_name);
			imageLoader.displayImage(getItem(position).getLogo_url(),weiba_logo,options);
			weiba_name.setText(getItem(position).getWeiba_name());	
		return rootView;
	}

}
