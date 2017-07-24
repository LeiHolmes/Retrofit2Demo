package com.LeiHolmes.retrofit2demo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.LeiHolmes.retrofit2demo.R;
import com.LeiHolmes.retrofit2demo.service.MovieService;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UseRxJavaActivity extends AppCompatActivity {
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_rx_java);
        tvResult = (TextView) findViewById(R.id.tv_result);
    }

    public void onRequestClicked(View view) {
        getMovie();
    }

    private void getMovie() {
        String baseUrl = "https://api.douban.com/v2/movie/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        MovieService movieService = retrofit.create(MovieService.class);
        movieService.getTopMovie(0, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieEntity -> tvResult.setText(movieEntity.toString()));
    }
}
