package com.yang.foodsearch.util;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.yang.foodsearch.R;
import com.yang.foodsearch.application.FoodSearchApplication;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 17/4/21.
 */

public class HttpUtil {
    private static final String APPKEY = "49814079";
    private static final String APPSECRET = "90e3438a41d646848033b6b9d461ed54";
    private static RequestQueue queue = Volley.newRequestQueue(FoodSearchApplication.getInstance());

    public static String getUrl(String url,Map<String,String> params ){
        String result="";
        String sign = getSign(APPKEY,APPSECRET,params);
        String query = getQuery(APPKEY,sign,params);
        result = url + "?" + query;
        return result;
    }

    /**
     * ªÒµ√∑˚∫œ¥Û÷⁄µ„∆¿∑˛ŒÒ∆˜“™«Ûµƒ«©√˚
     */
    private static String getSign(String appkey, String appsecret, Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder();
        // ∂‘≤Œ ˝√˚Ω¯––◊÷µ‰≈≈–Ú
        String[] keyArray = params.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);
        // ∆¥Ω””––Úµƒ≤Œ ˝√˚-÷µ¥Æ
        stringBuilder.append(appkey);
        for (String key : keyArray){

            stringBuilder.append(key).append(params.get(key));
        }
        String codes = stringBuilder.append(appsecret).toString();
        //String sign = org.apache.commons.codec.digest.DigestUtils.shaHex(codes).toUpperCase();
        String sign = new String(Hex.encodeHex(DigestUtils.sha(codes))).toUpperCase();
        return sign;
    }

    /**
     * ªÒµ√∑√Œ ≤Œ ˝
     */
    private static String getQuery(String appkey, String sign, Map<String, String> params) {

        try {
            // ÃÌº”«©√˚
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("appkey=").append(appkey).append("&sign=").append(sign);
            for (Map.Entry<String, String> entry : params.entrySet()){

                stringBuilder.append('&').append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(),"utf8"));
            }
            String queryString = stringBuilder.toString();

            return queryString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("httputil exception");
        }
    }

    private static ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
        //lru:least recently use
        LruCache<String,Bitmap> cache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory()/8)){
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            };
        };
        @Override
        public void putBitmap(String arg0, Bitmap arg1) {
            cache.put(arg0, arg1);
        }

        @Override
        public Bitmap getBitmap(String arg0) {
            return cache.get(arg0);
        }
    });

    /**
     * 通过Volley的ImageLoader
     * 在指定的ImageView中显示网络图片
     *
     * @param url 要显示图片的网络地址
     * @param iv  显示图片的ImageView
     */
    public static void displayImage(String url,ImageView iv){
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv
                , R.mipmap.bucket_no_picture, R.mipmap.bucket_no_picture);
        imageLoader.get(url, listener);
    }
    public static void getFoods(String city,int page,String region,Response.Listener<String> listener){

        Map<String, String> params = new HashMap<String, String>();
        params.put("city", city);
        params.put("category", "美食");
        params.put("limit","10");
        params.put("page",String.valueOf(page));
        if(!TextUtils.isEmpty(region)){
            params.put("region", region);
        }
        String url = "http://api.dianping.com/v1/business/find_businesses";
        StringRequest req = new StringRequest(getUrl(url , params ), listener, null);
        queue.add(req);
    }

    public static void getFoodsbyMeishi(String city,int page,String region,String category,Response.Listener<String> listener){

        Map<String, String> params = new HashMap<String, String>();
        params.put("city", city);
        params.put("category", category);
        params.put("limit","10");
        params.put("page",String.valueOf(page));
        if(!TextUtils.isEmpty(region)){
            params.put("region", region);
        }
        String url = "http://api.dianping.com/v1/business/find_businesses";
        StringRequest req = new StringRequest(getUrl(url , params ), listener, null);
        queue.add(req);
    }


    public static void getMeishi(String city,String region,String category,String sort,String radius,int limit,Response.Listener<String> listener){

        Map<String, String> params = new HashMap<String, String>();
        params.put("city", city);
        params.put("limit",String.valueOf(limit));
        if(!TextUtils.isEmpty(category)){
            params.put("category", category);
        }
        if(!TextUtils.isEmpty(region)){
            params.put("region", region);
        }
        if(!TextUtils.isEmpty(sort)){
            params.put("sort",sort);
        }
        if(!TextUtils.isEmpty(radius)){
            params.put("radius",radius);
        }

        String url = "http://api.dianping.com/v1/business/find_businesses";
        StringRequest req = new StringRequest(getUrl(url , params ), listener, null);
        queue.add(req);
    }
    public static void getComments(String url,Response.Listener<String> listener){

        StringRequest req = new StringRequest(url, listener, null);
        queue.add(req);

    }

    public static void getDistricts(String city,Response.Listener<String> listener){

        Map<String, String> params = new HashMap<String, String>();
        params.put("city", city);
        StringRequest req = new StringRequest(getUrl("http://api.dianping.com/v1/metadata/get_regions_with_businesses", params ), listener, null);
        queue.add(req);
    }

    public static void getCityFood(String city,Response.Listener<String> listener){

        Map<String,String> params = new HashMap<>();
        params.put("city",city);

        StringRequest request = new StringRequest(getUrl("http://api.dianping.com/v1/metadata/get_categories_with_businesses",params),listener,null);
        queue.add(request);

    }



}
