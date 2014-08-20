package com.thinksns.jkfs.util.http;

import java.util.Map;

/**
 * http工具类
 * 
 * @author wangjia
 * 
 */
public class HttpUtility {

	private static HttpUtility httpUtility = new HttpUtility();

	private HttpUtility() {
	}

	public static HttpUtility getInstance() {
		return httpUtility;
	}

	/**
	 * 执行http GET/POST请求
	 * 
	 * @param httpMethod
	 * @param url
	 * @param param
	 * @return
	 */
	public String executeNormalTask(HttpMethod httpMethod, String url,
			Map<String, String> param) {
		return new HttpUtils().executeNormalTask(httpMethod, url, param);
	}
}
