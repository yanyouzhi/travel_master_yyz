package com.example.travel_master_yyz.page_fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.travel_master_yyz.EditInfoActivity;
import com.example.travel_master_yyz.R;
import com.example.travel_master_yyz.SecurityActivity;
import com.example.travel_master_yyz.UserDiaryActivity;
import com.example.travel_master_yyz.adapter.MineDiaryAdapter;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.DiaryPost;
import com.example.travel_master_yyz.api.DiaryResponse;
import com.example.travel_master_yyz.api.MineDiaryResponse;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.api.UserResponse;
import com.example.travel_master_yyz.dao.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MineFragment extends Fragment {

    private TextView tvUsername, tvDesc, tvJob, tvLocation, tvFollow,tvEdit,tvSex,tvLike,tvSee,tvFav,tvCollect;
    private ImageView ivAvatar;
    private LinearLayout ly1,ly2,ly3;
    ProgressBar progressBar;
    MineDiaryAdapter diaryAdapter;
    String token,id;


    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        init(view);
        loadUserInfo();
        loadDiaries();
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                startActivity(intent);
            }
        });

        ly1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SecurityActivity.class);
                startActivity(intent);
            }
        });

        tvSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserDiaryActivity.class);
                intent.putExtra("search_Did", id);
                startActivity(intent);
            }
        });
        return view;
    }

    public void init(View view){
        progressBar = view.findViewById(R.id.pb);
        tvEdit = view.findViewById(R.id.tv_edit);
        tvUsername = view.findViewById(R.id.tv_username);
        tvFav = view.findViewById(R.id.tv_collect_num);
        tvCollect = view.findViewById(R.id.tv_favor_user_num);
        tvSex = view.findViewById(R.id.tv_sex);
        tvLike = view.findViewById(R.id.tv_like);
        tvDesc = view.findViewById(R.id.tv_desc);
        tvJob = view.findViewById(R.id.tv_job);
        tvLocation = view.findViewById(R.id.tv_location);
        ly1 = view.findViewById(R.id.tv_account_security);
        tvFollow = view.findViewById(R.id.tv_follow);
        tvSee = view.findViewById(R.id.tv_seeDiary);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        SessionManager sessionManager = new SessionManager(getContext());
        id = sessionManager.getId();
        RecyclerView recyclerView = view.findViewById(R.id.two_community_recylerview);
        diaryAdapter = new MineDiaryAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(diaryAdapter);
        //Log.i("id",id);
        token = sessionManager.getToken();
    }

    private void loadUserInfo() {
        ApiService userApi = RetrofitClient.getInstance().create(ApiService.class);
        Call<UserResponse> call = userApi.getUserInfo(id, id, token);

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

    private void loadDiaries() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService diaryService = RetrofitClient.getInstance().create(ApiService.class);
        diaryService.getDiaries(1, 2, id,  id,token).enqueue(new Callback<MineDiaryResponse>() {
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


    private void updateUI(UserResponse.UserData userData) {
        tvUsername.setText(userData.getName());
        tvDesc.setText(userData.getDescription());
        tvJob.setText(userData.getOccupation() + " | ");
        tvLocation.setText(userData.getAddr());
        tvFav.setText("已收藏" + String.valueOf(userData.getCollect_landscape_number()) + "个景点");
        tvCollect.setText("已关注" + String.valueOf(userData.getPraise()) + "个用户");
        tvSex.setText(userData.getSex() == 1 ? "♂" : "♀");
        tvLike.setText(userData.getPraise() + " 获赞");
        tvFollow.setText(userData.getCollectMy() + " 关注");

        // 加载头像（Base64 转 Bitmap）
        if (userData.getPhoto() != null && !userData.getPhoto().isEmpty()) {

            Glide.with(this)
                    .load(userData.getPhoto())
                    .transform(new CircleCrop()) // 设置圆形
                    .into(ivAvatar);
        }
    }
}