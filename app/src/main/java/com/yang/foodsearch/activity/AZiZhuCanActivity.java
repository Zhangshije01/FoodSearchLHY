package com.yang.foodsearch.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.yang.foodsearch.R;
import com.yang.foodsearch.adapter.SearchFoodAdapter;
import com.yang.foodsearch.bean.BusinessBean;
import com.yang.foodsearch.databinding.ActivityAziZhuCanBinding;
import com.yang.foodsearch.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

public class AZiZhuCanActivity extends AppCompatActivity {

    ActivityAziZhuCanBinding mBinding;
    private List<BusinessBean.Business> businesses_List = new ArrayList<>();
    private SearchFoodAdapter searchFoodAdapter;
    private int countNum;
    private String city_name;
    private String category;
    private static final String TAG = "zizhucan zsj";
    private ImageView iv_search_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_azi_zhu_can);
        city_name = getIntent().getStringExtra("city_name");
        category = getIntent().getStringExtra("category");
        getZiZhuCan();

        searchFoodAdapter = new SearchFoodAdapter(this,businesses_List);
        mBinding.listviewAZizhucan.setAdapter(searchFoodAdapter);

        mBinding.listviewAZizhucan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BusinessBean.Business business = null;
                business = searchFoodAdapter.getItem(position);
                Log.d(TAG, "onItemClick: "+business.toString());
                Intent intent = new Intent(AZiZhuCanActivity.this,DetailBussinessActivity.class);
                intent.putExtra("business", business);
                startActivity(intent);
            }
        });

        iv_search_back = (ImageView) mBinding.includeAZizhu.findViewById(R.id.iv_search_header_back);
        iv_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void getZiZhuCan(){
        countNum = 1;

        HttpUtil.getMeishi(city_name, null,category, null,null, 40, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                BusinessBean businessBean = new Gson().fromJson(s,BusinessBean.class);
                businesses_List = businessBean.getBusinesses();
                Log.d(TAG, "onResponse: "+businesses_List.get(0).toString());
                Log.d(TAG, "onResponse: "+businesses_List.size());
                searchFoodAdapter.addAll(businesses_List,true);
            }
        });
    }
}
