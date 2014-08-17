package com.thinksns.jkfs.ui;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.ui.view.ImageLoadingDialog;
import com.thinksns.jkfs.util.Utility;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ImageView;
import android.widget.Toast;

public class BrowseImageActivity extends Activity implements OnGestureListener {
	private String url;
	private ImageView image;
	private ImageLoadingDialog dialog;
	private DisplayImageOptions options;

	private GestureDetector detector;
	private Bitmap bitmap;
	private int width, height;
	private float currentScale = 1;
	private Matrix matrix;

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

		detector = new GestureDetector(this);
		matrix = new Matrix();

		if (Utility.isConnected(this)) {
			ImageLoader.getInstance().displayImage(url, image, options);
			dialog.dismiss();
			bitmap = ImageLoader.getInstance().loadImageSync(url);
			width = bitmap.getWidth();
			height = bitmap.getHeight();
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
		// 将该Activity上的触碰事件交给GestureDetector处理
		return detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		velocityX = velocityX > 4000 ? 4000 : velocityX;
		velocityX = velocityX < -4000 ? -4000 : velocityX;
		// 根据手势的速度来计算缩放比，如果velocityX>0，放大图像，否则缩小图像
		currentScale += currentScale * velocityX / 4000.0f;
		// 保证currentScale不会等于0
		currentScale = currentScale > 0.01 ? currentScale : 0.01f;
		// 重置Matrix
		matrix.reset();
		// 缩放Matrix
		matrix.setScale(currentScale, currentScale, 160, 200);
/*		// 如果图片还未回收，先强制回收该图片
		if (!bitmap.isRecycled()) {
			bitmap.recycle();
		}*/
		// 根据原始位图和Matrix创建新图片
		Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		// 显示新的位图
		image.setImageBitmap(bitmap2);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
