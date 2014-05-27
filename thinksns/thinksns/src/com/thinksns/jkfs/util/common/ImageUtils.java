package com.thinksns.jkfs.util.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.util.FileManager;
import com.thinksns.jkfs.util.MD5;
import com.thinksns.jkfs.util.ThreadPoolManager;
import com.thinksns.jkfs.util.Utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class ImageUtils {
	private static final String TAG = "ImageUtils";
	private static int DayCount = 10;// 天数
	private static final long CLEARTIME = DayCount * 24 * 60 * 60 * 1000;
	/**
	 * 默认图片
	 */
	private final static int Default_Img = R.drawable.ic_launcher;

	private static Object lock = new Object();

	/**
	 * 内存图片软引用缓冲
	 */
	private static LinkedHashMap<String, SoftReference> imageCache = new LinkedHashMap<String, SoftReference>(
			20);

	// 回调接口
	public interface ImageCallback {
		public void loadImage(Bitmap bitmap, String imagePath);
	}

	/**
	 * 入口
	 * 
	 * @param imageUrl
	 * @param iv_item_image
	 * @param context
	 * @param callback
	 */
	public static void setThumbnailView(String imageUrl,
			ImageView iv_item_image, Context context, ImageCallback callback) {
		String md5 = ImageUtils.md5(imageUrl);
		String cachePath = context.getCacheDir().getAbsolutePath() + "/" + md5; // data里的缓存
		String imagePath = getExternalCacheDir(context) + File.separator + md5; // sd卡
		if (!Utility.sdCardIsAvailable())/* true 为可用 */{
			setThumbnailImage(iv_item_image, imageUrl, cachePath, callback);
			iv_item_image.setTag(cachePath);
		} else {
			setThumbnailImage(iv_item_image, imageUrl, imagePath, callback);
			iv_item_image.setTag(imagePath);
		}
	}

	/**
	 * 获得程序在sd开上的cahce目录
	 * 
	 * @param context
	 *            The context to use
	 * @return The external cache dir
	 */
	@SuppressLint("NewApi")
	public static String getExternalCacheDir(Context context) {
		// android 2.2 以后才支持的特性
		if (hasExternalCacheDir()) {
			return context.getExternalCacheDir().getPath() + File.separator
					+ "img";
		}
		// 2.2以前我们需要自己构造
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/img/";
		return Environment.getExternalStorageDirectory().getPath() + cacheDir;
	}

	public static boolean hasExternalCacheDir() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * 设置图片函数
	 * 
	 * @param view
	 * @param imageUrl
	 * @param cachePath
	 * @param callback
	 */
	private static void setThumbnailImage(ImageView view, String imageUrl,
			String cachePath, ImageCallback callback) {
		Bitmap bitmap = null;
		bitmap = ImageUtils.loadThumbnailImage(cachePath, imageUrl, callback);
		if (bitmap == null) {// 查找本地sd卡,若没有则从网站加载,若网站上没有图片或错误时返回null
			// 设置默认图片
			view.setImageResource(Default_Img);
		} else {
			// 设置本地SD卡缓存图片
			view.setImageBitmap(bitmap);
		}
	}

	/**
	 * 保存图片到SD卡
	 * 
	 * @param imagePath
	 * @param buffer
	 * @throws IOException
	 */
	public static void saveImage(String imagePath, byte[] buffer)
			throws IOException {
		File f = new File(imagePath);
		if (f.exists()) {
			return;
		} else {
			File parentFile = f.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(imagePath);
			fos.write(buffer);
			fos.flush();
			fos.close();
		}
	}

	/**
	 * 保存图片到缓存
	 * 
	 * @param imagePath
	 * @param bm
	 */
	public static void saveImage(String imagePath, Bitmap bm) {

		if (bm == null || imagePath == null || "".equals(imagePath)) {
			return;
		}

		File f = new File(imagePath);
		if (f.exists()) {
			return;
		} else {
			try {
				File parentFile = f.getParentFile();
				if (!parentFile.exists()) {
					parentFile.mkdirs();
				}
				f.createNewFile();
				FileOutputStream fos;
				fos = new FileOutputStream(f);
				bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.close();
			} catch (FileNotFoundException e) {
				f.delete();
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				f.delete();
			}
		}
	}

	/**
	 * 从SD卡加载图片
	 * 
	 * @param imagePath
	 * @return
	 */
	public static Bitmap getImageFromLocal(String imagePath) {
		File file = new File(imagePath);
		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			file.setLastModified(System.currentTimeMillis());
			return bitmap;
		}
		return null;
	}

	/**
	 * 从本地文件中删除文件
	 * 
	 * @param imagePath
	 */
	private static void deleteImageFromLocal(String imagePath) {
		File file = new File(imagePath);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 从本地或者服务端异步加载缩略图图片
	 * 
	 * @return
	 * @param imagePath
	 *            本地缓存路径
	 * @param imgUrl
	 *            拼接后的请求路径
	 * @param callback
	 *            得到数据后的处理方法回调
	 * @throws IOException
	 */
	public static Bitmap loadThumbnailImage(final String imagePath,
			final String imgUrl, final ImageCallback callback) {
		// 在软链接缓存中，则返回Bitmap对象
		if (imageCache.containsKey(imgUrl)) {
			SoftReference reference = imageCache.get(imgUrl);
			Bitmap bitmap = (Bitmap) reference.get();
			if (bitmap != null) {
				return bitmap;
			}
		}
		// 若软链接缓存没有，从网络获取
		Bitmap bitmap = null;
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					Bitmap bitmap = (Bitmap) msg.obj;
					callback.loadImage(bitmap, imagePath);
				}
			}
		};
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(imgUrl);
					URLConnection conn = url.openConnection();
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					conn.connect();
					InputStream in = conn.getInputStream();
					BitmapFactory.Options options = new Options();
					options.inSampleSize = 1;
					Bitmap bitmap = BitmapFactory.decodeStream(in, new Rect(0,
							0, 0, 0), options);
					imageCache.put(imgUrl, new SoftReference(bitmap));

					Message msg = handler.obtainMessage();
					msg.obj = bitmap;
					handler.sendMessage(msg);
					if (bitmap != null) {
						// 保存文件到sd卡
						saveImage(imagePath, bitmap);

					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					Log.e(ImageUtils.class.getName(), "图片url不存在");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		ThreadPoolManager.getInstance().addTask(runnable);

		return null;
	}

	/**
	 * MD5
	 * 
	 * @param paramString
	 * @return
	 */
	private static String md5(String paramString) {
		return MD5.getMD5Code(paramString);
	}

	/**
	 * 每次打开含有大量图片的activity时,开一个新线程,检查并清理缓存
	 * 
	 * @param context
	 */
	public static void checkCache(final Context context) {
		new Thread() {
			public void run() {
				int state = 0;// 记录清除结果 0为都没清除, 1为只清除了sd卡, 2为只清除了rom Cache ,3
				// 为都清除了
				String cacheS = "0M";
				String cacheD = "0M";
				File sdCache = new File(getExternalCacheDir(context)); // sd卡"mnt/sdcard/android/data/..";
				File cacheDir = context.getCacheDir(); // 手机data/data/../cache
				try {
					if (sdCache != null && sdCache.exists()) {
						long sdFileSize = getFileSize(sdCache);
						if (sdFileSize > 1024 * 1024 * 50) {
							// SD需要清理
							long clearFileSize = clear(sdCache);
							state += 1;
							cacheS = clearFileSize + "";
						}
					}
					if (cacheDir != null && cacheDir.exists()) {
						long cacheFileSize = getFileSize(cacheDir);
						if (cacheFileSize > 1024 * 1024 * 50) {
							// ROM需要清理
							long clearFileSize = clear(cacheDir);
							state += 2;
							cacheD = clearFileSize + "";
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 清除路径
	 * 
	 * @param cacheDir
	 * @return
	 */
	public static long clear(File cacheDir) {
		long clearFileSize = 0;
		File[] files = cacheDir.listFiles();
		if (files == null)
			return 0;
		for (File f : files) {
			if (f.isFile()) {
				if (System.currentTimeMillis() - f.lastModified() > CLEARTIME) {
					long fileSize = f.length();
					if (f.delete()) {
						clearFileSize += fileSize;
					}
				}
			} else {
				clear(f);
			}
		}
		return clearFileSize;
	}

	/**
	 * 取得文件大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return
	 */
	public static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 压缩图片
	 * 
	 * @param context
	 * @param picPath
	 * @param quality
	 *            1.不压缩 2.压缩到50% 3.压缩到25%
	 * @return 图片路径
	 */
	public static String compressPic(Context context, String picPath,
			int quality) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 1;

		switch (quality) {
		case 1:
			return picPath; // 不压缩
		case 2:
			options.inSampleSize = 2; // 压缩到50%
			break;
		case 3:
			options.inSampleSize = 4; // 压缩到25%
			break;
		case 4:
			options.inSampleSize = 2;

			if (Utility.isWifi(context)) {
				return picPath;
			}
			break;
		}

		Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
		FileOutputStream stream = null;
		String tmp = FileManager.getUploadPicTempFile();
		try {
			new File(tmp).getParentFile().mkdirs();
			new File(tmp).createNewFile();
			stream = new FileOutputStream(new File(tmp));
		} catch (IOException ignored) {

		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

		if (stream != null) {
			try {
				stream.close();
				bitmap.recycle();
			} catch (IOException ignored) {

			}
		}
		return tmp;
	}

}
