package com.thinksns.jkfs.ui.fragment;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.thinksns.jkfs.R;
import com.thinksns.jkfs.ui.MainFragmentActivity;

/**
 * Created by mosl on 14-5-20.
 */
public class MenuFragmentList extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] birds = getResources().getStringArray(R.array.menu_list_array);
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, birds);
        setListAdapter(colorAdapter);
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        Fragment newContent = new HomeFragment();
        if (newContent != null)
            switchFragment(position);
    }

    // the meat of switching the above fragment
    private void switchFragment(int fragment_id) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof MainFragmentActivity) {
            MainFragmentActivity ra = (MainFragmentActivity) getActivity();
            ra.switchContent(fragment_id);
        }
    }
}
