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
}
