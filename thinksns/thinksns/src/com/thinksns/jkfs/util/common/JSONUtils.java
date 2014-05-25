package com.thinksns.jkfs.util.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.thinksns.jkfs.bean.WeiboAttachBean;
import com.thinksns.jkfs.bean.WeiboBean;

/**
 * Json Utils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-5-12
 */
public class JSONUtils {

	public static boolean isPrintException = true;

	/**
	 * 根据json转换成weibo数组
	 * 
	 * @param JSONData
	 *            需要转换的json格式的String
	 * @return 数组size为0时，出错了
	 * @throws JSONException 需要解决抛出的异常
	 */
	public static ArrayList<WeiboBean> JSONToWeibos(String jsonData) throws JSONException {
		ArrayList<WeiboBean> weibolList = new ArrayList<WeiboBean>();
			JSONArray weiboArray = new JSONArray(jsonData);
			JSONObject weiboObj;
			Iterator<String> it;
			for (int i = 0; i < weiboArray.length(); i++) {
				weiboObj = weiboArray.getJSONObject(i);

				String feed_id = weiboObj.getString("feed_id");
				String type = weiboObj.getString("type");
				String content = weiboObj.getString("content");
				String ctime = weiboObj.getString("ctime");
				String from = weiboObj.getString("from");
				String uid = weiboObj.getString("uid");
				String uname = weiboObj.getString("uname");
				String avatar_big = weiboObj.getString("avatar_big");
				String avatar_middle = weiboObj.getString("avatar_middle");
				String avatar_small = weiboObj.getString("avatar_small");
				String has_attach = weiboObj.getString("has_attach");
				int comment_count = weiboObj.getInt("comment_count");
				int repost_count = weiboObj.getInt("repost_count");

				ArrayList<WeiboAttachBean> attachList = new ArrayList<WeiboAttachBean>();
				// attach==1时赋值
				if ("1".equals(weiboObj.getString("has_attach"))) {
					JSONArray attachArray = weiboObj.getJSONArray("attach");
					for (int j = 0; j < attachArray.length(); j++) {
						JSONObject attachObj = attachArray.getJSONObject(j);
						WeiboAttachBean attach = new WeiboAttachBean();
						attach.setFile_name(attachObj.getString("attach_id"));
						attach.setId(attachObj.getString("attach_name"));
						attach.setUrl(attachObj.getString("attach_url"));
						attachList.add(attach);
					}
				}
				weibolList.add(new WeiboBean(feed_id, type, content, ctime,
						from, uid, uname, avatar_big, avatar_middle,
						avatar_small, has_attach, comment_count, repost_count,
						attachList));

			}
		// 返回size==0说明错误
		return weibolList;
	}

