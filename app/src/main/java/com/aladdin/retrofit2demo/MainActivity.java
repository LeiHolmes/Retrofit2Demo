package com.aladdin.retrofit2demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aladdin.retrofit2demo.bean.DataBean1;
import com.aladdin.retrofit2demo.bean.DataBean2;
import com.aladdin.retrofit2demo.net.NetWorkService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    public static final String BASE_URL = "https://api.github.com/";
    private NetWorkService service;
    private TextView tvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMain = (TextView) findViewById(R.id.tv_main);

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

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        //使用Retrofit类生成接口NetWorkService的实现
        service = retrofit.create(NetWorkService.class);
    }

    public void onClick(View view) {
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
        Call<DataBean1> call = service.getData(parameter1);

        //同步请求,不可在主线程中调用。放入后台线程
//       call.execute();
        
        //异步请求
        call.enqueue(new Callback<DataBean1>() {
            @Override
            public void onResponse(Call<DataBean1> call, Response<DataBean1> response) {
                Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                DataBean1 bean = response.body();
                tvMain.setText(bean.toString());
            }

            @Override
            public void onFailure(Call<DataBean1> call, Throwable t) {

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
        Call<List<DataBean2>> call = service.getData(parameter1, parameter2, parameter3);
        call.enqueue(new Callback<List<DataBean2>>() {
            @Override
            public void onResponse(Call<List<DataBean2>> call, Response<List<DataBean2>> response) {
                Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                List<DataBean2> list = response.body();
                tvMain.setText(list.toString());
            }

            @Override
            public void onFailure(Call<List<DataBean2>> call, Throwable t) {

            }
        });
    }
}
