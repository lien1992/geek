package com.thinksns.jkfs.ui;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.ui.view.ImageLoadingDialog;
import com.thinksns.jkfs.util.Utility;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

public class BrowseImageActivity extends Activity {
	private String url;
	private ImageView image;
	private ImageLoadingDialog dialog;
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browseimage);

		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		Log.d("wj", "browse image url:" + url);

		options = new DisplayImageOptions.Builder().showStubImage(
				R.drawable.ic_launcher).cacheInMemory().cacheOnDisc().build();

		image = (ImageView) findViewById(R.id.browse_big_image);
		dialog = new ImageLoadingDialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		if (Utility.isConnected(this)) {
			ImageLoader.getInstance().displayImage(url, image, options);
			dialog.dismiss();
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		finish();
		return true;
	}

}
