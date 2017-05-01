package com.yang.foodsearch.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.yang.foodsearch.R;
import com.yang.foodsearch.application.FoodSearchApplication;
import com.yang.foodsearch.databinding.FragmentMapsBinding;

import java.util.List;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment {

    FragmentMapsBinding mBinding;
    public LocationClient mLocationClient = null;
    String address="";
    public BDLocationListener myListener = new MyLocationListener();
    private BaiduMap baiduMap;
    private static final String TAG = "myFragment zsj";
    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 101;
    LatLng loc;
    public MapsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater , R.layout.fragment_maps,container,false);
        return mBinding.getRoot();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLocationClient = new LocationClient(getContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );

        initLocation();
        initBaiduMap();


        mBinding.btSearchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSearchFood();

            }
        });
    }


    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=1000;
//        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
        Log.d(TAG, "initLocation: "+mLocationClient.isStarted());
        if(!mLocationClient.isStarted()){
            Log.d(TAG, "initLocation: "+mLocationClient.isStarted());
            mLocationClient.start();
        }
    }

    private void initBaiduMap() {
        baiduMap = mBinding.bmapView.getMap();
        baiduMap.setMaxAndMinZoomLevel(20, 15);

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundle = marker.getExtraInfo();
                if(bundle!=null){
                    String name = bundle.getString("name");
                    String address = bundle.getString("address");
                    String phone = bundle.getString("phone");


                    TextView view = new TextView(getContext());

                    int distance = (int) DistanceUtil.getDistance(loc, marker.getPosition());

                    view.setText(name+"  "+distance+"米");
                    view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                    view.setTextColor(Color.BLUE);
                    int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
                    view.setPadding(padding,padding,padding,padding);
                    view.setBackgroundColor(Color.GRAY);

                    InfoWindow infoWindow = new InfoWindow(view , marker.getPosition(), -60);
                    baiduMap.showInfoWindow(infoWindow );
                }
                return true;
            }
        });

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

//            TextView view = new TextView(getContext());
//            view.setText(address.substring(2,address.length()));
//            FoodSearchApplication.getInstance().setLocation_info(address.substring(2,address.indexOf("区")+1));
//            Log.d(TAG, "onReceiveLocation: "+address.substring(2,address.indexOf("区")+1));
//            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//            view.setTextColor(Color.BLUE);
//            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());
//            view.setPadding(padding,padding,padding,padding);
//            view.setBackgroundColor(Color.GRAY);
//
//            InfoWindow infoWindow = new InfoWindow(view , loc, -120);
//            baiduMap.showInfoWindow(infoWindow );

            Log.d(TAG, "onReceiveLocation: --"+mLocationClient.isStarted());
            if(mLocationClient.isStarted()){
                mLocationClient.unRegisterLocationListener(myListener);
                mLocationClient.stop();
            }
            Log.d(TAG, "onReceiveLocation: =="+mLocationClient.isStarted());

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    public void initSearchFood(){
        Log.d(TAG, "initSearchFood: "+loc);
        PoiSearch poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

            @Override
            public void onGetPoiResult(PoiResult arg0) {
                if(arg0==null||arg0.error!= SearchResult.ERRORNO.NO_ERROR){

                    Toast.makeText(getContext(), "meishi", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<PoiInfo> pois = arg0.getAllPoi();

                baiduMap.clear();

                for(PoiInfo poi:pois){
                    //Log.d("TAG", "poi£∫"+poi.name+" / "+poi.address+"/ "+poi.phoneNum);
                    //Ω´√ø“ª∏ˆpoiµƒŒª÷√∂º◊˜Œ™“ª∏ˆ∏≤∏«ŒÔÃÌº”µΩµÿÕº…œ
                    MarkerOptions option = new MarkerOptions();
                    option.position(poi.location);

                    option.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_locate));
                    Marker marker = (Marker) baiduMap.addOverlay(option);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", poi.name);
                    bundle.putString("address", poi.address);
                    bundle.putString("phone", poi.phoneNum);
                    marker.setExtraInfo(bundle );
                }
                //∞—µ±«∞ π”√’ﬂŒª÷√ÃÌº”µΩµÿÕº…œ
                MarkerOptions option = new MarkerOptions();
                option.position(loc);
                option.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_nav_about));
                baiduMap.addOverlay(option );

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });

        PoiNearbySearchOption options = new PoiNearbySearchOption();
        //À—À˜µƒ÷––ƒµ„
        options.location(loc);
        //À—À˜µƒ∞Îæ∂(µ•Œª£∫√◊)
        options.radius(3000);
//        options.pageCapacity(30);
        //À—À˜µƒƒ⁄»›
        options.keyword("美食");
        poiSearch.searchNearby(options);//∑¢∆–À»§µ„À—À˜
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mBinding.bmapView.onDestroy();
        mLocationClient.stop();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mBinding.bmapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mBinding.bmapView.onPause();
    }

}
