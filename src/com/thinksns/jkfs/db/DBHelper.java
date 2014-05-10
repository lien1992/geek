package com.thinksns.jkfs.db;

import com.thinksns.jkfs.support.util.ThinkSNSApplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static DBHelper singleton = null;

	private static final String DATABASE_NAME = "thinksns.db";

	private static final int DATABASE_VERSION = 1;

	static final String CREATE_ACCOUNT_TABLE_SQL = "create table "
			+ AccountOperator.TABLE_NAME + "(" + AccountOperator.UID
			+ " text primary key," + AccountOperator.OAUTH_TOKEN + " text,"
			+ AccountOperator.OAUTH_TOKEN_SECRET + " text," + ");";

	static final String CREATE_USER_INFO_TABLE_SQL = "create table "
			+ UserInfoOperator.TABLE_NAME + "(" + UserInfoOperator.ID
			+ " text primary key autoincrement," + UserInfoOperator.UID + "text,"
			+ UserInfoOperator.UNAME + " text," + UserInfoOperator.EMAIL
			+ " text," + UserInfoOperator.SEX + " text,"
			+ UserInfoOperator.PROVINCE + " text," + UserInfoOperator.CITY
			+ " text," + UserInfoOperator.AVATAR_URL + " text," + ");";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	public static synchronized DBHelper getInstance() {
		if (singleton == null) {
			singleton = new DBHelper(ThinkSNSApplication.getInstance());
		}
		return singleton;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_ACCOUNT_TABLE_SQL);
		db.execSQL(CREATE_USER_INFO_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}
