package com.thinksns.jkfs.util.db;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thinksns.jkfs.bean.PostBean;

/**
 * @author 杨智勇
 * @since 2014-5-30
 */
public class PostOperator {
	public static final String TABLE_NAME1 = "following_posts_table";
	public static final String TABLE_NAME2 = "posteds_table";
	public static final String TABLE_NAME3 = "commenteds_table";
	public static final String TABLE_NAME4 = "post_favorite_table";
	public static final String POST_ID = "post_id";
	public static final String WEIBA_ID = "weiba_id";
	public static final String POST_UID = "post_uid";
	public static final String UNAME = "uname";
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String POST_TIME = "post_time";
	public static final String REPLY_COUNT = "reply_count";
	public static final String READ_COUNT = "read_count";
	public static final String TOP = "top";
	public static final String RECOMMEND = "recommend";
	public static final String AVATAR_TINY = "avatar_tiny";
	public static final String FAVORITE = "favorite";

	private String table;
	private DBHelper helper;
	private SQLiteDatabase db;

	public PostOperator(int flag) {
		helper = DBHelper.getInstance();
		db = helper.getWritableDatabase();
		switch (flag) {
		case 0:
			table = TABLE_NAME1;
			break;
		case 1:
			table = TABLE_NAME2;
			break;
		case 2:
			table = TABLE_NAME3;
			break;
		case 3:
			table = TABLE_NAME4;
			break;
		}
	}

	public static PostOperator getInstance(int flag) {
		return new PostOperator(flag);
	}

	public static String getTableName(int flag) {
		switch (flag) {
		case 1:
			return TABLE_NAME2;
		case 2:
			return TABLE_NAME3;
		case 3:
			return TABLE_NAME4;
		default:
			return TABLE_NAME1;
		}
	}

	public void addOrUpdateList(List<PostBean> postList) {
		db.beginTransaction();
		try {
			for (PostBean post : postList) {
				Cursor c = db.query(table, null, POST_ID + "=?",
						new String[] { post.getPost_id() }, null, null, null);
				if (c != null && c.getCount() > 0) {
					update(post);
				} else {
					add(post);
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public void add(PostBean post) {
		ContentValues cv = new ContentValues();
		cv.put(POST_ID, post.getPost_id());
		cv.put(WEIBA_ID, post.getWeiba_id());
		cv.put(POST_UID, post.getPost_uid());
		cv.put(UNAME, post.getUname());
		cv.put(TITLE, post.getTitle());
		cv.put(CONTENT, post.getContent());
		cv.put(POST_TIME, post.getPost_time());
		cv.put(REPLY_COUNT, post.getReply_count());
		cv.put(READ_COUNT, post.getRead_count());
		cv.put(TOP, post.getTop());
		cv.put(RECOMMEND, post.getRecommend());
		cv.put(AVATAR_TINY, post.getAvatar_tiny());
		cv.put(FAVORITE, post.getFavorite());
		db.insert(table, null, cv);
	}

	public void update(PostBean post) {
		ContentValues cv = new ContentValues();
		cv.put(POST_ID, post.getPost_id());
		cv.put(WEIBA_ID, post.getWeiba_id());
		cv.put(POST_UID, post.getPost_uid());
		cv.put(UNAME, post.getUname());
		cv.put(TITLE, post.getTitle());
		cv.put(CONTENT, post.getContent());
		cv.put(POST_TIME, post.getPost_time());
		cv.put(REPLY_COUNT, post.getReply_count());
		cv.put(READ_COUNT, post.getRead_count());
		cv.put(TOP, post.getTop());
		cv.put(RECOMMEND, post.getRecommend());
		cv.put(AVATAR_TINY, post.getAvatar_tiny());
		cv.put(FAVORITE, post.getFavorite());
		db.update(table, cv, POST_ID + "=?", new String[] { post.getPost_id() });
	}

	public void updateFavoriteState(String post_id, int flag) {
		ContentValues cv = new ContentValues();
		cv.put(FAVORITE, flag);
		db.update(table, cv, POST_ID + "=?", new String[] { post_id });
	}

	public LinkedList<PostBean> queryList() {
		LinkedList<PostBean> postList = new LinkedList<PostBean>();
		Cursor c = db.rawQuery("SELECT * FROM " + table, null);
		if (c != null) {
			while (c.moveToNext()) {
				PostBean post = new PostBean();
				post.setPost_id(c.getString(c.getColumnIndex(POST_ID)));
				post.setWeiba_id(c.getString(c.getColumnIndex(WEIBA_ID)));
				post.setPost_uid(c.getString(c.getColumnIndex(POST_UID)));
				post.setUname(c.getString(c.getColumnIndex(UNAME)));
				post.setTitle(c.getString(c.getColumnIndex(TITLE)));
				post.setContent(c.getString(c.getColumnIndex(CONTENT)));
				post.setPost_time(c.getString(c.getColumnIndex(POST_TIME)));
				post.setReply_count(c.getString(c.getColumnIndex(REPLY_COUNT)));
				post.setRead_count(c.getString(c.getColumnIndex(READ_COUNT)));
				post.setTop(c.getString(c.getColumnIndex(TOP)));
				post.setRecommend(c.getString(c.getColumnIndex(RECOMMEND)));
				post.setAvatar_tiny(c.getString(c.getColumnIndex(AVATAR_TINY)));
				post.setFavorite(c.getInt(c.getColumnIndex(FAVORITE)));
				postList.add(post);
			}
			c.close();
		}
		return postList;
	}

	public PostBean queryByID(String post_id) {
		Cursor c = db.rawQuery("SELECT * FROM " + table + " WHERE post_id="
				+ post_id, null);
		PostBean post = new PostBean();
		if (c != null) {
			while (c.moveToNext()) {
				post.setPost_id(c.getString(c.getColumnIndex(POST_ID)));
				post.setWeiba_id(c.getString(c.getColumnIndex(WEIBA_ID)));
				post.setPost_uid(c.getString(c.getColumnIndex(POST_UID)));
				post.setUname(c.getString(c.getColumnIndex(UNAME)));
				post.setTitle(c.getString(c.getColumnIndex(TITLE)));
				post.setContent(c.getString(c.getColumnIndex(CONTENT)));
				post.setPost_time(c.getString(c.getColumnIndex(POST_TIME)));
				post.setReply_count(c.getString(c.getColumnIndex(REPLY_COUNT)));
				post.setRead_count(c.getString(c.getColumnIndex(READ_COUNT)));
				post.setTop(c.getString(c.getColumnIndex(TOP)));
				post.setRecommend(c.getString(c.getColumnIndex(RECOMMEND)));
				post.setAvatar_tiny(c.getString(c.getColumnIndex(AVATAR_TINY)));
				post.setFavorite(c.getInt(c.getColumnIndex(FAVORITE)));
			}
			c.close();
		}
		return post;
	}

	public void deleteAll() {
		db.delete(table, null, null);
	}

	public void closeDB() {
		db.close();
	}
}
