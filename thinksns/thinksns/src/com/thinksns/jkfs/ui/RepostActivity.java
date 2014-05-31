package com.thinksns.jkfs.ui;

import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseActivity;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.util.FaceDialog;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.FaceDialog.FaceSelect;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;

/**
 * 转发微博，尚未完成（at用户）
 * 
 * @author wangjia
 * 
 */
public class RepostActivity extends BaseActivity implements OnClickListener,
		FaceSelect {
	private ImageView back;
	private TextView desc;
	private EditText content;
	private TextView count;
	private ImageView send;
	private ImageView add_pic;
	private ImageView at;
	private ImageView topic;
	private ImageView emotion;
	private CheckBox check;
	private RelativeLayout bottom;
	private AccountBean account;
	private WeiboBean weibo;
	private ThinkSNSApplication application;
	private ProgressDialog sendProgress;
	private int comment_origin = 0; // 0不评论原作者，1评论原作者

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast
						.makeText(RepostActivity.this, "网络未连接",
								Toast.LENGTH_SHORT).show();
				break;
			case 1:
				sendDialogDismiss();
				Toast.makeText(RepostActivity.this, "转发成功", Toast.LENGTH_SHORT)
						.show();
				finish();
				break;
			case 2:
				sendDialogDismiss();
				Toast.makeText(RepostActivity.this, "出现意外，转发失败:(",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writeweibo);
		weibo = getIntent().getParcelableExtra("repost");

		application = (ThinkSNSApplication) getApplicationContext();
		account = application.getAccount(this);

		initViews();

		desc.setText("转发微博");
		if (weibo.getTranspond_data() != null) {
			content.setText("//@" + weibo.getUname() + ": "
					+ weibo.getContent());
		} else {
			content.setHint("请输入转发内容:)");
		}
		content.setSelection(0);
	}

	private void initViews() {
		// TODO Auto-generated method stub
		back = (ImageView) findViewById(R.id.write_weibo_back);
		back.setOnClickListener(this);
		desc = (TextView) findViewById(R.id.write_weibo_desc_txt);
		send = (ImageView) findViewById(R.id.write_weibo_send);
		send.setOnClickListener(this);
		count = (TextView) findViewById(R.id.write_weibo_word_count);
		add_pic = (ImageView) findViewById(R.id.write_weibo_add_pic);
		add_pic.setVisibility(View.GONE);
		at = (ImageView) findViewById(R.id.write_weibo_at);
		at.setOnClickListener(this);
		topic = (ImageView) findViewById(R.id.write_weibo_add_topic);
		topic.setOnClickListener(this);
		emotion = (ImageView) findViewById(R.id.write_weibo_emotion);
		emotion.setOnClickListener(this);
		check = (CheckBox) findViewById(R.id.comment_to_origin_check);
		check.setVisibility(View.VISIBLE);
		bottom = (RelativeLayout) findViewById(R.id.write_weibo_bottom);
		content = (EditText) findViewById(R.id.write_weibo_content);
		content.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				int number = s.length();
				count.setText(String.valueOf(number));
				selectionStart = content.getSelectionStart();
				selectionEnd = count.getSelectionEnd();
				if (temp.length() > 140) {
					s.delete(selectionStart - 1, selectionEnd); // 字数超过140，跳转到转发出bug
					int tempSelection = selectionEnd;
					content.setText(s);
					content.setSelection(tempSelection);
				}
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1000:
				// AT..
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.write_weibo_back:
			if (content.getText().toString().trim().length() > 0) {
				AlertDialog.Builder builder = new Builder(this);
				builder.setTitle("提示");
				builder.setMessage("取消转发微博？");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								finish();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				builder.create().show();
			} else {
				finish();
			}
			break;
		case R.id.write_weibo_send:
			if (check.isChecked())
				comment_origin = 1;
			if (content.getText().toString().trim().length() == 0) {
				Toast.makeText(RepostActivity.this, "请输入微博内容",
						Toast.LENGTH_SHORT).show();
			} else {
				if (Utility.isConnected(this)) {
					// 待添加超时判断
					sendDialogShow();
					new Thread() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("app", "api");
							map.put("mod", "WeiboStatuses");
							map.put("act", "repost");
							map.put("id", weibo.getFeed_id());
							map.put("content", content.getText().toString());
							map.put("from", "3");
							map.put("comment", comment_origin + "");
							map.put("oauth_token", account.getOauth_token());
							map.put("oauth_token_secret", account
									.getOauth_token_secret());
							String result = HttpUtility.getInstance()
									.executeNormalTask(HttpMethod.Get,
											HttpConstant.THINKSNS_URL, map);
							if (result.equals("1")) {
								mHandler.sendEmptyMessage(1);
							} else {
								mHandler.sendEmptyMessage(2);
							}
						}
					}.start();

				} else {
					mHandler.sendEmptyMessage(0);
				}
			}
			break;
		case R.id.write_weibo_at:
			// startActivityForResult(new Intent(WriteWeiboActivity.this,
			// AtUserActivity.class), 1000);
			break;
		case R.id.write_weibo_add_topic:
			String origin = content.getText().toString();
			String topicTag = "##";
			content.setText(topicTag + origin);
			content.setSelection(1);
			break;
		case R.id.write_weibo_emotion:
			Utility.hideSoftInput(this);
			FaceDialog.showFaceDialog(this, bottom, bottom.getHeight(), this);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		FaceDialog.release();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (content.getText().toString().trim().length() > 0) {
				AlertDialog.Builder builder = new Builder(this);
				builder.setTitle("提示");
				builder.setMessage("取消转发微博？");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								finish();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				builder.create().show();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void sendDialogShow() {
		if (sendProgress == null) {
			sendProgress = new ProgressDialog(this);
			sendProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			sendProgress.setMessage("正在发送..");
		}
		sendProgress.show();
	}

	private void sendDialogDismiss() {
		if (sendProgress != null && sendProgress.isShowing()) {
			sendProgress.dismiss();
		}
	}

	@Override
	public void onFaceSelect(SpannableString spannableString) {
		// TODO Auto-generated method stub
		content.append(spannableString);
	}

}
