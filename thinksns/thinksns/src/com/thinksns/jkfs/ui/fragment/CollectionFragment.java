package com.thinksns.jkfs.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thinksns.jkfs.R;

public class CollectionFragment extends Fragment {

    public static final String TAG="CollectionFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.collection_fragment_layout,container,false);
    }
}
