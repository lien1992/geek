package com.thinksns.jkfs.support.http;

import java.util.Map;

public class HttpUtility {

	private static HttpUtility httpUtility = new HttpUtility();

	private HttpUtility() {
	}

	public static HttpUtility getInstance() {
		return httpUtility;
	}

	public String executeNormalTask(HttpMethod httpMethod, String url,
			Map<String, String> param) {
		return new HttpUtils().executeNormalTask(httpMethod, url, param);
	}
}
