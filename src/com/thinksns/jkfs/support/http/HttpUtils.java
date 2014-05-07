package com.thinksns.jkfs.support.http;

import android.text.TextUtils;

import com.thinksns.jkfs.support.util.AppLogger;
import com.thinksns.jkfs.support.util.Utility;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * HTTP工具类
 * 
 */
public class HttpUtils {

	private static final int CONNECT_TIMEOUT = 10 * 1000;
	private static final int READ_TIMEOUT = 10 * 1000;

	public class NullHostNameVerifier implements HostnameVerifier {

		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	public String executeNormalTask(HttpMethod httpMethod, String url,
			Map<String, String> param) {
		switch (httpMethod) {
		case Post:
			return doPost(url, param);
		case Get:
			return doGet(url, param);
		}
		return "";
	}

	private static Proxy getProxy() {
		String proxyHost = System.getProperty("http.proxyHost");
		String proxyPort = System.getProperty("http.proxyPort");
		if (!TextUtils.isEmpty(proxyHost) && !TextUtils.isEmpty(proxyPort))
			return new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(
					proxyHost, Integer.valueOf(proxyPort)));
		else
			return null;
	}

	/**
	 * 发送HTTP GET请求，返回响应JSON字符串
	 * 
	 * @param urlStr
	 * @param param
	 * @return
	 */
	public String doGet(String urlStr, Map<String, String> param) {
		HttpURLConnection urlConnection = null;
		try {

			StringBuilder urlBuilder = new StringBuilder(urlStr);
			urlBuilder.append("?").append(Utility.encodeUrl(param));
			URL url = new URL(urlBuilder.toString());
			AppLogger.d("get request" + url);
			Proxy proxy = getProxy();
			if (proxy != null)
				urlConnection = (HttpURLConnection) url.openConnection(proxy);
			else
				urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(false);
			urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
			urlConnection.setReadTimeout(READ_TIMEOUT);
			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.setRequestProperty("Charset", "UTF-8");
			urlConnection
					.setRequestProperty("Accept-Encoding", "gzip, deflate");
			urlConnection.connect();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return handleResponse(urlConnection);
	}

	/**
	 * 发送HTTP POST请求，返回响应JSON字符串
	 * 
	 * @param urlAddress
	 * @param param
	 * @return
	 */
	public String doPost(String urlAddress, Map<String, String> param) {
		HttpURLConnection uRLConnection = null;
		try {
			URL url = new URL(urlAddress);
			Proxy proxy = getProxy();
			if (proxy != null)
				uRLConnection = (HttpURLConnection) url.openConnection(proxy);
			else
				uRLConnection = (HttpURLConnection) url.openConnection();

			uRLConnection.setDoInput(true);
			uRLConnection.setDoOutput(true);
			uRLConnection.setRequestMethod("POST");
			uRLConnection.setUseCaches(false);
			uRLConnection.setConnectTimeout(CONNECT_TIMEOUT);
			uRLConnection.setReadTimeout(READ_TIMEOUT);
			uRLConnection.setInstanceFollowRedirects(false);
			uRLConnection.setRequestProperty("Connection", "Keep-Alive");
			uRLConnection.setRequestProperty("Charset", "UTF-8");
			uRLConnection
					.setRequestProperty("Accept-Encoding", "gzip, deflate");
			uRLConnection.connect();

			DataOutputStream out = new DataOutputStream(uRLConnection
					.getOutputStream());
			out.write(Utility.encodeUrl(param).getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return handleResponse(uRLConnection);
	}

	private String handleResponse(HttpURLConnection httpURLConnection) {
		int status = 0;
		try {
			status = httpURLConnection.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
			httpURLConnection.disconnect();
		}

		if (status != HttpURLConnection.HTTP_OK) {
			return handleError(httpURLConnection);
		}

		return readResult(httpURLConnection);
	}

	private String handleError(HttpURLConnection urlConnection) {
		String result = readError(urlConnection);
		AppLogger.e("error=" + result);
		return result;
	}

	private String readResult(HttpURLConnection urlConnection) {
		InputStream is = null;
		BufferedReader buffer = null;
		StringBuilder strBuilder = new StringBuilder();
		try {
			is = urlConnection.getInputStream();

			String content_encode = urlConnection.getContentEncoding();

			if (null != content_encode && !"".equals(content_encode)
					&& content_encode.equals("gzip")) {
				is = new GZIPInputStream(is);
			}

			buffer = new BufferedReader(new InputStreamReader(is));

			String line;
			while ((line = buffer.readLine()) != null) {
				strBuilder.append(line);
			}
			AppLogger.d("result=" + strBuilder.toString());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Utility.closeSilently(is);
			Utility.closeSilently(buffer);
			urlConnection.disconnect();
		}
		return strBuilder.toString();

	}

	private String readError(HttpURLConnection urlConnection) {
		InputStream is = null;
		BufferedReader buffer = null;
		StringBuilder strBuilder = new StringBuilder();
		try {
			is = urlConnection.getErrorStream();

			String content_encode = urlConnection.getContentEncoding();

			if (null != content_encode && !"".equals(content_encode)
					&& content_encode.equals("gzip")) {
				is = new GZIPInputStream(is);
			}

			buffer = new BufferedReader(new InputStreamReader(is));

			String line;
			while ((line = buffer.readLine()) != null) {
				strBuilder.append(line);
			}
			AppLogger.d("error result=" + strBuilder.toString());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Utility.closeSilently(is);
			Utility.closeSilently(buffer);
			urlConnection.disconnect();
		}
		return strBuilder.toString();

	}

}
