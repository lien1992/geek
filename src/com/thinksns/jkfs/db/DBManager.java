package com.thinksns.jkfs.db;

import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	private static DBManager singleton = null;

	private SQLiteDatabase wdb = null;

	private SQLiteDatabase rdb = null;

	private DBHelper databaseHelper = null;

	private DBManager() {

	}

	public synchronized static DBManager getInstance() {

		if (singleton == null) {
			DBHelper databaseHelper = DBHelper.getInstance();
			SQLiteDatabase wdb = databaseHelper.getWritableDatabase();
			SQLiteDatabase rdb = databaseHelper.getReadableDatabase();

			singleton = new DBManager();
			singleton.wdb = wdb;
			singleton.rdb = rdb;
			singleton.databaseHelper = databaseHelper;
		}

		return singleton;
	}

	public static void close() {
		if (singleton != null) {
			singleton.databaseHelper.close();
		}
	}

}
