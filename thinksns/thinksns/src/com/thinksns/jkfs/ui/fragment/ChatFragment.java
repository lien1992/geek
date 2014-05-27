package com.thinksns.jkfs.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.AdapterBase;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.ChanelBean;
import com.thinksns.jkfs.bean.ChatBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.adapter.ChatListAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment {

    public static final String TAG="ChatFragment";

    public static final int HANDLER_GET_JSON=2;
    private String jsonData;
    private AccountBean accountBean;
    private PullToRefreshListView chat_listview;
    private ChatListAdapter chatListAdapter;
    private List<ChatBean> listChat=new ArrayList<ChatBean>();
    private LayoutInflater mInflater;
    ThinkSNSApplication application;
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg){

            if(msg.what==HANDLER_GET_JSON){
                try {
                   JSONArray jsonObject=new JSONArray(jsonData);
                    if(jsonObject!=null)
                    for(int i=0;i<jsonObject.length();i++){
                        JSONObject obj=jsonObject.getJSONObject(i);
                        listChat.add(ChatBean.JsonToBean(obj));
                    }

                    chatListAdapter=new ChatListAdapter(listChat,getActivity(),mInflater);
                    chat_listview.setAdapter(chatListAdapter);
                    chatListAdapter.notifyDataSetChanged();
                   } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG,"onCreateview is create");
        mInflater=inflater;
        application = ThinkSNSApplication.getInstance();
        accountBean=application.getAccount(getActivity());
        setHasOptionsMenu(true);
        View view=inflater.inflate(R.layout.chat_fragment_layout,container,false);
        chat_listview=(PullToRefreshListView)view.findViewById(R.id.chat_list);
        if(application.isNewWork(getActivity())){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Map<String, String> map = new HashMap<String, String>();
                    map.put("app","api");
                    map.put("mod","Message");
                    map.put("act","get_message_list");
                    map.put("oauth_token",accountBean.getOauth_token());
                    map.put("oauth_token_secret",accountBean.getOauth_token_secret());
                    map.put("format","json");
                    jsonData = HttpUtility.getInstance().executeNormalTask(
                            HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
                    mHandler.sendEmptyMessage(2);

                }
            }).start();
        }
        else{
            Toast.makeText(getActivity(), "没有联网", 1000).show();
        }

        chat_listview.setListener(new PullToRefreshListView.RefreshAndLoadMoreListener() {

            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {

            }
        });

        return view;
    }




}
