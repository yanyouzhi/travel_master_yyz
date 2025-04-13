package com.example.travel_master_yyz.api;

import com.example.travel_master_yyz.model_mvvm.CommunityResponse;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // 获取验证码
    @GET("login/sendVerify")
    Call<ApiResponse> sendVerifyCode(@Query("email") String email);

    // 注册用户
    @POST("login/signup")
    Call<ApiResponse> registerUser(@Body RegisterRequest request);

    @GET("/diary/selectRandomCommunityList")
    Call<BaseResponse<List<CommunityHomePost>>> getRandomCommunityList(
            @Header("token") String token,
            @Query("uid") String uid
    );


    // 重置密码
    @POST("login/resetPwd")
    Call<ApiResponse> resetPassword(@Body ResetPasswordRequest request);
    //登录
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/user/editPhone")
    Call<BaseResponse> editPhone(
            @Header("TOKEN") String token,
            @Body JsonObject body
    );

    @POST("/user/editEmail")
    Call<BaseResponse> editEamil(
            @Header("TOKEN") String token,
            @Body JsonObject body
    );

    @GET("diary/selectPage")
    Call<MineDiaryResponse> getDiaries(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("uid") String uid,
            @Query("fid") String fid,
            @Header("TOKEN") String token
    );

    @GET("diary/selectPage")
    Call<CommunityResponse> getCommunityPosts(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("uid") String uid,
            @Query("fid") String fid,
            @Query("community") int comment,
            @Header("TOKEN") String token
    );

    @GET("diary/selectPage")
    Call<CommunityResponse> getUserCommunityPosts(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("uid") String uid,
            @Query("fid") String fid,
            @Header("TOKEN") String token
    );

    @GET("diary/selectPage_2")
    Call<NewCommunityResponse> getUserCommunityPosts_2(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("uid") String uid,
            @Header("TOKEN") String token
    );

    @POST("diary/add")
    Call<PostResponse> postDiary(
            @Header("TOKEN") String token,
            @Body PostRequest postRequest
    );

    @GET("diary/selectData")
    Call<DiaryDetailResponse> getDiaryDetail(
            @Header("TOKEN") String token,
            @Query("id") String id,
            @Query("uid") String uid
    );

    @GET("diary/selectPageComment")
    Call<CommentResponse> getComments(
            @Header("TOKEN") String token,
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("fid") String fid
    );

    @GET("user/selectPageCollect")
    Call<FollowDataResponse> getFollowedPosts(
            @Header("TOKEN") String token,
            @Query("uid") String uid,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @GET("landscape/selectPageCollect")
    Call<CollectResponse> getCollectList(
            @Header("TOKEN") String token,
            @Query("page") String page,
            @Query("limit") String limit,
            @Query("uid") String uid
    );

    @GET("user/selectPageFan")
    Call<FollowDataResponse> getFollow(
            @Header("TOKEN") String token,
            @Query("uid") String uid,
            @Query("page") int page,
            @Query("limit") int limit
    );

        @POST("diary/addComment")
        Call<BaseResponse> postComment(
                @Header("TOKEN") String token,
                @Query("uid") String uid,
                @Query("fid") String fid,
                @Body CommentRequest request
        );

    @GET("diary/addPraise")
    Call<BaseResponse> addPraise(
            @Header("TOKEN") String token,
            @Query("uid") String uid,
            @Query("fid") String fid
    );

    @GET("diary/delPraise")
    Call<BaseResponse> delPraise(
            @Header("TOKEN") String token,
            @Query("uid") String uid,
            @Query("fid") String fid
    );

    @POST("user/follow")
    Call<BaseResponse> follow(
            @Header("TOKEN") String token,
            @Query("uid") String uid,
            @Query("fid") String fid
    );

    @POST("user/unfollow")
    Call<BaseResponse> unFollow(
            @Header("TOKEN") String token,
            @Query("uid") String uid,
            @Query("fid") String fid
    );

    @GET("landscape/selectPage")
    Call<LandscapeResponse> getLandscapes(
            @Header("TOKEN") String token,
            @Query("page") String page,
            @Query("limit") String limit,
            @Query("uid") String uid,
            @Query("sort") String sort
    );

    @GET("landscape/selectPage")
    Call<LandscapeResponse> getLandscapesSearch(
            @Header("TOKEN") String token,
            @Query("page") String page,
            @Query("limit") String limit,
            @Query("uid") String uid,
            @Query("keyword") String keyword,
            @Query("sort") String sort
    );

    @GET("landscape/selectData")
    Call<LandscapeDetailResponse> getLandscapeDetail(
            @Query("uid") String uid,
            @Query("id") String fid,
            @Header("TOKEN") String token
    );

    // 发送评论请求
    @POST("landscape/addComment")
    Call<AddCommentResponse> addComment(
            @Query("uid") String uid,
            @Query("fid") String fid,
            @Header("TOKEN") String token,
            @Body AddCommentRequest addCommentRequest
    );

    @GET("landscape/selectPageComment")
    Call<CommentListResponse> getLandscapeComments(
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("fid") String fid,
            @Header("TOKEN") String token
    );

    @GET("landscape/addScore")
    Call<Void> addScore(
            @Query("uid") String uid,
            @Query("fid") String fid,
            @Query("score") float score,
            @Header("TOKEN") String token
    );

        @GET("landscape/addCollect")
        Call<BaseResponse> addCollect(
                @Query("uid") String uid,
                @Query("fid") String fid,
                @Header("TOKEN") String token
        );

        @GET("landscape/delCollect")
        Call<BaseResponse> delCollect(
                @Query("uid") String uid,
                @Query("fid") String fid,
                @Header("TOKEN") String token
        );

    @GET("landscape/addPraise")
    Call<BaseResponse> addLPraise(
            @Query("uid") String uid,
            @Query("fid") String fid,
            @Header("TOKEN") String token
    );

    @GET("landscape/delPraise")
    Call<BaseResponse> delLPraise(
            @Query("uid") String uid,
            @Query("fid") String fid,
            @Header("TOKEN") String token
    );

    @GET("diary/delete")
    Call<BaseResponse> delDiary(
            @Header("TOKEN") String token,
            @Query("fid") String fid
    );

    @GET("/user/selectData")
    Call<UserResponse> getUserInfo(
            @Query("id") String id,
            @Query("uid") String uid,
            @Header("TOKEN") String token
    );

    @POST("/user/editUserInfo")
    Call<ResponseBody> editUserInfo(@Header("TOKEN") String token, @Body UserInfoRequest request);



}
