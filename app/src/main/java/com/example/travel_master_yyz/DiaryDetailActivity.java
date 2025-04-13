package com.example.travel_master_yyz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.travel_master_yyz.adapter.CommentAdapter;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.BaseResponse;
import com.example.travel_master_yyz.api.CommentRequest;
import com.example.travel_master_yyz.api.CommentResponse;
import com.example.travel_master_yyz.api.DiaryDetailResponse;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.dao.SessionManager;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiaryDetailActivity extends BaseActivity {

    private TextView tvContent, tvLikeDetail, tvSend,tvTime,tvCommentNum,tvName;
    private ImageView ivImage, imgLike,imgAva,imgDel;
    private EditText etComment;
    private String diaryId, diaryUid, token,postUerId,loginId;
    private boolean isLiked = false;
    private int likeCount = 0;
    private int commentNum;
    private ApiService diaryApi;
    private CommentAdapter commentAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);

        SessionManager sessionManager = new SessionManager(DiaryDetailActivity.this);
        token = sessionManager.getToken();
        loginId = sessionManager.getId();

        Intent intent = getIntent();
        diaryUid = intent.getStringExtra("diary_uid"); //发嵩帖子用户的id
        diaryId = intent.getStringExtra("diary_id");

        init();

        diaryApi = RetrofitClient.getInstance().create(ApiService.class);

        fetchDiaryDetail();


//        Log.i("canshu",diaryId);
//        Log.i("canshu2",diaryUid);

        fetchComments();

        tvSend.setOnClickListener(v -> postCommentDetail());

