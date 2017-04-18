package com.yang.foodsearch.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.yang.foodsearch.R;
import com.yang.foodsearch.databinding.FragmentSearchBinding;
import com.yang.foodsearch.databinding.ItemSearchHeaderIconBinding;
import com.yang.foodsearch.databinding.ItemSearchOneBinding;
import com.yang.foodsearch.databinding.ItemSearchTabBinding;
import com.yang.foodsearch.databinding.ItemSearchTwoBinding;
import com.yang.foodsearch.view.MeiTuanListView;
import com.zaaach.citypicker.CityPickerActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    FragmentSearchBinding mBinding;
    private static final int REQUEST_CODE_PICK_CITY = 0;

    private MeiTuanListView mListView;
    private List<String> mDatas;
    private ArrayAdapter<String> mAdapter;
    private final static int REFRESH_COMPLETE = 0;
    private static final String TAG = "searchFragment zsj";
    ItemSearchHeaderIconBinding itemHeaderIcon;
    ItemSearchOneBinding itemSearchOne;
    ItemSearchTwoBinding itemSearchTwo;
    ItemSearchTabBinding itemSearchTab;
    /**
     * mInterHandler是一个私有静态内部类继承自Handler，内部持有MainActivity的弱引用，
     * 避免内存泄露
     */
    private InterHandler mInterHandler = new InterHandler(this);


    private static class InterHandler extends Handler {
        private WeakReference<SearchFragment> mActivity;
        public InterHandler(SearchFragment activity){
            mActivity = new WeakReference<SearchFragment>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            SearchFragment activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case REFRESH_COMPLETE:
                        activity.mListView.setOnRefreshComplete();
                        activity.mAdapter.notifyDataSetChanged();
                        activity.mListView.setSelection(0);
                        break;
                }
            }else{
                super.handleMessage(msg);
            }
        }
    }
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_search,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = mBinding.searchList;
        String[] data = new String[]{"hello world","hello world","hello world","hello world",
                "hello world","hello world","hello world","hello world","hello world",
                "hello world","hello world","hello world","hello world","hello world",};
        mDatas = new ArrayList<String>(Arrays.asList(data));
        mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,mDatas);

        itemHeaderIcon = ItemSearchHeaderIconBinding.inflate(LayoutInflater.from(getContext()),mListView,false);
        View itemHeader = itemHeaderIcon.getRoot();
        mListView.addHeaderView(itemHeader);

        itemSearchOne = ItemSearchOneBinding.inflate(LayoutInflater.from(getContext()),mListView,false);
        View header_item_search_one = itemSearchOne.getRoot();
        mListView.addHeaderView(header_item_search_one);

        itemSearchTwo = ItemSearchTwoBinding.inflate(LayoutInflater.from(getContext()),mListView,false);
        View header_item_search_two = itemSearchTwo.getRoot();
        mListView.addHeaderView(header_item_search_two);

        itemSearchTab = ItemSearchTabBinding.inflate(LayoutInflater.from(getContext()),mListView,false);
        View header_item_tab = itemSearchTab.getRoot();
        mListView.addHeaderView(header_item_tab);
        itemHeaderIcon.ivSearchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), CityPickerActivity.class),
                        REQUEST_CODE_PICK_CITY);
            }
        });
        mListView.setAdapter(mAdapter);
        mListView.setOnMeiTuanRefreshListener(new MeiTuanListView.OnMeiTuanRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            mDatas.add(0, "new data");
                            mInterHandler.sendEmptyMessage(REFRESH_COMPLETE);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
            if (data != null){
                String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                itemHeaderIcon.tvSearchCity.setText("" + city);
            }
        }
    }

}
