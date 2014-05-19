package com.thinksns.jkfs.ui.view;

import java.util.List;

import com.tencent.a.b.m;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.ChanelBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 代码生成chanel菜单
 * chanel_menu_item.xml 是子模块
 * 
 * @author zcc
 * @since 14-05-19
 *
 */
public class ChanelMenu extends View {
	List<ChanelBean> mlist;
	Context mcontext;

	public ChanelMenu(Context context, List<ChanelBean> list) {
		super(context);
		mlist = list;
		mcontext = context;
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(mcontext);
		View itemView = inflater.inflate(R.layout.chanel_menu_item, null);
	}
}
