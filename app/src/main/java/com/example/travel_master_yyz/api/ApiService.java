package com.example.travel_master_yyz.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // 获取验证码
    @GET("login/sendVerify")
    Call<ApiResponse> sendVerifyCode(@Query("email") String email);

    // 注册用户
    @POST("login/signup")
    Call<ApiResponse> registerUser(@Body RegisterRequest request);


    // 重置密码
    @POST("login/resetPwd")
    Call<ApiResponse> resetPassword(@Body ResetPasswordRequest request);
    //登录
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
