package com.example.travel_master_yyz.view_model;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.CommunityPost;
import com.example.travel_master_yyz.api.NewCommunityResponse;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.model_mvvm.DiaryPost;
import com.example.travel_master_yyz.model_mvvm.CommunityResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityViewModel extends ViewModel {
    private MutableLiveData<List<NewCommunityResponse.NewCommunityPost>> diaryPosts = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> refreshDataTrigger = new MutableLiveData<>();

    private ApiService apiService;

    public CommunityViewModel() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    public LiveData<List<NewCommunityResponse.NewCommunityPost>> getDiaryPosts() {
        return diaryPosts;
    }

    public LiveData<Boolean> getLoadingState() {
        return isLoading;
    }

    public LiveData<Boolean> getRefreshDataTrigger() {
        return refreshDataTrigger;
    }

    public void triggerDataRefresh() {
        refreshDataTrigger.setValue(true);
    }

    public void fetchNewCommunityPosts(ProgressBar progressBar, int page, int limit, String uid, String token) {
        progressBar.setVisibility(View.VISIBLE);
        isLoading.setValue(true);

        apiService.getUserCommunityPosts_2(page, limit, uid, token).enqueue(new Callback<NewCommunityResponse>() {
            @Override
            public void onResponse(Call<NewCommunityResponse> call, Response<NewCommunityResponse> response) {
                progressBar.setVisibility(View.GONE); // 隐藏加载动画
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<NewCommunityResponse.NewCommunityPost> posts = response.body().getData();
                    if (posts != null) {
                        diaryPosts.setValue(posts);
                    } else {
                        Log.e("Community1", "API 返回的 posts 数据为空");
                    }
                } else {
                    Log.i("Community1", "API 返回的数据为空");
                }
            }

            @Override
            public void onFailure(Call<NewCommunityResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                isLoading.setValue(false);
                Log.e("Community1", "API 请求失败", t);
            }
        });
    }

}


