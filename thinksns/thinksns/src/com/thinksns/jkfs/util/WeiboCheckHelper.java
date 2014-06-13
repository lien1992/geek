package com.thinksns.jkfs.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;


public class WeiboCheckHelper implements Runnable{
	
	private static final String TAG = "WeiboCheckHelper";
	private static final String APP = "api";
	private static final String MOD = "Checkin";
	private static final String ACT_CHECKIN = "checkin";
	private static final String GET_CHECK_INFO = "get_check_info";
	private static String OAUTH_TOKEN;
	private static String OAUTH_TOKEN_SECRECT;
	private Toast toast;
    private Context mContext;
	
	public WeiboCheckHelper(Context mContext,Toast toast){
		this.mContext=mContext;
		this.toast=toast;
		ThinkSNSApplication application = ThinkSNSApplication.getInstance();
		OAUTH_TOKEN = application.getOauth_token(mContext);
		OAUTH_TOKEN_SECRECT = application.getOauth_token_secret(mContext);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		boolean netState = Utility.isConnected(mContext);
		Log.w("什么情况", "网络状态"+netState);
		if(netState){
			Log.w("什么情况", "走网络"+netState);
			checkViaNet();
		}else{
			Log.w("什么情况", "没网络"+netState);
			LinearLayout view=(LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.checkin_fail,null);
			TextView status=(TextView) view.findViewById(R.id.checkin_status);
			status.setText("网络不给力，稍后再试");
			toast.setView(view);
			toast.show();
		}
	}


	public void checkViaNet() {
		boolean checked=getCheckinStatus();
		if(checked){
			RelativeLayout view=(RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.checkin_succuss,null);
			TextView status=(TextView) view.findViewById(R.id.checkin_status);
			TextView info=(TextView) view.findViewById(R.id.checkin_info);
			info.setVisibility(View.GONE);
			status.setText("你已经签到过了");
			toast.setView(view);
			toast.show();	
		}else{
			final Map<String, String> check = new HashMap<String, String>();
			check.put("app", APP);
			check.put("mod", MOD);
			check.put("act", ACT_CHECKIN);
			check.put("oauth_token", OAUTH_TOKEN);
			check.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
			String jsonData = getJSONData(check);
			JSONObject jsonObj;
			if (jsonData != null) {
				try {
					String con_num,total_num,day;
					boolean ischeck;
					jsonObj = new JSONObject(jsonData);
					ischeck=jsonObj.optBoolean("ischeck");
					//con_num=jsonObj.optString("con_num");
					total_num=jsonObj.optString("total_num");
					//day=jsonObj.optString("day");
					if(ischeck){
						RelativeLayout view=(RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.checkin_succuss,null);
						TextView info=(TextView) view.findViewById(R.id.checkin_info);
						info.setText("你已签到"+total_num+"天");
						toast.setView(view);
						toast.show();
					}else{
						LinearLayout view=(LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.checkin_fail,null);
						toast.setView(view);
						toast.show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			} else {
				LinearLayout view=(LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.checkin_fail,null);
				TextView status=(TextView) view.findViewById(R.id.checkin_status);
				status.setText("网络不给力，稍后再试");
				toast.setView(view);
				toast.show();
			}	
		}
		
		
	}
	public boolean getCheckinStatus(){
		final Map<String, String> check = new HashMap<String, String>();
		check.put("app", APP);
		check.put("mod", MOD);
		check.put("act", GET_CHECK_INFO);
		check.put("oauth_token", OAUTH_TOKEN);
		check.put("oauth_token_secret", OAUTH_TOKEN_SECRECT);
		String jsonData = getJSONData(check);
		try {
			JSONObject jsonObj=new JSONObject(jsonData);
			boolean ischeck=jsonObj.optBoolean("ischeck");
			return ischeck;	
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
	}
	
	public String getJSONData(Map<String, String> map) {
		try {
			return HttpUtility.getInstance().executeNormalTask(HttpMethod.Get,
					HttpConstant.GET_MESSAGE_LIST, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

}