package com.example.travel_master_yyz.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.115.202:11451/";

    //家里192.168.1.107
    //广外：192.168.206.71
    //手机热点（会变得）：192.168.115.202
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(
                    //TODO 配置OkHttp，加日志拦截器
                    new OkHttpClient.Builder()
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .build()
            ).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
