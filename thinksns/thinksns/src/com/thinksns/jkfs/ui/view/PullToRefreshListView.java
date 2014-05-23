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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

/**
 * ListView, 顶部:下拉刷新 + 底部:加载更多
 * 使用时实现RefreshAndLoadMoreListener接口，并调用setListener()进行绑定;
 * 要添加底部加载更多，调用setLoadMoreEnable(true)。
 * 
 */
public class PullToRefreshListView extends ListView implements OnScrollListener {

	private LayoutInflater inflater;

	/** 顶部下拉刷新 **/
	private final static int RELEASE_TO_REFRESH = 0;// 松开刷新
	private final static int PULL_TO_REFRESH = 1;// 下拉刷新
	private final static int REFRESHING = 2;// 正在刷新中
	private final static int DONE = 3;// 刷新完成
	private LinearLayout headView;
	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;
	private boolean isRefreshable;
	private int headContentHeight;
	private int state;
	private boolean isBack;// 是RELEASE_TO_REFRESH状态转变来的

	/** 底部加载更多 **/
	public final static int STATE_NORMAL = 4; // 加载更多
	public final static int STATE_READY = 5; // 松开加载更多
	public final static int STATE_LOADING = 6; // 正在加载
	private LinearLayout footView;
	private RelativeLayout footContent;
	private LinearLayout footProgressBar;
	private TextView footHintView;
	private boolean mIsFooterReady;
	private boolean mEnablePullLoad;// 默认是false
	private boolean mPullLoading;
	private int mTotalItemCount;
	private final static int PULL_LOAD_MORE_DELTA = 50; // 上拉大于50px触发加载更多

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	// 手势按下的起点位置
	private int startY;
	private int firstItemIndex;

	private RefreshAndLoadMoreListener listener;

	private final static int RATIO = 3;// 实际的padding的距离与界面上偏移距离的比例

	/**
	 * 监听器接口
	 * 
	 */
	public interface RefreshAndLoadMoreListener {
		public void onRefresh();

		public void onLoadMore();
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

		headView = (LinearLayout) inflater.inflate(R.layout.refresh_head, null);// 顶部下拉刷新
		arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);

		headView.measure(0, 0);
		headContentHeight = headView.getMeasuredHeight();
		headView.getMeasuredWidth();

		// 设置padding -1 * headContentHeight就可以把该headview隐藏在屏幕顶部
		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		/*
		 * Invalidate the whole view. If the view is visible,
		 * onDraw(android.graphics.Canvas) will be called at some point in the
		 * future. This must be called from a UI thread. To call from a non-UI
		 * thread, call postInvalidate().
		 */
		headView.invalidate();

		addHeaderView(headView, null, false);

		footView = (LinearLayout) inflater.inflate(R.layout.refresh_foot, null);// 底部加载更多
		footContent = (RelativeLayout) footView
				.findViewById(R.id.footer_content);
		footProgressBar = (LinearLayout) footView
				.findViewById(R.id.footer_progressbar);
		footHintView = (TextView) footView
				.findViewById(R.id.footer_hint_textview);
		footView.setVisibility(View.GONE);

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
	}

	/**
	 * 实现OnScrollListener接口的两个方法
	 */
	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
			int totalItemCount) {
		firstItemIndex = firstVisiableItem;
		mTotalItemCount = totalItemCount;
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
	 * 3 ACTION_UP：根据state的状态来判断
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
				if (getFirstVisiblePosition() == 0 && state == PULL_TO_REFRESH) {
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
				if (getFirstVisiblePosition() == 0
						&& state == RELEASE_TO_REFRESH) {
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
				if (getFirstVisiblePosition() == 0 && state == DONE) {
					if (tempY - startY > 0) {
						state = PULL_TO_REFRESH;
						changeHeaderViewByState();
					}
				}
				if (getLastVisiblePosition() == mTotalItemCount - 1
						&& (getBottomMargin() > 0 || tempY - startY < 0)) {
					// last item, already pulled up or want to pull up.
					updateFooterHeight(-(tempY - startY) / RATIO);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (state != REFRESHING) {
					// 不在刷新状态
					if (state == PULL_TO_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
					} else if (state == RELEASE_TO_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
						if (mEnablePullLoad
								&& getBottomMargin() > PULL_LOAD_MORE_DELTA) {
							onLoadMore();
						}
						footView.invalidate(); // 待定...
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

	public void setListener(RefreshAndLoadMoreListener listener) {
		this.listener = listener;
		isRefreshable = true;
	}

	public void onRefreshComplete() {
		state = DONE;
		lastUpdatedTextView.setText("最近更新:" + new Date().toLocaleString());
		changeHeaderViewByState();
	}

	private void onRefresh() {
		if (listener != null) {
			listener.onRefresh();
		}
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		lastUpdatedTextView.setText("最近更新:" + new Date().toLocaleString());
		if (mIsFooterReady == false) { // 确保footView为最后面的View，只添加一次
			mIsFooterReady = true;
			addFooterView(footView);
		}
		super.setAdapter(adapter);
	}

	private void updateFooterHeight(float delta) {
		int height = getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) {
				setState(STATE_READY);
			} else {
				setState(STATE_NORMAL);
			}
		}
		setBottomMargin(height);
	}

	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) footContent
				.getLayoutParams();
		return lp.bottomMargin;
	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) footContent
				.getLayoutParams();
		lp.bottomMargin = height;
		footContent.setLayoutParams(lp);
	}

	/**
	 * 使上拉或单击加载更多生效，需要单独调用
	 * 
	 * @param enable
	 */
	public void setLoadMoreEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			hide();
			footView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			show();
			setState(STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			footView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onLoadMore();
				}
			});
		}
	}

	private void onLoadMore() {
		mPullLoading = true;
		setState(STATE_LOADING);
		if (listener != null) {
			listener.onLoadMore();
		}
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) footContent
				.getLayoutParams();
		lp.height = 0;
		footContent.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) footContent
				.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		footContent.setLayoutParams(lp);
	}

	public void setState(int state) {
		footContent.setVisibility(View.INVISIBLE);
		footProgressBar.setVisibility(View.INVISIBLE);
		footHintView.setVisibility(View.INVISIBLE);
		if (state == STATE_READY) {
			footHintView.setVisibility(View.VISIBLE);
			footHintView.setText("松开载入更多");
		} else if (state == STATE_LOADING) {
			footProgressBar.setVisibility(View.VISIBLE);
		} else {
			footHintView.setVisibility(View.VISIBLE);
			footHintView.setText("加载更多");
		}
	}

}
