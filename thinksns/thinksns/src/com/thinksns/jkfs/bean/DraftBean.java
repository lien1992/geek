package com.thinksns.jkfs.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 草稿
 * 
 */
@Table(name = "draft")
public class DraftBean {
	@Id
	private int id;
	@Column(column = "content")
	private String content;

	public DraftBean() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
