package com.thinksns.jkfs.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thinksns.jkfs.BuildConfig;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.bean.CommentBean;
import com.thinksns.jkfs.bean.WeiboBean;

/**
 * 工具类
 * 
 */
public class Utility {
	static String[] emotions = new String[] { "aoman", "baiyan", "bishi",
			"bizui", "cahan", "caidao", "chajin", "cheer", "chong", "ciya",
			"da", "dabian", "dabing", "dajiao", "daku", "dangao", "danu",
			"dao", "deyi", "diaoxie", "e", "fadai", "fadou", "fan", "fanu",
			"feiwen", "fendou", "gangga", "geili", "gouyin", "guzhang", "haha",
			"haixiu", "haqian", "hua", "huaixiao", "hufen", "huishou",
			"huitou", "jidong", "jingkong", "jingya", "kafei", "keai",
			"kelian", "ketou", "kiss", "ku", "kuaikule", "kulou", "kun",
			"lanqiu", "lenghan", "liuhan", "liulei", "liwu", "love", "ma",
			"meng", "nanguo", "no", "ok", "peifu", "pijiu", "pingpang",
			"pizui", "qiang", "qinqin", "qioudale", "qiu", "quantou", "ruo",
			"se", "shandian", "shengli", "shenma", "shuai", "shuijiao",
			"taiyang", "tiao", "tiaopi", "tiaosheng", "tiaowu", "touxiao",
			"tu", "tuzi", "wabi", "weiqu", "weixiao", "wen", "woshou", "xia",
			"xianwen", "xigua", "xinsui", "xu", "yinxian", "yongbao",
			"youhengheng", "youtaiji", "yueliang", "yun", "zaijian", "zhadan",
			"zhemo", "zhuakuang", "zhuanquan", "zhutou", "zuohengheng",
			"zuotaiji", "zuqiu" };

	private Utility() {
		// Forbidden being instantiated.
	}

