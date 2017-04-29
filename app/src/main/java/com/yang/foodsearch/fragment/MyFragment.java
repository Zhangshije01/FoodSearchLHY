package com.yang.foodsearch.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yang.foodsearch.R;
import com.yang.foodsearch.activity.AboutActivity;
import com.yang.foodsearch.activity.DeatilActivity;
import com.yang.foodsearch.activity.DetailUserActivity;
import com.yang.foodsearch.activity.YiJianActivity;
import com.yang.foodsearch.application.FoodSearchApplication;
import com.yang.foodsearch.databinding.FragmentMyBinding;
import com.yang.foodsearch.util.ToastUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {
    FragmentMyBinding myBinding;
    private boolean isFirstLogin;
    private String TAG = "myfragment zsj";

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
                isFirstLogin = FoodSearchApplication.getInstance().isFirstLogin();
                if(isFirstLogin){
                    DetailUserActivity.start(getContext());
                }else{
                    DeatilActivity.start(getContext());
                }
            }
        });

        myBinding.llMyYijian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), YiJianActivity.class));
            }
        });

        myBinding.llMyAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AboutActivity.class));
            }
        });

        myBinding.llMyFragmentDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast("升级中");
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: "+FoodSearchApplication.getInstance().getUser_name());
        if (FoodSearchApplication.getInstance().getUser_name() != null){
            myBinding.tvMyName.setText(FoodSearchApplication.getInstance().getUser_name());
        }
    }
}
