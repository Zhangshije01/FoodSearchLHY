package com.yang.foodsearch.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.yang.foodsearch.R;
import com.yang.foodsearch.application.FoodSearchApplication;
import com.yang.foodsearch.bean.BusinessBean;
import com.yang.foodsearch.databinding.ActivityDetailMapBinding;

import java.util.List;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class DetailMapActivity extends AppCompatActivity {

    ActivityDetailMapBinding mBinding;
    public LocationClient mLocationClient = null;
    String address="";
    public BDLocationListener myListener = new MyLocationListener();
    private BaiduMap baiduMap;
    private LatLng loc;
    private LatLng arriveLoc;
    private static final String TAG = "myFragment zsj";
    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 101;
    private BusinessBean.Business business;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_detail_map);

        business = (BusinessBean.Business) getIntent().getSerializableExtra("business");
        Log.d(TAG, "onCreate: "+business.toString());
        arriveLoc = new LatLng(business.getLatitude(),business.getLongitude());
        Log.d(TAG, "onCreate: "+arriveLoc);
        mLocationClient = new LocationClient(this);
        //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );
        initLocation();
        initBaiduMap();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//ø…—°£¨ƒ¨»œ∏ﬂæ´∂»£¨…Ë÷√∂®Œªƒ£ Ω£¨∏ﬂæ´∂»£¨µÕπ¶∫ƒ£¨Ωˆ…Ë±∏
        option.setCoorType("bd09ll");//ø…—°£¨ƒ¨»œgcj02£¨…Ë÷√∑µªÿµƒ∂®ŒªΩ·π˚◊¯±Íœµ
        int span=1000;
        option.setScanSpan(span);//ø…—°£¨ƒ¨»œ0£¨º¥Ωˆ∂®Œª“ª¥Œ£¨…Ë÷√∑¢∆∂®Œª«Î«Ûµƒº‰∏Ù–Ë“™¥Û”⁄µ»”⁄1000ms≤≈ «”––ßµƒ
        option.setIsNeedAddress(true);//ø…—°£¨…Ë÷√ «∑Ò–Ë“™µÿ÷∑–≈œ¢£¨ƒ¨»œ≤ª–Ë“™
        option.setOpenGps(true);//ø…—°£¨ƒ¨»œfalse,…Ë÷√ «∑Ò π”√gps
        option.setLocationNotify(true);//ø…—°£¨ƒ¨»œfalse£¨…Ë÷√ «∑Òµ±GPS”––ß ±∞¥’’1S/1¥Œ∆µ¬  ‰≥ˆGPSΩ·π˚
        option.setIsNeedLocationDescribe(true);//ø…—°£¨ƒ¨»œfalse£¨…Ë÷√ «∑Ò–Ë“™Œª÷√”Ô“ÂªØΩ·π˚£¨ø…“‘‘⁄BDLocation.getLocationDescribe¿Ôµ√µΩ£¨Ω·π˚¿‡À∆”⁄°∞‘⁄±±æ©ÃÏ∞≤√≈∏ΩΩ¸°±
        option.setIsNeedLocationPoiList(true);//ø…—°£¨ƒ¨»œfalse£¨…Ë÷√ «∑Ò–Ë“™POIΩ·π˚£¨ø…“‘‘⁄BDLocation.getPoiList¿Ôµ√µΩ
        option.setIgnoreKillProcess(false);//ø…—°£¨ƒ¨»œtrue£¨∂®ŒªSDKƒ⁄≤ø «“ª∏ˆSERVICE£¨≤¢∑≈µΩ¡À∂¿¡¢Ω¯≥Ã£¨…Ë÷√ «∑Ò‘⁄stopµƒ ±∫Ú…±À¿’‚∏ˆΩ¯≥Ã£¨ƒ¨»œ≤ª…±À¿
        option.SetIgnoreCacheException(false);//ø…—°£¨ƒ¨»œfalse£¨…Ë÷√ «∑Ò ’ºØCRASH–≈œ¢£¨ƒ¨»œ ’ºØ
        option.setEnableSimulateGps(false);//ø…—°£¨ƒ¨»œfalse£¨…Ë÷√ «∑Ò–Ë“™π˝¬ÀGPS∑¬’ÊΩ·π˚£¨ƒ¨»œ–Ë“™
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private void initBaiduMap() {
        baiduMap = mBinding.bmapViewDetail.getMap();
        baiduMap.setMaxAndMinZoomLevel(20, 5);


        MarkerOptions opt = new MarkerOptions();
        opt.position(arriveLoc);
        opt.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_locate));
        baiduMap.addOverlay(opt );

        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(arriveLoc);
        baiduMap.animateMapStatus(msu);

        TextView view = new TextView(DetailMapActivity.this);
        view.setText(business.getName().substring(0,business.getName().indexOf("(")));
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        view.setTextColor(Color.BLUE);
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
        view.setPadding(padding,padding,padding,padding);
        view.setBackgroundColor(Color.GRAY);

        InfoWindow infoWindow = new InfoWindow(view , arriveLoc, -120);
        baiduMap.showInfoWindow(infoWindow );

    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            int code = location.getLocType();
            double lat = -1;
            double lng = -1;
            Log.d(TAG, "onReceiveLocation: "+code);

            String cityAddress;
            if(code==61||code==66||code==161){
                lat = location.getLatitude();
                lng = location.getLongitude();
                address = location.getAddrStr();
                Log.d(TAG, "onReceiveLocation: "+location.getCity());
                cityAddress = location.getCity();
            }else{
                lat = 39.876402;
                lng = 116.465049;
                address = "河北大学工商学院";
                cityAddress = "保定";
            }

            FoodSearchApplication.getInstance().setCityAddress(cityAddress);

            Log.d(TAG, "onReceiveLocation: 123"+lat+"---"+lng);
            loc = new LatLng(lat, lng);
            Log.d(TAG, "onReceiveLocation: loc"+loc);
            FoodSearchApplication.getInstance().setLastpoint(loc);

            MarkerOptions opt = new MarkerOptions();
            opt.position(loc);
            opt.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_nav_about));
            baiduMap.addOverlay(opt );

            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(loc);
            baiduMap.animateMapStatus(msu);


            int distance = (int) DistanceUtil.getDistance(loc, arriveLoc);

            Log.d(TAG, "initBaiduMap: "+distance);

            TextView view = new TextView(DetailMapActivity.this);
            view.setText(address.substring(2,address.length())+" "+distance+"米");
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            view.setTextColor(Color.BLUE);
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
            view.setPadding(padding,padding,padding,padding);
            view.setBackgroundColor(Color.GRAY);

            InfoWindow infoWindow = new InfoWindow(view , loc, -120);
            baiduMap.showInfoWindow(infoWindow );

            //Õ£÷πºÃ–¯∑¢∆∂®Œª«Î«Û
            if(mLocationClient.isStarted()){
                mLocationClient.unRegisterLocationListener(myListener);
                mLocationClient.stop();
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mBinding.bmapViewDetail.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mBinding.bmapViewDetail.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mBinding.bmapViewDetail.onPause();
    }

}
