package com.thinksns.jkfs.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseActivity;
import com.thinksns.jkfs.ui.adapter.ChatAdapter;
import com.thinksns.jkfs.ui.adapter.Message;
import com.thinksns.jkfs.ui.view.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mosl on 14-5-24.
 */
public class ChatActivity extends BaseActivity {

    public static final String TAG="ChatActivity";

    private PullToRefreshListView listView;
    private ChatAdapter chatAdapter;
    private EditText sendEditText;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_layout);
        listView=(PullToRefreshListView)findViewById(R.id.chat_list);
        listView.setAdapter(getAdapter());
        sendEditText=(EditText)findViewById(R.id.messageInput);
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
}