package com.thinksns.jkfs.util.common;

import java.util.Calendar;

public class DateUtils {
	private static final int ONEHOUR = 60 * 60 * 1000;
	private static Calendar cal = Calendar.getInstance();

	public static String getCurrentTime(String formats) {
		String date = new java.text.SimpleDateFormat(formats)
				.format(new java.util.Date());
		return date;
	}

	public static String TimeStamp2Date(String timestampString, String formats) {
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = new java.text.SimpleDateFormat(formats)
				.format(new java.util.Date(timestamp));
		return date;
	}

	public static String getTimeInString(String arg) {
		long date = Long.parseLong(arg) * 1000;
		long currentDate = cal.getTimeInMillis();
		long interval = currentDate - date;
		if (interval > 0 && interval < ONEHOUR) {
			return (interval / 60 * 1000) + "分钟前";
		}
		return TimeStamp2Date(arg, "yyyy-MM-dd  HH:mm");
	}
}
