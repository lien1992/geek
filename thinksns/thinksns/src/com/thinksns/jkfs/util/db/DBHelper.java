package com.thinksns.jkfs.util.db;

import com.thinksns.jkfs.base.ThinkSNSApplication;

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
			+ AccountOperator.OAUTH_TOKEN_SECRET + " text" + ");";
	
	static final String CREATE_WEIBA_TABLE_SQL = "create table "
			+ WeibaOperator.TABLE_NAME + "(" + WeibaOperator.WEIBA_ID
			+ " text primary key," + WeibaOperator.WEIBA_NAME + " text,"
			+ WeibaOperator.INTRO + " text," + WeibaOperator.FOLLOWER_COUNT
			+ " text," + WeibaOperator.THREAD_COUNT + " text,"
			+ WeibaOperator.NOTIFY + " text," + WeibaOperator.LOGO_URL
			+ " text," + WeibaOperator.FOLLOW_STATE + " integer,"
			+ WeibaOperator.POST_STATUS + " integer" + ");";

	static final String CREATE_POST_TABLE_SQL = "(" + PostOperator.POST_ID
			+ " text primary key," + PostOperator.WEIBA_ID + " text,"
			+ PostOperator.POST_UID + " text," + PostOperator.UNAME + " text,"
			+ PostOperator.TITLE + " text," + PostOperator.CONTENT + " text,"
			+ PostOperator.POST_TIME + " text," + PostOperator.REPLY_COUNT
			+ " text," + PostOperator.READ_COUNT + " text," + PostOperator.TOP
			+ " text," + PostOperator.RECOMMEND + " text,"
			+ PostOperator.AVATAR_TINY + " text," + PostOperator.FAVORITE
			+ " integer" + ");";

	static final String CREATE_POST_COMMENT_TABLE_SQL = "create table "
			+ PostCommentOperator.TABLE_NAME + "("
			+ PostCommentOperator.REPLY_ID + " text primary key,"
			+ PostCommentOperator.POST_ID + " text," + PostCommentOperator.UID
			+ " text," + PostCommentOperator.UNAME + " text,"
			+ PostCommentOperator.AVATAR_TINY + " text,"
			+ PostCommentOperator.TO_REPLY_ID + " text,"
			+ PostCommentOperator.TO_UID + " text," + PostCommentOperator.CTIME
			+ " text," + PostCommentOperator.CONTENT + " text,"
			+ PostCommentOperator.STOREY + " text" + ");";

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
		db.execSQL(CREATE_WEIBA_TABLE_SQL);
		db.execSQL(CREATE_POST_COMMENT_TABLE_SQL);
		for (int i = 0; i < 4; i++) {
			String temp = "create table " + PostOperator.getTableName(i)
					+ CREATE_POST_TABLE_SQL;
			db.execSQL(temp);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	public void deleteTableData(){
		for (int i = 0; i < 4; i++) {
			PostOperator.getInstance(i).deleteAll();
		}
		PostCommentOperator.getInstance().deleteAll();
		WeibaOperator.getInstance().deleteAll();
	}

}
