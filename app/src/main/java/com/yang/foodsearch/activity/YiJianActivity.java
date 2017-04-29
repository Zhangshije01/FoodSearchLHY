package com.yang.foodsearch.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import com.yang.foodsearch.R;
import com.yang.foodsearch.databinding.ActivityYiJianBinding;

import java.util.ArrayList;

public class YiJianActivity extends AppCompatActivity {

    ActivityYiJianBinding mBinding;
    private String message;
    private String userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_yi_jian);

        mBinding.buttonMyTijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = mBinding.etMyYijian.getText().toString();
                userPhone = mBinding.etMyShouji.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:13381154963"));
                intent.putExtra("sms_body", message+"电话："+userPhone);
                startActivity(intent);
                Toast.makeText(YiJianActivity.this,"ok",Toast.LENGTH_SHORT).show();
            }
        });


    }
}
