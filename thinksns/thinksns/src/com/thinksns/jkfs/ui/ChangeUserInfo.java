package com.thinksns.jkfs.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.util.common.ImageTools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;



/**
 * @author 邓思宇
 *
 *	修改个人信息
 *
 */
public class ChangeUserInfo extends Activity {

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;

	private static final int SCALE = 5;// 照片缩小比例
	private ImageView iv_image = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_user_info);

		iv_image = (ImageView) findViewById(R.id.c_head);

		Button button = (Button) findViewById(R.id.c_icon);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ChangeUserInfo.this);
				builder.setTitle("选择方式")
						.setItems(new String[] { "拍照", "相册" },
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										switch (which) {
										case TAKE_PICTURE:
											Intent openCameraIntent = new Intent(
													MediaStore.ACTION_IMAGE_CAPTURE);
											Uri imageUri = Uri.fromFile(new File(
													Environment
															.getExternalStorageDirectory(),
													"image.jpg"));
											// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
											openCameraIntent.putExtra(
													MediaStore.EXTRA_OUTPUT,
													imageUri);
											startActivityForResult(
													openCameraIntent,
													TAKE_PICTURE);

											break;

										case CHOOSE_PICTURE:
											Intent openAlbumIntent = new Intent(
													Intent.ACTION_GET_CONTENT);
											openAlbumIntent.setType("image/*");
											startActivityForResult(
													openAlbumIntent,
													CHOOSE_PICTURE);

											break;

										default:
											break;
										}
									}
								}).setNegativeButton("取消", null);
				AlertDialog dialog = builder.create();
				dialog.show();

			}

		});

		// change uname
		Button button1 = (Button) findViewById(R.id.c_uname);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						ChangeUserInfo.this);
				builder.setTitle("用户名")
						.setView(new EditText(ChangeUserInfo.this))
						.setPositiveButton("确定", null)
						.setNegativeButton("取消", null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		// change uname
		Button button2 = (Button) findViewById(R.id.c_email);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						ChangeUserInfo.this);
				builder.setTitle("邮箱")
						.setView(new EditText(ChangeUserInfo.this))
						.setPositiveButton("确定", null)
						.setNegativeButton("取消", null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		// change uname
		Button button3 = (Button) findViewById(R.id.c_sex);
		button3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						ChangeUserInfo.this);
				builder.setTitle("性别")
						.setSingleChoiceItems(new String[] { "男", "女" }, 0,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).setPositiveButton("确定", null)
						.setNegativeButton("取消", null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		// change uname
		Button button4 = (Button) findViewById(R.id.c_add);
		button4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						ChangeUserInfo.this);
				builder.setTitle("地址")
						.setView(new EditText(ChangeUserInfo.this))
						.setPositiveButton("确定", null)
						.setNegativeButton("取消", null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		// change uname
		Button button5 = (Button) findViewById(R.id.c_selfinfo);
		button5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						ChangeUserInfo.this);
				builder.setTitle("自我介绍")
						.setView(new EditText(ChangeUserInfo.this))
						.setPositiveButton("确定", null)
						.setNegativeButton("取消", null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				// 将保存在本地的图片取出并缩小后显示在界面上
				Bitmap bitmap = BitmapFactory.decodeFile(Environment
						.getExternalStorageDirectory() + "/image.jpg");
				Bitmap newBitmap = ImageTools.zoomBitmap(bitmap,
						bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
				// 由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
				bitmap.recycle();
				startPhotoZoom(data.getData());

				// 将处理过的图片显示在界面上，并保存到本地

				// iv_image.setImageBitmap(newBitmap);
				ImageTools.savePhotoToSDCard(newBitmap, Environment
						.getExternalStorageDirectory().getAbsolutePath(),
						String.valueOf(System.currentTimeMillis()));
				break;

			case CHOOSE_PICTURE:
				ContentResolver resolver = getContentResolver();
				// 照片的原始资源地址
				Uri originalUri = data.getData();
				try {
					// 使用ContentProvider通过URI获取原始图片
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);
					if (photo != null) {
						// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap = ImageTools.zoomBitmap(photo,
								photo.getWidth() / SCALE, photo.getHeight()
										/ SCALE);
						// 释放原始图片占用的内存，防止out of memory异常发生
						photo.recycle();

						startPhotoZoom(data.getData());
						// iv_image.setImageBitmap(smallBitmap);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				if (data != null) {
					getImageToView(data);
				}
				break;
			default:
				break;
			}
		}
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
			iv_image.setImageDrawable(drawable);
		}
	}

}