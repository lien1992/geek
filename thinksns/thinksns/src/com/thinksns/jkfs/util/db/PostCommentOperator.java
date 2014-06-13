package com.thinksns.jkfs.util.db;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thinksns.jkfs.bean.PostCommentBean;

/**
 * @author 杨智勇
 * @since 2014-5-30
 */

public class PostCommentOperator {
	public static final String TABLE_NAME = "post_comment_table";
	public static final String REPLY_ID = "reply_id";
	public static final String POST_ID = "post_id";
	public static final String UID = "uid";
	public static final String UNAME = "uname";
	public static final String AVATAR_TINY = "avatar_tiny";
	public static final String TO_REPLY_ID = "to_reply_id";
	public static final String TO_UID = "to_uid";
	public static final String CTIME = "ctime";
	public static final String CONTENT = "content";
	public static final String STOREY = "storey";

	private DBHelper helper;
	private SQLiteDatabase db;

	public PostCommentOperator() {
		helper = DBHelper.getInstance();
		db = helper.getWritableDatabase();
	}

	public static PostCommentOperator getInstance() {
		return new PostCommentOperator();
	}

	public void addOrUpdateList(List<PostCommentBean> commentList) {
		db.beginTransaction();
		try {
			for (PostCommentBean comment : commentList) {
				Cursor c = db.query(TABLE_NAME, null, REPLY_ID + "=?",
						new String[] { comment.getReply_id() }, null, null,
						null);
				if (c != null && c.getCount() > 0) {
					update(comment);
				} else {
					add(comment);
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public void add(PostCommentBean comment) {
		ContentValues cv = new ContentValues();
		cv.put(REPLY_ID, comment.getReply_id());
		cv.put(POST_ID, comment.getPost_id());
		cv.put(UID, comment.getUid());
		cv.put(UNAME, comment.getUname());
		cv.put(AVATAR_TINY, comment.getAvatar_tiny());
		cv.put(TO_REPLY_ID, comment.getTo_reply_id());
		cv.put(TO_UID, comment.getTo_reply_id());
		cv.put(CTIME, comment.getCtime());
		cv.put(CONTENT, comment.getContent());
		cv.put(STOREY, comment.getStorey());
		db.insert(TABLE_NAME, null, cv);
	}

	public void update(PostCommentBean comment) {
		ContentValues cv = new ContentValues();
		cv.put(REPLY_ID, comment.getReply_id());
		cv.put(POST_ID, comment.getPost_id());
		cv.put(UID, comment.getUid());
		cv.put(UNAME, comment.getUname());
		cv.put(AVATAR_TINY, comment.getAvatar_tiny());
		cv.put(TO_REPLY_ID, comment.getTo_reply_id());
		cv.put(TO_UID, comment.getTo_reply_id());
		cv.put(CTIME, comment.getCtime());
		cv.put(CONTENT, comment.getContent());
		cv.put(STOREY, comment.getStorey());
		db.update(TABLE_NAME, cv, REPLY_ID + "=?",
				new String[] { comment.getReply_id() });
	}

	public LinkedList<PostCommentBean> queryList() {
		LinkedList<PostCommentBean> commentList = new LinkedList<PostCommentBean>();
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
		if (c != null) {
			while (c.moveToNext()) {
				PostCommentBean comment = new PostCommentBean();
				comment.setReply_id(c.getString(c.getColumnIndex(REPLY_ID)));
				comment.setPost_id(c.getString(c.getColumnIndex(POST_ID)));
				comment.setUid(c.getString(c.getColumnIndex(UID)));
				comment.setUname(c.getString(c.getColumnIndex(UNAME)));
				comment.setAvatar_tiny(c.getString(c.getColumnIndex(AVATAR_TINY)));
				comment.setTo_reply_id(c.getString(c.getColumnIndex(TO_REPLY_ID)));
				comment.setTo_uid(c.getString(c.getColumnIndex(TO_UID)));
				comment.setCtime(c.getString(c.getColumnIndex(CTIME)));
				comment.setContent(c.getString(c.getColumnIndex(CONTENT)));
				comment.setStorey(c.getString(c.getColumnIndex(STOREY)));
				commentList.add(comment);
			}
			c.close();
		}
		return commentList;
	}

	public PostCommentBean queryByID(String reply_id) {
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME
				+ " WHERE comment_id=" + reply_id, null);
		PostCommentBean comment = new PostCommentBean();
		if (c != null) {
			while (c.moveToNext()) {
				comment.setReply_id(c.getString(c.getColumnIndex(REPLY_ID)));
				comment.setPost_id(c.getString(c.getColumnIndex(POST_ID)));
				comment.setUid(c.getString(c.getColumnIndex(UID)));
				comment.setUname(c.getString(c.getColumnIndex(UNAME)));
				comment.setAvatar_tiny(c.getString(c.getColumnIndex(AVATAR_TINY)));
				comment.setTo_reply_id(c.getString(c.getColumnIndex(TO_REPLY_ID)));
				comment.setTo_uid(c.getString(c.getColumnIndex(TO_UID)));
				comment.setCtime(c.getString(c.getColumnIndex(CTIME)));
				comment.setContent(c.getString(c.getColumnIndex(CONTENT)));
				comment.setStorey(c.getString(c.getColumnIndex(STOREY)));
			}
			c.close();
		}
		
		return comment;
	}

	public void deleteById(String reply_id){
		db.delete(TABLE_NAME, "reply_id=?", new String[]{reply_id});
	}
	
	public void deleteAll() {
		db.delete(TABLE_NAME, null, null);
	}
 
	public void closeDB() {
		db.close();
	}

}
