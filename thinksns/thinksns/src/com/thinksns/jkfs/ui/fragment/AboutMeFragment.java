package com.thinksns.jkfs.ui.fragment;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.AccountBean;
import com.thinksns.jkfs.bean.UserInfoBean;
import com.thinksns.jkfs.bean.UserInfoMedalBean;
import com.thinksns.jkfs.constant.BaseConstant;
import com.thinksns.jkfs.constant.HttpConstant;
import com.thinksns.jkfs.ui.UserInfoFollowList;
import com.thinksns.jkfs.util.Base64;
import com.thinksns.jkfs.util.Utility;
import com.thinksns.jkfs.util.common.ImageUtils;
import com.thinksns.jkfs.util.http.HttpMethod;
import com.thinksns.jkfs.util.http.HttpUtility;
import com.thinksns.jkfs.util.http.HttpUtils;

/**
 * @author 邓思宇 我的主页列表显示信息 还未完成
 * 
 */
@SuppressLint( { "HandlerLeak", "ValidFragment" })
public class AboutMeFragment extends Fragment {

	public static final String TAG = "AboutMeFragment";
	// get the account
	private ThinkSNSApplication application;
	private AccountBean account;
	private UserInfoBean userinfo;
	private int FLAG = 0;// 判断是打开的主页还是其他人的页面 其他人页面设为1
	private String uuid;// 如果FLAG为1 会收到其他人的UID 存入此
	private int follow;// 如果FLAG为1 会收到其他人的FOLLOWING值存入此

	private String FOLLOW_DESTROY = "follow_destroy";
	private String FOLLOW_CREATE = "follow_create";

	private ImageView faceImage;

	private Uri imageFileUri;

	private String[] items = new String[] { "选择本地图片", "拍照" };
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage.jpg";

