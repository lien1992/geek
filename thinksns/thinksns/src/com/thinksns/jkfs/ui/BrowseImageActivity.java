package com.thinksns.jkfs.ui;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.ui.view.DragImageView;
import com.thinksns.jkfs.ui.view.ImageLoadingDialog;
import com.thinksns.jkfs.util.Utility;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.Toast;

public class BrowseImageActivity extends Activity {
	private String url;
	private DragImageView image;
	private ImageLoadingDialog dialog;
	private DisplayImageOptions options;
	private Bitmap bitmap;

	private int window_width, window_height;
	private int state_height;// 状态栏的高度

	private ViewTreeObserver viewTreeObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browseimage);

		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		Log.d("wj", "browse image url:" + url);

		WindowManager manager = getWindowManager();
		window_width = manager.getDefaultDisplay().getWidth();
		window_height = manager.getDefaultDisplay().getHeight();

		options = new DisplayImageOptions.Builder().showStubImage(
				R.drawable.ic_launcher).cacheInMemory().cacheOnDisc().build();

		image = (DragImageView) findViewById(R.id.browse_big_image);
		dialog = new ImageLoadingDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		if (Utility.isConnected(this)) {
			dialog.dismiss();
			ImageSize targetSize = new ImageSize(window_width, window_height);
			bitmap = ImageLoader.getInstance().loadImageSync(url, targetSize,
					options);
			image.setImageBitmap(bitmap);
			image.setmActivity(this);
			viewTreeObserver = image.getViewTreeObserver();
			viewTreeObserver
					.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

						@Override
						public void onGlobalLayout() {
							if (state_height == 0) {
								// 获取状况栏高度
								Rect frame = new Rect();
								getWindow().getDecorView()
										.getWindowVisibleDisplayFrame(frame);
								state_height = frame.top;
								image.setScreen_H(window_height - state_height);
								image.setScreen_W(window_width);
							}

						}
					});
		} else {
			dialog.dismiss();
			Toast.makeText(BrowseImageActivity.this, "网络未连接",
					Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					finish();
				}
			}, 1000 * 2);
		}
	}

}
