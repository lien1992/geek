package com.thinksns.jkfs.ui.view;

import java.util.Date;

import com.thinksns.jkfs.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

/**
 * 下拉刷新ListView
 * 
 */
public class PullToRefreshListView extends ListView implements OnScrollListener {

	/** 松开刷新 */
	private final static int RELEASE_TO_REFRESH = 0;
	/** 下拉刷新 */
	private final static int PULL_TO_REFRESH = 1;
	/** 正在刷新中 */
	private final static int REFRESHING = 2;
	/** 完成 */
	private final static int DONE = 3;

	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 3;

	private LayoutInflater inflater;

	private LinearLayout headView;

	private TextView tipsTextview;
	private TextView lastUpdatedTextView;

	private ImageView arrowImageView;
	private ProgressBar progressBar;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	private int headContentHeight;

	/** 手势按下的起点位置 */
	private int startY;
	private int firstItemIndex;

	private int state;

	private boolean isBack;

	private OnRefreshListener refreshListener;

	private boolean isRefreshable;

	/**
	 * 下拉刷新监听器接口
	 * 
	 */
	public interface OnRefreshListener {
		public void onRefresh();
	}

	public PullToRefreshListView(Context context) {
		super(context);
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		inflater = LayoutInflater.from(context);

		headView = (LinearLayout) inflater.inflate(R.layout.refresh_head, null);// 加载头布局

		arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);// 箭头
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);

		headView.measure(0, 0);
		headContentHeight = headView.getMeasuredHeight();// 头部高度
		headView.getMeasuredWidth();
		/*
		 * 设置padding -1 * headContentHeight就可以把该headview隐藏在屏幕顶部，
		 * 前提是要得到headview的确切高度
		 */
		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		/*
		 * Invalidate the whole view. If the view is visible,
		 * onDraw(android.graphics.Canvas) will be called at some point in the
		 * future. This must be called from a UI thread. To call from a non-UI
		 * thread, call postInvalidate().
		 */
		headView.invalidate();

		addHeaderView(headView, null, false);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
	}

	/**
	 * 实现OnScrollListener接口的两个方法
	 */
	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
			int arg3) {
		firstItemIndex = firstVisiableItem;
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}

	/**
	 * 设置触摸事件
	 * 
	 * 1 ACTION_DOWN：记录起始位置
	 * 
	 * 2 ACTION_MOVE：计算当前位置与起始位置的距离，来设置state的状态
	 * 
	 * 3 ACTION_UP：根据state的状态来判断是否下载
	 */
	public boolean onTouchEvent(MotionEvent event) {

		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0) {
					startY = (int) event.getY();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				if (state == PULL_TO_REFRESH) {
					setSelection(0);// 这句代码很重要
					// 下拉到可以进入RELEASE_TO_REFRESH的状态
					if ((tempY - startY) / RATIO >= headContentHeight) {
						state = RELEASE_TO_REFRESH;
						isBack = true;
						changeHeaderViewByState();
					}
					// 上推到顶了
					else if (tempY - startY <= 0) {
						state = DONE;
						changeHeaderViewByState();
					}
					headView.setPadding(0, -headContentHeight
							+ (tempY - startY) / RATIO, 0, 0);
				}
				if (state == RELEASE_TO_REFRESH) {
					setSelection(0);
					// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
					if (((tempY - startY) / RATIO < headContentHeight)
							&& (tempY - startY) > 0) {
						state = PULL_TO_REFRESH;
						changeHeaderViewByState();
					}
					headView.setPadding(0, -headContentHeight
							+ (tempY - startY) / RATIO, 0, 0);
				}
				// done状态下
				if (state == DONE) {
					if (tempY - startY > 0) {
						state = PULL_TO_REFRESH;
						changeHeaderViewByState();
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if (state != REFRESHING) {
					// 不在刷新状态
					if (state == PULL_TO_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
					}
					if (state == RELEASE_TO_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}
				isBack = false;
				break;

			}
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 当状态改变时候，调用该方法，以更新界面
	 */
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_TO_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText("松开刷新");

			break;
		case PULL_TO_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			tipsTextview.setText("下拉刷新");
			// 是RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				arrowImageView.startAnimation(reverseAnimation);
			}

			break;

		case REFRESHING:
			headView.setPadding(0, 0, 0, 0);
			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("正在刷新...");
			break;
		case DONE:
			headView.setPadding(0, -headContentHeight, 0, 0);
			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.arrow);
			tipsTextview.setText("下拉刷新");
			break;
		}
	}

	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public void onRefreshComplete() {
		state = DONE;
		lastUpdatedTextView.setText("最近更新:" + new Date().toLocaleString());
		changeHeaderViewByState();
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		lastUpdatedTextView.setText("最近更新:" + new Date().toLocaleString());
		super.setAdapter(adapter);
	}

}