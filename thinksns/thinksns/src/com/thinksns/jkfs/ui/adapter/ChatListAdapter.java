package com.thinksns.jkfs.ui.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.ChatBean;
import com.thinksns.jkfs.bean.WeiboBean;

/**
 * Created by mosl on 14-5-27.
 */
public class ChatListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;

	public ChatListAdapter(LinkedList<ChatBean> list, Context context,
			LayoutInflater inflater) {
		mList = list;
		mContext = context;
		mInflater = inflater;
	}

	private LinkedList<ChatBean> mList = new LinkedList<ChatBean>();

	// 更新微博列表，不要原来的数据
	public void removeList(int position) {

		mList.remove(position);
		notifyDataSetChanged();
	}

	public void appendToList(List<ChatBean> list) {
		if (list == null) {
			return;
		}
		mList.addAll(list);
		notifyDataSetChanged();
	}

	public void clearList() {
		mList.clear();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public String getId(int position) {
		ChatBean chatBean = (ChatBean) mList.get(position);
		return chatBean.list_id;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.chat_fragment_item,
					parent, false);
		}
		if (mList.size() == 0)
			return convertView;
		ImageView userImage = (ImageView) convertView
				.findViewById(R.id.user_image);
		TextView userName = (TextView) convertView.findViewById(R.id.user_name);
		TextView content = (TextView) convertView.findViewById(R.id.content);
		ChatBean chatBean = (ChatBean) mList.get(position);
		ImageLoader.getInstance().displayImage(chatBean.from_face, userImage);
		userName.setText(chatBean.from_uname);
		content.setText(chatBean.content);
		return convertView;
	}

	static class ViewHolder {
		TextView text;
		ImageView icon;
	}

}
