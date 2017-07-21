package com.aladdin.retrofit2demo.net;

import com.aladdin.retrofit2demo.bean.DataBean1;
import com.aladdin.retrofit2demo.bean.DataBean2;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Description:
 * author         xulei
 * Date           16/4/27
 */
public interface NetWorkService {
    //根据不同请求方式Retrofit提供了5种注解方式：GET、POST、PUT、DELETE和HEAD
    //转换HTTP API为Java接口

    //可直接指定源绝对URL,baseUrl就失效了
//    @GET("https://api.github.com/users/basil2style")

    //可指定源相对URL
    @GET("users/basil2style")
    Call<DataBean1> getData();

    //也可在URL里使用替换块和参数进行动态更新
    //替换块是{ and }包围的字母数字组成的字符串，相应的参数必须使用相同的字符串被@Path进行注释
    @GET("users/{params1}")
    Call<DataBean1> getData(@Path("params1") String parameter1);

    @GET("repos/{params1}/{params2}/{params3}")
    Call<List<DataBean2>> getData(
            @Path("params1") String parameter1,
            @Path("params2") String parameter2,
            @Path("params3") String parameter3
    );

    //添加查询参数(?x1=y1)
    @GET("repos/square/{retrofit}/contributors")
    Call<List<DataBean2>> groupData(@Path("retrofit") String retrofit, @Query("sort") String sort);

    //复杂的查询参数可以使用Map进行组合(?x1=y1&?x2=y2&...)
    @GET("repos/square/{retrofit}/contributors")
    Call<List<DataBean2>> getData(@Path("repos") String repos, @QueryMap Map<String, String> parameters);

    //可以通过@Body注解指定一个对象作为Http请求的请求体
    @POST("users/new")
    Call<DataBean2> createUser(@Body DataBean2 dataBean2);
}
