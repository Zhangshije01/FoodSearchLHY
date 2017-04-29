package com.yang.foodsearch.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.yang.foodsearch.R;
import com.yang.foodsearch.adapter.MyFragmentAdapter;
import com.yang.foodsearch.databinding.ActivityMainBinding;
import com.yang.foodsearch.fragment.MapsFragment;
import com.yang.foodsearch.fragment.MyFragment;
import com.yang.foodsearch.fragment.SearchFragment;
import com.yang.foodsearch.mvp.contract.HomeContract;
import com.yang.foodsearch.mvp.precenter.HomePresenter;

public class MainActivity extends AppCompatActivity implements HomeContract.MvpView{

    ActivityMainBinding mBinding;
    private MyFragmentAdapter myFragmentAdapter;
    private MyFragment myFragment;
    private MapsFragment mapFragment;
    private SearchFragment searchFragment;
    private HomeContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        mPresenter = new HomePresenter(this);
        initFragment();
        setBottomRadioButtionClick();
    }
    public void initFragment(){
        myFragment = new MyFragment();
        mapFragment = new MapsFragment();
        searchFragment = new SearchFragment();

        myFragmentAdapter.addFragment(searchFragment);
        myFragmentAdapter.addFragment(mapFragment);
        myFragmentAdapter.addFragment(myFragment);
        mBinding.viewpagerMain.setAdapter(myFragmentAdapter);
    }

    public void setBottomRadioButtionClick(){
        mBinding.radioGroupMainBottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int index = mBinding.radioGroupMainBottom.indexOfChild(group.findViewById(checkedId));
                if(mBinding.viewpagerMain != null){
                    mBinding.viewpagerMain.setCurrentItem(index);
                }
            }
        });
    }

    @Override
    public void attachPresenter(HomeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
