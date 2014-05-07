package com.thinksns.jkfs.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.sina.PreferenceUtil;
import com.thinksns.jkfs.sina.SinaWeiboUtil;
import com.thinksns.jkfs.sina.Sinas;
import com.thinksns.jkfs.sina.WeiboListener;
import com.thinksns.jkfs.support.http.HttpMethod;
import com.thinksns.jkfs.support.http.HttpUtility;
import com.thinksns.jkfs.support.util.DES;
import com.thinksns.jkfs.support.util.MD5;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 用户登录+第三方登录
 * 
 * @author wangjia
 * 
 */

public class LoginActivity extends Activity implements OnClickListener {

	private Button loginBtn;
	private ImageView regist;
	private EditText name;
	private EditText pwd;
	private ImageView qq, weibo;
	private ProgressDialog mDialog;
	private static Handler handler;
	private String jsonData;

	public static final String THINKSNS_URL = "http://demo.thinksns.com/t3/";

	/** 第三方登录ThinkSNS暂未实现，目前仅获取用户名发送到RegistActivity **/
	
	// QQ第三方登录
	public static final String APP_ID = "101073421";
	private Tencent mTencent;
	private UserInfo mInfo;

	// 新浪微博第三方登录
	private SinaWeiboUtil weibo_sina;
	private String accessToken;
	private String mUserId;
	private HashMap<String, Object> hashMap;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (mDialog != null) {
				mDialog.dismiss();
			}
			if (msg.what == 0) {
				JSONObject response = (JSONObject) msg.obj;
				if (response.has("nickname")) {
					try {
						String nickName = response.getString("nickname");
						if ("".equals(nickName) || nickName == null) {
							return;
						}
						Intent intent = new Intent(LoginActivity.this,
								RegistActivity.class);
						intent.putExtra("qq_nick", nickName);
						startActivity(intent); // 待定

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if (msg.what == 1) {
				String nickName = (String) hashMap.get("name");
				if ("".equals(nickName) || nickName == null) {
					return;
				}
				Intent intent = new Intent(LoginActivity.this,
						RegistActivity.class);
				intent.putExtra("weibo_nick", nickName);
				startActivity(intent); // 暂时仅传递微博昵称给注册页面

			} else if (msg.what == 2) {
				if (!TextUtils.isEmpty(jsonData)) {
					try {
						JSONObject response = new JSONObject(jsonData);
						String code = response.optString("code");
						if (!TextUtils.isEmpty(code)) {
							Toast.makeText(LoginActivity.this, "用户名或密码错误，登录失败",
									Toast.LENGTH_SHORT).show();
							return;
						}
						AccountBean ac = new AccountBean();
						ac.setUid(response.getString("uid"));
						ac.setOauth_token(response.getString("oauth_token"));
						ac.setOauth_token_secret(response
								.getString("oauth_token_secret"));
						Intent intent = new Intent(LoginActivity.this,
								MainActivity.class);
						intent.putExtra("acount", ac);
						startActivity(intent);
						LoginActivity.this.finish();

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		handler = new Handler();

		initViews();

		mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
		weibo_sina = SinaWeiboUtil.getInstance(this);
	}

	private void initViews() {

		loginBtn = (Button) findViewById(R.id.login_btn);
		loginBtn.setOnClickListener(this);
		name = (EditText) findViewById(R.id.user_name);
		pwd = (EditText) findViewById(R.id.user_pwd);
		regist = (ImageView) findViewById(R.id.regist_btn);
		regist.setOnClickListener(this);
		qq = (ImageView) findViewById(R.id.qq_btn);
		qq.setOnClickListener(this);
		weibo = (ImageView) findViewById(R.id.weibo_btn);
		weibo.setOnClickListener(this);
		mDialog = new ProgressDialog(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_btn:
			String u_name = name.getText().toString().trim();
			String u_pwd = pwd.getText().toString().trim();
			if (u_name.equals("")) {
				Toast.makeText(LoginActivity.this, "用户名不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (u_pwd.equals("")) {
				Toast
						.makeText(LoginActivity.this, "密码不能为空",
								Toast.LENGTH_SHORT).show();
				return;
			}
			if (u_pwd.length() < 6) {
				Toast.makeText(LoginActivity.this, "密码出错，长度不能小于6位",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (isNetworkAvailable(this)) {
				mDialog = ProgressDialog.show(LoginActivity.this, "",
						"正在登录...", true);
				final Map<String, String> map = new HashMap<String, String>();
				DES crypt = new DES();
				map.put("app", "api");
				map.put("mod", "Oauth");
				map.put("act", "authorize");
				map.put("uid", crypt.encrypt(u_name)); // 用户名用DES加密
				map.put("passwd", crypt.encrypt(MD5.getMD5Code(u_pwd))); // 密码用MD5+DES加密
				new Thread(new Runnable() {
					@Override
					public void run() {
						jsonData = HttpUtility.getInstance().executeNormalTask(
								HttpMethod.Post, THINKSNS_URL, map);
						mHandler.sendEmptyMessage(2);
					}
				}).start();
			} else {
				Toast.makeText(LoginActivity.this, "网络未连接", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.regist_btn:
			startActivity(new Intent(LoginActivity.this, RegistActivity.class));
			break;
		case R.id.qq_btn:
			qqLogin();
			break;
		case R.id.weibo_btn:
			weiboLogin();
			break;
		}

	}

	private void qqLogin() {
		if (!mTencent.isSessionValid()) {
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					updateUserInfo();
				}
			};
			mTencent.login(this, "get_simple_userinfo", listener);
		} else {
			mTencent.logout(this);
		}

	}

	private void updateUserInfo() {
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onComplete(final Object response) {
					Message msg = new Message();
					msg.obj = response;
					msg.what = 0;
					mHandler.sendMessage(msg);
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
				}

				@Override
				public void onError(UiError arg0) {
					// TODO Auto-generated method stub
				}
			};

			mInfo = new UserInfo(this, mTencent.getQQToken());
			mInfo.getUserInfo(listener);
		}
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {
		}

		@Override
		public void onError(UiError e) {
		}

		@Override
		public void onCancel() {
		}
	}

	private void weiboLogin() {
		weibo_sina.auth(new WeiboListener() {
			@Override
			public void onResult() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						String sinaToken = PreferenceUtil.getInstance(
								LoginActivity.this).getString(
								Sinas.PREF_SINA_ACCESS_TOKEN, "");
						if (sinaToken.isEmpty()) {
							Toast.makeText(LoginActivity.this, "未授权",
									Toast.LENGTH_SHORT).show();
						} else {
							// 初始化新浪微博，判断是否授权过期
							SinaWeiboUtil.getInstance(LoginActivity.this)
									.initSinaWeibo(new WeiboListener() {
										@Override
										public void init(boolean isValid) {
											if (isValid) {
												accessToken = PreferenceUtil
														.getInstance(
																LoginActivity.this)
														.getString(
																Sinas.PREF_SINA_ACCESS_TOKEN,
																"");
												mUserId = PreferenceUtil
														.getInstance(
																LoginActivity.this)
														.getString(
																Sinas.PREF_SINA_UID,
																"");

												Editor editor = LoginActivity.this
														.getSharedPreferences(
																"thinksns_prefs",
																Context.MODE_WORLD_WRITEABLE)
														.edit();
												editor.putString("token_Sina",
														accessToken);
												editor.putString("uid_Sina",
														mUserId);
												editor.commit();

												handler.post(new Runnable() {

													@Override
													public void run() {
														toHttpSinaURL(mHandler);
													}
												});

											} else {
												Toast.makeText(
														LoginActivity.this,
														"微博授权已过期，请重新绑定",
														Toast.LENGTH_SHORT)
														.show();
											}
										}
									});
						}
					}
				});
			}
		});

	}

	/**
	 * 新浪微博Get方式获取用户信息
	 * 
	 * @param uid
	 * @param access_token
	 * @return
	 */
	private String query(String http, String uid, String access_token) {
		String queryString = "uid=" + uid + "&access_token=" + access_token;
		// url
		String url = http + "?" + queryString;
		// 查询返回结果
		String string = queryStringForGet(url);
		return string;

	}

	// 发送Get请求，获得查询结果
	private String queryStringForGet(String url) {
		HttpGet request = new HttpGet(url);
		String result = null;
		try {
			HttpResponse response = new DefaultHttpClient().execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 异步获取资料
	 */
	private void toHttpSinaURL(final Handler handler) {
		mDialog = ProgressDialog.show(LoginActivity.this, "", "请稍候...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				hashMap = getMessage_Sina(query(Sinas.URL_USERS_SHOW, mUserId,
						accessToken));
				handler.obtainMessage(1, hashMap).sendToTarget();
			}
		}).start();

	}

	/**
	 * 解析数据得到用户信息
	 * 
	 * @param string
	 * @return
	 */
	protected HashMap<String, Object> getMessage_Sina(String string) {
		// TODO Auto-generated method stub
		JSONObject object = null;
		HashMap<String, Object> hash = new HashMap<String, Object>();
		if (!"".equals(string) && string != null && string.trim().length() > 0) {
			try {
				object = new JSONObject(string);
				hash.put("id", object.getString("idstr"));
				if (object.getString("gender").equals("f")) {
					hash.put("sex", "0");
				} else {
					hash.put("sex", "1");
				}
				hash.put("name", object.getString("screen_name"));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return hash;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		weibo_sina.authCallBack(requestCode, resultCode, data);
	}

	long oldTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - oldTime < 3 * 1000) {
				finish();
			} else {
				Toast
						.makeText(LoginActivity.this, "再按一次退出",
								Toast.LENGTH_SHORT).show();
				oldTime = currentTime;
			}
		}
		return true;
	}

	/**
	 * 判断网络是否可用
	 * 
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

}
