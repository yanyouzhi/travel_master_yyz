package com.example.travel_master_yyz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.travel_master_yyz.adapter.MineDiaryAdapter;
import com.example.travel_master_yyz.adapter.UserInfoDiaryAdapter;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.BaseResponse;
import com.example.travel_master_yyz.api.DiaryPost;
import com.example.travel_master_yyz.api.MineDiaryResponse;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.api.UserResponse;
import com.example.travel_master_yyz.dao.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends BaseActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    String token,search_id,id;
    TextView fa,name,desc,sex,job,location,phone,see,love;
    ImageView ava;
    RecyclerView recyclerView;

    ProgressBar progressBar;
    UserInfoDiaryAdapter diaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        SessionManager sessionManager = new SessionManager(UserInfoActivity.this);
        id = sessionManager.getId();
        token = sessionManager.getToken();
        Intent intent = getIntent();
        search_id = intent.getStringExtra("search_id");
        init();
        loadUserInfo();
        loadDiaries();
        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, UserDiaryActivity.class);
                intent.putExtra("search_Did", search_id);
                startActivity(intent);
            }
        });

    }

    private void init(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        fa = findViewById(R.id.tv_fa);
        name = findViewById(R.id.tv_username);
        desc = findViewById(R.id.tv_desc);
        sex = findViewById(R.id.tv_sex);
        job = findViewById(R.id.tv_job);
        location = findViewById(R.id.tv_location);
        phone = findViewById(R.id.tv_phone);
        see = findViewById(R.id.tv_seeDiary);
        ava = findViewById(R.id.iv_avatar);
        recyclerView = findViewById(R.id.recylerview_user);
        progressBar = findViewById(R.id.pb);
        diaryAdapter = new UserInfoDiaryAdapter(UserInfoActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserInfoActivity.this));
        recyclerView.setAdapter(diaryAdapter);
        if(search_id == id){
            fa.setVisibility(View.GONE);
        }else{
            fa.setVisibility(View.VISIBLE);
        }
    }

    private void loadDiaries() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService diaryService = RetrofitClient.getInstance().create(ApiService.class);
        diaryService.getDiaries(1, 6, id,  search_id ,token).enqueue(new Callback<MineDiaryResponse>() {
            @Override
            public void onResponse(Call<MineDiaryResponse> call, Response<MineDiaryResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<DiaryPost> diaryList = response.body().getData().getRecords();
                    diaryAdapter.setDiaryList(diaryList);
                }
            }

            @Override
            public void onFailure(Call<MineDiaryResponse> call, Throwable t) {
                Log.e("MineFragment", "获取动态失败", t);
            }
        });
    }


    private void loadUserInfo() {
        ApiService userApi = RetrofitClient.getInstance().create(ApiService.class);
        Call<UserResponse> call = userApi.getUserInfo(search_id, id, token);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();
                    if (userResponse.getCode() == 200) {
                        updateUI(userResponse.getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // 处理请求失败
            }
        });
    }


    private void unFollow() {
        RetrofitClient.getInstance().create(ApiService.class)
                .unFollow(token,search_id,id)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null && response.body().getCode() == 200) {
                            fa.setText(getString(R.string.tv_userInfo_1));
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.e("Error", "取消点赞失败：" + t.getMessage());
                    }
                });
    }

    private void addFollow() {
        RetrofitClient.getInstance().create(ApiService.class)
                .follow(token, search_id, id)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null && response.body().getCode() == 200) {
                            fa.setText(getString(R.string.tv_userInfo_2));
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.e("Error", "取消点赞失败：" + t.getMessage());
                    }
                });
    }

    private void updateUI(UserResponse.UserData userData) {
        name.setText(userData.getName());
        desc.setText(userData.getDescription());

        if(!TextUtils.isEmpty(userData.getOccupation())){
            job.setText("| " + userData.getOccupation());
        }else{
            job.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(userData.getAddr())){
            location.setText(userData.getAddr());
        }else{
            location.setVisibility(View.GONE);
        }
        sex.setText(userData.getSex() == 1 ? getString(R.string.tv_edit_8) : getString(R.string.tv_edit_9));
        phone.setText(userData.getEmail());
        fa.setText(userData.getCollect_my() == 0 ? getString(R.string.tv_userInfo_1) : getString(R.string.tv_userInfo_2));
        if(id.equals(userData.getId())){
            fa.setVisibility(View.GONE);
        }else {
            fa.setVisibility(View.VISIBLE);
        }

        fa.setOnClickListener(v -> {
            if (userData.getCollect_my() == 0 ) {
                addFollow();
            } else {
                unFollow();
            }
        });

        // 加载头像（Base64 转 Bitmap）
        if (userData.getPhoto() != null && !userData.getPhoto().isEmpty()) {
            Glide.with(this)
                    .load(userData.getPhoto())
                    .transform(new CircleCrop()) // 设置圆形
                    .into(ava);
        }else{
            Glide.with(this)
                    .load("https://yanyouzhi8758.oss-cn-guangzhou.aliyuncs.com/%E9%BB%91%E7%8C%AB.jpg")
                    .transform(new CircleCrop())
                    .into(ava);
        }
    }


}