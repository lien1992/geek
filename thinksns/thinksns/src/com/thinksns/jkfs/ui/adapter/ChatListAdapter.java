package com.thinksns.jkfs.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.ChatBean;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mosl on 14-5-27.
 */
public class ChatListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    public ChatListAdapter(List<ChatBean> list,Context context,LayoutInflater inflater){
        mList=list;
        mContext=context;
        mInflater=inflater;
    }

    private  List<ChatBean> mList = new LinkedList<ChatBean>();

    public List<ChatBean> getList(){
        return mList;
    }

    public void appendToList(List<ChatBean> list) {
        if (list == null) {
            return;
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void  notifyDataSetChanged(){

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= mInflater.inflate(R.layout.chat_fragment_item,parent,false);
        }
        if(getList().size()==0)return convertView;
        ImageView userImage=(ImageView)convertView.findViewById(R.id.user_image);
        TextView userName=(TextView)convertView.findViewById(R.id.user_name);
        TextView content=(TextView)convertView.findViewById(R.id.content);
        ChatBean chatBean=(ChatBean)getList().get(position);
        userName.setText(chatBean.from_uname);
        content.setText(chatBean.content);
        return convertView;
    }

    static class ViewHolder {
        TextView text;
        ImageView icon;
    }

    
}