	/**
	 * get Long from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>if {@link org.json.JSONObject#getLong(String)} exception,
	 *         return defaultValue</li>
	 *         <li>return {@link org.json.JSONObject#getLong(String)}</li>
	 *         </ul>
	 */
	public static Long getLong(JSONObject jsonObject, String key,
			Long defaultValue) {
		if (jsonObject == null || StringUtils.isEmpty(key)) {
			return defaultValue;
		}

		try {
			return jsonObject.getLong(key);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get Long from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link org.json.JSONObject#JSONObject(String)}
	 *         exception, return defaultValue</li>
	 *         <li>return
	 *         {@link com.thinksns.jkfs.util.common.JSONUtils#getLong(org.json.JSONObject, String, org.json.JSONObject)}
	 *         </li>
	 *         </ul>
	 */
	public static Long getLong(String jsonData, String key, Long defaultValue) {
		if (StringUtils.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getLong(jsonObject, key, defaultValue);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see com.thinksns.jkfs.util.common.JSONUtils#getLong(org.json.JSONObject,
	 *      String, Long)
	 */
	public static long getLong(JSONObject jsonObject, String key,
			long defaultValue) {
		return getLong(jsonObject, key, (Long) defaultValue);
	}

	/**
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see com.thinksns.jkfs.util.common.JSONUtils#getLong(String, String,
	 *      Long)
	 */
	public static long getLong(String jsonData, String key, long defaultValue) {
		return getLong(jsonData, key, (Long) defaultValue);
	}

	/**
	 * get Int from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>if {@link org.json.JSONObject#getInt(String)} exception,
	 *         return defaultValue</li>
	 *         <li>return {@link org.json.JSONObject#getInt(String)}</li>
	 *         </ul>
	 */
	public static Integer getInt(JSONObject jsonObject, String key,
			Integer defaultValue) {
		if (jsonObject == null || StringUtils.isEmpty(key)) {
			return defaultValue;
		}

		try {
			return jsonObject.getInt(key);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get Int from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link org.json.JSONObject#JSONObject(String)}
	 *         exception, return defaultValue</li>
	 *         <li>return
	 *         {@link com.thinksns.jkfs.util.common.JSONUtils#getInt(org.json.JSONObject, String, org.json.JSONObject)}
	 *         </li>
	 *         </ul>
	 */
	public static Integer getInt(String jsonData, String key,
			Integer defaultValue) {
		if (StringUtils.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getInt(jsonObject, key, defaultValue);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see com.thinksns.jkfs.util.common.JSONUtils#getInt(org.json.JSONObject,
	 *      String, Integer)
	 */
	public static int getInt(JSONObject jsonObject, String key, int defaultValue) {
		return getInt(jsonObject, key, (Integer) defaultValue);
	}

	/**
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see com.thinksns.jkfs.util.common.JSONUtils#getInt(String, String,
	 *      Integer)
	 */
	public static int getInt(String jsonData, String key, int defaultValue) {
		return getInt(jsonData, key, (Integer) defaultValue);
	}

	/**
	 * get Double from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>if {@link org.json.JSONObject#getDouble(String)} exception,
	 *         return defaultValue</li>
	 *         <li>return {@link org.json.JSONObject#getDouble(String)}</li>
	 *         </ul>
	 */
	public static Double getDouble(JSONObject jsonObject, String key,
			Double defaultValue) {
		if (jsonObject == null || StringUtils.isEmpty(key)) {
			return defaultValue;
		}

		try {
			return jsonObject.getDouble(key);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get Double from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link org.json.JSONObject#JSONObject(String)}
	 *         exception, return defaultValue</li>
	 *         <li>return
	 *         {@link com.thinksns.jkfs.util.common.JSONUtils#getDouble(org.json.JSONObject, String, org.json.JSONObject)}
	 *         </li>
	 *         </ul>
	 */
	public static Double getDouble(String jsonData, String key,
			Double defaultValue) {
		if (StringUtils.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getDouble(jsonObject, key, defaultValue);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see com.thinksns.jkfs.util.common.JSONUtils#getDouble(org.json.JSONObject,
	 *      String, Double)
	 */
	public static double getDouble(JSONObject jsonObject, String key,
			double defaultValue) {
		return getDouble(jsonObject, key, (Double) defaultValue);
	}

	/**
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see com.thinksns.jkfs.util.common.JSONUtils#getDouble(String, String,
	 *      Double)
	 */
	public static double getDouble(String jsonData, String key,
			double defaultValue) {
		return getDouble(jsonData, key, (Double) defaultValue);
	}

	/**
	 * get String from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>if {@link org.json.JSONObject#getString(String)} exception,
	 *         return defaultValue</li>
	 *         <li>return {@link org.json.JSONObject#getString(String)}</li>
	 *         </ul>
	 */
	public static String getString(JSONObject jsonObject, String key,
			String defaultValue) {
		if (jsonObject == null || StringUtils.isEmpty(key)) {
			return defaultValue;
		}

		try {
			return jsonObject.getString(key);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get String from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link org.json.JSONObject#JSONObject(String)}
	 *         exception, return defaultValue</li>
	 *         <li>return
	 *         {@link com.thinksns.jkfs.util.common.JSONUtils#getString(org.json.JSONObject, String, org.json.JSONObject)}
	 *         </li>
	 *         </ul>
	 */
	public static String getString(String jsonData, String key,
			String defaultValue) {
		if (StringUtils.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getString(jsonObject, key, defaultValue);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get String array from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>if {@link org.json.JSONObject#getJSONArray(String)}
	 *         exception, return defaultValue</li>
	 *         <li>if {@link org.json.JSONArray#getString(int)} exception,
	 *         return defaultValue</li>
	 *         <li>return string array</li>
	 *         </ul>
	 */
	public static String[] getStringArray(JSONObject jsonObject, String key,
			String[] defaultValue) {
		if (jsonObject == null || StringUtils.isEmpty(key)) {
			return defaultValue;
		}

		try {
			JSONArray statusArray = jsonObject.getJSONArray(key);
			if (statusArray != null) {
				String[] value = new String[statusArray.length()];
				for (int i = 0; i < statusArray.length(); i++) {
					value[i] = statusArray.getString(i);
				}
				return value;
			}
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
		return defaultValue;
	}

	/**
	 * get String array from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link org.json.JSONObject#JSONObject(String)}
	 *         exception, return defaultValue</li>
	 *         <li>return
	 *         {@link com.thinksns.jkfs.util.common.JSONUtils#getStringArray(org.json.JSONObject, String, org.json.JSONObject)}
	 *         </li>
	 *         </ul>
	 */
	public static String[] getStringArray(String jsonData, String key,
			String[] defaultValue) {
		if (StringUtils.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getStringArray(jsonObject, key, defaultValue);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get JSONObject from jsonObject
	 * 
	 * @param jsonObject
	 *            <em><em></em></em>
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>if {@link org.json.JSONObject#getJSONObject(String)}
	 *         exception, return defaultValue</li>
	 *         <li>return {@link org.json.JSONObject#getJSONObject(String)}</li>
	 *         </ul>
	 */
	public static JSONObject getJSONObject(JSONObject jsonObject, String key,
			JSONObject defaultValue) {
		if (jsonObject == null || StringUtils.isEmpty(key)) {
			return defaultValue;
		}

		try {
			return jsonObject.getJSONObject(key);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get JSONObject from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link org.json.JSONObject#JSONObject(String)}
	 *         exception, return defaultValue</li>
	 *         <li>return
	 *         {@link com.thinksns.jkfs.util.common.JSONUtils#getJSONObject(org.json.JSONObject, String, org.json.JSONObject)}
	 *         </li>
	 *         </ul>
	 */
	public static JSONObject getJSONObject(String jsonData, String key,
			JSONObject defaultValue) {
		if (StringUtils.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getJSONObject(jsonObject, key, defaultValue);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get JSONArray from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>if {@link org.json.JSONObject#getJSONArray(String)}
	 *         exception, return defaultValue</li>
	 *         <li>return {@link org.json.JSONObject#getJSONArray(String)}</li>
	 *         </ul>
	 */
	public static JSONArray getJSONArray(JSONObject jsonObject, String key,
			JSONArray defaultValue) {
		if (jsonObject == null || StringUtils.isEmpty(key)) {
			return defaultValue;
		}

		try {
			return jsonObject.getJSONArray(key);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get JSONArray from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link org.json.JSONObject#JSONObject(String)}
	 *         exception, return defaultValue</li>
	 *         <li>return
	 *         {@link com.thinksns.jkfs.util.common.JSONUtils#getJSONArray(org.json.JSONObject, String, org.json.JSONObject)}
	 *         </li>
	 *         </ul>
	 */
	public static JSONArray getJSONArray(String jsonData, String key,
			JSONArray defaultValue) {
		if (StringUtils.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getJSONArray(jsonObject, key, defaultValue);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get Boolean from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if key is null or empty, return defaultValue</li>
	 *         <li>return {@link org.json.JSONObject#getBoolean(String)}</li>
	 *         </ul>
	 */
	public static boolean getBoolean(JSONObject jsonObject, String key,
			Boolean defaultValue) {
		if (jsonObject == null || StringUtils.isEmpty(key)) {
			return defaultValue;
		}

		try {
			return jsonObject.getBoolean(key);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get Boolean from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return <ul>
	 *         <li>if jsonObject is null, return defaultValue</li>
	 *         <li>if jsonData {@link org.json.JSONObject#JSONObject(String)}
	 *         exception, return defaultValue</li>
	 *         <li>return
	 *         {@link com.thinksns.jkfs.util.common.JSONUtils#getBoolean(org.json.JSONObject, String, Boolean)}
	 *         </li>
	 *         </ul>
	 */
	public static boolean getBoolean(String jsonData, String key,
			Boolean defaultValue) {
		if (StringUtils.isEmpty(jsonData)) {
			return defaultValue;
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getBoolean(jsonObject, key, defaultValue);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return defaultValue;
		}
	}

	/**
	 * get map from jsonObject.
	 * 
	 * @param jsonObject
	 *            key-value pairs json
	 * @param key
	 * @return <ul>
	 *         <li>if jsonObject is null, return null</li>
	 *         <li>return
	 *         {@link com.thinksns.jkfs.util.common.JSONUtils#parseKeyAndValueToMap(String)}
	 *         </li>
	 *         </ul>
	 */
	public static Map<String, String> getMap(JSONObject jsonObject, String key) {
		return JSONUtils.parseKeyAndValueToMap(JSONUtils.getString(jsonObject,
				key, null));
	}

	/**
	 * get map from jsonData.
	 * 
	 * @param jsonData
	 *            key-value pairs string
	 * @param key
	 * @return <ul>
	 *         <li>if jsonData is null, return null</li>
	 *         <li>if jsonData length is 0, return empty map</li>
	 *         <li>if jsonData {@link org.json.JSONObject#JSONObject(String)}
	 *         exception, return null</li>
	 *         <li>return
	 *         {@link com.thinksns.jkfs.util.common.JSONUtils#getMap(org.json.JSONObject, String)}
	 *         </li>
	 *         </ul>
	 */
	public static Map<String, String> getMap(String jsonData, String key) {

		if (jsonData == null) {
			return null;
		}
		if (jsonData.length() == 0) {
			return new HashMap<String, String>();
		}

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getMap(jsonObject, key);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * parse key-value pairs to map. ignore empty key, if getValue exception,
	 * put empty value
	 * 
	 * @param sourceObj
	 *            key-value pairs json
	 * @return <ul>
	 *         <li>if sourceObj is null, return null</li>
	 *         <li>else parse entry by
	 *         {@link MapUtils#putMapNotEmptyKey(java.util.Map, String, String)}
	 *         one by one</li>
	 *         </ul>
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> parseKeyAndValueToMap(JSONObject sourceObj) {
		if (sourceObj == null) {
			return null;
		}

		Map<String, String> keyAndValueMap = new HashMap<String, String>();
		for (Iterator iter = sourceObj.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			MapUtils.putMapNotEmptyKey(keyAndValueMap, key,
					getString(sourceObj, key, ""));

		}
		return keyAndValueMap;
	}

	/**
	 * parse key-value pairs to map. ignore empty key, if getValue exception,
	 * put empty value
	 * 
	 * @param source
	 *            key-value pairs json
	 * @return <ul>
	 *         <li>if source is null or source's length is 0, return empty map</li>
	 *         <li>if source {@link org.json.JSONObject#JSONObject(String)}
	 *         exception, return null</li>
	 *         <li>return
	 *         {@link com.thinksns.jkfs.util.common.JSONUtils#parseKeyAndValueToMap(org.json.JSONObject)}
	 *         </li>
	 *         </ul>
	 */
	public static Map<String, String> parseKeyAndValueToMap(String source) {
		if (StringUtils.isEmpty(source)) {
			return null;
		}

		try {
			JSONObject jsonObject = new JSONObject(source);
			return parseKeyAndValueToMap(jsonObject);
		} catch (JSONException e) {
			if (isPrintException) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
