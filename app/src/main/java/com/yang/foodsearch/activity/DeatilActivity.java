package com.yang.foodsearch.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yang.foodsearch.R;

public class DeatilActivity extends AppCompatActivity {

    public static void start(Context context){
        Intent intent = new Intent(context,DeatilActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatil);
    }
}
