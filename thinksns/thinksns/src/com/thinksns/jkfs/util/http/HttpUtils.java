package com.thinksns.jkfs.util.http;

import android.text.TextUtils;
import android.util.Log;

import com.thinksns.jkfs.base.ThinkSNSApplication;
import com.thinksns.jkfs.util.AppLogger;
import com.thinksns.jkfs.util.Utility;

import javax.net.ssl.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
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

	public static String executeNormalTask(HttpMethod httpMethod, String url,
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
	public static String doGet(String urlStr, Map<String, String> param) {
		HttpURLConnection urlConnection = null;
		try {

			StringBuilder urlBuilder = new StringBuilder(urlStr);
			urlBuilder.append("?").append(Utility.encodeUrl(param));
			URL url = new URL(urlBuilder.toString());
			Log.d("MOSL", url.toString());
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
	public static String doPost(String urlAddress, Map<String, String> param) {
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

	private static String handleResponse(HttpURLConnection httpURLConnection) {
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

	private static String handleError(HttpURLConnection urlConnection) {
		String result = readError(urlConnection);
		AppLogger.e("error=" + result);
		return result;
	}

	private static String readResult(HttpURLConnection urlConnection) {
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

	private static String readError(HttpURLConnection urlConnection) {
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
			if (is != null)
				buffer = new BufferedReader(new InputStreamReader(is));

			String line;
			if (buffer != null) {
				while ((line = buffer.readLine()) != null) {
					strBuilder.append(line);
				}
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

	private static String getBoundry() {
		StringBuffer _sb = new StringBuffer();
		for (int t = 1; t < 12; t++) {
			long time = System.currentTimeMillis() + t;
			if (time % 3 == 0) {
				_sb.append((char) time % 9);
			} else if (time % 3 == 1) {
				_sb.append((char) (65 + time % 26));
			} else {
				_sb.append((char) (97 + time % 26));
			}
		}
		return _sb.toString();
	}

	/**
	 * 文件上传
	 * 
	 * @param urlStr
	 * @param param
	 * @param path
	 * @return
	 */
	public static boolean doUploadFile(String urlStr,
			Map<String, String> param, String path) {
		String BOUNDARYSTR = getBoundry();
		String BOUNDARY = "--" + BOUNDARYSTR + "\r\n";
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		FileInputStream fis = null;
		ThinkSNSApplication globalContext = ThinkSNSApplication.getInstance();
		globalContext = null;
		InputStream is = null;
		try {
			URL url = null;

			url = new URL(urlStr);

			Proxy proxy = getProxy();
			if (proxy != null)
				urlConnection = (HttpURLConnection) url.openConnection(proxy);
			else
				urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setConnectTimeout(5000);
			urlConnection.setReadTimeout(5000);
			//urlConnection.setChunkedStreamingMode(128 * 1024);
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.setRequestProperty("Charset", "UTF-8");
			urlConnection.setRequestProperty("Content-type",
					"multipart/form-data;boundary=" + BOUNDARYSTR);
			urlConnection.connect();

			out = new BufferedOutputStream(urlConnection.getOutputStream());

			StringBuilder sb = new StringBuilder();

			Map<String, String> paramMap = new HashMap<String, String>();

			for (String key : param.keySet()) {
				if (param.get(key) != null) {
					paramMap.put(key, param.get(key));
				}
			}

			for (String str : paramMap.keySet()) {
				sb.append(BOUNDARY);
				sb.append("Content-Disposition:form-data;name=\"");
				sb.append(str);
				sb.append("\"\r\n\r\n");
				sb.append(param.get(str));
				sb.append("\r\n");
			}

			out.write(sb.toString().getBytes());

			File file = new File(path);
			out.write(BOUNDARY.getBytes());
			StringBuilder filenamesb = new StringBuilder();
			filenamesb
					.append("Content-Disposition:form-data;Content-Type:application/octet-stream;name=\"File");
			filenamesb.append("\";filename=\"");
			filenamesb.append(file.getName() + "\"\r\n\r\n");
			out.write(filenamesb.toString().getBytes());

			fis = new FileInputStream(file);

			int bytesRead;
			int bytesAvailable;
			int bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024;

			bytesAvailable = fis.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			bytesRead = fis.read(buffer, 0, bufferSize);
			long transferred = 0;
			final Thread thread = Thread.currentThread();
			while (bytesRead > 0) {

				if (thread.isInterrupted()) {
					file.delete();
					throw new InterruptedIOException();
				}
				out.write(buffer, 0, bufferSize);
				bytesAvailable = fis.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fis.read(buffer, 0, bufferSize);
				transferred += bytesRead;
				if (transferred % 50 == 0)
					out.flush();

			}

			out.write("\r\n\r\n".getBytes());
			fis.close();

			out.write(("--" + BOUNDARYSTR + "--\r\n").getBytes());
			out.flush();
			out.close();
			int status = urlConnection.getResponseCode();
			if (status != HttpURLConnection.HTTP_OK) {
				String error = handleError(urlConnection);
				return false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Utility.closeSilently(fis);
			Utility.closeSilently(out);
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return true;
	}

}
