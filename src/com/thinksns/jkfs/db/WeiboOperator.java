package com.thinksns.jkfs.db;

import java.util.ArrayList;
import java.util.List;

import com.thinksns.jkfs.bean.WeiboBean;
import com.thinksns.jkfs.bean.WeiboListBean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WeiboOperator {
	public static final String TABLE_NAME = "weibo_table";
	public static final String ID = "_id";
	public static final String CONTENT = "content";
	public static final String TIME = "time";
	public static final String FROM = "from";
	public static final String UID = "uid";
	public static final String UNAME = "uname";
	public static final String COMMENT_COUNT = "comment_count";
	public static final String REPOST_COUNT = "repost_count";

	private WeiboOperator() {

	}

	private static SQLiteDatabase getWdb() {
		DBHelper databaseHelper = DBHelper.getInstance();
		return databaseHelper.getWritableDatabase();
	}

	private static SQLiteDatabase getRdb() {
		DBHelper databaseHelper = DBHelper.getInstance();
		return databaseHelper.getReadableDatabase();
	}

	/**
	 * 保存接收到的微博列表
	 * 
	 * @param weiboList
	 */
	public static void addWeiboList(WeiboListBean weiboList) {
		if (weiboList == null || weiboList.getSize() == 0)
			return;
		List<WeiboBean> weibos = weiboList.getWeibos();
		for (int i = weibos.size(); i >= 0; --i) {
			ContentValues cv = new ContentValues();
			cv.put(ID, weibos.get(i).getId());
			cv.put(CONTENT, weibos.get(i).getContent());
			cv.put(TIME, weibos.get(i).getTime());
			cv.put(FROM, weibos.get(i).getFrom());
			cv.put(UID, weibos.get(i).getUid());
			cv.put(UNAME, weibos.get(i).getUname());
			cv.put(COMMENT_COUNT, weibos.get(i).getComment_count());
			cv.put(REPOST_COUNT, weibos.get(i).getRepost_count());
			getWdb().insert(TABLE_NAME, CONTENT, cv);
		}

	}

	/**
	 * 获取缓存的微博列表
	 * 
	 * @return
	 */
	public static WeiboListBean getWeiboList() {
		WeiboListBean wlb = new WeiboListBean();
		List<WeiboBean> weibos = new ArrayList<WeiboBean>();
		String sql = "select * from " + TABLE_NAME;
		Cursor c = getWdb().rawQuery(sql, null);
		while (c.moveToNext()) {
			WeiboBean weibo = new WeiboBean();
			weibo.setId(c.getString(c.getColumnIndex(ID)));
			weibo.setContent(c.getString(c.getColumnIndex(CONTENT)));
			weibo.setTime(c.getString(c.getColumnIndex(TIME)));
			weibo.setFrom(c.getString(c.getColumnIndex(FROM)));
			weibo.setUid(c.getString(c.getColumnIndex(UID)));
			weibo.setUname(c.getString(c.getColumnIndex(UNAME)));
			weibo.setComment_count(c.getInt(c.getColumnIndex(COMMENT_COUNT)));
			weibo.setRepost_count(c.getInt(c.getColumnIndex(REPOST_COUNT)));
			weibos.add(weibo);
		}
		wlb.setWeibos(weibos);
		return wlb;
	}

}
