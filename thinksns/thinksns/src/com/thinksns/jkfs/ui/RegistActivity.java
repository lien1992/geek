package com.thinksns.jkfs.ui;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * 新用户注册
 * 
 * @author wangjia
 * 
 */
public class RegistActivity extends Activity {

	private Button regBtn;
	private ImageView back;
	private EditText email;
	private EditText nick;
	private EditText pwd;
	private EditText pwdConfirm;
	private RadioButton male, female;
	private RadioGroup sex;
	private String sexChoice;

	private ProgressDialog sendProgress;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				sendDialogDismiss();
				Toast.makeText(RegistActivity.this, "恭喜，注册成功！",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				sendDialogDismiss();
				Toast.makeText(RegistActivity.this, "[注册失败]" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);

		initViews();

		Intent intent = this.getIntent();
		String qq_nick = intent.getStringExtra("qq_nick");
		if (qq_nick != null) {
			nick.setText(qq_nick);
		}
		String weibo_nick = intent.getStringExtra("weibo_nick");
		if (weibo_nick != null) {
			nick.setText(weibo_nick);
		}

		sex
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int id) {
						// TODO Auto-generated method stub
						if (id == male.getId()) {
							sexChoice = "1";

						} else if (id == female.getId()) {
							sexChoice = "2";
						}

					}

				});

		regBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String reg_email = email.getText().toString().trim();
				final String reg_nick = nick.getText().toString().trim();
				final String reg_pwd = pwd.getText().toString().trim();
				final String reg_pwdConfirm = pwdConfirm.getText().toString()
						.trim();
				if (reg_nick.equals("")) {
					Toast.makeText(RegistActivity.this, "昵称不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (reg_pwd.equals("") || reg_pwdConfirm.equals("")) {
					Toast.makeText(RegistActivity.this, "密码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (reg_pwd.length() < 6) {
					Toast.makeText(RegistActivity.this, "密码长度不能小于6位",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (!reg_pwd.equals(reg_pwdConfirm)) {
					Toast.makeText(RegistActivity.this, "密码和确认密码不一致",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (!checkEmail(reg_email)) {
					Toast.makeText(RegistActivity.this, "电子邮箱格式不正确",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (LoginActivity.isNetworkAvailable(RegistActivity.this)) {
					sendDialogShow();
					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "Oauth");
							map.put("act", "register");
							map.put("uname", reg_nick);
							map.put("sex", sexChoice);
							map.put("password", reg_pwdConfirm);
							map.put("email", reg_email);
							String result = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Post,
											HttpConstant.THINKSNS_URL, map);
							try {
								if (result != null && result.startsWith("{")) {
									JSONObject response = new JSONObject(result);
									String code = response.optString("status");
									String message = response.optString("msg");
									if (code.equals("1")) {
										mHandler.sendEmptyMessage(0);
									} else if (code.equals("0")) {
										Message msg = new Message();
										msg.what = 1;
										msg.obj = message;
										mHandler.sendMessage(msg);
									}
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}.start();

				} else {
					Toast.makeText(RegistActivity.this, "网络未连接",
							Toast.LENGTH_SHORT).show();
				}

			}

		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RegistActivity.this.finish();
			}

		});
	}

	private void initViews() {

		regBtn = (Button) findViewById(R.id.regist_button);
		back = (ImageView) findViewById(R.id.back_login);
		email = (EditText) findViewById(R.id.regist_email);
		nick = (EditText) findViewById(R.id.regist_nick);
		pwd = (EditText) findViewById(R.id.regist_pwd_input);
		pwdConfirm = (EditText) findViewById(R.id.regist_pwd_confirm);
		sex = (RadioGroup) findViewById(R.id.radioGroup_sex);
		male = (RadioButton) findViewById(R.id.radioButton_male);
		female = (RadioButton) findViewById(R.id.radioButton_female);

	}

	/**
	 * 判断邮箱格式是否正确
	 * 
	 * @param email
	 * @return
	 */
	private boolean checkEmail(String email) {
		// TODO Auto-generated method stub
		boolean tag = true;
		String pattern1 = "(\\w+.)+@(\\w+.)+[a-z]{2,3}";
		Pattern pattern = Pattern.compile(pattern1);
		Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}

	private void sendDialogShow() {
		if (sendProgress == null) {
			sendProgress = new ProgressDialog(this);
			sendProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			sendProgress.setMessage("正在注册..");
		}
		sendProgress.show();
	}

	private void sendDialogDismiss() {
		if (sendProgress != null && sendProgress.isShowing()) {
			sendProgress.dismiss();
		}
	}

}
