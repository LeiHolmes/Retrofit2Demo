package com.LeiHolmes.retrofit2demo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.LeiHolmes.retrofit2demo.R;
import com.LeiHolmes.retrofit2demo.entity.DataEntity1;
import com.LeiHolmes.retrofit2demo.entity.DataEntity2;
import com.LeiHolmes.retrofit2demo.service.NetWorkService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Description:   Retrofit网络请求基本实现
 * author         xulei
 * Date           2017/7/25 11:52
 */
public class RetrofitActivity extends AppCompatActivity {
    public static final String BASE_URL = "https://api.github.com/";
    TextView tvMain;
    private NetWorkService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        tvMain = (TextView) findViewById(R.id.tv_main);
        initRetrofit();
    }

    private void initRetrofit() {
        //Retrofit2.0后只支持OkHttp请求也不用添加okhttp依赖，也可配置好OkHttpClient后传入Retrofit。
        //不准备自定义的话可以不进行任何OkHttp的配置
//        OkHttpClient client = new OkHttpClient();
//        client.interceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                    Response response = chain.proceed(chain.request());
//                    // Do anything with response here
//                    return response;
//                }
//        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                //手动指定Gson解析方式,可指定不同Convert来使用如Jackson，XML等解析方式
                .addConverterFactory(GsonConverterFactory.create())
                //不支持fastjson解析方式，可继承Converter.Factory类自定义。
//                .addConverterFactory(FastjsonConverterFactory.create())
                //返回数据为RxJava形式
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                //设置自定义的OkHttpClient
//                .client(client)
                .build();

        //使用Retrofit类生成接口NetWorkService的实现
        service = retrofit.create(NetWorkService.class);
    }

    /**
     * 请求数据
     */
    public void onViewClicked(View view) {
        method1();
//        method2();
    }

    /**
     * 一个参数
     */
    public void method1() {
        //请求参数
        String parameter1 = "basil2style";
        //调用生成的GitHubServcie向服务器异步发送请求
        Call<DataEntity1> call = service.getData(parameter1);
        //同步请求,不可在主线程中调用。放入后台线程
//       call.execute();
        //异步请求
        call.enqueue(new Callback<DataEntity1>() {
            @Override
            public void onResponse(Call<DataEntity1> call, Response<DataEntity1> response) {
                Toast.makeText(RetrofitActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                DataEntity1 bean = response.body();
                tvMain.setText(bean.toString());
            }

            @Override
            public void onFailure(Call<DataEntity1> call, Throwable t) {

            }
        });
        //每一个实例仅仅能够被使用一次，但是可以通过clone()函数创建一个新的可用的实例。 
//        call.clone();
        //能让正在进行的事务取消
//        call.cancle();
    }

    /**
     * 多个参数
     */
    public void method2() {
        String parameter1 = "square";
        String parameter2 = "retrofit";
        String parameter3 = "contributors";
        Call<List<DataEntity2>> call = service.getData(parameter1, parameter2, parameter3);
        call.enqueue(new Callback<List<DataEntity2>>() {
            @Override
            public void onResponse(Call<List<DataEntity2>> call, Response<List<DataEntity2>> response) {
                Toast.makeText(RetrofitActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                List<DataEntity2> list = response.body();
                tvMain.setText(list.toString());
            }

            @Override
            public void onFailure(Call<List<DataEntity2>> call, Throwable t) {

            }
        });
    }
}
