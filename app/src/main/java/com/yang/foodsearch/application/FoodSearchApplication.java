package com.yang.foodsearch.application;

import android.app.Application;
import android.os.Handler;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;

import cn.bmob.v3.Bmob;
import cn.smssdk.SMSSDK;

/**
 * Created by admin on 17/4/14.
 */

public class FoodSearchApplication extends Application{
    private static FoodSearchApplication mInstance;
    private Handler mHandler;
    public LatLng lastpoint;
    public String cityAddress;
    private boolean isFirstLogin = true;
    private boolean isFujin;
    private String location_info;
    private String user_name;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mHandler = new Handler();
        SDKInitializer.initialize(getApplicationContext());
        Bmob.initialize(this,"261a403af0f502f2b699f874b118b11c");
        SMSSDK.initSDK(this,"1d0aecbc06980","8dd7830109badd549bd66e2f8a9ac1a1");

    }

    public LatLng getLastpoint() {
        return lastpoint;
    }

    public void setLastpoint(LatLng lastpoint) {
        this.lastpoint = lastpoint;
    }

    public static FoodSearchApplication getInstance(){
        return mInstance;
    }
    public Handler getUIHandler(){
        return mHandler;
    }

    public String getCityAddress() {
        return cityAddress;
    }

    public void setCityAddress(String cityAddress) {
        this.cityAddress = cityAddress;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFujin(boolean fujin) {
        isFujin = fujin;
    }

    public boolean isFujin() {
        return isFujin;
    }

    public String getLocation_info() {
        return location_info;
    }

    public void setLocation_info(String location_info) {
        this.location_info = location_info;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
