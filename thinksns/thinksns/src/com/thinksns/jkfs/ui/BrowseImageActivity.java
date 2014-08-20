package com.thinksns.jkfs.ui;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.ui.view.DragImageView;
import com.thinksns.jkfs.ui.view.ImageLoadingDialog;
import com.thinksns.jkfs.util.Utility;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BrowseImageActivity extends Activity {
	private Context ctx;
	private Activity activity;
	private String url;
	private DragImageView image;
	private ImageLoadingDialog dialog;
	private DisplayImageOptions options;
	private static Bitmap bitmap;
	private ImageSize targetSize;
	private ProgressBar p;

	private int window_width, window_height;
	private int state_height;// 状态栏的高度

	private ViewTreeObserver viewTreeObserver;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(BrowseImageActivity.this, "网络未连接",
						Toast.LENGTH_SHORT).show();
				BrowseImageActivity.this.finish();
				break;
			case 1:
				p.setVisibility(View.GONE);
				if (bitmap != null)
					image.setImageBitmap(bitmap);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browseimage);
		p = (ProgressBar) findViewById(R.id.browse_big_image_progressbar);

		ctx = this.getApplicationContext();
		activity = this;

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
		targetSize = new ImageSize(window_width, window_height);

		new Thread() {
			@Override
			public void run() {
				if (Utility.isConnected(ctx)) {
					dialog.dismiss();
					bitmap = ImageLoader.getInstance().loadImageSync(url,
							targetSize, options);
					// image.setImageBitmap(bitmap);
					mHandler.sendEmptyMessage(1);
				} else {
					dialog.dismiss();
					mHandler.sendEmptyMessageDelayed(0, 500);
				}
			}
		}.start();
		/*
		 * if (bitmap != null) image.setImageBitmap(bitmap);
		 */
		image.setmActivity(activity);
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
	}
}
