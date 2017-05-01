package com.yang.foodsearch.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener;
import com.baidu.mapapi.bikenavi.adapter.IBRoutePlanListener;
import com.baidu.mapapi.bikenavi.model.BikeRoutePlanError;
import com.baidu.mapapi.bikenavi.params.BikeNaviLauchParam;
import com.baidu.mapapi.model.LatLng;
import com.yang.foodsearch.R;
import com.yang.foodsearch.adapter.CommentAdapter;
import com.yang.foodsearch.application.FoodSearchApplication;
import com.yang.foodsearch.bean.BusinessBean;
import com.yang.foodsearch.bean.Comment;
import com.yang.foodsearch.location.BNaviGuideActivity;
import com.yang.foodsearch.util.HttpUtil;
import com.yang.foodsearch.util.ToastUtils;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DetailBussinessActivity extends AppCompatActivity {

    private BikeNavigateHelper mNaviHelper;
    BikeNaviLauchParam param;
    private static boolean isPermissionRequested = false;

    private BusinessBean.Business business;
    private ListView listview;
    private static final String TAG = "DetailBussiness zsj";

    private List<Comment> comments;
    private CommentAdapter adapter;
    private boolean isFujin;
    int[] stars = {R.mipmap.movie_star10,
            R.mipmap.movie_star20,
            R.mipmap.movie_star30,
            R.mipmap.movie_star35,
            R.mipmap.movie_star40,
            R.mipmap.movie_star45,
            R.mipmap.movie_star50};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bussiness);
        business = (BusinessBean.Business) getIntent().getSerializableExtra("business");
        Log.d(TAG, "onCreate: " + business.toString());
        isFujin = FoodSearchApplication.getInstance().isFujin();
        if(!isFujin){
            refresh();
        }
        initListView();

        requestPermission();

        mNaviHelper = BikeNavigateHelper.getInstance();

        LatLng startPt = FoodSearchApplication.getInstance().getLastpoint();
        LatLng endPt = new LatLng(business.getLatitude(), business.getLongitude());
        Log.d(TAG, "onCreate: "+startPt+"--"+endPt);

        param = new BikeNaviLauchParam().stPt(startPt).endPt(endPt);

        ImageView iv_detail_bussiness_back = (ImageView) findViewById(R.id.iv_search_header_back);
        iv_detail_bussiness_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    private void initListView() {
        listview = (ListView) findViewById(R.id.listview_detail_business);
        comments = new ArrayList<Comment>();
        adapter = new CommentAdapter(this, comments);
        LayoutInflater inflater = getLayoutInflater();


        View headerView1 = inflater.inflate(R.layout.item_business_layout, listview, false);
        listview.addHeaderView(headerView1);
        initHeaderView1(headerView1);
        View headerView2 = inflater.inflate(R.layout.header_list_detail_info, listview, false);
        listview.addHeaderView(headerView2);
        initHeaderView2(headerView2);

        listview.setAdapter(adapter);
    }

    private void initHeaderView1(View view) {


        ImageView ivPicture = (ImageView) view.findViewById(R.id.iv_item_business_picture);
        ImageView ivRating = (ImageView) view.findViewById(R.id.iv_item_business_rating);
        TextView tvName = (TextView) view.findViewById(R.id.tv_item_business_name);
        TextView tvPrice = (TextView) view.findViewById(R.id.tv_item_business_price);
        TextView tvInfo = (TextView) view.findViewById(R.id.tv_item_business_info);
        TextView tvDistance = (TextView) view.findViewById(R.id.tv_item_business_distance);

        //显示商家图片
        if (isFujin){
            ivPicture.setImageResource(R.mipmap.bucket_no_picture);
        }else{
            HttpUtil.displayImage(business.getS_photo_url(), ivPicture);
        }
        //显示打分图片
        int idx = new Random().nextInt(7);
        ivRating.setImageResource(stars[idx]);

        //商家名称
        String name = business.getName();
        //去掉商家名称后面的“这是一条测试数据....”
        name = name.substring(0, name.indexOf("("));
        //添加上分店名称

        if (!TextUtils.isEmpty(business.getBranch_name())) {
            name = name + "(" + business.getBranch_name() + ")";
        }
        tvName.setText(name);

        //人均价格
        tvPrice.setText("￥" + (new Random().nextInt(101) + 50) + "元/人");

        if(!isFujin){
            //商户的信息（包括地段和菜系）
            StringBuilder sb = new StringBuilder();
            //拼接商户的地段信息
            for (int i = 0; i < business.getRegions().size(); i++) {
                if (i == 0) {
                    sb.append(business.getRegions().get(i));
                } else {
                    sb.append("/").append(business.getRegions().get(i));
                }
            }

            sb.append(" ");
            //拼接商户的菜系信息
            for (int i = 0; i < business.getCategories().size(); i++) {
                if (i == 0) {
                    sb.append(business.getCategories().get(i));
                } else {
                    sb.append("/").append(business.getCategories().get(i));
                }
            }

            tvInfo.setText(sb.toString());
        }else{
            tvInfo.setText("");
        }


        tvDistance.setText("");

    }

    private void initHeaderView2(View view) {
        TextView tvAddress = (TextView) view.findViewById(R.id.tv_header_list_detail_info_address);
        TextView tvPhone = (TextView) view.findViewById(R.id.tv_header_list_detail_info_phone);
        tvAddress.setText(business.getAddress());

        tvPhone.setText(business.getTelephone());

        LinearLayout ll_detail_address = (LinearLayout) view.findViewById(R.id.ll_detail_bussiness_address);

        ll_detail_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBikeNavi();
            }
        });

        LinearLayout ll_detail_phone = (LinearLayout) view.findViewById(R.id.ll_detail_bussiness_phone);
        ll_detail_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+business.getTelephone()));
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void refresh() {

        String url = business.getReview_list_url();
        Log.d(TAG, "refresh: " + url);
        HttpUtil.getComments(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String arg0) {

                Document document = Jsoup.parse(arg0);

                List<Comment> comments = new ArrayList<Comment>();

                Elements elements = document.select(".comment-list ul li[data-id]");

                for (Element element : elements) {
                    Comment comment = new Comment();
                    //从element中取出相应的内容作为comment的属性值
                    //取网友的用户名、头像
                    Elements imgElements = element.select(".pic a img");
                    Element imgElement = imgElements.get(0);
                    comment.setUsername(imgElement.attr("title"));
                    comment.setAvatar(imgElement.attr("src"));
                    //选取为商家的打分
                    Elements spanElements = element.select(".user-info span[title]");
                    Element spanElement = spanElements.get(0);
                    String rat = spanElement.attr("class");//item-rank-rst irr-star10
                    String rating = rat.split(" ")[1].split("-")[1];//star10
                    comment.setRating(rating);
                    //选取评论的正文
                    Elements divElements = element.select(".J_brief-cont");
                    Element divElement = divElements.get(0);
                    comment.setContent(divElement.text());

                    //选取人均消费价格
                    Elements spans = element.select(".comm-per");
                    if (spans != null && spans.size() > 0) {
                        //提供了人均价格
                        String avgPrice = spans.get(0).text();//人均 ￥90
                        comment.setAvgPrice(avgPrice.split(" ")[1]);//￥90
                    } else {
                        //评论中未提供人均消费价格
                        comment.setAvgPrice("");
                    }

                    //选取评论的配图
                    Elements imgs = element.select(".shop-photo img");

                    if (imgs != null && imgs.size() > 0) {
                        //评论中有配图
                        int num = imgs.size();
                        if (num > 3) {
                            //配图多于3张，最多就取3张
                            num = 3;
                        }
                        String[] pics = new String[num];
                        for (int i = 0; i < num; i++) {
                            Element img = imgs.get(i);
                            pics[i] = img.attr("src");
                        }
                        comment.setImgs(pics);
                    } else {
                        //评论中没有配图
                        comment.setImgs(null);
                    }

                    comments.add(comment);
                }

                adapter.addAll(comments, true);

            }
        });
    }


    private void startBikeNavi() {
        Log.d("View", "startBikeNavi");
        mNaviHelper.initNaviEngine(this, new IBEngineInitListener() {
            @Override
            public void engineInitSuccess() {
                Log.d("View", "engineInitSuccess");
                routePlanWithParam();
            }

            @Override
            public void engineInitFail() {
                Log.d("View", "engineInitFail");
            }
        });
    }

    private void routePlanWithParam() {
        mNaviHelper.routePlanWithParams(param, new IBRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d("View", "onRoutePlanStart");
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.d("View", "onRoutePlanSuccess");
                Intent intent = new Intent();
                intent.setClass(DetailBussinessActivity.this, BNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(BikeRoutePlanError error) {
                Log.d("View", "onRoutePlanFail");
            }

        });
    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {

            isPermissionRequested = true;

            ArrayList<String> permissions = new ArrayList<>();
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.CALL_PHONE);

            }

            if (permissions.size() == 0) {
                return;
            } else {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
            }
        }
    }


}
