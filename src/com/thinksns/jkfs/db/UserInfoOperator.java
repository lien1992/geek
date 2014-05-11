package com.thinksns.jkfs.db;

import java.util.ArrayList;
import java.util.List;

import com.thinksns.jkfs.bean.UserInfoBean;
import com.thinksns.jkfs.bean.UserInfoListBean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * user_info表操作类
 * 
 * @author wangjia
 * 
 */
public class UserInfoOperator {
	public static final String TABLE_NAME = "user_info_table";
	public static final String ID = "_id";
	public static final String UID = "uid";
	public static final String UNAME = "uname";
	public static final String EMAIL = "email";
	public static final String SEX = "sex";
	public static final String PROVINCE = "province";
	public static final String CITY = "city";
	public static final String AVATAR_URL = "avatar_url";

	private UserInfoOperator() {

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
	 * 添加/更新用户资料
	 * 
	 * @param userInfo
	 */
	public static void addOrUpdate(UserInfoBean userInfo) {
		ContentValues cv = new ContentValues();
		cv.put(UID, userInfo.getUid());
		cv.put(UNAME, userInfo.getUname());
		cv.put(EMAIL, userInfo.getEmail());
		cv.put(SEX, userInfo.getSex());
		cv.put(PROVINCE, userInfo.getProvince());
		cv.put(CITY, userInfo.getCity());
		Cursor c = getWdb().query(TABLE_NAME, null, UID + "=?",
				new String[] { userInfo.getUid() }, null, null, null);
		if (c != null && c.getCount() > 0) {
			getWdb().update(TABLE_NAME, cv, UID + "=?",
					new String[] { userInfo.getUid() });
		} else {
			getWdb().insert(TABLE_NAME, UID, cv);
		}
	}

	/**
	 * 返回用户信息列表
	 * 
	 * @return
	 */
	public static UserInfoListBean getUserInfoList() {
		UserInfoListBean uilb = new UserInfoListBean();
		List<UserInfoBean> userInfoList = new ArrayList<UserInfoBean>();
		String sql = "select * from " + TABLE_NAME;
		Cursor c = getWdb().rawQuery(sql, null);
		while (c.moveToNext()) {
			UserInfoBean uib = new UserInfoBean();
			uib.setUid(c.getString(c.getColumnIndex(UID)));
			uib.setUname(c.getString(c.getColumnIndex(UNAME)));
			uib.setEmail(c.getString(c.getColumnIndex(EMAIL)));
			uib.setSex(c.getString(c.getColumnIndex(SEX)));
			uib.setProvince(c.getString(c.getColumnIndex(PROVINCE)));
			uib.setCity(c.getString(c.getColumnIndex(CITY)));
			userInfoList.add(uib);
		}
		uilb.setUsers(userInfoList);
		return uilb;
	}

}
