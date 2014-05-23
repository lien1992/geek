package com.thinksns.jkfs.ui.view;

import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class PopupWindowMenu {

	private Context mContext;
	private static PopupWindow popupWindow;
	ArrayList<String> mlist;

	public PopupWindowMenu(Context mContext, View view, Button btn_parent,
			ArrayList<String> list) {
		this.mContext = mContext;
		mlist = list;
		if (popupWindow == null) {
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			popupWindow.setOutsideTouchable(true);
			popupWindow.showAsDropDown(btn_parent, 0, 0);
			popupWindow.update();
			popupWindow.setFocusable(true);
			popupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					popupWindow.dismiss();
				}
			});
		}
	}

}
