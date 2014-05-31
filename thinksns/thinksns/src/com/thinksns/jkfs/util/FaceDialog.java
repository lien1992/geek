package com.thinksns.jkfs.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinksns.jkfs.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

/**
 * 表情选择,bug待修复：在评论中添加完表情后转发无法添加表情
 */

public class FaceDialog {
	private static PopupWindow popupWindow;

	public static interface FaceSelect {
		// 选中表情后回调
		void onFaceSelect(SpannableString spannableString);
	}

	public static void showFaceDialog(Context context, View parent, int y,
			FaceSelect faceSelect) {
		if (popupWindow == null) {
			popupWindow = new PopupWindow(context);
			popupWindow.setWidth(LayoutParams.MATCH_PARENT);
			popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.face_dialog, null);
			int[] dotId = { R.id.face_dialog_dot_1, R.id.face_dialog_dot_2,
					R.id.face_dialog_dot_3, R.id.face_dialog_dot_4,
					R.id.face_dialog_dot_5, R.id.face_dialog_dot_6 };
			ViewPager pager = (ViewPager) view
					.findViewById(R.id.face_dialog_viewpager);
			final ImageView[] dots = new ImageView[dotId.length];
			for (int i = 0; i < dotId.length; i++)
				dots[i] = (ImageView) view.findViewById(dotId[i]);
			dots[0].setBackgroundResource(R.drawable.dot_selected);// 选中第一个圆点
			try {
				pager.setAdapter(new MyViewPagerAdapter(getViews(context,
						inflater, faceSelect)));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					for (int i = 0; i < dots.length; i++) {
						if (i == arg0)
							dots[i].setImageResource(R.drawable.dot_selected);
						else
							dots[i].setImageResource(R.drawable.dot_unselected);
					}

				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}

				@Override
				public void onPageScrollStateChanged(int arg0) {

				}
			});
			popupWindow.setContentView(view);
		}
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int height = wm.getDefaultDisplay().getHeight();
		Log.d("screen height", height + "");// 888
		Log.d("y height", y + "");// 75
		popupWindow.showAtLocation(parent, Gravity.TOP, 0, height - y - 160);
	}

	public static void close() {
		if (popupWindow != null)
			popupWindow.dismiss();
	}

	public final static String[] imageNames = { "aoman", "baiyan", "bishi",
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

	private static List<View> getViews(final Context context,
			LayoutInflater inflater, final FaceSelect faceSelect) {
		List<View> views = new ArrayList<View>();
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < imageNames.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			try {
				listItem.put("image", R.drawable.class.getField(imageNames[i])
						.getInt(R.drawable.class));
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
			listItems.add(listItem);
		}

		for (int i = 0; i < 6; i++) {
			GridView view = new GridView(context);
			SimpleAdapter simpleAdapter;
			if (i != 5) {
				simpleAdapter = new SimpleAdapter(context, listItems.subList(
						i * 21, (i + 1) * 21),
						R.layout.face_dialog_gridview_item,
						new String[] { "image" }, new int[] { R.id.face_image });

			} else
				simpleAdapter = new SimpleAdapter(context, listItems.subList(
						i * 21, listItems.size()),
						R.layout.face_dialog_gridview_item,
						new String[] { "image" }, new int[] { R.id.face_image });

			view.setAdapter(simpleAdapter);
			view.setNumColumns(7);
			view.setHorizontalSpacing(0);
			view.setVerticalSpacing(0);
			view.setSelector(R.drawable.remove_yellow_bg);
			view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			final int p = i;
			view.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					Bitmap bitmap = null;
					int po = position + 21 * p;
					try { // 反射机制获取图片ID，测试中..
						bitmap = BitmapFactory.decodeResource(context
								.getResources(), R.drawable.class.getField(
								imageNames[po % imageNames.length]).getInt(
								R.drawable.class));
						Log.d("image ID", R.drawable.class.getField(
								imageNames[po % imageNames.length]).getInt(
								R.drawable.class)
								+ "");
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
					ImageSpan imageSpan = new ImageSpan(context, bitmap);
					String str = "[" + imageNames[po % imageNames.length] + "]";
					SpannableString spannableString = new SpannableString(str);
					spannableString.setSpan(imageSpan, 0, str.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					if (faceSelect != null)
						faceSelect.onFaceSelect(spannableString);
				}
			});
			views.add(view);
		}

		return views;
	}

	public static void release() {
		popupWindow = null;
	}
}

class MyViewPagerAdapter extends PagerAdapter {

	private List<View> mListViews;

	public MyViewPagerAdapter(List<View> mListViews) {
		this.mListViews = mListViews;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mListViews.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mListViews.get(position), 0);
		return mListViews.get(position);
	}

	@Override
	public int getCount() {
		return mListViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
