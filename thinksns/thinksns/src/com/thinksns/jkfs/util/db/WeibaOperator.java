package com.thinksns.jkfs.util.db;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thinksns.jkfs.bean.WeibaBean;

/**
 * @author 杨智勇
 * @since 2014-5-30
 */
public class WeibaOperator {
	public static final String TABLE_NAME = "weiba_table";
	public static final String WEIBA_ID = "weiba_id";
	public static final String WEIBA_NAME = "weiba_name";
	public static final String INTRO = "intro";
	public static final String FOLLOWER_COUNT = "follower_count";
	public static final String THREAD_COUNT = "thread_count";
	public static final String NOTIFY = "notify";
	public static final String LOGO_URL = "logo_url";
	public static final String FOLLOW_STATE = "follow_state";
	public static final String POST_STATUS = "post_status";

	private DBHelper helper;
	private SQLiteDatabase db;
	private static WeibaOperator wo;

	private WeibaOperator() {
		helper = DBHelper.getInstance();
		db = helper.getWritableDatabase();
	}

	public static WeibaOperator getInstance() {
		if (wo == null) {
			wo = new WeibaOperator();
		}
		return wo;
	}

	public void addOrUpdateList(List<WeibaBean> weibaList) {
		db.beginTransaction();
		try {
			for (WeibaBean weiba : weibaList) {
				Cursor c = db.query(TABLE_NAME, null, WEIBA_ID + "=?",
						new String[] { weiba.getWeiba_id() }, null, null, null);
				if (c != null && c.getCount() > 0) {
					update(weiba);
				} else {
					add(weiba);
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public void add(WeibaBean weiba) {
		ContentValues cv = new ContentValues();
		cv.put(WEIBA_ID, weiba.getWeiba_id());
		cv.put(WEIBA_NAME, weiba.getWeiba_name());
		cv.put(INTRO, weiba.getIntro());
		cv.put(FOLLOWER_COUNT, weiba.getFollower_count());
		cv.put(THREAD_COUNT, weiba.getThread_count());
		cv.put(NOTIFY, weiba.getNotify());
		cv.put(LOGO_URL, weiba.getLogo_url());
		cv.put(FOLLOW_STATE, weiba.getFollow_state());
		cv.put(POST_STATUS, weiba.getPost_status());
		db.insert(TABLE_NAME, null, cv);
	}

	public void update(WeibaBean weiba) {
		ContentValues cv = new ContentValues();
		cv.put(WEIBA_NAME, weiba.getWeiba_name());
		cv.put(INTRO, weiba.getIntro());
		cv.put(FOLLOWER_COUNT, weiba.getFollower_count());
		cv.put(THREAD_COUNT, weiba.getThread_count());
		cv.put(NOTIFY, weiba.getNotify());
		cv.put(LOGO_URL, weiba.getLogo_url());
		cv.put(FOLLOW_STATE, weiba.getFollow_state());
		cv.put(POST_STATUS, weiba.getPost_status());
		db.update(TABLE_NAME, cv, WEIBA_ID + "=?",
				new String[] { weiba.getWeiba_id() });
	}

	public void updateFollowState(String weiba_id, int flag) {
		ContentValues cv = new ContentValues();
		cv.put(FOLLOW_STATE, flag);
		db.update(TABLE_NAME, cv, WEIBA_ID + "=?", new String[] { weiba_id });
	}

	public LinkedList<WeibaBean> queryList() {
		LinkedList<WeibaBean> weibaList = new LinkedList<WeibaBean>();
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
		if (c != null) {
			while (c.moveToNext()) {
				WeibaBean weiba = new WeibaBean();
				weiba.setWeiba_id(c.getString(c.getColumnIndex(WEIBA_ID)));
				weiba.setWeiba_name(c.getString(c.getColumnIndex(WEIBA_NAME)));
				weiba.setIntro(c.getString(c.getColumnIndex(INTRO)));
				weiba.setFollower_count(c.getString(c
						.getColumnIndex(FOLLOWER_COUNT)));
				weiba.setThread_count(c.getString(c
						.getColumnIndex(THREAD_COUNT)));
				weiba.setNotify(c.getString(c.getColumnIndex(NOTIFY)));
				weiba.setLogo_url(c.getString(c.getColumnIndex(LOGO_URL)));
				weiba.setFollow_state(c.getInt(c.getColumnIndex(FOLLOW_STATE)));
				weiba.setPost_status(c.getInt(c.getColumnIndex(POST_STATUS)));
				weibaList.add(weiba);
			}
			c.close();
		}
		return weibaList;
	}

	public WeibaBean queryByID(String weiba_id) {
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " WHERE weiba_id=" + weiba_id, null);
		WeibaBean weiba = new WeibaBean();
		if (c != null) {
			while (c.moveToNext()) {
				weiba.setWeiba_id(c.getString(c.getColumnIndex(WEIBA_ID)));
				weiba.setWeiba_name(c.getString(c.getColumnIndex(WEIBA_NAME)));
				weiba.setIntro(c.getString(c.getColumnIndex(INTRO)));
				weiba.setFollower_count(c.getString(c
						.getColumnIndex(FOLLOWER_COUNT)));
				weiba.setThread_count(c.getString(c
						.getColumnIndex(THREAD_COUNT)));
				weiba.setNotify(c.getString(c.getColumnIndex(NOTIFY)));
				weiba.setLogo_url(c.getString(c.getColumnIndex(LOGO_URL)));
				weiba.setFollow_state(c.getInt(c.getColumnIndex(FOLLOW_STATE)));
				weiba.setPost_status(c.getInt(c.getColumnIndex(POST_STATUS)));
				break;
			}
			c.close();
		}
		return weiba;
	}

	public String queryWeibaNameByID(String weiba_id) {
		Cursor c = db.rawQuery("SELECT " + WEIBA_NAME + " FROM " + TABLE_NAME
				+ " WHERE weiba_id=" + weiba_id, null);
		String weiba_name = "未找到";
		if (c != null) {
			while (c.moveToNext()) {
				weiba_name = c.getString(0);
				break;
			}
			c.close();
		}
		return weiba_name;
	}

	public LinkedList<WeibaBean> queryFollowedWeibaList() {
		LinkedList<WeibaBean> weibaList = new LinkedList<WeibaBean>();
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
				+ FOLLOW_STATE + "=1", null);
		if (c != null) {
			while (c.moveToNext()) {
				WeibaBean weiba = new WeibaBean();
				weiba.setWeiba_id(c.getString(c.getColumnIndex(WEIBA_ID)));
				weiba.setWeiba_name(c.getString(c.getColumnIndex(WEIBA_NAME)));
				weiba.setIntro(c.getString(c.getColumnIndex(INTRO)));
				weiba.setFollower_count(c.getString(c
						.getColumnIndex(FOLLOWER_COUNT)));
				weiba.setThread_count(c.getString(c
						.getColumnIndex(THREAD_COUNT)));
				weiba.setNotify(c.getString(c.getColumnIndex(NOTIFY)));
				weiba.setLogo_url(c.getString(c.getColumnIndex(LOGO_URL)));
				weiba.setFollow_state(c.getInt(c.getColumnIndex(FOLLOW_STATE)));
				weiba.setPost_status(c.getInt(c.getColumnIndex(POST_STATUS)));
				weibaList.add(weiba);
			}
			c.close();
		}
		return weibaList;
	}

	public void deleteAll() {
		db.delete(TABLE_NAME, null, null);
	}

	public void closeDB() {
		db.close();
	}

}
