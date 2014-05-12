package com.thinksns.jkfs.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thinksns.jkfs.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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

						} else if (id == female.getId()) {

						}

					}

				});

		regBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String reg_email = email.getText().toString().trim();
				String reg_nick = nick.getText().toString().trim();
				String reg_pwd = pwd.getText().toString().trim();
				String reg_pwdConfirm = pwdConfirm.getText().toString().trim();
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
				if (reg_pwd.length()<6) {
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
					
					// 暂未能实现注册功能

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

		regBtn = (Button) findViewById(R.id.regist_btn);
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

}
