package com.yang.foodsearch.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.jakewharton.rxbinding.support.design.widget.RxNavigationView;
import com.yang.foodsearch.R;
import com.yang.foodsearch.adapter.MyFragmentAdapter;
import com.yang.foodsearch.databinding.ActivityMainBinding;
import com.yang.foodsearch.fragment.MapsFragment;
import com.yang.foodsearch.fragment.MyFragment;
import com.yang.foodsearch.fragment.SearchFragment;
import com.yang.foodsearch.mvp.contract.HomeContract;
import com.yang.foodsearch.mvp.precenter.HomePresenter;
import com.yang.foodsearch.rx.RxActionBarDrawerToggle;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity implements HomeContract.MvpView{

    ActivityMainBinding mBinding;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private MyFragmentAdapter myFragmentAdapter;
    private MyFragment myFragment;
    private MapsFragment mapFragment;
    private SearchFragment searchFragment;
    private HomeContract.Presenter mPresenter;
    private CompositeSubscription mSubscription = new CompositeSubscription();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mNavigationView = mBinding.navigationView;
        mToolbar = mBinding.toolbar;
        mDrawerLayout = mBinding.drawerLayout;
        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());

        mPresenter = new HomePresenter(this);

        setUpNavigationView();
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //TODO inflate can't find
//        NavDrawerHeaderBinding navHeaderBinding = NavDrawerHeaderBinding.inflate(LayoutInflater
//                .from(this), mNavigationView, false);
//        View headerView = navHeaderBinding.getRoot();
//        mNavigationView.addHeaderView(headerView);

//        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, HomeFragment.newInstance()).commit();
        initFragment();
        setBottomRadioButtionClick();


        Observable<Boolean> observable = RxActionBarDrawerToggle.drawerOpen(this, mDrawerLayout, mToolbar);
        Subscription subscription = observable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                notifyDrawerStatus(aBoolean);
            }
        });
        mSubscription.add(subscription);
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
    public void onBackPressed() {
        super.onBackPressed();
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void notifyDrawerStatus(boolean isOpen) {
        if (isOpen) {
        }
    }

    private void setUpNavigationView() {
        mNavigationView.setItemIconTintList(null);

        Observable<MenuItem> observable = RxNavigationView.itemSelections(mNavigationView);
        Subscription subscription = observable.subscribe(new Action1<MenuItem>() {
            @Override
            public void call(MenuItem menuItem) {
                onNavigationMenuItemSelected(menuItem);
            }
        });
        mSubscription.add(subscription);
    }

    private void onNavigationMenuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_rate_us:
                Log.d("TAG","nav_rate_usasda");
                mPresenter.showName();
                break;
            case R.id.nav_share_weather:
                Log.d("TAG","nav_rate_us");

                break;
            case R.id.nav_share_lifely:
                Log.d("TAG","nav_rate_us");

                break;
            case R.id.nav_about:
                Log.d("TAG","nav_rate_us");

                break;
            case R.id.nav_feedback:
                Log.d("TAG","nav_rate_us");

                break;
        }
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
