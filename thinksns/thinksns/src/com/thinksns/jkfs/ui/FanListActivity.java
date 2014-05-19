package com.thinksns.jkfs.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.thinksns.jkfs.R;

/**
 * @author 邓思宇
 * 关注或者粉丝列表布局代码
 *
 */
public class FanListActivity extends Activity {

	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_list);

		// 测试布局例子
		listView = (ListView) this.findViewById(R.id.listAllPeople);

		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 10; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("Image", R.drawable.mushishi);
			map.put("Name", "Username");
			map.put("LastWeibo", "LastWeibo");
			listItem.add(map);
		}

		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,
				R.layout.people_item, new String[] { "Image", "Name",
						"LastWeibo" }, new int[] { R.id.item_head,
						R.id.item_name, R.id.item_weibo });

		listView.setAdapter(listItemAdapter);

		// 最后布局 获取数据获得
		// listView = (ListView) this.findViewById(R.id.listAllPeople);
		// UserInfoListBean userList = new UserInfoListBean();
		// List<UserInfoBean> listusers = userList.getUsers();
		// PeopleAdapter adapter = new PeopleAdapter(this,listusers); // a
		// listactivity as a listview, need a adapter to full up the content
		// listView.setAdapter(adapter);

		// listView.setOnItemClickListener(new OnItemClickListener() {
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3){
		// Intent intent = new Intent(ListActivity.this,SubActivity.class);
		// intent.putExtra("id", arg2);
		// startActivity(intent);
		// }
		// });
	}
}