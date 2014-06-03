package com.thinksns.jkfs.util;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

public class MyURLSpan extends ClickableSpan implements ParcelableSpan {

	private final String mURL;

	public MyURLSpan(String url) {
		mURL = url;
	}

	public MyURLSpan(Parcel src) {
		mURL = src.readString();
	}

	public int getSpanTypeId() {
		return 11;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mURL);
	}

	public String getURL() {
		return mURL;
	}

	public void onClick(View widget) { // 待修复..
//<<<<<<< HEAD
//		Uri uri = Uri.parse(getURL());
//		Context context = widget.getContext();
//		// Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//		// intent.putExtra(Browser.EXTRA_APPLICATION_ID,
//		// context.getPackageName());
//		Intent intent = new Intent(context, UserInfoActivity.class);
//		context.startActivity(intent);
//=======
		Uri uri = Uri.parse(getURL());
		Log.d("wj", "URI:"+uri.toString());
		Context context = widget.getContext();
		// Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		// intent.putExtra(Browser.EXTRA_APPLICATION_ID,
		// context.getPackageName());
		//Intent intent = new Intent(context, UserInfoActivity.class);
		//context.startActivity(intent);
//>>>>>>> 3e0f56cc26d5bb0dbf3e23d39756e7c334e8e74e
	}

	@Override
	public void updateDrawState(TextPaint tp) {

	}
}
