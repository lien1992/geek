package com.thinksns.jkfs.ui.fragment;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.AdapterBase;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.ChannelBean;
import com.thinksns.jkfs.bean.ChatBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.ChatActivity;
import com.thinksns.jkfs.ui.MainFragmentActivity;
import com.thinksns.jkfs.ui.adapter.ChatListAdapter;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChatFragment extends Fragment {

    public static final String TAG="ChatFragment";

    public static final int HANDLER_GET_JSON=2;
    public static final int HANDLER_GET_JSON_REFRESH=3;

    private static final String[] m={"全部分组","B型","O型","AB型","其他"};
    private String mJsonData;
    private AccountBean mAccountBean;
    private PullToRefreshListView chat_listview;
    private ChatListAdapter chatListAdapter;
    private List<ChatBean> mListChat=new ArrayList<ChatBean>();
    private LayoutInflater mInflater;
    private View mMenuSlide;
    private ThinkSNSApplication mApplication;
    private ArrayAdapter<String> mAdapter;
    private Spinner mSpinner;
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg){

            switch(msg.what){
                case HANDLER_GET_JSON:
                    try {
                        JSONArray jsonObject=new JSONArray(mJsonData);
                        if(jsonObject!=null)
                            for(int i=0;i<jsonObject.length();i++){
                                JSONObject obj=jsonObject.getJSONObject(i);
                                mListChat.add(ChatBean.JsonToBean(obj));
                            }
                        chatListAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                break;
                case HANDLER_GET_JSON_REFRESH:
                    try {
                        JSONArray jsonObject=new JSONArray(mJsonData);
                        if(jsonObject!=null)
                            chatListAdapter.clearList();
                            for(int i=0;i<jsonObject.length();i++){
                                JSONObject obj=jsonObject.getJSONObject(i);
                                mListChat.add(ChatBean.JsonToBean(obj));
                            }
                        chatListAdapter.notifyDataSetChanged();
                        chat_listview.onRefreshComplete();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG,"onCreateview is create");
        mInflater=inflater;
        mApplication = ThinkSNSApplication.getInstance();
        mAccountBean=mApplication.getAccount(getActivity());
        setHasOptionsMenu(true);
        View view=inflater.inflate(R.layout.chat_fragment_layout,container,false);
        initViews(view);

        chat_listview.setLoadMoreEnable(true);
        if(mApplication.isNewWork(getActivity())){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Map<String, String> map = new HashMap<String, String>();
                    map.put("app","api");
                    map.put("mod","Message");
                    map.put("act","get_message_list");
                    map.put("oauth_token",mAccountBean.getOauth_token());
                    map.put("oauth_token_secret",mAccountBean.getOauth_token_secret());
                    map.put("format","json");
                    mJsonData = HttpUtility.getInstance().executeNormalTask(
                            HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
                    mHandler.sendEmptyMessage(HANDLER_GET_JSON);

                }
            }).start();
        }
        else{
            Toast.makeText(getActivity(), "没有联网", 1000).show();
        }

        chat_listview.setListener(new PullToRefreshListView.RefreshAndLoadMoreListener() {

            @Override
            public void onRefresh() {
                Log.d(TAG,"onRefresh");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Map<String, String> map = new HashMap<String, String>();
                        map.put("app","api");
                        map.put("mod","Message");
                        map.put("act","get_message_list");
                        map.put("oauth_token",mAccountBean.getOauth_token());
                        map.put("oauth_token_secret",mAccountBean.getOauth_token_secret());
                        map.put("format","json");
                        mJsonData = HttpUtility.getInstance().executeNormalTask(
                                HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
                        mHandler.sendEmptyMessage(HANDLER_GET_JSON_REFRESH);

                    }
                }).start();
            }

            @Override
            public void onLoadMore() {
                Log.d(TAG,"onLoadMore");
            }

        });

        return view;
    }

    private void initViews(View view){

        chat_listview=(PullToRefreshListView)view.findViewById(R.id.chat_list);
        mMenuSlide=(View)view.findViewById(R.id.menu_icon);
        mMenuSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragmentActivity) getActivity()).getSlidingMenu()
                        .toggle();
            }
        });


        mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,m);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner=(Spinner)view.findViewById(R.id.group_spinner);
        mSpinner.setAdapter(mAdapter);

        chatListAdapter=new ChatListAdapter(mListChat,getActivity(),mInflater);
        chat_listview.setAdapter(chatListAdapter);

        chat_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"position"+position+" "+"id"+id);

                TextView userName=(TextView)view.findViewById(R.id.user_name);

                startActivity(new Intent(getActivity(), ChatActivity.class));
                Log.d(TAG,"position"+userName.getText());


            }
        });
    }


}
