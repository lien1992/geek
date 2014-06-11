package com.thinksns.jkfs.util;

import com.thinksns.jkfs.ui.OtherInfoActivity;
import com.thinksns.jkfs.ui.WeiboTopicActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

public class MyURLSpan extends URLSpan {

	private final String mURL;

	private String u_id;

	private String topic;

	public MyURLSpan(String url) {
		super(url);
		mURL = url;
	}

	public MyURLSpan(String url, String uid) {
		super(url);
		mURL = url;
		u_id = uid;
	}

	public MyURLSpan(String url, String uid, String t) {
		super(url);
		mURL = url;
		u_id = uid;
		topic = t;
	}

	public String getURL() {
		return mURL;
	}

	@Override
	public void onClick(View widget) {
		Uri uri = Uri.parse(getURL());
		Context context = widget.getContext();
		if (uri.getScheme().startsWith("http")) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(uri);
			context.startActivity(i);
		} else if (uri.getScheme().startsWith("com.thinksns.jkfs.topic")) {
			Intent i = new Intent(context, WeiboTopicActivity.class);
			i.putExtra("topic", topic);
			context.startActivity(i);
		} else {
			/*
			 * Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			 * intent.putExtra("uid", u_id); context.startActivity(intent);
			 */
			Intent intent = new Intent(context, OtherInfoActivity.class);
			intent.putExtra("uid", u_id);
			context.startActivity(intent);
		}
	}

	@Override
	public void updateDrawState(TextPaint tp) {
		tp.setColor(Color.parseColor("#0066ff"));
	}

}