	/**
	 * 将Map中的key-value对转换成URL中的请求参数
	 * 
	 * @param param
	 * 
	 */
	public static String encodeUrl(Map<String, String> param) {
		if (param == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		Set<String> keys = param.keySet();
		boolean first = true;

		for (String key : keys) {
			String value = param.get(key);
			if (!TextUtils.isEmpty(value) || key.equals("description")
					|| key.equals("url")) {
				if (first) {
					first = false;
				} else {
					sb.append("&");
				}
				try {
					sb.append(URLEncoder.encode(key, "UTF-8")).append("=")
							.append(URLEncoder.encode(param.get(key), "UTF-8"));
				} catch (UnsupportedEncodingException e) {

				}
			}
		}

		return sb.toString();
	}

	/**
	 * 将URL分解到bundle中
	 * 
	 * @param s
	 * @return
	 */

	public static Bundle decodeUrl(String s) {
		Bundle params = new Bundle();
		if (s != null) {
			String array[] = s.split("&");
			for (String parameter : array) {
				String v[] = parameter.split("=");
				try {
					params.putString(URLDecoder.decode(v[0], "UTF-8"),
							URLDecoder.decode(v[1], "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return params;
	}

	public static void closeSilently(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ignored) {

			}
		}
	}

	/**
	 * 根据一个网络连接(String)获取bitmap图像
	 * 
	 * @param imageUri
	 * @return
	 * @throws MalformedURLException
	 */
	public static Bitmap getBitmap(String imageUri) {
		// 显示网络上的图片
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * Parse a URL query and fragment parameters into a key-value bundle.
	 */
	public static Bundle parseUrl(String url) {
		// hack to prevent MalformedURLException
		url = url.replace("weiboconnect", "http");
		try {
			URL u = new URL(url);
			Bundle b = decodeUrl(u.getQuery());
			b.putAll(decodeUrl(u.getRef()));
			return b;
		} catch (MalformedURLException e) {
			return new Bundle();
		}
	}

	public static int length(String paramString) {
		int i = 0;
		for (int j = 0; j < paramString.length(); j++) {
			if (paramString.substring(j, j + 1).matches("[Α-￥]")) {
				i += 2;
			} else {
				i++;
			}
		}

		if (i % 2 > 0) {
			i = 1 + i / 2;
		} else {
			i = i / 2;
		}

		return i;
	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * 判断是否是WiFi连接
	 * 
	 * @param context
	 * @return
	 */

	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取网络类型
	 * 
	 * @param context
	 * @return
	 */
	public static int getNetType(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return networkInfo.getType();
		}
		return -1;
	}

	/**
	 * 判断当前网络是否是gprs
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isGprs(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			if (networkInfo.getType() != ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @return
	 */
	public static int getScreenWidth() {
		Activity activity = ThinkSNSApplication.getInstance().getActivity();
		if (activity != null) {
			Display display = activity.getWindowManager().getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);
			return metrics.widthPixels;
		}

		return 480;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @return
	 */
	public static int getScreenHeight() {
		Activity activity = ThinkSNSApplication.getInstance().getActivity();
		if (activity != null) {
			Display display = activity.getWindowManager().getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);
			return metrics.heightPixels;
		}
		return 800;
	}

	public static boolean isDevicePort() {
		return ThinkSNSApplication.getInstance().getResources()
				.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}

	public static void printStackTrace(Exception e) {
		if (BuildConfig.DEBUG) {
			e.printStackTrace();
		}
	}

	/**
	 * 检测sdcard是否可用
	 * 
	 * @return true为可用，否则为不可用
	 */
	public static boolean sdCardIsAvailable() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}

	/**
	 * 隐藏软键盘
	 */
	public static void hideSoftInput(Context context) {
		if (context == null)
			return;
		InputMethodManager manager = ((InputMethodManager) context
				.getSystemService(Activity.INPUT_METHOD_SERVICE));
		View view = ((Activity) context).getCurrentFocus();
		if (view == null)
			return;
		manager.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

	}

	/**
	 * 解析微博内容
	 * 
	 * @param bean
	 */
	public static void addHighLightLinks(WeiboBean bean) {

		bean.setListViewSpannableString(getHighLightLinks(bean.getContent()));
		if (bean.getTranspond_data() != null) {
			SpannableString value;
			if (!TextUtils.isEmpty(bean.getTranspond_data().getContent())) {
				value = getHighLightLinks(bean.getTranspond_data().getContent());
			} else {
				value = SpannableString.valueOf("");
			}
			bean.getTranspond_data().setListViewSpannableString(value);
		}
	}

	/**
	 * 解析评论内容
	 * 
	 * @param bean
	 */

	public static void addHighLightLinks(CommentBean bean) {

		bean.setListViewSpannableString(getHighLightLinks(bean.getContent()));
	}

	public static SpannableString getHighLightLinks(String txt) {
		String hackTxt;
		if (txt.startsWith("[") && txt.endsWith("]")) {
			hackTxt = txt + " ";
		} else {
			hackTxt = txt;
		}

		// 替换a标签为at
		Pattern AT_URL = Pattern.compile("<[^>]+>([^<]+)<[^>]+>");
		Matcher m = AT_URL.matcher(hackTxt);
		ArrayList<String> urls = new ArrayList<String>();
		while (m.find()) {
			urls.add(m.group(0));
			hackTxt = hackTxt.replace(m.group(0), m.group(1));
		}
		Log.d("wj", "at pattern after:" + hackTxt);

		Pattern MENTION_URL = Pattern
				.compile("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}");

		Pattern WEB_URL = Pattern
				.compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]");

		Pattern TOPIC_URL = Pattern
				.compile("#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#");

		String WEB_SCHEME = "http://";
		String TOPIC_SCHEME = "com.thinksns.jkfs.topic://";
		String MENTION_SCHEME = "com.thinksns.jkfs://";
		SpannableString value = SpannableString.valueOf(hackTxt);
		Linkify.addLinks(value, MENTION_URL, MENTION_SCHEME);
		Linkify.addLinks(value, WEB_URL, WEB_SCHEME);
		Linkify.addLinks(value, TOPIC_URL, TOPIC_SCHEME);
		URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
		MyURLSpan weiboSpan = null;
		int at_count = 0;
		Log.d("wj", "urlSpans.length:" + urlSpans.length);
		if (urlSpans.length > 0)
			for (int i = 0; i < urlSpans.length; ++i) {
				String scheme = Uri.parse(urlSpans[i].getURL()).getScheme();
				Log.d("wj", "urlSpans scheme:" + scheme);
				if (scheme.startsWith("http")) {
					weiboSpan = new MyURLSpan(urlSpans[i].getURL());
				} else if (scheme.startsWith("com.thinksns.jkfs.topic")) {
					int length = urlSpans[i].getURL().length();
					String topic = urlSpans[i].getURL().substring(27,
							length - 1);
					weiboSpan = new MyURLSpan(urlSpans[i].getURL(), "", topic);
				} else {
					Log.d("wj", "it goes to here -- contains at");
					if (urls.size() > 0) { // 为何size为0？待定
						String content = urls.get(at_count++);
						String result = "";
						String pattern = "uid=([0-9]*)";
						Pattern p = Pattern
								.compile(pattern, 2 | Pattern.DOTALL);
						Matcher matcher = p.matcher(content);
						if (matcher.find()) {
							result = matcher.group(1); // 得到url中的uid
						}
						weiboSpan = new MyURLSpan(urlSpans[i].getURL(), result);
					}
				}
				int start = value.getSpanStart(urlSpans[i]);
				int end = value.getSpanEnd(urlSpans[i]);
				value.removeSpan(urlSpans[i]);
				value.setSpan(weiboSpan, start, end,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		addEmotions(value);

		return value;
	}

	/**
	 * 增加表情
	 * 
	 * @param value
	 */
	public static void addEmotions(SpannableString value) {
		Matcher localMatcher = Pattern.compile("\\[(\\S+?)\\]").matcher(value);
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			int end = str2.length();
			String string = str2.substring(1, end - 1);
			Log.v("emotion image name", string);
			int k = localMatcher.start();
			int m = localMatcher.end();
			Bitmap bitmap;
			if (Arrays.asList(emotions).contains(string)) // 防止解析特殊字符影响反射
				try {
					bitmap = BitmapFactory.decodeResource(ThinkSNSApplication
							.getInstance().getResources(), R.drawable.class
							.getField(string).getInt(R.drawable.class));
					if (bitmap != null) {
						ImageSpan localImageSpan = new ImageSpan(
								ThinkSNSApplication.getInstance(), bitmap,
								ImageSpan.ALIGN_BASELINE);
						value.setSpan(localImageSpan, k, m,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
	}

}
