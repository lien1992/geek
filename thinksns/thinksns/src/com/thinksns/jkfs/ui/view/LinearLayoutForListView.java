package com.thinksns.jkfs.ui.view;

import com.thinksns.jkfs.ui.adapter.CommentAdapter;

import android.widget.LinearLayout;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 取代ListView的LinearLayout，使之能够成功嵌套在ScrollView中
 */
public class LinearLayoutForListView extends LinearLayout {
	private CommentAdapter adapter;

	public LinearLayoutForListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public LinearLayoutForListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void bindLinearLayout() {
		int count = adapter.getCount();
		this.removeAllViews();
		for (int i = 0; i < count; i++) {
			View v = adapter.getView(i, null, null);
			addView(v, i);
		}
	}

	public CommentAdapter getAdpater() {
		return adapter;
	}

	public void setAdapter(CommentAdapter adpater) {
		this.adapter = adpater;
		bindLinearLayout();
	}

}
