package com.yang.foodsearch.mvp.precenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.yang.foodsearch.application.FoodSearchApplication;
import com.yang.foodsearch.bean.User;
import com.yang.foodsearch.mvp.contract.HomeContract;
import com.yang.foodsearch.util.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by admin on 17/4/12.
 */

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.MvpView mvpView;
    private static final String TAG = "HomePresenter zsj TAG";

    public HomePresenter(HomeContract.MvpView mvpView) {
        this.mvpView = mvpView;
        this.mvpView.attachPresenter(this);
    }

    @Override
    public void subscribe() {
        Log.d(TAG, "subscribe: ");
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void showName() {
        Log.d(TAG, "showName: ");
    }

    @Override
    public void commitRegistDetail(final Context context, final ProgressDialog progressDialog
            ,final String userName, final String userPassword) {
        if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(userPassword)){
            ToastUtils.showToast("用户名和密码不能为空");
            return;
        }

        BmobQuery<User> query = new BmobQuery<User>();
        //增加一个查询条件
        query.addWhereEqualTo("userNamer", userName);
        //发起查询
        query.findObjects(context, new FindListener<User>() {
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
                        progressDialog.dismiss();

//                        User user=arg0.get(0);
////                        user.setInstallationId(BmobInstallation.getInstallationId(LoginnActivity.this));
//                        user.update(context, new UpdateListener() {
//
//                            @Override
//                            public void onSuccess() {
//                                // TODO Auto-generated method stub
//                                progressDialog.dismiss();
//                            }
//                            @Override
//                            public void onFailure(int arg0, String arg1) {
//                                // TODO Auto-generated method stub
//                                ToastUtils.showToast("更新设备ID失败");
//                            }
//                        });

                    }else{
                        ToastUtils.showToast("用户名或密码错误");
                        progressDialog.dismiss();
                        return;
                    }
                }else{
                    ToastUtils.showToast("用户名或密码不存在");
                    progressDialog.dismiss();
                    return;
                }
            }
            @Override
            public void onError(int arg0, String arg1) {
                ToastUtils.showToast("查询失败，请稍后重试。错误代码"+arg0+arg1);
                progressDialog.dismiss();
                return;
            }
        });


    }


}