	/* 请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private static final int RESULT_CAMERA_CODE = 3;

	private String picPath = "";

	private Button sendM;

	private DbUtils db;

	private DisplayImageOptions options;

	public AboutMeFragment() {
		super();
	}

	public AboutMeFragment(String i, String uid, String following) {
		if (i == null) {
			this.FLAG = 1;
		} else {
			this.FLAG = Integer.parseInt(i);
		}
		this.uuid = uid;
		this.follow = Integer.parseInt(following);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:

				ImageView head = (ImageView) getActivity().findViewById(
						R.id.m_head);
				TextView uname = (TextView) getActivity().findViewById(
						R.id.m_username);
				TextView address = (TextView) getActivity().findViewById(
						R.id.m_address);
				ImageView sex = (ImageView) getActivity().findViewById(
						R.id.m_sex);

				TextView msex = (TextView) getActivity().findViewById(
						R.id.m_sex1);
				Button mweibo = (Button) getActivity().findViewById(
						R.id.m_weibo);
				Button mfollow = (Button) getActivity().findViewById(
						R.id.m_follow);
				Button mfollowme = (Button) getActivity().findViewById(
						R.id.m_followme);
				TextView muid = (TextView) getActivity().findViewById(
						R.id.m_uid);
				TextView muname = (TextView) getActivity().findViewById(
						R.id.m_uname1);
				TextView memail = (TextView) getActivity().findViewById(
						R.id.m_email);
				TextView maddress = (TextView) getActivity().findViewById(
						R.id.m_add);
				LinearLayout medals = (LinearLayout) getActivity()
						.findViewById(R.id.m_medals);

				uname.setText(userinfo.getUname());
				address.setText(userinfo.getLocation());
				msex.setText(userinfo.getSex());
				if (userinfo.count_info != null) {
					mweibo.setText(userinfo.count_info.getWeibo_count());
					mfollow.setText(userinfo.count_info.getFollowing_count());
					mfollowme.setText(userinfo.count_info.getFollower_count());
				}
				muid.setText(userinfo.getUid());
				muname.setText(userinfo.getUname());
				memail.setText(userinfo.getEmail());

				String us = userinfo.getLocation();
				maddress.setText(us);

				ImageLoader.getInstance().displayImage(
						userinfo.getAvatar_original(), head, options);

				Button button = (Button) getActivity().findViewById(
						R.id.changeinfo);

				View button3 = (View) getActivity().findViewById(
						R.id.sendmessage);

				if (FLAG == 0) {

					button.setText("修改头像");
					button3.setVisibility(View.GONE);

				} else if (FLAG == 1) {

					switch (follow) {
					case 0:
						button.setText("关注");

						break;
					case 1:
						button.setText("取消关注");

						break;
					}
				}

				if (userinfo.getSex().equals("男")) {
					sex.setBackgroundResource(R.drawable.male);
				} else {
					sex.setBackgroundResource(R.drawable.female);
				}

				ArrayList<UserInfoMedalBean> ms = (ArrayList<UserInfoMedalBean>) userinfo
						.getMedals();
				if (ms != null && ms.size() > 0) {
					for (UserInfoMedalBean um : ms) {
						ImageView image = new ImageView(getActivity());
						image.setLayoutParams(new LayoutParams(80, 80));
						image.setPadding(5, 5, 5, 5);
						medals.addView(image);
						ImageLoader.getInstance().displayImage(um.getSrc(),
								image, options);
					}
				}
				if (FLAG == 0)
					ThinkSNSApplication.getInstance().getMenu().setUserInfo(
							userinfo);

				break;

			case 2:

				Button button2 = (Button) getActivity().findViewById(
						R.id.changeinfo);

				View button4 = (View) getActivity().findViewById(
						R.id.sendmessage);

				if (FLAG == 0) {
					button2.setText("修改头像");
					button4.setVisibility(View.GONE);

				} else if (FLAG == 1) {
					switch (follow) {
					case 0:
						button2.setText("关注");

						break;
					case 1:
						button2.setText("取消关注");

						break;
					}
				}

				break;

			case 3:
				Toast toast = Toast.makeText(getActivity(), "网络未连接",
						Toast.LENGTH_SHORT);
				toast.show();
				break;

			case 4:
				View footer = (View) getActivity().findViewById(
						R.id.bottomfooter);
				footer.setVisibility(View.GONE);

				Toast toast2 = Toast.makeText(getActivity(), "私信发送成功",
						Toast.LENGTH_SHORT);
				toast2.show();
				break;

			}

		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		return inflater.inflate(R.layout.aboutme_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);

		application = (ThinkSNSApplication) this.getActivity()
				.getApplicationContext();
		account = application.getAccount(this.getActivity());

		options = new DisplayImageOptions.Builder().showStubImage(
				R.drawable.ic_launcher).cacheInMemory().cacheOnDisc().build();
		db = DbUtils.create(getActivity());
		db.configDebug(true);

		openPage();

		sendM = (Button) getActivity().findViewById(R.id.sendmessage);

		sendM.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				View footer = (View) getActivity().findViewById(
						R.id.bottomfooter);

				footer.setVisibility(View.VISIBLE);

			}
		});

		Button mess = (Button) getActivity().findViewById(R.id.buttonsend);

		mess.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				EditText things = (EditText) getActivity().findViewById(
						R.id.inputmessage);

				String content = things.getText().toString().trim();

				if (content.equals("")) {
					Toast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				createMess(content);

			}
		});

	}

	// 创建新的私信
	protected void createMess(final String content) {
		// TODO Auto-generated method stub
		if (Utility.isConnected(getActivity())) {
			// 待添加超时判断

			new Thread() {
				@Override
				public void run() {
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "Message");
					map.put("act", "create");
					map.put("to_uid", uuid);
					map.put("content", content);
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					HttpUtils.doPost(HttpConstant.THINKSNS_URL, map);

					mHandler.sendEmptyMessage(4);
				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(3);
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 显示修改信息活动
		Button button = (Button) getActivity().findViewById(R.id.changeinfo);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// get the change info activity

				if (FLAG == 0) {

					showDialog();

				} else if (FLAG == 1) {
					switch (follow) {
					case 0:

						followif(uuid, FOLLOW_CREATE); // 点击关注
						follow = 1;
						break;

					case 1:

						followif(uuid, FOLLOW_DESTROY); // 点击取消关注
						follow = 0;
						break;

					}
				}

			}
		});

		// 显示微博LIST
		Button button2 = (Button) getActivity().findViewById(R.id.m_weibo);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserInfoWeiboListFragment userInfoWeiboListFragment = new UserInfoWeiboListFragment();
				Bundle args = new Bundle();
				args.putParcelable("uinfo", userinfo);
				userInfoWeiboListFragment.setArguments(args);
				getChildFragmentManager().beginTransaction().replace(
						R.id.aboutme_frame, userInfoWeiboListFragment).commit();

			}
		});

		// 显示关注LIST
		Button button3 = (Button) getActivity().findViewById(R.id.m_follow);
		button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// get the change info activity
				switch (FLAG) {
				case 0:

					Intent intent = new Intent(getActivity(),
							UserInfoFollowList.class);
					intent.putExtra("FLAG", "0");
					intent.putExtra("FLAGG", "0");
					intent.putExtra("uuid", "");
					startActivity(intent);

					break;
				case 1:

					// 前面的1表示打开的是其他人的页面 后面的0表示的是打开的是关注人页面

					Intent intent2 = new Intent(getActivity(),
							UserInfoFollowList.class);
					intent2.putExtra("FLAG", "1");
					intent2.putExtra("FLAGG", "0");
					intent2.putExtra("uuid", uuid);
					startActivity(intent2);
					break;
				}

			}
		});

		// 显示粉丝LIST
		Button button4 = (Button) getActivity().findViewById(R.id.m_followme);
		button4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (FLAG) {
				case 0:

					Intent intent = new Intent(getActivity(),
							UserInfoFollowList.class);
					intent.putExtra("FLAG", "0");
					intent.putExtra("FLAGG", "1");
					intent.putExtra("uuid", "");
					startActivity(intent);

					break;
				case 1:

					// 前面的1表示打开的是其他人的页面 后面的1表示的是打开的是粉丝人页面

					Intent intent2 = new Intent(getActivity(),
							UserInfoFollowList.class);
					intent2.putExtra("FLAG", "1");
					intent2.putExtra("FLAGG", "1");
					intent2.putExtra("uuid", uuid);
					startActivity(intent2);
					break;
				}
			}
		});
	}

	private void openPage() {
		switch (FLAG) {
		case 0:

			/*
			 * if (application.getUser() != null) { userinfo =
			 * application.getUser(); mHandler.sendEmptyMessage(1); } else
			 */
			if (Utility.isConnected(getActivity())) {
				// 待添加超时判断

				new Thread() {
					@Override
					public void run() {
						Gson gson = new Gson();
						HashMap<String, String> map = new HashMap<String, String>();
						map = new HashMap<String, String>();
						map.put("app", "api");
						map.put("mod", "User");
						map.put("act", "show");
						map.put("user_id", account.getUid());
						map.put("oauth_token", account.getOauth_token());
						map.put("oauth_token_secret", account
								.getOauth_token_secret());
						String json = HttpUtility.getInstance()
								.executeNormalTask(HttpMethod.Get,
										HttpConstant.THINKSNS_URL, map);

						if (json != null && !"".equals(json)
								&& json.startsWith("{")) {
							userinfo = gson.fromJson(json, UserInfoBean.class);
							mHandler.sendEmptyMessage(1);
						}

					}
				}.start();
			} else {
				try {
					userinfo = db.findFirst(Selector.from(UserInfoBean.class)
							.where("uid", "=", account.getUid()));
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (userinfo != null)
					mHandler.sendEmptyMessage(1);
				else
					mHandler.sendEmptyMessage(3);

			}
			break;
		case 1:
			if (Utility.isConnected(getActivity())) {
				// 待添加超时判断

				new Thread() {
					@Override
					public void run() {
						Gson gson = new Gson();
						HashMap<String, String> map = new HashMap<String, String>();
						map = new HashMap<String, String>();
						map.put("app", "api");
						map.put("mod", "User");
						map.put("act", "show");
						map.put("user_id", uuid);
						map.put("oauth_token", account.getOauth_token());
						map.put("oauth_token_secret", account
								.getOauth_token_secret());
						String json = HttpUtility.getInstance()
								.executeNormalTask(HttpMethod.Get,
										HttpConstant.THINKSNS_URL, map);

						if (json != null && !"".equals(json)) {

							userinfo = gson.fromJson(json, UserInfoBean.class);
							mHandler.sendEmptyMessage(1);
						}

					}
				}.start();
			} else {
				mHandler.sendEmptyMessage(3);
			}
			break;
		}
	}

	// 点击按钮 取消关注或再次关注
	private void followif(final String uid, final String act) {
		if (Utility.isConnected(getActivity())) {
			// 待添加超时判断

			new Thread() {
				@Override
				public void run() {

					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "User");
					map.put("act", act);
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					map.put("user_id", uid);
					String json = HttpUtility.getInstance().executeNormalTask(
							HttpMethod.Post, HttpConstant.THINKSNS_URL, map);

					mHandler.sendEmptyMessage(2);

				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(3);
		}
	}

	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		new AlertDialog.Builder(getActivity()).setTitle("设置头像").setItems(items,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							/*
							 * Intent intentFromGallery = new Intent();
							 * intentFromGallery.setType("image/*"); // 设置文件类型
							 * intentFromGallery
							 * .setAction(Intent.ACTION_GET_CONTENT);
							 */
							Intent choosePictureIntent = new Intent(
									Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(choosePictureIntent,
									IMAGE_REQUEST_CODE);
							break;
						case 1:
							imageFileUri = getActivity()
									.getContentResolver()
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
								startActivityForResult(i, CAMERA_REQUEST_CODE);
							} else {
								Toast.makeText(getActivity(), "出现了点小意外",
										Toast.LENGTH_SHORT).show();
							}
							break;
						}
					}
				}).setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != 0) {

			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				/*
				 * if (hasSdcard()) { File tempFile = new File(Environment
				 * .getExternalStorageDirectory() + IMAGE_FILE_NAME);
				 * startPhotoZoom(Uri.fromFile(tempFile)); } else {
				 * Toast.makeText(getActivity(), "未找到存储卡，无法存储照片！",
				 * Toast.LENGTH_LONG).show(); }
				 */
				startCameraZoom(imageFileUri);
				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					// Uri imageFileUri = data.getData();
					// picPath = getPicPathFromUri(imageFileUri);
					// changeHead(picPath);
					//
					getImageToView(data);
				}
			case RESULT_CAMERA_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);
	}

	public void startCameraZoom(Uri uri) {

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_CAMERA_CODE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);

			Uri imageFileUri;
			if (data.getData() != null) {
				imageFileUri = data.getData();
			} else {
				imageFileUri = Uri.parse(MediaStore.Images.Media.insertImage(
						getActivity().getContentResolver(), photo, null, null));
			}
			Log.d("uri is null？", (imageFileUri == null) + "");

			String picPath = getPicPathFromUri(imageFileUri);
			InputStream fis;
			try {
				fis = new FileInputStream(picPath);
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inTempStorage = new byte[100 * 1024];// 100kb缓存
				opts.inPreferredConfig = Bitmap.Config.RGB_565;
				opts.inPurgeable = true;// 可被回收
				opts.inSampleSize = 4;
				opts.inInputShareable = true;
				Bitmap btp = BitmapFactory.decodeStream(fis, null, opts);
				faceImage = (ImageView) getActivity().findViewById(R.id.m_head);
				faceImage.setImageBitmap(btp);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			changeHead(picPath);
		}
	}

	/**
	 * 检查是否存在SDCard
	 * 
	 * @return
	 */
	public boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}

	}

	// 上传头像
	private void changeHead(final String head) {

		final String uploadPicPath = ImageUtils.compressPic(getActivity(),
				head, 3);

		if (Utility.isConnected(getActivity())) {
			// 待添加超时判断

			new Thread() {
				@Override
				public void run() {

					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("app", "api");
					map.put("mod", "User");
					map.put("act", "upload_face");
					map.put("Filedata", head);
					map.put("oauth_token", account.getOauth_token());
					map.put("oauth_token_secret", account
							.getOauth_token_secret());
					HttpUtils.doPost(HttpConstant.THINKSNS_URL, map);
					boolean result = HttpUtils.doUploadFile(
							HttpConstant.THINKSNS_URL, map, uploadPicPath);
					Log.d("wj", "url before:" + userinfo.getAvatar_original());
					if (result) {
						HashMap<String, String> m = new HashMap<String, String>();
						m = new HashMap<String, String>();
						m.put("app", "api");
						m.put("mod", "User");
						m.put("act", "show");
						m.put("user_id", account.getUid());
						m.put("oauth_token", account.getOauth_token());
						m.put("oauth_token_secret", account
								.getOauth_token_secret());
						String json = HttpUtility.getInstance()
								.executeNormalTask(HttpMethod.Get,
										HttpConstant.THINKSNS_URL, map);
						if (json != null && !"".equals(json)) {
							Gson g = new Gson();
							UserInfoBean user = g.fromJson(json,
									UserInfoBean.class);
							Log.d("wj", "url middle:" + userinfo.getAvatar_original());
							ThinkSNSApplication.getInstance().getMenu()
									.setUserInfo(user);
						}
					}

				}
			}.start();
		} else {
			mHandler.sendEmptyMessage(3);
		}

	}

	public String BitMapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		String temp = Base64.encodeToString(b, Base64.DEFAULT);
		return temp;
	}

	private String getPicPathFromUri(Uri uri) {
		String value = uri.getPath();
		if (value.startsWith("/external")) {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = getActivity().managedQuery(uri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else {
			return value;
		}
	}

}
