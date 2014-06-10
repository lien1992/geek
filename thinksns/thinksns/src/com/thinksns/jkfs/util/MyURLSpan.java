package com.thinksns.jkfs.util;

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

	public MyURLSpan(String url) {
		super(url);
		mURL = url;
	}

	public MyURLSpan(String url, String uid) {
		super(url);
		mURL = url;
		u_id = uid;
	}

	public String getURL() {
		return mURL;
	}

	@Override
	public void onClick(View widget) {
		Uri uri = Uri.parse(getURL());
		Context context = widget.getContext();
		if (uri.getScheme().startsWith("http")) {

		} else if (uri.getScheme().startsWith("com.thinksns.jkfs.topic")) {

		} else {
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.putExtra("uid", u_id);
			context.startActivity(intent);
		}
	}

	@Override
	public void updateDrawState(TextPaint tp) {
		tp.setColor(Color.parseColor("#0066ff"));
	}

}
