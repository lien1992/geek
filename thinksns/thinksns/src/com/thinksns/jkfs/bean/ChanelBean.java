package com.thinksns.jkfs.bean;

/**
 * 频道
 * 
 * @author zcc
 * @since 14-05-19
 */
public class ChanelBean {
	private String channel_category_id;
	private String title;
	private String pid;
	private String sort;
	private String icon_url;

	public ChanelBean(String channel_category_id, String title, String pid,
			String sort, String icon_url) {
		super();
		this.channel_category_id = channel_category_id;
		this.title = title;
		this.pid = pid;
		this.sort = sort;
		this.icon_url = icon_url;
	}

	public ChanelBean() {
		super();
	}

	public String getChannel_category_id() {
		return channel_category_id;
	}

	public void setChannel_category_id(String channel_category_id) {
		this.channel_category_id = channel_category_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}


	public String getIcon_url() {
		return icon_url;
	}

	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}

}
