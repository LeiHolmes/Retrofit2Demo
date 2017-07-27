# Retrofit2Demo
## Retrofit2.0基本使用及Retrofit+RxJava1的实现方式
### Retrofit2.0简介  
　　Retrofit是一套RESTful架构的Android(Java)客户端实现，基于注解，提供JSON to POJO(Plain Ordinary Java Object,简单Java对象)，POJO to JSON，网络请求(POST，GET,PUT，DELETE等)封装。用官方自己的介绍就是:
>A type-safe REST client for Android and Java

　　现已更新到2.0的版本，与1.0版本的使用上还是不小的差别，我也是第一次用，这里主要和大家研究研究2.0版本简单使用。也可参详[官方示例](http://square.github.io/retrofit/)。
<!--more-->  
### 准备工作
#### 添加权限
　　首先添加网络请求权限
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```
#### 添加依赖
　　Retrofit2.0版本后只支持okhttp请求，也已经封装好了，就不需要添加okhttp的依赖了。
```xml
dependencies {
    compile 'com.squareup.retrofit2:retrofit:2.0.2'
    compile 'com.squareup.retrofit2:converter-gson:2.0.0-beta3'
}
```
### 简单使用
　　接下来瞧一瞧Retrofit初始化以及如何请求数据。
#### 初始化Retrofit对象
```java
public static final String BASE_URL = "https://api.github.com/";
Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
```
　　BASE_URL就是你请求的Server地址。
```java
.addConverterFactory(GsonConverterFactory.create())
```
　　Retrofit2.0不提供返回JSON数据的默认解析方式，需要手动指定，支持Jackson等多种解析方式。需要哪种就添加相应的依赖，这里添加的是Retrofit提供的converter-gson依赖。有点不爽的就是不支持FastJson解析，有需要的话可以自己写一个FastjsonConverterFactory继承Converter.Factory实现。
　　虽然Retrofit2.0后只支持okhttp请求，但你也可以自定义一个okhttp再配置进Retrofit。
```java
OkHttpClient client = new OkHttpClient();
client.interceptors().add(new Interceptor() {
    @Override
    public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            // Do anything with response here
            return response;
        }
Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
});
```
#### 定义请求接口
　　实现转换HTTP API为Java接口，Retrofit提供了5种内置的注解：GET、POST、PUT、DELETE和HEAD，在注解中指定的资源的相对URL。
```java
public interface NetWorkService {
	@GET("users/basil2style")
    Call<DataBean> getData();
}
```
　　使用替换块和参数进行动态更新,替换块是{ and }包围的字母数字组成的字符串，相应的参数必须使用相同的字符串被@Path进行注释。
```java
@GET("repos/{params1}/{params2}/contributors")
Call<List<DataBean2>> getData(
        @Path("params1") String params1,
        @Path("params2") String params2,
);
```
　　当我们调用getData()这个方法的时候，Retrofit会创建这个URL。如果我们传入Square和Retrofit字符串，分别作为owner和repo参数。我们就会得到这个URL：https://api.github.com/repos/square/retrofit/contributors
　　添加查询参数
```java
@GET("repos/square/{retrofit}/contributors")
Call<List<DataBean2>> groupData(@Path("retrofit") String retrofit, @Query("sort") String sort);
```
　　当我们调用getData()方法时，传入一个查询参数字符串"ok",这样我们就能得到URL：https://api.github.com/repos/square/retrofit/contributors?sort=ok
　　当然如果查询参数过多，我们也可以使用Map进行组合再传进来。
```java
@GET("repos/square/{retrofit}/contributors")
Call<List<DataBean2>> getData(@Path("repos") String repos, @QueryMap Map<String, String> parameters);
```
#### 请求数据
　　Retrifot支持同步和异步的请求方式，先使用Retrofit类生成接口NetWorkService的实现。
```java
NetWorkService service = retrofit.create(NetWorkService.class);
```
##### 同步请求
```java
Call<DataBean> call = service.getData(Square,Retrofit);
DataBean bean = call.execute().body();
```
　　注意同步请求不可在主线程执行，你懂得。且call只能执行execute()方法一次，若要再次请求可通过`Call<DataBean> call = call.clone()`来再复制一个Call对象。
##### 异步请求
```java
call.enqueue(new Callback<DataBean>() {
    @Override
    public void onResponse(Call<DataBean> call, Response<DataBean> response) {
        Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
        DataBean bean = response.body();
        tvMain.setText(bean.toString());
    }
    @Override
    public void onFailure(Call<DataBean> call, Throwable t) {
    }
});
```
　　当我们执行的同步或异步加入队列后，可以随时使用call.cancel()方法取消请求。
### 注意
　　注解中参数的写法与BASE_URL的拼接一定要注意，请看以下写法。
#### 错误示例1
　　BASE_URL：https://api.github.com/repos/square  
　　Get注解：　@GET("/basil2style")  
　　结果URL：　https://api.github.com/basil2style
#### 错误示例2
　　BASE_URL：https://api.github.com/repos/square  
　　Get注解：　@GET("basil2style")  
　　结果URL：　https://api.github.com/repos/basil2style
#### 推荐写法
　　BASE_URL：https://api.github.com/repos/square/  
　　Get注解：　@GET("basil2style")  
　　结果URL：　https://api.github.com/repos/square/basil2style
### 总结
　　Retrofit2.0的基本实现讲解完毕，Retrofit+Okhttp+Gson可以算是目前来说相当快的超级网络请求框架了。相比较于Volley都快不少，亲测结果很爽。小伙伴们赶紧整起来吧！
　　技术渣一枚，有写的不对的地方欢迎大神们留言指正，有什么疑惑或者不懂的地方也可以在Issues中提出，我会及时解答。

