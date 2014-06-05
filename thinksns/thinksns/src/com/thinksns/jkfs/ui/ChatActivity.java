package com.thinksns.jkfs.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.google.gson.Gson;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseActivity;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.ChatBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.adapter.ChatAdapter;
import com.thinksns.jkfs.ui.adapter.Message;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mosl on 14-5-24.
 */
public class ChatActivity extends BaseActivity {

    public static final String TAG="ChatActivity";

    private PullToRefreshListView listView;
    private ChatAdapter chatAdapter;
    private EditText sendEditText;
    private String chat_list_id;
    private ThinkSNSApplication application;
    private AccountBean accountBean;
    public static final int HANDLER_GET_JSON=2;
    public static final int HANDLER_GET_JSON_REFRESH=3;

    public List<ChatMessage> list_chatMessage;
    private String mJsonData;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(android.os.Message msg){

            switch(msg.what){
                case HANDLER_GET_JSON:

                    Gson gson=new Gson();
                    JSONArray jsonArray=null;
                    try {
                       jsonArray =new JSONArray(mJsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for(int i=0;i<jsonArray.length();i++){
                        try {
                            JSONObject object=jsonArray.getJSONObject(i);
                            ChatMessage chatMessage= gson.fromJson(object.toString(),ChatMessage.class);
                            list_chatMessage.add(chatMessage);
                            Log.d(TAG,chatMessage.content);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d(TAG,list_chatMessage.size()+"size");
//                    for(int i=0;i<list_chatMessage.size();i++){
//                        Log.d(TAG,list_chatMessage.get(i).from_uname);
//                        Log.d(TAG,list_chatMessage.get(i).content);
//                    }
                    Log.d(TAG, mJsonData);
                    break;
                case HANDLER_GET_JSON_REFRESH:

                    break;
            }
        }
    };
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        if(intent!=null){

            chat_list_id=intent.getStringExtra("data");
            Log.d(TAG,chat_list_id);
        }

        Log.d(TAG,chat_list_id);
        application=ThinkSNSApplication.getInstance();
        accountBean=application.getAccount(this);
        setContentView(R.layout.chat_activity_layout);
        listView=(PullToRefreshListView)findViewById(R.id.chat_list);
        listView.setAdapter(getAdapter());
        sendEditText=(EditText)findViewById(R.id.messageInput);

        new Thread(new Runnable() {
            @Override
            public void run() {

                final Map<String, String> map = new HashMap<String, String>();
                map.put("app","api");
                map.put("mod","Message");
                map.put("act","get_message_detail");
                map.put("id",chat_list_id);
                Log.d(TAG,chat_list_id);
                map.put("oauth_token",accountBean.getOauth_token());
                map.put("oauth_token_secret",accountBean.getOauth_token_secret());
                map.put("format","json");
                mJsonData= HttpUtility.getInstance().executeNormalTask(
                        HttpMethod.Get, HttpConstant.THINKSNS_URL, map);
                mHandler.sendEmptyMessage(HANDLER_GET_JSON);

            }
        }).start();
    }

    private BaseAdapter getAdapter(){
        chatAdapter=new ChatAdapter(this, getMyData());
        return chatAdapter;
    }

    private List<Message> getMyData(){

        List<Message> msgList = new ArrayList<Message>();
        Message msg;
        msg = new Message();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("100");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_TIME_TIP);
        msg.setValue("2012-12-23 下午2:23");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("99");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("98");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_TIME_TIP);
        msg.setValue("2012-12-23 下午2:25");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("97");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("96");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("95");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_TIME_TIP);
        msg.setValue("2012-12-23 下午3:25");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("94");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_LEFT_IMAGE);
        msg.setValue("93");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("92");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("91");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_LEFT_IMAGE);
        msg.setValue("0");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("1");
        msgList.add(msg);


        msg = new Message();
        msg.setType(ChatAdapter.VALUE_LEFT_IMAGE);
        msg.setValue("2");
        msgList.add(msg);


        msg = new Message();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("3");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_LEFT_AUDIO);
        msg.setValue("4");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("5");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("6");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("7");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("8");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_RIGHT_IMAGE);
        msg.setValue("9");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("10");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("11");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("12");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        msg.setValue("13");
        msgList.add(msg);

        msg = new Message();
        msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
        msg.setValue("14");
        msgList.add(msg);
        return msgList;

    }

    public void sendButton(View view) {

        String input=sendEditText.getText().toString();
        Message msg=new Message();
        msg.setValue(input);
        msg.setType(1);
        chatAdapter.addMessage(msg);
        sendEditText.setText("");
    }

    class ChatMessage{

        public String message_id;
        public String list_id;
        public String from_id;
        public String content;
        public String is_del;
        public String mtime;
        public String from_uname;
        public String from_face;
        public String timestmap;
        public String ctime;

    }
}