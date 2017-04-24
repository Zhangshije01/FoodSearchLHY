package com.yang.foodsearch.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yang.foodsearch.R;
import com.yang.foodsearch.databinding.ActivityDetailBinding;
import com.yang.foodsearch.mvp.contract.HomeContract;
import com.yang.foodsearch.mvp.precenter.HomePresenter;

public class DetailUserActivity extends AppCompatActivity implements HomeContract.MvpView{

    ActivityDetailBinding mBinding;
    String myDetailName;
    String myDetailPassword;
    private HomeContract.Presenter mPresenter;
    private ProgressDialog progressDialog_login;
    public static void start(Context context){
        Intent intent = new Intent(context,DetailUserActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail);
        mPresenter = new HomePresenter(this);

        commitRegistDetail();
        mBinding.tvDetailZhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistActivity.start(DetailUserActivity.this);
            }
        });
    }
    public void commitRegistDetail(){


        mBinding.btMyDetailCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDetailName = mBinding.etMyDetailName.getText().toString();
                myDetailPassword = mBinding.etMyDetailPassword.getText().toString();
                progressDialog_login = ProgressDialog.show(DetailUserActivity.this,"","正在登录");
                mPresenter.commitRegistDetail(DetailUserActivity.this,progressDialog_login,myDetailName,myDetailPassword);
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
