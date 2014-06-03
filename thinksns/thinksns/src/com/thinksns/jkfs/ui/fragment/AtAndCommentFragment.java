package com.thinksns.jkfs.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.NotificationBean;
import com.thinksns.jkfs.ui.MainFragmentActivity;

/**
 * 与我有关：评论（评论我的、我评论的) + at我的
 * 
 * @author wangjia
 * 
 */
public class AtAndCommentFragment extends Fragment {

	public static final String TAG = "AtAndCommentFragment";
	private PopupWindow popupWindow;
	private ListView lv_group;
	private List<String> groups;
	private ImageView back;
	private TextView comment;
	private TextView at;
	private TextView comment_unread;
	private TextView at_unread;
	private LayoutInflater inflater;
	private NotificationBean comment_unread_bean, at_unread_bean;
	int comment_count, at_count;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle unread = this.getArguments();
		if (unread != null) {
			comment_unread_bean = unread.getParcelable("comment_unread");
			at_unread_bean = unread.getParcelable("at_unread");
			if (comment_unread_bean != null) {
				comment_count = comment_unread_bean.getCount();
			}
			if (at_unread_bean != null) {
				at_count = at_unread_bean.getCount();
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.inflater = inflater;
		View view = inflater.inflate(R.layout.at_comment_fragment_layout, null);
		back = (ImageView) view.findViewById(R.id.wb_at_back);
		comment = (TextView) view.findViewById(R.id.wb_comment_tab);
		at = (TextView) view.findViewById(R.id.wb_at_tab);
		comment_unread = (TextView) view
				.findViewById(R.id.wb_comment_tab_unread);
		at_unread = (TextView) view.findViewById(R.id.wb_at_tab_unread);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (comment_count != 0) {
			comment_unread.setText(comment_count + "");
			comment_unread.setVisibility(View.VISIBLE);
		}
		if (at_count != 0) {
			at_unread.setText(at_count + "");
			at_unread.setVisibility(View.VISIBLE);
		}
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				((MainFragmentActivity) getActivity()).getSlidingMenu()
						.toggle();
			}
		});
		at.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				at.setTextColor(getResources().getColor(R.color.green));
				comment.setTextColor(getResources().getColor(R.color.grey));
				getChildFragmentManager().beginTransaction().replace(
						R.id.at_comment_frame, new AtMeFragment()).commit();
				at_unread.setVisibility(View.GONE);

			}
		});
		comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				comment.setTextColor(getResources().getColor(R.color.green));
				at.setTextColor(getResources().getColor(R.color.grey));
				showWindow(v);
			}
		});
		getChildFragmentManager().beginTransaction().replace(
				R.id.at_comment_frame, new CommentToMeFragment()).commit();

	}

	private void showWindow(View parent) {
		if (popupWindow == null) {
			View view = inflater.inflate(R.layout.at_comment_popup_listview,
					null);
			lv_group = (ListView) view
					.findViewById(R.id.comment_popup_listview);
			groups = new ArrayList<String>();
			groups.add("评论我的");
			groups.add("我评论的");

			GroupAdapter groupAdapter = new GroupAdapter(groups);
			lv_group.setAdapter(groupAdapter);
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}

		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);

		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 显示位置
		popupWindow.showAsDropDown(parent, 10, 0);
		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				comment.setText(groups.get(position));
				if (position == 0) {
					getChildFragmentManager().beginTransaction().replace(
							R.id.at_comment_frame, new CommentToMeFragment())
							.commit();
					comment_unread.setVisibility(View.GONE);
				} else {
					getChildFragmentManager().beginTransaction().replace(
							R.id.at_comment_frame, new CommentByMeFragment())
							.commit();
				}

				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
	}

	class GroupAdapter extends BaseAdapter {

		private List<String> list;

		public GroupAdapter(List<String> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup viewGroup) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.at_comment_popup_listview_item, null);
				holder = new ViewHolder();
				holder.groupItem = (TextView) convertView
						.findViewById(R.id.comment_popup_listview_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.groupItem.setText(list.get(position));
			return convertView;
		}

		class ViewHolder {
			TextView groupItem;
		}

	}
}