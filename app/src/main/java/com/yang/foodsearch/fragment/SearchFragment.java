package com.yang.foodsearch.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.google.gson.Gson;
import com.yang.foodsearch.R;
import com.yang.foodsearch.activity.AZiZhuCanActivity;
import com.yang.foodsearch.activity.DetailBussinessActivity;
import com.yang.foodsearch.adapter.SearchFoodAdapter;
import com.yang.foodsearch.application.FoodSearchApplication;
import com.yang.foodsearch.bean.BusinessBean;
import com.yang.foodsearch.bean.DistrictBean;
import com.yang.foodsearch.bean.FoodBean;
import com.yang.foodsearch.databinding.FragmentSearchBinding;
import com.yang.foodsearch.databinding.ItemSearchHeaderIconBinding;
import com.yang.foodsearch.databinding.ItemSearchOneBinding;
import com.yang.foodsearch.databinding.ItemSearchTabBinding;
import com.yang.foodsearch.databinding.ItemSearchTwoBinding;
import com.yang.foodsearch.util.HttpUtil;
import com.yang.foodsearch.view.MeiTuanListView;
import com.zaaach.citypicker.CityPickerActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment{


    FragmentSearchBinding mBinding;
    private static final int REQUEST_CODE_PICK_CITY = 0;

    private MeiTuanListView mListView;
    private final static int REFRESH_COMPLETE = 0;
    private static final String TAG = "searchFragment zsj";
    ItemSearchHeaderIconBinding itemHeaderIcon;
    ItemSearchOneBinding itemSearchOne;
    ItemSearchTwoBinding itemSearchTwo;
    ItemSearchTabBinding itemSearchTab;
    private ImageView iv_location_city;
    private TextView tv_location_city;

    private LinearLayout ll_search_food;
    private LinearLayout ll_search_huoguo;
    private LinearLayout ll_search_xiaochikuaican;
    private LinearLayout ll_search_shaokao;
    private LinearLayout ll_search_mianbao;
    private LinearLayout ll_search_cafeiting;
    private LinearLayout ll_search_xican;
    private LinearLayout ll_search_hanguoliaoli;


    private ImageView iv_search_city;
    private ImageView iv_search_header;
    private EditText edit_search_header;

    private String city_name;
    List<BusinessBean.Business> businessList = new ArrayList<>();
    SearchFoodAdapter searchFoodAdapter;
    private int countNum;

    private TextView tv_search_bar_fujin;
    private TextView tv_search_bar_meishi;
    private TextView tv_search_bar_paixu;
    private TextView tv_search_bar_shaixuan;
    private View viewFujin;
    private View viewmeishi;
    private View viewpaixu;
    private View viewshaixuan;


    private ListView listViewLeft;
    private ListView listViewRight;
    List<String> left;
    List<String> right;
    ArrayAdapter<String> leftAdapter;
    ArrayAdapter<String> rightAdapter;
    List<DistrictBean.City.District> districts;

    ListView listview_meishi;
    final List<String> list_meishi = new ArrayList<>();
    ArrayAdapter<String> adapter;
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
                        activity.searchFoodAdapter.notifyDataSetChanged();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        View view_header_icon = getActivity().getLayoutInflater().inflate(R.layout.item_search_header_icon,mListView,false);
        mListView.addHeaderView(view_header_icon);

        View view_item_search_one = getActivity().getLayoutInflater().inflate(R.layout.item_search_one,mListView,false);
        mListView.addHeaderView(view_item_search_one);


        View view_item_search_two = getActivity().getLayoutInflater().inflate(R.layout.item_search_two,mListView,false);
        mListView.addHeaderView(view_item_search_two);

        View view_item_search_tab = getActivity().getLayoutInflater().inflate(R.layout.item_search_tab,mListView,false);
        mListView.addHeaderView(view_item_search_tab);

        ll_search_food = (LinearLayout) view_item_search_one.findViewById(R.id.ll_icons_list_food);
        ll_search_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: llsearchfood");
                Intent intent = new Intent(getContext(), AZiZhuCanActivity.class);
                intent.putExtra("city_name",city_name);
                intent.putExtra("category","自助餐");

                startActivity(intent);
            }
        });

        ll_search_huoguo = (LinearLayout) view_item_search_one.findViewById(R.id.ll_icons_list_huoguo);
        ll_search_xiaochikuaican = (LinearLayout) view_item_search_one.findViewById(R.id.ll_icons_list_xiaochikuaican);
        ll_search_shaokao = (LinearLayout) view_item_search_one.findViewById(R.id.ll_icons_list_shaokao);
        ll_search_mianbao = (LinearLayout) view_item_search_one.findViewById(R.id.ll_icons_list_mianbao);
        ll_search_cafeiting = (LinearLayout) view_item_search_one.findViewById(R.id.ll_icons_list_kafeiting);
        ll_search_xican = (LinearLayout) view_item_search_one.findViewById(R.id.ll_icons_list_xican);
        ll_search_hanguoliaoli = (LinearLayout) view_item_search_one.findViewById(R.id.ll_icons_list_hanguoliaoli);

        ll_search_huoguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: llsearchfood");
                Intent intent = new Intent(getContext(), AZiZhuCanActivity.class);
                intent.putExtra("city_name",city_name);
                intent.putExtra("category","火锅");
                startActivity(intent);
            }
        });

        ll_search_xiaochikuaican.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: llsearchfood");
                Intent intent = new Intent(getContext(), AZiZhuCanActivity.class);
                intent.putExtra("city_name",city_name);
                intent.putExtra("category","小吃快餐");

                startActivity(intent);
            }
        });

        ll_search_shaokao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: llsearchfood");
                Intent intent = new Intent(getContext(), AZiZhuCanActivity.class);
                intent.putExtra("city_name",city_name);
                intent.putExtra("category","烧烤");

                startActivity(intent);
            }
        });

        ll_search_mianbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: llsearchfood");
                Intent intent = new Intent(getContext(), AZiZhuCanActivity.class);
                intent.putExtra("city_name",city_name);
                intent.putExtra("category","面包甜点");

                startActivity(intent);
            }
        });

        ll_search_cafeiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: llsearchfood");
                Intent intent = new Intent(getContext(), AZiZhuCanActivity.class);
                intent.putExtra("city_name",city_name);
                intent.putExtra("category","咖啡厅");

                startActivity(intent);
            }
        });

        ll_search_xican.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: llsearchfood");
                Intent intent = new Intent(getContext(), AZiZhuCanActivity.class);
                intent.putExtra("city_name",city_name);
                intent.putExtra("category","西餐");

                startActivity(intent);
            }
        });

        ll_search_hanguoliaoli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: llsearchfood");
                Intent intent = new Intent(getContext(), AZiZhuCanActivity.class);
                intent.putExtra("city_name",city_name);
                intent.putExtra("category","韩国料理");

                startActivity(intent);
            }
        });




        iv_search_city = (ImageView) view_header_icon.findViewById(R.id.iv_header_main_location);
        iv_search_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                startActivityForResult(new Intent(getContext(), CityPickerActivity.class),
                        REQUEST_CODE_PICK_CITY);
            }
        });

        tv_location_city  = (TextView) view_header_icon.findViewById(R.id.tv_header_main_city);
