package com.yang.foodsearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yang.foodsearch.R;
import com.yang.foodsearch.bean.BusinessBean;
import com.yang.foodsearch.util.HttpUtil;
import com.yang.foodsearch.view.TextViewPlus;

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
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BusinessBean.Business business = getItem(position);
        HttpUtil.displayImage(business.getS_photo_url(),viewHolder.iv_search_food_pic);

        String name = business.getName();
        viewHolder.tv_search_food_name.setText(name.substring(0,name.indexOf("(")));
        int perMoeny = new Random().nextInt(30)+40;
        viewHolder.tv_search_food_money.setText("￥"+perMoeny+"/人");
        viewHolder.tv_search_food_caixi.setText(business.getCategories().get(0));
        viewHolder.tv_search_food_pingfen.setText(new Random().nextInt(100)+400+"条");
        viewHolder.iv_search_food_pingfen.setImageResource(stars[new Random().nextInt(7)]);

        return convertView;
    }
    public class ViewHolder{
        ImageView iv_search_food_pic,iv_search_food_pingfen;
        TextViewPlus tv_search_food_name,tv_search_food_pingfen,
            tv_search_food_caixi,tv_search_food_money;
    }
}
