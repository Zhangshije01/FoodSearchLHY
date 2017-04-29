package com.yang.foodsearch.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.yang.foodsearch.R;
import com.yang.foodsearch.application.FoodSearchApplication;
import com.yang.foodsearch.application.FoodSearchUtil;
import com.yang.foodsearch.bean.BusinessBean;
import com.yang.foodsearch.util.HttpUtil;
import com.yang.foodsearch.view.TextViewPlus;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * Created by 蜗牛 on 2017-04-22.
 */
public class SearchFoodAdapter extends MyBaseAdapter<BusinessBean.Business>{

    int[] stars = {R.mipmap.movie_star10,
            R.mipmap.movie_star20,
            R.mipmap.movie_star30,
            R.mipmap.movie_star35,
            R.mipmap.movie_star40,
            R.mipmap.movie_star45,
            R.mipmap.movie_star50};

    public SearchFoodAdapter(Context context, List<BusinessBean.Business> datasource) {
        super(context, datasource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder= null;
        if(viewHolder == null){
            convertView = inflater.inflate(R.layout.item_search_food_bussiness,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.iv_search_food_pic = (ImageView) convertView.findViewById(R.id.iv_search_food_pic);
            viewHolder.iv_search_food_pingfen = (ImageView) convertView.findViewById(R.id.iv_search_food_pingfen);
            viewHolder.tv_search_food_name = (TextViewPlus) convertView.findViewById(R.id.tv_search_food_name);
            viewHolder.tv_search_food_pingfen = (TextViewPlus) convertView.findViewById(R.id.tv_search_food_pingfen);
            viewHolder.tv_search_food_caixi = (TextViewPlus) convertView.findViewById(R.id.tv_search_food_caixi);
            viewHolder.tv_search_food_money = (TextViewPlus) convertView.findViewById(R.id.tv_search_food_money);
            viewHolder.tv_search_food_address = (TextViewPlus) convertView.findViewById(R.id.tv_search_food_address);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BusinessBean.Business business = getItem(position);
        if(business.getS_photo_url()!= null){
            HttpUtil.displayImage(business.getS_photo_url(),viewHolder.iv_search_food_pic);
        }else{
            HttpUtil.displayImage(FoodSearchUtil.food_url,viewHolder.iv_search_food_pic);
        }

        Log.d("zjs", "getView: "+business.getS_photo_url());
        String name = business.getName();
        viewHolder.tv_search_food_name.setText(name.substring(0,name.indexOf("(")));
        int perMoeny = new Random().nextInt(30)+40;
        viewHolder.tv_search_food_money.setText("￥"+perMoeny+"/人");
        StringBuilder sb = new StringBuilder();
        if(business.getRegions()!=null){
            //商户的信息（包括地段和菜系）
            //拼接商户的地段信息
            for(int i=0;i<business.getRegions().size();i++){
                if(i==0){
                    sb.append(business.getRegions().get(i));
                }else{
                    sb.append("/").append(business.getRegions().get(i));
                }
            }
            sb.append(" ");
            sb.append(business.getCategories().get(0));
        }else{
            sb.append(business.getAddress());
        }

        viewHolder.tv_search_food_caixi.setText(sb.toString());
        viewHolder.tv_search_food_pingfen.setText(new Random().nextInt(100)+400+"条");
        viewHolder.iv_search_food_pingfen.setImageResource(stars[new Random().nextInt(7)]);


        //距离

        if(FoodSearchApplication.getInstance().getLastpoint()!=null){
            //商户的经纬度
            double lat1 = business.getLatitude();
            double lng1 = business.getLongitude();
            //使用者的经纬度

            LatLng latLng = new LatLng(lat1,lng1);

            Log.d("zsj", "getView: "+latLng+"\n"+FoodSearchApplication.getInstance().getLastpoint());

            //通过自己的工具类计算距离
            double distance = DistanceUtil.getDistance(latLng,FoodSearchApplication.getInstance().getLastpoint());
            DecimalFormat df = new DecimalFormat("#.00");
            viewHolder.tv_search_food_address.setText(df.format(distance) + "米");
        }else{
            viewHolder.tv_search_food_address.setText("");
        }


        return convertView;
    }
    public class ViewHolder{
        ImageView iv_search_food_pic,iv_search_food_pingfen;
        TextViewPlus tv_search_food_name,tv_search_food_pingfen,
            tv_search_food_caixi,tv_search_food_money,tv_search_food_address;
    }
}
