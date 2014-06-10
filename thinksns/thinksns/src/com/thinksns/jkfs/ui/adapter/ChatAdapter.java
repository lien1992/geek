package com.thinksns.jkfs.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.thinksns.jkfs.R;

import java.util.List;

/**
 * 比原来的多了getItemViewType和getViewTypeCount这两个方法，
 * 
 * */
public class ChatAdapter extends BaseAdapter {

	public static final String KEY = "key";
	public static final String VALUE = "value";

	public static final int VALUE_TIME_TIP = 0;// 7种不同的布局
	public static final int VALUE_LEFT_TEXT = 1;
	public static final int VALUE_LEFT_IMAGE = 2;
	public static final int VALUE_LEFT_AUDIO = 3;
	public static final int VALUE_RIGHT_TEXT = 4;
	public static final int VALUE_RIGHT_IMAGE = 5;
	public static final int VALUE_RIGHT_AUDIO = 6;
	private LayoutInflater mInflater;

	private List<Message> myList;

	public ChatAdapter(Context context, List<Message> myList) {
		this.myList = myList;

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addMessage(Message msg) {
		myList.add(msg);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return myList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return myList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {

		Message msg = myList.get(position);
		int type = getItemViewType(position);
		ViewHolderTime holderTime = null;
		ViewHolderRightText holderRightText = null;
		ViewHolderRightImg holderRightImg = null;
		ViewHolderRightAudio holderRightAudio = null;
		ViewHolderLeftText holderLeftText = null;
		ViewHolderLeftImg holderLeftImg = null;
		ViewHolderLeftAudio holderLeftAudio = null;

		if (convertView == null) {
			switch (type) {


			// 左边
			case VALUE_LEFT_TEXT:
				holderLeftText = new ViewHolderLeftText();
				convertView = mInflater.inflate(R.layout.list_item_left_text,
						null);
				// holderLeftText.ivLeftIcon = (ImageView) convertView
				// .findViewById(R.id.iv_icon);
				ImageLoader.getInstance().displayImage(msg.getFace(),
						holderLeftText.ivLeftIcon);
				holderLeftText.btnLeftText = (Button) convertView
						.findViewById(R.id.btn_left_text);
				holderLeftText.btnLeftText.setText(msg.getValue());
				convertView.setTag(holderLeftText);
				break;

			// case VALUE_LEFT_IMAGE:
			// holderLeftImg = new ViewHolderLeftImg();
			// convertView = mInflater.inflate(R.layout.list_item_left_iamge,
			// null);
			// holderLeftImg.ivLeftIcon = (ImageView) convertView
			// .findViewById(R.id.iv_icon);
			// holderLeftImg.ivLeftImage = (ImageView) convertView
			// .findViewById(R.id.iv_left_image);
			// //holderLeftImg.ivLeftImage.setImageResource(R.drawable.test);
			// convertView.setTag(holderLeftImg);
			// break;

			// 右边
			case VALUE_RIGHT_TEXT:
				holderRightText = new ViewHolderRightText();
				convertView = mInflater.inflate(R.layout.list_item_right_text,
						null);
				
				// holderRightText.ivRightIcon = (ImageView) convertView
				// .findViewById(R.id.iv_icon);
				ImageLoader.getInstance().displayImage(msg.getFace(),
						holderRightText.ivRightIcon);
				holderRightText.btnRightText = (Button) convertView
						.findViewById(R.id.btn_right_text);
				holderRightText.btnRightText.setText(msg.getValue());

				convertView.setTag(holderRightText);
				break;

			// case VALUE_RIGHT_IMAGE:
			// holderRightImg= new ViewHolderRightImg();
			// convertView = mInflater.inflate(R.layout.list_item_right_iamge,
			// null);
			// holderRightImg.ivRightIcon = (ImageView) convertView
			// .findViewById(R.id.iv_icon);
			// holderRightImg.ivRightImage = (ImageView) convertView
			// .findViewById(R.id.iv_right_image);
			// //holderRightImg.ivRightImage.setImageResource(R.drawable.test);
			// convertView.setTag(holderRightImg);
			// break;

			default:
				break;
			}

		} else {
			Log.d("baseAdapter", "Adapter_:" + (convertView == null));
			switch (type) {
			case VALUE_TIME_TIP:
				holderTime = (ViewHolderTime) convertView.getTag();
				holderTime.tvTimeTip.setText(msg.getValue());
				break;
			case VALUE_LEFT_TEXT:
				holderLeftText = (ViewHolderLeftText) convertView.getTag();
				holderLeftText.btnLeftText.setText(msg.getValue());
				break;
			case VALUE_LEFT_IMAGE:
				holderLeftImg = (ViewHolderLeftImg) convertView.getTag();
				// holderLeftImg.ivLeftImage.setImageResource(R.drawable.test);
				break;
			case VALUE_LEFT_AUDIO:
				holderLeftAudio = (ViewHolderLeftAudio) convertView.getTag();
				holderLeftAudio.tvLeftAudioTime.setText(msg.getValue());
				break;
			case VALUE_RIGHT_TEXT:
				holderRightText = (ViewHolderRightText) convertView.getTag();
				holderRightText.btnRightText.setText(msg.getValue());
				break;
			case VALUE_RIGHT_IMAGE:
				holderRightImg = (ViewHolderRightImg) convertView.getTag();
				break;
			case VALUE_RIGHT_AUDIO:
				holderRightAudio = (ViewHolderRightAudio) convertView.getTag();
				holderRightAudio.tvRightAudioTime.setText(msg.getValue());
				break;

			default:
				break;
			}
		}
		return convertView;
	}

	/**
	 * 根据数据源的position返回需要显示的的layout的type
	 * 
	 * type的值必须从0开始
	 * 
	 * */
	@Override
	public int getItemViewType(int position) {

		Message msg = myList.get(position);
		int type = msg.getType();
		Log.e("TYPE:", "" + type);
		return type;
	}

	/**
	 * 返回所有的layout的数量
	 * 
	 * */
	@Override
	public int getViewTypeCount() {
		return 7;
	}

	class ViewHolderTime {
		private TextView tvTimeTip;// 时间
	}

	class ViewHolderRightText {
		private ImageView ivRightIcon;// 右边的头像
		private Button btnRightText;// 右边的文本
	}

	class ViewHolderRightImg {
		private ImageView ivRightIcon;// 右边的头像
		private ImageView ivRightImage;// 右边的图像
	}

	class ViewHolderRightAudio {
		private ImageView ivRightIcon;// 右边的头像
		private Button btnRightAudio;// 右边的声音
		private TextView tvRightAudioTime;// 右边的声音时间
	}

	class ViewHolderLeftText {
		private ImageView ivLeftIcon;// 左边的头像
		private Button btnLeftText;// 左边的文本
	}

	class ViewHolderLeftImg {
		private ImageView ivLeftIcon;// 左边的头像
		private ImageView ivLeftImage;// 左边的图像
	}

	class ViewHolderLeftAudio {
		private ImageView ivLeftIcon;// 左边的头像
		private Button btnLeftAudio;// 左边的声音
		private TextView tvLeftAudioTime;// 左边的声音时间
	}

}
