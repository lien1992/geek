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
import android.widget.Toast;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChatFragment extends Fragment {
    private String jsonData;
    private AccountBean accountBean;
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg){

            if(msg.what==2){
//                try {
//                    //JSONArray jsonObject=new JSONArray(jsonData);
//                   // Log.d("MOSL", jsonObject.toString() + "----");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                //Log.d("MOSL",jsonData);
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        accountBean= ThinkSNSApplication.getInstance().getAccount(getActivity());
        setHasOptionsMenu(true);
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
        return inflater.inflate(R.layout.chat_fragment_layout,container,false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        //oast.makeText(getActivity(), "index is" && menu text is " + item.getTitle(), 1000).show();
        return super.onOptionsItemSelected(item);
    }

}
