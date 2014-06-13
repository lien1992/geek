package com.thinksns.jkfs.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.thinksns.jkfs.R;


public class PostPicAdapter extends BaseAdapter{
	private Context mContext;  
    private List<String> dataList;
    private static DisplayImageOptions options;
    private ImageLoader imageLoader;

    
	public PostPicAdapter(Context mContext, List<String> dataList,ImageLoader imageLoader) {
		super();
		this.mContext = mContext;
		this.dataList = dataList;
		this.imageLoader=imageLoader;
		options = new DisplayImageOptions.Builder()
		        .showImageOnLoading(R.drawable.no_picture)
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
	public String getItem(int position) {
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
		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {  
			view=LayoutInflater.from(mContext).inflate(  
                    R.layout.post_pic_item, null);
			holder = new ViewHolder();
            holder.img=(ImageView)view.findViewById(R.id.post_img);
            view.setTag(holder);  
        } else {  
            holder = (ViewHolder) view.getTag();  
        } 
		imageLoader.displayImage(getItem(position),
				holder.img,options);
		
		return view;	
	}
	final class ViewHolder {  
        ImageView img;  
     }  
}

