package com.thinksns.jkfs.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.thinksns.jkfs.R;

//从git上pull下来以后报错，我先注释掉了
@SuppressLint("ValidFragment")
public class MyListFragment extends ListFragment implements
		LoaderCallbacks<Void> {

	private static final String TAG = "FragmentTabs";

	private String mTag;
	private MyAdapter mAdapter;
	private ArrayList<String> mItems;
	private LayoutInflater mInflater;
	private int mTotal;
	private int mPosition;

	private static final String[] WORDS = { "Lorem", "ipsum", "dolor", "sit",
			"amet", "consectetur", "adipiscing", "elit", "Fusce", "pharetra",
			"luctus", "sodales" };
	private static final String[] NUMBERS = { "I", "II", "III", "IV", "V",
			"VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV" };

	private static final int SLEEP = 1000;

	private final int wordBarColor = R.color.grey;
	private final int numberBarColor = R.color.black;

	public MyListFragment() {
	}


/*	public MyListFragment(String tag) {
		mTag = tag;
		mTotal = AboutMeFragment.TAB_WEIBO.equals(mTag) ? WORDS.length
				: NUMBERS.length;

		Log.d(TAG, "Constructor: tag=" + tag);
=======
	public MyListFragment(String tag) {
		/*
		 * mTag = tag; mTotal = AboutMeFragment.TAB_WEIBO.equals(mTag) ?
		 * WORDS.length : NUMBERS.length;
		 * 
		 * Log.d(TAG, "Constructor: tag=" + tag);
		 */

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// this is really important in order to save the state across screen
		// configuration changes for example
		setRetainInstance(true);

		mInflater = LayoutInflater.from(getActivity());

		// you only need to instantiate these the first time your fragment is
		// created; then, the method above will do the rest
		if (mAdapter == null) {
			mItems = new ArrayList<String>();
			mAdapter = new MyAdapter(getActivity(), mItems);
		}
		getListView().setAdapter(mAdapter);

		// initiate the loader to do the background work
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Void> onCreateLoader(int id, Bundle args) {
		AsyncTaskLoader<Void> loader = new AsyncTaskLoader<Void>(getActivity()) {

			@Override
			public Void loadInBackground() {
				try {
					// simulate some time consuming operation going on in the
					// background
					Thread.sleep(SLEEP);
				} catch (InterruptedException e) {
				}
				return null;
			}
		};
		// somehow the AsyncTaskLoader doesn't want to start its job without
		// calling this method
		loader.forceLoad();
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Void> loader, Void result) {

		// add the new item and let the adapter know in order to refresh the
		// views

		/*
		 * mItems.add(AboutMeFragment.TAB_WEIBO.equals(mTag) ? WORDS[mPosition]
		 * : NUMBERS[mPosition]); mAdapter.notifyDataSetChanged();
		 * 
		 * // advance in your list with one step mPosition++; if (mPosition <
		 * mTotal - 1) { getLoaderManager().restartLoader(0, null, this);
		 * Log.d(TAG, "onLoadFinished(): loading next..."); } else { Log.d(TAG,
		 * "onLoadFinished(): done loading!"); }
		 */

	}

	@Override
	public void onLoaderReset(Loader<Void> loader) {
	}

	private class MyAdapter extends ArrayAdapter<String> {

		public MyAdapter(Context context, List<String> objects) {
			super(context, R.layout.people_item, R.id.item_name, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;

			/*
			 * Wrapper wrapper;
			 * 
			 * if (view == null) { view =
			 * mInflater.inflate(R.layout.people_item, null); wrapper = new
			 * Wrapper(view); view.setTag(wrapper); } else { wrapper = (Wrapper)
			 * view.getTag(); }
			 * 
			 * wrapper.getTextView().setText(getItem(position));
			 * wrapper.getBar().setBackgroundColor( mTag ==
			 * AboutMeFragment.TAB_WEIBO ? getResources()
			 * .getColor(wordBarColor) : getResources().getColor(
			 * numberBarColor));
			 */
			return view;
		}

	}

	// use an wrapper (or view holder) object to limit calling the
	// findViewById() method, which parses the entire structure of your
	// XML in search for the ID of your view
	private class Wrapper {
		private final View mRoot;
		private TextView mText;
		private View mBar;

		public Wrapper(View root) {
			mRoot = root;
		}

		public TextView getTextView() {
			if (mText == null) {
				mText = (TextView) mRoot.findViewById(R.id.item_name);
			}
			return mText;
		}

		public View getBar() {
			if (mBar == null) {
				mBar = mRoot.findViewById(R.id.item_weibo);
			}
			return mBar;
		}
	}
}