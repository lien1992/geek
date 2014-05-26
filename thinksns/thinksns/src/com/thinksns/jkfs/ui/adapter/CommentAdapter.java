package com.thinksns.jkfs.ui.adapter;

import java.util.LinkedList;
import java.util.List;

import com.thinksns.jkfs.bean.CommentBean;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.common.ImageUtils.ImageCallback;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * 微博评论适配器，待完善..
 * 
 * @author wangjia
 * 
 */
public class CommentAdapter extends BaseAdapter {
	LinkedList<CommentBean> cList = new LinkedList<CommentBean>();

	Activity ctx;
	PullToRefreshListView lv;
	LayoutInflater in;

	// 追加微博到表尾
	public void append(List<CommentBean> lists) {
		if (lists == null) {
			return;
		}
		cList.addAll(lists);
		notifyDataSetChanged();
	}

	// 将新微博加到表头
	public void insertToHead(List<CommentBean> lists) {
		if (lists == null) {
			return;
		}
		for (int i = lists.size() - 1; i >= 0; --i) {
			cList.addFirst(lists.get(i));
		}
		notifyDataSetChanged();
	}

	// 图片加载回调
	ImageCallback callback = new ImageCallback() {
		@Override
		public void loadImage(Bitmap bitmap, String imagePath) {
			// TODO Auto-generated method stub
			try {
				ImageView img = (ImageView) lv.findViewWithTag(imagePath);
				img.setImageBitmap(bitmap);
			} catch (NullPointerException ex) {
				Log.e("error", "ImageView = null");
			}
		}
	};

	public CommentAdapter(Activity context, LayoutInflater inflater,
			PullToRefreshListView listView) {
		ctx = context;
		in = inflater;
		lv = listView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return cList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	class ViewHolder {

	}

}
