package com.thinksns.jkfs.db;


import com.thinksns.jkfs.bean.AccountBean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * account表操作类
 * 
 * @author wangjia
 * 
 */
public class AccountOperator {

	public static final String TABLE_NAME = "account_table";
	public static final String UID = "_id";
	public static final String OAUTH_TOKEN = "oauth_token";
	public static final String OAUTH_TOKEN_SECRET = "oauth_token_secret";

	private AccountOperator() {

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
	 * 添加/更新帐户信息
	 * 
	 * @param account
	 */
	public static void addOrUpdate(AccountBean account) {
		ContentValues cv = new ContentValues();
		cv.put(UID, account.getUid());
		cv.put(OAUTH_TOKEN, account.getOauth_token());
		cv.put(OAUTH_TOKEN_SECRET, account.getOauth_token_secret());
		Cursor c = getWdb().query(TABLE_NAME, null, UID + "=?",
				new String[] { account.getUid() }, null, null, null);
		if (c != null && c.getCount() > 0) {
			getWdb().update(TABLE_NAME, cv, UID + "=?",
					new String[] { account.getUid() });
		} else {
			getWdb().insert(TABLE_NAME, UID, cv);
		}
	}

	/**
	 * 返回帐户信息
	 * 
	 * @param uid
	 * @return
	 */
	public static AccountBean getAccount(String uid) {
		String sql = "select * from " + TABLE_NAME + " where " + UID + " = "
				+ uid;
		Cursor c = getRdb().rawQuery(sql, null);
		if (c.moveToNext()) {
			AccountBean account = new AccountBean();
			account.setUid(uid);
			account.setOauth_token(c.getString(c.getColumnIndex(OAUTH_TOKEN)));
			account.setOauth_token_secret(c.getString(c
					.getColumnIndex(OAUTH_TOKEN_SECRET)));
			return account;
		}
		return null;
	}

}
