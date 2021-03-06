package com.thinksns.jkfs.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.BaseActivity;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.DraftBean;
import com.thinksns.jkfs.constant.BaseConstant;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.util.FaceDialog;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.FaceDialog.FaceSelect;
import com.thinksns.jkfs.util.common.ImageUtils;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;
import com.thinksns.jkfs.util.http.HttpUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 发送微博
 * 
 * @author wangjia
 * 
 */
public class WriteWeiboActivity extends BaseActivity implements
		OnClickListener, FaceSelect {
	private ImageView back;
	private EditText content;
	private TextView count;
	private ImageView send;
	private ImageView add_pic;
	private ImageView at;
	private ImageView topic;
	private ImageView emotion;
	private ImageView pic;
	private RelativeLayout bottom;
	private AccountBean account;
	private ThinkSNSApplication application;
	private boolean hasImage;
	private ProgressDialog sendProgress;
	private Uri imageFileUri;
	private String picPath = "";
	private int draft_id = -1;
	private int image_upload_quality = 1;

	private DbUtils db;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				sendDialogDismiss();
				Toast.makeText(WriteWeiboActivity.this, "微博发送成功",
						Toast.LENGTH_SHORT).show();
				Intent in = new Intent();
				in.setAction(BaseConstant.REFRESH_MAIN_WEIBO_LIST_BROADCAST);
				WriteWeiboActivity.this.sendBroadcast(in);
				finish();
				break;
			case 1:
				break;
			case 2:
				Toast.makeText(WriteWeiboActivity.this, "网络未连接",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				sendDialogDismiss();
				Toast.makeText(WriteWeiboActivity.this, "出现意外，微博发送失败:(",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writeweibo);
		application = (ThinkSNSApplication) getApplicationContext();
		account = application.getAccount(this);
		Intent intent = getIntent();
		draft_id = intent.getIntExtra("id", -1);
		String draft = intent.getStringExtra("draft");

		initViews();

		if (!TextUtils.isEmpty(draft))
			content.setText(draft);

		db = DbUtils.create(this, "thinksns2.db");
		db.configDebug(true);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String up_quality = prefs
				.getString(BaseConstant.IMAGE_QUALITY_KEY, "1");
		image_upload_quality = Integer.parseInt(up_quality);

	}

	private void initViews() {
		// TODO Auto-generated method stub
		back = (ImageView) findViewById(R.id.write_weibo_back);
		back.setOnClickListener(this);
		send = (ImageView) findViewById(R.id.write_weibo_send);
		send.setOnClickListener(this);
		count = (TextView) findViewById(R.id.write_weibo_word_count);
		add_pic = (ImageView) findViewById(R.id.write_weibo_add_pic);
		add_pic.setOnClickListener(this);
		at = (ImageView) findViewById(R.id.write_weibo_at);
		at.setOnClickListener(this);
		topic = (ImageView) findViewById(R.id.write_weibo_add_topic);
		topic.setOnClickListener(this);
		emotion = (ImageView) findViewById(R.id.write_weibo_emotion);
		emotion.setOnClickListener(this);
		pic = (ImageView) findViewById(R.id.write_weibo_content_pic);
		pic.setOnClickListener(this);
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
				selectionEnd = content.getSelectionEnd();
				if (temp.length() > 140) {
					Toast.makeText(WriteWeiboActivity.this, "字数超限，最多输入140字 :)",
							Toast.LENGTH_SHORT).show();
					s.delete(selectionStart - 1, selectionEnd);
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
				// at用户
				String name = intent.getStringExtra("at_name");
				String origin = content.getText().toString();
				int index = content.getSelectionStart();
				StringBuilder stringBuilder = new StringBuilder(origin);
				stringBuilder.insert(index, name);
				content.setText(stringBuilder.toString());
				content.setSelection(index + name.length());
				break;
			case 1001:
				// 拍照
				if (TextUtils.isEmpty(content.getText().toString())) {
					content.setText("分享图片");
					content.setSelection(content.getText().toString().length());
				}
				// fix OOM exception
				picPath = getPicPathFromUri(imageFileUri);
				Log.d("wj", "result from camera, image path:" + picPath);
				InputStream fis;
				try {
					fis = new FileInputStream(picPath);
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inTempStorage = new byte[100 * 1024];// 100kb缓存
					opts.inPreferredConfig = Bitmap.Config.RGB_565;
					opts.inPurgeable = true;// 可被回收
					opts.inSampleSize = 4;// 缩放为1/4
					opts.inInputShareable = true;
					Bitmap btp = BitmapFactory.decodeStream(fis, null, opts);
					pic.setImageBitmap(btp);
					pic.setVisibility(View.VISIBLE);
					hasImage = true;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 1002:
				// 本地图片
				if (TextUtils.isEmpty(content.getText().toString())) {
					content.setText("分享图片");
					content.setSelection(content.getText().toString().length());
				}
				Uri imageFileUri = intent.getData();
				picPath = getPicPathFromUri(imageFileUri);
				InputStream lfis;
				try {
					lfis = new FileInputStream(picPath);
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inTempStorage = new byte[100 * 1024];// 100kb缓存
					opts.inPreferredConfig = Bitmap.Config.RGB_565;
					opts.inPurgeable = true;// 可被回收
					opts.inSampleSize = 4;// 缩放为1/4
					opts.inInputShareable = true;
					Bitmap btp = BitmapFactory.decodeStream(lfis, null, opts);
					pic.setImageBitmap(btp);
					pic.setVisibility(View.VISIBLE);
					hasImage = true;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				builder.setMessage("取消发送微博？");
				builder.setPositiveButton("取消发送",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								if (draft_id != -1) {
									DraftBean draft = new DraftBean();
									draft.setId(draft_id);
									draft.setContent(content.getText()
											.toString());
									try {
										db.delete(draft);
										Intent in = new Intent();
										in
												.setAction(BaseConstant.DRAFT_BROADCAST);
										WriteWeiboActivity.this
												.sendBroadcast(in);
									} catch (DbException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								dialog.dismiss();
								finish();
							}
						});
				builder.setNegativeButton("存入草稿箱",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (draft_id == -1) {
									DraftBean draft = new DraftBean();
									draft.setContent(content.getText()
											.toString());
									try {
										db.save(draft);
										Intent in = new Intent();
										in
												.setAction(BaseConstant.DRAFT_BROADCAST);
										WriteWeiboActivity.this
												.sendBroadcast(in);
									} catch (DbException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								dialog.dismiss();
								finish();
							}
						});
				builder.create().show();
			} else {
				finish();
			}
			break;
		case R.id.write_weibo_send:
			if (content.getText().toString().trim().length() == 0) {
				Toast.makeText(WriteWeiboActivity.this, "请输入微博内容",
						Toast.LENGTH_SHORT).show();
			} else {
				if (Utility.isConnected(this)) {
					// 待添加超时判断

					if (!hasImage) {
						sendDialogShow();
						new Thread() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Gson gson = new Gson();
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("app", "api");
								map.put("mod", "WeiboStatuses");
								map.put("act", "update");
								map
										.put("content", content.getText()
												.toString());
								map.put("from", "2");

								map
										.put("oauth_token", account
												.getOauth_token());
								map.put("oauth_token_secret", account
										.getOauth_token_secret());
								String result = HttpUtility.getInstance()
										.executeNormalTask(HttpMethod.Get,
												HttpConstant.THINKSNS_URL, map);
								Log.d("post weibo result", result);
								if (result.equals("0")) {
									mHandler.sendEmptyMessage(3);
								} else {
									mHandler.sendEmptyMessage(0);
								}
							}
						}.start();
					} else {
						sendDialogShow();
						final String uploadPicPath = ImageUtils.compressPic(
								this, picPath, image_upload_quality);
						new Thread() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Gson gson = new Gson();
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("app", "api");
								map.put("mod", "WeiboStatuses");
								map.put("act", "upload");
								map
										.put("content", content.getText()
												.toString());
								map.put("from", "2");
								map
										.put("oauth_token", account
												.getOauth_token());
								map.put("oauth_token_secret", account
										.getOauth_token_secret());
								boolean result = HttpUtils.doUploadFile(
										HttpConstant.THINKSNS_URL, map,
										uploadPicPath);
								if (result == true) {
									mHandler.sendEmptyMessage(0);
								} else {
									mHandler.sendEmptyMessage(3);
								}

							}
						}.start();

					}

				} else {
					mHandler.sendEmptyMessage(2);
				}
			}
			break;
		case R.id.write_weibo_add_pic:
			new AlertDialog.Builder(this).setTitle("选择").setItems(
					new String[] { "拍照", "本地图片" },
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							switch (which) {
							case 0:
								imageFileUri = getContentResolver()
										.insert(
												MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
												new ContentValues());
								if (imageFileUri != null) {
									Intent i = new Intent(
											android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
									i
											.putExtra(
													android.provider.MediaStore.EXTRA_OUTPUT,
													imageFileUri);
									startActivityForResult(i, 1001);
								} else {
									Toast.makeText(WriteWeiboActivity.this,
											"出现了点小意外", Toast.LENGTH_SHORT)
											.show();
								}
								break;
							case 1:
								Intent choosePictureIntent = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(choosePictureIntent,
										1002);
								break;
							}

						}

					}).setNegativeButton("确定", null).show();
			break;
		case R.id.write_weibo_at:
			startActivityForResult(new Intent(WriteWeiboActivity.this,
					AtUserActivity.class), 1000);
			break;
		case R.id.write_weibo_add_topic:
			String origin = content.getText().toString();
			String topicTag = "##";
			content.setText(origin + topicTag);
			content.setSelection(content.getText().toString().length() - 1);
			break;
		case R.id.write_weibo_emotion:
			Utility.hideSoftInput(this);
			FaceDialog.showFaceDialog(this, bottom, bottom.getHeight(), this);
			break;
		case R.id.write_weibo_content_pic:
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
				builder.setMessage("取消发送微博？");
				builder.setPositiveButton("取消发送",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								if (draft_id != -1) {
									Log.d("wj", "draft_id=" + draft_id);
									DraftBean draft = new DraftBean();
									draft.setId(draft_id);
									draft.setContent(content.getText()
											.toString());
									try {
										db.delete(draft);
										Intent in = new Intent();
										in
												.setAction(BaseConstant.DRAFT_BROADCAST);
										WriteWeiboActivity.this
												.sendBroadcast(in);
									} catch (DbException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								dialog.dismiss();
								finish();
							}
						});
				builder.setNegativeButton("存入草稿箱",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (draft_id == -1) {
									DraftBean draft = new DraftBean();
									draft.setContent(content.getText()
											.toString());
									try {
										db.save(draft);
										Intent in = new Intent();
										in
												.setAction(BaseConstant.DRAFT_BROADCAST);
										WriteWeiboActivity.this
												.sendBroadcast(in);
									} catch (DbException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								dialog.dismiss();
								finish();
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

	private String getPicPathFromUri(Uri uri) {
		String value = uri.getPath();

		if (value.startsWith("/external")) {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = this.managedQuery(uri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else {
			return value;
		}
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
