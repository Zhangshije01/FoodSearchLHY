package com.yang.foodsearch.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.yang.foodsearch.R;
import com.yang.foodsearch.application.FoodSearchApplication;
import com.yang.foodsearch.bean.User;
import com.yang.foodsearch.databinding.ActivityRegistBinding;
import com.yang.foodsearch.util.ToastUtils;

import java.util.concurrent.TimeUnit;

import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import rx.Observer;
import rx.functions.Action1;

public class RegistActivity extends AppCompatActivity {

    ActivityRegistBinding mBinding;
    private String regist_phone;
    private String regist_yanzhengma;
    private String regist_password;
    private int num = 1;
    private EventHandler eh;
    private static final String TAG = "regist zsj";
    public static void start(Context context){
        Intent intent = new Intent(context , RegistActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_regist);

        initRegistOnclick();
        initYanzhengmaOnclick();

    }
    public void initRegistOnclick(){
        Log.d(TAG, "onClick: "+num);

        RxView.clicks(mBinding.btRegsist)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        switch (num){
                            case 1:
                                regist_phone = mBinding.etRegist.getText().toString();
                                if(regist_phone.length() != 11){
                                    ToastUtils.showToast("请输入正确的11位验证码");
                                    return;
                                }
                                mBinding.tvRegistPhone.setTextColor(Color.BLACK);
                                mBinding.tvRegistYanzhengma.setTextColor(RegistActivity.this.getResources().getColor(R.color.color_orange_end));
                                mBinding.buRegistYanzhengma.setVisibility(View.VISIBLE);
                                mBinding.btRegsist.setText("提交验证码");

                                num++;
                                break;
                            case 2:
                                if(TextUtils.isEmpty(regist_phone)){
                                    ToastUtils.showToast("验证码不能为空");
                                    return;
                                }
                                regist_yanzhengma = mBinding.etRegist.getText().toString();
                                SMSSDK.submitVerificationCode("86", regist_phone , regist_yanzhengma);
                                Log.d(TAG, "call: "+regist_phone+regist_yanzhengma);
                                eh=new EventHandler(){
                                    @Override
                                    public void afterEvent(int event, int result, Object data) {

                                        if (result == SMSSDK.RESULT_COMPLETE) {
                                            //回调完成
                                            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                                //提交验证码成功
                                                ToastUtils.showToast("短信验证成功");
                                                Log.d(TAG, "afterEvent: "+regist_phone+regist_yanzhengma);

                                            }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                                                //获取验证码成功
                                                Log.d(TAG, "afterEvent: 2"+regist_phone+regist_yanzhengma);
                                            }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                                                //返回支持发送验证码的国家列表
                                            }
                                        }else{
                                            ((Throwable)data).printStackTrace();
                                        }
                                    }
                                };
                                SMSSDK.registerEventHandler(eh);
                                mBinding.tvRegistYanzhengma.setTextColor(Color.BLACK);
                                mBinding.tvRegistPassword.setTextColor(RegistActivity.this.getResources().getColor(R.color.color_orange_end));
                                mBinding.buRegistYanzhengma.setVisibility(View.GONE);
                                mBinding.btRegsist.setText("提交密码");
                                mBinding.etRegist.setText("");
                                mBinding.etRegist.setHint("请输入密码");
                                num++;
                                break;
                            case 3:
                                regist_password = mBinding.etRegist.getText().toString();
                                User user = new User();
                                user.setUserName(regist_phone);
                                user.setUserPassword(regist_password);
                                user.save(RegistActivity.this, new SaveListener() {
                                    @Override
                                    public void onSuccess() {
                                        ToastUtils.showToast("保存成功");

                                        finish();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Log.d(TAG, "onFailure: "+i+s.toString());
                                        ToastUtils.showToast("保存失败"+i+s.toString());

                                    }
                                });
                                break;
                        }
                    }
                });
    }
    public void initYanzhengmaOnclick(){
        mBinding.buRegistYanzhengma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getYanzhengma();
                mBinding.etRegist.setText("");
                mBinding.etRegist.setHint("请输入验证码");
            }
        });
    }
    public void getYanzhengma(){
        SMSSDK.getVerificationCode("86", regist_phone, new OnSendMessageHandler() {
            @Override
            public boolean onSendMessage(String s, String s1) {
                Log.d("sendRAG",s+s1);
                ToastUtils.showToast("ok");
                return false;
            }
        });
    }
}
