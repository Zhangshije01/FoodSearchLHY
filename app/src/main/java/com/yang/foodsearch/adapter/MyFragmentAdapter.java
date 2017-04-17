package com.yang.foodsearch.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 17/4/7.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter{
    List<Fragment> fragmentLists = new ArrayList<>();

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment){
        if(fragment != null){
            fragmentLists.add(fragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentLists.get(position);
    }

    @Override
    public int getCount() {
        return fragmentLists.size();
    }
}
