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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.travel_master_yyz.adapter.LandscapeCommentAdapter;
import com.example.travel_master_yyz.api.AddCommentRequest;
import com.example.travel_master_yyz.api.AddCommentResponse;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.BaseResponse;
import com.example.travel_master_yyz.api.CommentListResponse;
import com.example.travel_master_yyz.api.LandscapeData;
import com.example.travel_master_yyz.api.LandscapeDetailData;
import com.example.travel_master_yyz.api.LandscapeDetailResponse;
import com.example.travel_master_yyz.api.LandscapeResponse;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.dao.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandscapeDetailActivity2 extends AppCompatActivity {



    private RecyclerView recyclerView;
    private LandscapeCommentAdapter commentAdapter;
    private List<CommentListResponse.Comment> commentList = new ArrayList<>();
    private ImageView imageView,ivPraise,collect;
    private TextView textViewName, textViewDescription, textViewPraise, textViewScore, textViewComment,
            sendScore,sendComment,tvCollect,tvPraiseCount;
    private RatingBar ratingBar;
    private EditText editComment;
    private String uid;
    private String fid;
    private String token;
    private  ApiService apiService;
    private boolean isCollected,isPraised,isScored;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landscape_detail2);
        SessionManager sessionManager = new SessionManager(this);
        uid = sessionManager.getId();
        token = sessionManager.getToken();
        init();
        loadLandscapeDetail();
        fetchComments();


        sendComment.setOnClickListener(v -> {
            String commentText = editComment.getText().toString().trim();
            sendComment(commentText);
        });

        sendScore.setOnClickListener(view -> submitScore());

        collect.setOnClickListener(v -> {
            if (isCollected) {
                cancelCollect(uid, fid);
            } else {
                addCollect(uid, fid);
            }
        });

        ivPraise.setOnClickListener(v -> {
            if (isPraised) {
                cancelPraise(uid, fid);
            } else {
                addPraise(uid, fid);
            }
        });

    }

    public void init(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        imageView = findViewById(R.id.imageView);
        textViewName = findViewById(R.id.textViewName2);
        textViewDescription = findViewById(R.id.textViewDescription2);
        textViewPraise = findViewById(R.id.textViewPraise2);
        textViewScore = findViewById(R.id.textViewScore2);
        textViewComment = findViewById(R.id.detail_comment);
        ratingBar = findViewById(R.id.ratingBar);
        editComment = findViewById(R.id.editTextComment);
        sendComment = findViewById(R.id.btnSubmit);
        sendScore = findViewById(R.id.btnSubmitScore);
        tvCollect = findViewById(R.id.textViewCollect);
        ivPraise = findViewById(R.id.btnLike);
        tvPraiseCount = findViewById(R.id.textViewPraise2);
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        // 获取上个页面传来的景点ID
        Intent intent = getIntent();
        if (intent != null) {
            fid = intent.getStringExtra("fid");
        }
        collect = findViewById(R.id.btnCollect);

        recyclerView = findViewById(R.id.recyclerViewComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new LandscapeCommentAdapter(LandscapeDetailActivity2.this,commentList);
        recyclerView.setAdapter(commentAdapter);

    }

    private void fetchComments() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<CommentListResponse> call = apiService.getLandscapeComments(1, 100, fid, token);

        call.enqueue(new Callback<CommentListResponse>() {
            @Override
            public void onResponse(Call<CommentListResponse> call, Response<CommentListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    commentList.clear();
                    commentList.addAll(response.body().getData().getRecords());
                    commentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(LandscapeDetailActivity2.this, "获取评论失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommentListResponse> call, Throwable t) {
                Toast.makeText(LandscapeDetailActivity2.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //todo:获取景点详情
    private void loadLandscapeDetail() {
        Call<LandscapeDetailResponse> call = apiService.getLandscapeDetail(uid,fid,token);

        call.enqueue(new Callback<LandscapeDetailResponse>() {
            @Override
            public void onResponse(Call<LandscapeDetailResponse> call, Response<LandscapeDetailResponse> response) {
                Log.d("Retrofit", "onResponse: 请求返回，线程 = " + Thread.currentThread().getName());
                if (!isFinishing() && response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    updateUI(response.body().getData());
                } else {
                    Toast.makeText(LandscapeDetailActivity2.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LandscapeDetailResponse> call, Throwable t) {
                Log.e("API_ERROR", "网络请求失败: " + t.getMessage());
                Toast.makeText(LandscapeDetailActivity2.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //todo:发送评论
    private void sendComment(String commentText) {
        if (commentText.isEmpty()) {
            Toast.makeText(this, "评论不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        AddCommentRequest addCommentRequest = new AddCommentRequest(commentText);

        Call<AddCommentResponse> call = apiService.addComment(uid, fid, token, addCommentRequest);
        call.enqueue(new Callback<AddCommentResponse>() {
            @Override
            public void onResponse(Call<AddCommentResponse> call, Response<AddCommentResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    Toast.makeText(LandscapeDetailActivity2.this, "评论成功", Toast.LENGTH_SHORT).show();
                    loadLandscapeDetail();
                    fetchComments();
                    editComment.setText(" ");
                } else {
                    Toast.makeText(LandscapeDetailActivity2.this, "评论失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddCommentResponse> call, Throwable t) {
                Toast.makeText(LandscapeDetailActivity2.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateUI(LandscapeDetailData data) {

        textViewName.setText(data.getName());
        textViewDescription.setText(data.getDescription());
        textViewPraise.setText(String.valueOf(data.getPraise()));
        textViewScore.setText(String.valueOf(data.getAverageScore()) + " 分");
        textViewComment.setText("全部评论("+String.valueOf(data.getComment())+")");
        ratingBar.setRating(data.getAverageScore());

        isCollected = (data.getCollect_my() > 0);
        collect.setSelected(isCollected);
        if (isCollected) {
            tvCollect.setText("已收藏");
        } else {
            tvCollect.setText("收藏");
        }

        isPraised = (data.getPraise_my() > 0);  // 是否已点赞
        ivPraise.setSelected(isPraised);

        isScored = (data.getScore_my() > 0);
        if (isScored) {
            sendScore.setVisibility(View.GONE);
        } else {
            sendScore.setVisibility(View.VISIBLE);
        }

        if (data.getImage() != null && !data.getImage().isEmpty()) {
            Glide.with(LandscapeDetailActivity2.this)
                    .load(data.getImage())
                    .transform(new CircleCrop()) // 设置圆形
                    .into(imageView);
        }else{
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    private void addCollect(String uid, String fid) {
        RetrofitClient.getInstance().create(ApiService.class)
                .addCollect(uid, fid, token)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                            Log.d("Collect", "收藏成功");
                            Toast.makeText(LandscapeDetailActivity2.this, "收藏成功", Toast.LENGTH_SHORT).show();
                            collect.setSelected(true); // 变成已收藏
                            tvCollect.setText("已收藏");
                            isCollected = true;
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.e("Collect", "收藏失败: " + t.getMessage());
                        Toast.makeText(LandscapeDetailActivity2.this, "收藏失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cancelCollect(String uid, String fid) {
        RetrofitClient.getInstance().create(ApiService.class)
                .delCollect(uid, fid, token)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                            Log.d("Collect", "取消收藏成功");
                            Toast.makeText(LandscapeDetailActivity2.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                            collect.setSelected(false); // 变成未收藏
                            tvCollect.setText("收藏");
                            isCollected = false;
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.e("Collect", "取消收藏失败: " + t.getMessage());
                        Toast.makeText(LandscapeDetailActivity2.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //评分
    private void submitScore() {
        float score = ratingBar.getRating();
        Log.i("shuzi",String.valueOf(score));

        if (score == 0) {
            Toast.makeText(this, "请先评分！", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<Void> call = apiService.addScore(uid, fid, score, token);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loadLandscapeDetail();
                    Toast.makeText(LandscapeDetailActivity2.this, "评分提交成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LandscapeDetailActivity2.this, "评分提交失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LandscapeDetailActivity2.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPraise(String uid, String fid) {
        RetrofitClient.getInstance().create(ApiService.class)
                .addLPraise(uid, fid, token)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                            Log.d("Praise", "点赞成功");
                            Toast.makeText(LandscapeDetailActivity2.this, "点赞成功", Toast.LENGTH_SHORT).show();
                            ivPraise.setSelected(true);
                            tvPraiseCount.setText(String.valueOf(Integer.parseInt(tvPraiseCount.getText().toString()) + 1));
                            isPraised = true;
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.e("Praise", "点赞失败: " + t.getMessage());
                        Toast.makeText(LandscapeDetailActivity2.this, "点赞失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cancelPraise(String uid, String fid) {
        RetrofitClient.getInstance().create(ApiService.class)
                .delLPraise(uid, fid, token)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                            Log.d("Praise", "取消点赞成功");
                            Toast.makeText(LandscapeDetailActivity2.this, "取消点赞成功", Toast.LENGTH_SHORT).show();
                            ivPraise.setSelected(false);
                            tvPraiseCount.setText(String.valueOf(Integer.parseInt(tvPraiseCount.getText().toString()) - 1));
                            isPraised = false;
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.e("Praise", "取消点赞失败: " + t.getMessage());
                        Toast.makeText(LandscapeDetailActivity2.this, "取消点赞失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}