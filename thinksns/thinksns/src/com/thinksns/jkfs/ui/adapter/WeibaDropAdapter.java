package com.thinksns.jkfs.ui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.WeibaBean;


public class WeibaDropAdapter extends BaseAdapter{
	private Context mContext;  
    private List<WeibaBean> dataList;
    private static DisplayImageOptions options;
    
	public WeibaDropAdapter(Context mContext, List<WeibaBean> dataList) {
		super();
		this.mContext = mContext;
		this.dataList = dataList;
		options = new DisplayImageOptions.Builder().showImageOnLoading(
				R.drawable.no_picture)
				.bitmapConfig(Bitmap.Config.RGB_565) 
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true)
				.cacheOnDisk(true)
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
                    R.layout.weiba_drop_item, null);  
            holder.weiba_drop_img = (ImageView) convertView.findViewById(R.id.weiba_drop_img);  
            holder.weiba_drop_txt = (TextView) convertView.findViewById(R.id.weiba_drop_txt);  
   
            convertView.setTag(holder);  
        } else {  
            holder = (ViewHolder) convertView.getTag();  
        } 
		ImageLoader.getInstance().displayImage(getItem(position).getLogo_url(),
				holder.weiba_drop_img,options);
		holder.weiba_drop_txt.setText(getItem(position).getWeiba_name());
		
		return convertView;
	}
	final class ViewHolder {  
        ImageView weiba_drop_img;  
        TextView weiba_drop_txt;
     }  
}

