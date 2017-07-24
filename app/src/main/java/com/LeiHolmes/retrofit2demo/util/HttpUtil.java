package com.LeiHolmes.retrofit2demo.util;

import com.LeiHolmes.retrofit2demo.bean.MovieEntity;
import com.LeiHolmes.retrofit2demo.service.MovieService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description:
 * author         xulei
 * Date           2017/7/24
 */

public class HttpUtil {
    public static final String BASE_URL = "https://api.douban.com/v2/movie/";
    private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit retrofit;
    private MovieService movieService;

    public HttpUtil() {
        //自定义okhttp设置超时时长
        OkHttpClient.Builder client = new OkHttpClient.Builder().connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(client.build())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        movieService = retrofit.create(MovieService.class);
    }

    private static class SingleHolder {
        private static final HttpUtil INSTANCE = new HttpUtil();
    }

    public static HttpUtil getInstance() {
        return SingleHolder.INSTANCE;
    }

    /**
     * 获取电影数据
     * @param start 起始位置
     * @param count 获取数量
     * @param subscriber RxJava观察者
     */
    public void getTopMovie(int start, int count, Subscriber<MovieEntity> subscriber) {
        movieService.getTopMovie(start, count)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
