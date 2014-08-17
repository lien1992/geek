package com.thinksns.jkfs.ui;

import java.util.LinkedList;
import java.util.List;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.thinksns.jkfs.R;
import com.thinksns.jkfs.bean.DraftBean;
import com.thinksns.jkfs.constant.BaseConstant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class DraftActivity extends Activity {
	private ImageView back;
	private ListView listView;
	private DraftAdapter adapter;
	private List<DraftBean> drafts = new LinkedList<DraftBean>();

	private DbUtils db;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				adapter.notifyDataSetChanged();
				Intent in = new Intent();
				in.setAction(BaseConstant.DRAFT_BROADCAST);
				DraftActivity.this.sendBroadcast(in);
				if (drafts.size() == 0)
					DraftActivity.this.finish();
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draft);

		db = DbUtils.create(this, "thinksns2.db");
		db.configDebug(true);
		try {
			drafts = db.findAll(Selector.from(DraftBean.class).orderBy("id",
					true));
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		back = (ImageView) findViewById(R.id.draft_weibo_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}

		});
		listView = (ListView) findViewById(R.id.draft_lv);
		adapter = new DraftAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int draft_id = drafts.get(arg2).getId();
				String draft_content = drafts.get(arg2).getContent();
				Intent intent = new Intent(DraftActivity.this,
						WriteWeiboActivity.class);
				intent.putExtra("id", draft_id);
				intent.putExtra("draft", draft_content);
				DraftActivity.this.startActivity(intent);
				finish();
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(DraftActivity.this);
				builder.setTitle("删除");
				builder.setMessage("确定要删除这条草稿吗？");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								try {
									db.delete(drafts.get(arg2));
								} catch (DbException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								drafts.remove(arg2);
								mHandler.sendEmptyMessage(0);
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				builder.create().show();

				return true;
			}

		});

	}

	class DraftAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return drafts.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return drafts.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			DraftBean draft = drafts.get(position);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(
						R.layout.draft_listview_item, null);
				holder.draft_desc = (TextView) convertView
						.findViewById(R.id.draft_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.draft_desc.setText(draft.getContent());

			return convertView;
		}

	}

	class ViewHolder {
		TextView draft_desc;
	}

}
