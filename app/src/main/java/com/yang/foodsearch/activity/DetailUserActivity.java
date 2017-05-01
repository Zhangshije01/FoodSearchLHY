package com.yang.foodsearch.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.yang.foodsearch.R;
import com.yang.foodsearch.application.FoodSearchApplication;
import com.yang.foodsearch.bean.User;
import com.yang.foodsearch.databinding.ActivityDetailBinding;
import com.yang.foodsearch.mvp.contract.HomeContract;
import com.yang.foodsearch.mvp.precenter.HomePresenter;
import com.yang.foodsearch.util.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class DetailUserActivity extends AppCompatActivity implements HomeContract.MvpView{

    ActivityDetailBinding mBinding;
    String myDetailName;
    String myDetailPassword;
    private HomeContract.Presenter mPresenter;
    private ProgressDialog progressDialog_login;
    private String TAG = "detailuserActivity zsj";

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
                commitRegistDetail(myDetailName,myDetailPassword);
            }
        });
    }

    public void commitRegistDetail(final String userName, final String userPassword) {
        if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(userPassword)){
            ToastUtils.showToast("用户名和密码不能为空");
            return;
        }

        BmobQuery<User> query = new BmobQuery<User>();
        //增加一个查询条件
        query.addWhereEqualTo("userName", userName);
        //发起查询
        query.findObjects(DetailUserActivity.this, new FindListener<User>() {
            @Override
            public void onSuccess(final List<User> arg0) {
                // TODO Auto-generated method stub
                String pwd=arg0.get(0).getUserPassword();
                Log.d(TAG, "onSuccess: "+pwd);
                if(arg0!=null&&arg0.size()>0){
                    if(pwd.equals(userPassword)){
                        //密码一致，登录成功
                        //跳转界面
                        FoodSearchApplication.getInstance().setFirstLogin(false);
                        progressDialog_login.dismiss();
                        FoodSearchApplication.getInstance().setUser_name(userName);
                        finish();

                    }else{
                        ToastUtils.showToast("用户名或密码错误");
                        progressDialog_login.dismiss();
                        return;
                    }
                }else{
                    ToastUtils.showToast("用户名或密码不存在");
                    progressDialog_login.dismiss();
                    return;
                }
            }
            @Override
            public void onError(int arg0, String arg1) {
                ToastUtils.showToast("查询失败，请稍后重试。错误代码"+arg0+arg1);
                progressDialog_login.dismiss();
                return;
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
