package com.thinksns.jkfs.ui.fragment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.ChatBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.ChatActivity;
import com.thinksns.jkfs.ui.MainFragmentActivity;
import com.thinksns.jkfs.ui.adapter.ChatListAdapter;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

public class ChatFragment extends Fragment {

    public static final String TAG="ChatFragment";

    public static final int HANDLER_GET_JSON=2;
    public static final int HANDLER_GET_JSON_REFRESH=3;
    public static final int CHANGE_ADAPTER=4;

    private ImageView addButton;
    private String mJsonData;
    private AccountBean mAccountBean;
    private ListView chat_listview;
    private ChatListAdapter chatListAdapter;
    private LinkedList<ChatBean> mListChat=new LinkedList<ChatBean>();
    private LayoutInflater mInflater;
    private View mMenuSlide;
    private ThinkSNSApplication mApplication;
    private String messageID;
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
                case CHANGE_ADAPTER:
                   
                	Toast.makeText(getActivity(), "成功删除私信", 1000).show();
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
            Toast.makeText(getActivity(), "网络未连接", 1000).show();
        }

        return view;
    }

    private void initViews(View view){

        chat_listview=(ListView)view.findViewById(R.id.chat_list);
        mMenuSlide=(View)view.findViewById(R.id.menu_icon);
        mMenuSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragmentActivity) getActivity()).getSlidingMenu()
                        .toggle();
            }
        });

        chatListAdapter=new ChatListAdapter(mListChat,getActivity(),mInflater);
        chat_listview.setAdapter(chatListAdapter);

        chat_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"position"+position+" "+"id"+id);

                TextView userName=(TextView)view.findViewById(R.id.user_name);
                String ID = chatListAdapter.getId(position);
                
                Intent i = new Intent(getActivity(), ChatActivity.class);
                i.putExtra("list_id",ID);
                startActivity(i);
               

                Log.d(TAG,"position"+userName.getText());


            }
        });
        
        chat_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				// TODO Auto-generated method stub
				 final String ID = chatListAdapter.getId(position);
				
				 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				 .setTitle("删除此私信")
				 .setPositiveButton("确定", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub						
						chatListAdapter.removeList(position);
						delMessage(ID);
					}
				})
				.setNegativeButton("取消",null);
				 
				 builder.create();
				 builder.show();
				 
				return true;
			}
        	
		});
    }
    
 // 发送信息
 	public void delMessage(final String id) {
 		if (mApplication.isNewWork(getActivity())) {
 			new Thread(new Runnable() {
 				@Override
 				public void run() {
 					final Map<String, String> map = new HashMap<String, String>();
 					map.put("app", "api");
 					map.put("mod", "Message");
 					map.put("act", "destroy");
 					map.put("list_id", id);
 					map.put("oauth_token", mAccountBean.getOauth_token());
 					map.put("oauth_token_secret",
 							mAccountBean.getOauth_token_secret());
 					messageID = HttpUtility.getInstance().executeNormalTask(
 							HttpMethod.Post, HttpConstant.THINKSNS_URL, map); 					
 					mHandler.sendEmptyMessage(CHANGE_ADAPTER);
 				}
 			}).start();
 		} else {
 			Toast.makeText(getActivity(), "没有联网", 1000).show();
 		}
 	}


}