// 设置点赞点击事件
        imgLike.setOnClickListener(v -> {
            if (isLiked) {
                cancelLike();
            } else {
                addLike();
            }
        });

        imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delD();
            }
        });

        jumpUserInfo();
    }

    private void init(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        tvContent = findViewById(R.id.tv_content);
        tvLikeDetail = findViewById(R.id.tv_like_detail);
        ivImage = findViewById(R.id.iv_image);
        imgLike = findViewById(R.id.img_like);
        etComment = findViewById(R.id.et_comment);
        tvTime = findViewById(R.id.tv_time);
        tvSend = findViewById(R.id.tv_send);
        tvCommentNum = findViewById(R.id.tv_num_comment);
        tvName = findViewById(R.id.tv_username);
        imgAva = findViewById(R.id.img_editava);
        recyclerView = findViewById(R.id.detail_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter();
        recyclerView.setAdapter(commentAdapter);
        imgDel = findViewById(R.id.img_del);
    }

    private void jumpUserInfo(){
        imgAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiaryDetailActivity.this, UserInfoActivity.class);
                intent.putExtra("search_id", postUerId);
                startActivity(intent);
            }
        });
    }

    // todo:获取动态详情
    private void fetchDiaryDetail() {
        diaryApi.getDiaryDetail(token, diaryId, loginId).enqueue(new Callback<DiaryDetailResponse>() {
            @Override
            public void onResponse(Call<DiaryDetailResponse> call, Response<DiaryDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DiaryDetailResponse.Data data = response.body().getData();

                    // 设置文本信息
                    tvTime.setText(data.getTime()); // 发布时间
                    tvContent.setText(data.getDescription()); // 动态内容
                    tvLikeDetail.setText(String.valueOf(data.getPraise())); // 点赞数
                    tvCommentNum.setText(getString(R.string.tv_diary_detail_2)+"(" + String.valueOf(data.getComment())+")"); //评论数
                    commentNum = data.getComment();
                    postUerId = data.getUid();
                    tvName.setText(data.getName());
                    if (data.getPhoto() != null && !data.getPhoto().isEmpty()) {
                        Glide.with(DiaryDetailActivity.this)
                                .load(data.getPhoto())
                                .transform(new CircleCrop()) // 设置圆形
                                .into(imgAva);
                    }else{
                        Glide.with(DiaryDetailActivity.this)
                                .load("https://yanyouzhi8758.oss-cn-guangzhou.aliyuncs.com/%E9%BB%91%E7%8C%AB.jpg")
                                .transform(new CircleCrop())
                                .into(imgAva);
                    }
                    Log.i("json", new Gson().toJson(response.body()));



                    Log.i("string",loginId + "==" + postUerId);
                    if(loginId.equals(postUerId)){
                        imgDel.setVisibility(View.VISIBLE);
                    }else{
                        imgDel.setVisibility(View.GONE);
                    }


                    // 处理 Base64 图片
                    if (data.getImage() != null && !data.getImage().isEmpty()) {
                        Glide.with(DiaryDetailActivity.this)
                                .load(data.getImage())
                                .into(ivImage);
                        ivImage.setVisibility(View.VISIBLE);
                    } else {
                        ivImage.setVisibility(View.GONE);
                    }

                    // 处理点赞状态
                    Log.i("sss",String.valueOf(data.getPraise_my()));
                    isLiked = data.getPraise_my() > 0; // 获取点赞状态（0 = 未点赞，>0 = 已点赞）
                    updateLikeUI();
                } else {
                    Toast.makeText(DiaryDetailActivity.this, "获取详情失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DiaryDetailResponse> call, Throwable t) {
                Toast.makeText(DiaryDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //todo:点赞红灰切换
    private void updateLikeUI() {
        imgLike.setSelected(isLiked);
    }

    // todo：获取评论列表
    private void fetchComments() {
        diaryApi.getComments(token, 1, 20, diaryId).enqueue(new Callback<CommentResponse>() {

            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e("API_ERROR", "请求失败，HTTP 状态码：" + response.code());
                    return;
                }

                CommentResponse commentResponse = response.body();

                if (commentResponse.getData() == null || commentResponse.getData().getRecords() == null) {
                    Log.e("API_ERROR", "返回数据为空：" + new Gson().toJson(commentResponse));
                    return;
                }

                List<CommentResponse.CommentData> comments = commentResponse.getData().getRecords();
                if (comments.isEmpty()) {
                    Log.e("API_ERROR", "没有评论数据");
                } else {
                    commentAdapter.setComments(DiaryDetailActivity.this,comments);
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Toast.makeText(DiaryDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //todo：点赞逻辑
    private void addLike() {

        RetrofitClient.getInstance().create(ApiService.class)
                .addPraise(token, loginId, diaryId)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null && response.body().getCode() == 200) {
                            isLiked = true;
                            // 获取当前点赞数并 +1
                            int currentPraiseCount = Integer.parseInt(tvLikeDetail.getText().toString());
                            tvLikeDetail.setText(String.valueOf(currentPraiseCount + 1));

                            updateLikeUI();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.e("Error", "点赞失败：" + t.getMessage());
                    }
                });
    }

    //todo：取消赞逻辑
    private void cancelLike() {
        RetrofitClient.getInstance().create(ApiService.class)
                .delPraise(token, loginId, diaryId)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null && response.body().getCode() == 200) {
                            isLiked = false;
                            // -1
                            int currentPraiseCount = Integer.parseInt(tvLikeDetail.getText().toString());
                            tvLikeDetail.setText(String.valueOf(currentPraiseCount - 1));
                            updateLikeUI();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.e("Error", "取消点赞失败：" + t.getMessage());
                    }
                });
    }


    private void delD() {
        RetrofitClient.getInstance().create(ApiService.class)
                .delDiary(token,diaryId)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null && response.body().getCode() == 200) {
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.e("Error", "删除失败：" + t.getMessage());
                    }
                });
    }



    //todo:发送评论
    // 发表评论
    private void postCommentDetail() {
        String commentText = etComment.getText().toString().trim();
        if (commentText.isEmpty()) {
            Toast.makeText(this, "评论不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        CommentRequest request = new CommentRequest(Collections.singletonList(commentText));
        diaryApi.postComment(token, loginId, diaryId, request).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    Toast.makeText(DiaryDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    etComment.setText(""); // 清空输入框
                    tvCommentNum.setText("全部评论(" + String.valueOf(commentNum+1)+")"); //评论数
                    fetchComments();// 发表评论后，刷新评论列表
                } else {
                    Toast.makeText(DiaryDetailActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(DiaryDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

}