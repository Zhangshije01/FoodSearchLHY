package com.yang.foodsearch.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yang.foodsearch.R;
import com.yang.foodsearch.activity.DetailActivity;
import com.yang.foodsearch.databinding.FragmentMyBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {
    FragmentMyBinding myBinding;
    public MyFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my,container,false);
        return myBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myBinding.llMyDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.start(getContext());
            }
        });
    }
}
