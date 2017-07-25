package com.LeiHolmes.retrofit2demo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.LeiHolmes.retrofit2demo.R;
import com.LeiHolmes.retrofit2demo.entity.MovieEntity;
import com.LeiHolmes.retrofit2demo.service.MovieService;
import com.LeiHolmes.retrofit2demo.util.HttpUtil;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:   Retrofit2+RxJava1
 * author         xulei
 * Date           2017/7/24 17:59
 */
public class RetrofitRxJavaActivity extends AppCompatActivity {
    TextView tvResult;
    Subscriber<MovieEntity> subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_rxjava);
        tvResult = (TextView) findViewById(R.id.tv_result);
    }

    public void onRequestClicked(View view) {
//        getMovie();
        getMovieByUtil();
    }

    public void onCancleClicked(View view) {
        //想取消请求直接取消订阅即可
        if (subscriber != null)
            subscriber.unsubscribe();
    }

    /**
     * 结合RxJava获取电影数据
     */
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

    /**
     * 使用HttpUtil获取电影数据
     */
    private void getMovieByUtil() {
        subscriber = new Subscriber<MovieEntity>() {
            @Override
            public void onCompleted() {
                Toast.makeText(RetrofitRxJavaActivity.this, "获取数据完毕", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                tvResult.setText(e.getMessage());
            }

            @Override
            public void onNext(MovieEntity movieEntity) {
                tvResult.setText(movieEntity.toString());
            }
        };
        HttpUtil.getInstance().getTopMovie(0, 10, subscriber);
    }
}
