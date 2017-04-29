package com.yang.foodsearch.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.yang.foodsearch.R;
import com.yang.foodsearch.application.FoodSearchUtil;
import com.yang.foodsearch.databinding.ActivityWebFoodBinding;

public class WebFoodActivity extends AppCompatActivity {

    ActivityWebFoodBinding mBinding;
    private String isMornign;
    private ImageView iv_webview_back;
    private int mCurrentItem;
    private int mCurrentChooseItem;
    private ImageView iv_search_web_textSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_web_food);
        isMornign = getIntent().getStringExtra("type");
        WebSettings settings = mBinding.webviewSearch.getSettings();
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        if(isMornign.equals("morning")){
            mBinding.webviewSearch.loadUrl(FoodSearchUtil.search_food_morning);
        }else if(isMornign.equals("format")){
            mBinding.webviewSearch.loadUrl(FoodSearchUtil.search_food_format);
        }
        mBinding.webviewSearch.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });

        mBinding.webviewSearch.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                //开始
                super.onPageFinished(view, url);
                mBinding.progressWebview.setVisibility(View.GONE);

            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                //结束
                super.onPageStarted(view, url, favicon);
                mBinding.progressWebview.setVisibility(View.VISIBLE);
            }
        });



        iv_webview_back = (ImageView) mBinding.includeWebZizhu.findViewById(R.id.iv_search_header_back);
        iv_webview_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_search_web_textSize = (ImageView) mBinding.includeWebZizhu.findViewById(R.id.iv_search_more);
        iv_search_web_textSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseDialog();
            }
        });
    }

    /**
     * 显示选择对话框
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体" };
        builder.setTitle("字体判断");
        builder.setSingleChoiceItems(items, mCurrentItem, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCurrentChooseItem = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            WebSettings settings = mBinding.webviewSearch.getSettings();
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (mCurrentChooseItem) {
                    case 0:
//就是通过设置settings的setTextSize来改变字体的大小
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;

                    default:
                        break;
                }
                //保存用户选择的状态
                mCurrentItem = mCurrentChooseItem;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}
