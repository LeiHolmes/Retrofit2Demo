package com.LeiHolmes.retrofit2demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.LeiHolmes.retrofit2demo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_retrofit: //retrofit实现网络请求
                startActivity(new Intent(this, RetrofitActivity.class));
                break;
            case R.id.bt_retrofit_rxjava: //retrofit+rxjava实现网络请求
                startActivity(new Intent(this, RetrofitRxJavaActivity.class));
                break;
        }
    }

}
