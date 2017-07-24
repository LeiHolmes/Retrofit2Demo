package com.LeiHolmes.retrofit2demo.service;

import com.LeiHolmes.retrofit2demo.bean.MovieEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description:
 * author         xulei
 * Date           2017/7/24
 */

public interface MovieService {
    @GET("top250")
    Observable<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);
}
