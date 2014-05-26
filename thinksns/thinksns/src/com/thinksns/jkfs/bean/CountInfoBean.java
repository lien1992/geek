package com.thinksns.jkfs.bean;


/**
 * @author 邓思宇
 * USERINFOBEAN 里面需要的 COUNTINFOBEAN
 *
 */
public class CountInfoBean {

	private String following_count;
	private String follower_count;
	private String weibo_count;

	public CountInfoBean() {
	}

	public CountInfoBean(String follower_count, String following_count,
			String weibo_count) {
		this.follower_count = follower_count;
		this.following_count = following_count;
		this.weibo_count = weibo_count;
	}

	public String getFollowing_count() {
		return following_count;
	}

	public void setFollowing_count(String following_count) {
		this.following_count = following_count;
	}

	public String getFollower_count() {
		return follower_count;
	}

	public void setFollower_count(String follower_count) {
		this.follower_count = follower_count;
	}

	public String getWeibo_count() {
		return weibo_count;
	}

	public void setWeibo_count(String weibo_count) {
		this.weibo_count = weibo_count;
	}

	@Override
	public String toString() {
		return "count_info =  " + this.follower_count + this.following_count
				+ this.weibo_count;
	}
}