//        city_name = tv_location_city.getText().toString();
        Log.d(TAG, "onViewCreated: "+FoodSearchApplication.getInstance().getCityAddress());
        if(FoodSearchApplication.getInstance().getCityAddress() != null){
            city_name = FoodSearchApplication.getInstance().getCityAddress();
            city_name = city_name.substring(0,city_name.length()-1);
        }else{
            city_name = "北京";
        }
        tv_location_city.setText(city_name);



        searchFoodAdapter = new SearchFoodAdapter(getContext(),businessList);
        mListView.setAdapter(searchFoodAdapter);

        loadData();
        iv_search_header = (ImageView) view_header_icon.findViewById(R.id.iv_search_header_main);
        edit_search_header = (EditText) view_header_icon.findViewById(R.id.edit_text_search_header);
        iv_search_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit_search = edit_search_header.getText().toString();

//                edit_search_header.setOnTouchListener(new View.OnTouchListener() {
//                    public boolean onTouch(View v, MotionEvent event) {
//                        edit_search_header.setInputType(InputType.TYPE_NULL); // 关闭软键盘
//                        return false;
//                    }
//                });

                HttpUtil.getMeishi(city_name, edit_search, null, null, 20, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        BusinessBean businessBean = new Gson().fromJson(s,BusinessBean.class);
                        businessList = businessBean.getBusinesses();
                        Log.d(TAG, "onResponse: "+businessList.get(0).toString());
                        Log.d(TAG, "onResponse: "+businessList.size());
                        searchFoodAdapter.addAll(businessList,true);
                    }
                });
            }
        });
        mListView.setOnMeiTuanRefreshListener(new MeiTuanListView.OnMeiTuanRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            countNum += 1;
                            Log.d(TAG, "run: "+countNum);
                            loadMoreData(countNum);
                            mInterHandler.sendEmptyMessage(REFRESH_COMPLETE);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.d(TAG, "onScroll: --------------------");
//                Log.d(TAG, "onScroll: "+firstVisibleItem);
//                Log.d(TAG, "onScroll: "+visibleItemCount);
//                Log.d(TAG, "onScroll: "+totalItemCount);
//                Log.d(TAG, "onScroll: --------------------");
//            }
//        });

        searchCityDistinct();

        tv_search_bar_fujin = (TextView) view_item_search_tab.findViewById(R.id.tv_tab_business_bar_fujin);
        tv_search_bar_meishi = (TextView) view_item_search_tab.findViewById(R.id.tv_tab_business_bar_meishi);
        tv_search_bar_paixu = (TextView) view_item_search_tab.findViewById(R.id.tv_tab_business_bar_paixu);
        tv_search_bar_shaixuan = (TextView) view_item_search_tab.findViewById(R.id.tv_tab_business_bar_shaixuan);

        searchBarFujin();

        initmeishi();
        searchBarMeishi();
        searchBarPaixu();
        searchBarShaixuan();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: "+position);
                if(position > 4){
                    BusinessBean.Business business = null;
                    business = searchFoodAdapter.getItem(position-5);
                    Log.d(TAG, "onItemClick: "+business.toString());
                    Intent intent = new Intent(getContext(),DetailBussinessActivity.class);
                    intent.putExtra("business", business);
                    startActivity(intent);
                }
            }
        });
    }

    public void searchCityDistinct(){
        HttpUtil.getDistricts(city_name, new Response.Listener<String>() {

            @Override
            public void onResponse(String arg0) {
                DistrictBean distictBean = new Gson().fromJson(arg0, DistrictBean.class);
                DistrictBean.City cityName = distictBean.getCities().get(0);
                districts = cityName.getDistricts();

                List<String> districtNames = new ArrayList<String>();
                for(DistrictBean.City.District district:districts){
                    districtNames.add(district.getDistrict_name());
                }
                left.clear();
                left.add("附近");
                left.addAll(districtNames);
                leftAdapter.notifyDataSetChanged();

                List<String> neighborhoodNames = new ArrayList<String>(districts.get(0).getNeighborhoods());
                neighborhoodNames.add(0, "全部"+districts.get(0).getDistrict_name());

                right.clear();
                right.addAll(neighborhoodNames);
                rightAdapter.notifyDataSetChanged();

            }
        });
    }

    public void searchBarFujin(){
        tv_search_bar_fujin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+"fujin");
                if (viewFujin.getVisibility() == View.INVISIBLE){
                    viewFujin.setVisibility(View.VISIBLE);
                }else{
                    viewFujin.setVisibility(View.INVISIBLE);
                }
            }
        });

        viewFujin = mBinding.getRoot().findViewById(R.id.item_search_tab_near);
        listViewLeft = (ListView) viewFujin.findViewById(R.id.lv_business_select_left);
        listViewRight = (ListView) viewFujin.findViewById(R.id.lv_business_select_right);

        listViewLeft.setBackgroundColor(Color.parseColor("#FFD1A4"));
        listViewRight.setBackgroundColor(Color.parseColor("#FFCC66"));

        left = new ArrayList<String>();
        leftAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,left);
        listViewLeft.setAdapter(leftAdapter);

        right = new ArrayList<String>();
        rightAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,right);
        listViewRight.setAdapter(rightAdapter);

        listViewLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                right.clear();
                Log.d(TAG, "onItemClick: "+position);
                if(position == 0){
                    List<String> listnear = new ArrayList<String>();
                    listnear.add("附近（智能距离）");
                    listnear.add("500米");
                    listnear.add("1千米");
                    listnear.add("2千米");
                    listnear.add("3千米");
                    listnear.add("全城");
                    //TODO  ??？
                    listnear.add(" ");
                    listnear.add(" ");
                    listnear.add(" ");
                    listnear.add(" ");
                    listnear.add(" ");
                    right.addAll(listnear);
                }else{
                    List<String> neighborhoodNames = new ArrayList<String>(districts.get(position).getNeighborhoods());
                    neighborhoodNames.add(0, "全部"+districts.get(position).getDistrict_name());
                    right.addAll(neighborhoodNames);
                }
                rightAdapter.notifyDataSetChanged();
            }
        });

        listViewRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //1)districtContainer消失
                viewFujin.setVisibility(View.INVISIBLE);
                //2)tvBtn1的内容变为用户点击的街道名称
                String neighborhoodName = rightAdapter.getItem(position);
                if(position==0){
                    neighborhoodName = neighborhoodName.substring(2);
                }
                tv_search_bar_fujin.setText(neighborhoodName);
                if(neighborhoodName.contains("米")|| neighborhoodName.contains("(智能距离)")||neighborhoodName.contains("全城")){
                    int radius;
                    int pagNum;
                    if(position == 0 ){
                        radius = 1500;
                        pagNum = 1;
                    }else if(position == 1){
                        radius = 500;
                        pagNum = 2;
                    }else if(position == 2){
                        radius = 1000;
                        pagNum = 3;
                    }else if(position == 3){
                        radius = 2000;
                        pagNum = 4;
                    }else if(position == 4){
                        radius = 3000;
                        pagNum = 3;
                    }else{
                        radius = 1000;
                        pagNum = 4;
                    }
                    Log.d(TAG, "onItemClick: "+radius);

                    Log.d(TAG, "onItemClick: "+ FoodSearchApplication.getInstance().getLastpoint());

                    LatLng loc = FoodSearchApplication.getInstance().getLastpoint();
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
                            List<BusinessBean.Business> buslist = new ArrayList<BusinessBean.Business>();
                            BusinessBean.Business business;
                            for(PoiInfo poi:pois){
                                Log.d(TAG, "onGetPoiResult: poi"+poi.address);
                                Log.d(TAG, "onGetPoiResult: poi"+poi.name);
                                Log.d(TAG, "onGetPoiResult: poi"+poi.phoneNum);
                                Log.d(TAG, "onGetPoiResult: poi"+poi.location);
                                Log.d(TAG, "onGetPoiResult: poi---------------");

                                business = new BusinessBean.Business();
                                business.setPhoto_url("http://qcloud.dpfile.com/pc/DuQK4dx9T9l8V9fufwvvynTQBi_i91ThdkChZxvXRdOrExPQva5D6SeX-3VIXlNYTYGVDmosZWTLal1WbWRW3A.jpg");
                                business.setAddress(poi.address);
                                business.setName(poi.name+"(");
                                business.setLatitude(poi.location.latitude);
                                business.setLongitude(poi.location.longitude);
                                buslist.add(business);
                            }
                            searchFoodAdapter.addAll(buslist, true);
                        }
                        @Override
                        public void onGetPoiDetailResult(PoiDetailResult arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
                    PoiNearbySearchOption options = new PoiNearbySearchOption();
                    //À—À˜µƒ÷––ƒµ„
                    options.location(loc);
                    //À—À˜µƒ∞Îæ∂(µ•Œª£∫√◊)
                    options.radius(radius);

                    options.pageCapacity(20);
                    options.pageNum(pagNum);
                    //À—À˜µƒƒ⁄»›
                    options.keyword("美食");
                    poiSearch.searchNearby(options);//∑¢∆–À»§µ„À—À˜

                }else{
                    //3)listView中显示用户点击的街道上所有的美食商户
                    HttpUtil.getFoods(city_name, 1 ,neighborhoodName, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String arg0) {
                            BusinessBean businessBean = new Gson().fromJson(arg0, BusinessBean.class);
                            List<BusinessBean.Business> b = businessBean.getBusinesses();
                            searchFoodAdapter.addAll(b, true);
                        }
                    });
                }
            }
        });
    }

    public void initmeishi(){
        viewmeishi = mBinding.getRoot().findViewById(R.id.item_search_tab_meishi);
        listview_meishi = (ListView) viewmeishi.findViewById(R.id.listview_item_meishi);
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_meishi);
        listview_meishi.setAdapter(adapter);
    }
    public void searchBarMeishi(){
        tv_search_bar_meishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+"fujin");
                if (viewmeishi.getVisibility() == View.INVISIBLE){
                    viewmeishi.setVisibility(View.VISIBLE);
                }else{
                    viewmeishi.setVisibility(View.INVISIBLE);
                }
            }
        });

        HttpUtil.getCityFood(city_name, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                FoodBean foodbean = new Gson().fromJson(s,FoodBean.class);
                FoodBean.Categories categories = foodbean.getCategories().get(0);
                List<FoodBean.Categories.Subcategories> subs = categories.getSubcategories();
                
                List<String> cityfoodname = new ArrayList<String>();
                for (FoodBean.Categories.Subcategories s1:subs) {
                    cityfoodname.add(s1.getCategory_name());
                }
                list_meishi.clear();
                list_meishi.addAll(cityfoodname);
                adapter.notifyDataSetChanged();

            }
        });

        listview_meishi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewmeishi.setVisibility(View.INVISIBLE);
                String category = adapter.getItem(position);
                tv_search_bar_meishi.setText(category);
                HttpUtil.getMeishi(city_name, category,null,null,40, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        BusinessBean businessBean = new Gson().fromJson(s, BusinessBean.class);
                        List<BusinessBean.Business> b = businessBean.getBusinesses();
                        searchFoodAdapter.addAll(b, true);
                    }
                });
            }
        });
    }

    public void searchBarPaixu(){
        viewpaixu = mBinding.getRoot().findViewById(R.id.item_search_tab_paixu);
        tv_search_bar_paixu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: "+"fujin");
                if (viewpaixu.getVisibility() == View.INVISIBLE){
                    viewpaixu.setVisibility(View.VISIBLE);
                }else{
                    viewpaixu.setVisibility(View.INVISIBLE);
                }
            }
        });

        List<String> list_paixu = new ArrayList<>();
        final ArrayAdapter<String> adapter_paixu = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_paixu);
        ListView listview_paixu = (ListView) viewpaixu.findViewById(R.id.listview_item_paixu);
        listview_paixu.setAdapter(adapter_paixu);

        list_paixu.clear();
        list_paixu.add("默认");
        list_paixu.add("星级高优先");
        list_paixu.add("产品评价高优先");
        list_paixu.add("环境评价高优先");
        list_paixu.add("服务评价高优先");
        list_paixu.add("点评数量多优先");
        list_paixu.add("人均价格低优先");
        list_paixu.add("人均价格高优先");
        adapter_paixu.notifyDataSetChanged();

        listview_paixu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewpaixu.setVisibility(View.INVISIBLE);
                int sort = new Random().nextInt(5)+1;
                tv_search_bar_paixu.setText(adapter_paixu.getItem(position));
                HttpUtil.getFoods(city_name,sort,null, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        BusinessBean businessBean = new Gson().fromJson(s, BusinessBean.class);
                        List<BusinessBean.Business> b = businessBean.getBusinesses();
                        searchFoodAdapter.addAll(b, true);
                    }
                });
            }
        });



    }

    public void searchBarShaixuan(){
        viewshaixuan = mBinding.getRoot().findViewById(R.id.item_search_tab_shaixuan);
        tv_search_bar_shaixuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewshaixuan.getVisibility() == View.INVISIBLE){
                    viewshaixuan.setVisibility(View.VISIBLE);
                }else{
                    viewshaixuan.setVisibility(View.INVISIBLE);
                }
            }
        });

        Button button_queding = (Button) viewshaixuan.findViewById(R.id.bt_search_shaixuan_queding);
        button_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewshaixuan.setVisibility(View.INVISIBLE);
                HttpUtil.getFoods(city_name, new Random().nextInt(5) + 1, null, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        BusinessBean businessBean = new Gson().fromJson(s, BusinessBean.class);
                        List<BusinessBean.Business> b = businessBean.getBusinesses();
                        searchFoodAdapter.addAll(b, true);
                    }
                });
            }
        });
    }
    public void loadData(){
        countNum = 1;
        Log.d(TAG, "loadData: "+city_name);
        HttpUtil.getFoods(city_name, countNum, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                BusinessBean businessBean = new Gson().fromJson(s,BusinessBean.class);
                businessList = businessBean.getBusinesses();
                Log.d(TAG, "onResponse: "+businessList.get(0).toString());
                Log.d(TAG, "onResponse: "+businessList.size());
                searchFoodAdapter.addAll(businessList,true);
            }
        });
    }
    public void loadMoreData(int count){
        HttpUtil.getFoods(city_name, count, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                BusinessBean businessBean = new Gson().fromJson(s,BusinessBean.class);
                businessList = businessBean.getBusinesses();
                Log.d(TAG, "onResponse: "+businessList.get(0).toString());
                Log.d(TAG, "onResponse: "+businessList.size());
                searchFoodAdapter.addAll(businessList,false);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
            if (data != null){
                city_name = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                tv_location_city.setText(city_name);
                tv_search_bar_fujin.setText("附近");
                tv_search_bar_meishi.setText("美食");
                loadData();
                searchBarMeishi();
                searchCityDistinct();
            }
        }
    }
}
