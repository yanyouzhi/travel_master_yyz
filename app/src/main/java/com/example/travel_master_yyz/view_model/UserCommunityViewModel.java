package com.example.travel_master_yyz.view_model;

import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.model_mvvm.DiaryPost;
import com.example.travel_master_yyz.model_mvvm.CommunityResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCommunityViewModel extends ViewModel {
    private MutableLiveData<List<DiaryPost>> diaryPosts = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> refreshDataTrigger = new MutableLiveData<>();

    private ApiService apiService;

    public UserCommunityViewModel() {
        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    public LiveData<List<DiaryPost>> getDiaryPosts() {
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

    public void fetchDiaryPosts(ProgressBar progressBar,int page, int limit, String uid, String fid, String token) {
        progressBar.setVisibility(View.VISIBLE);
        isLoading.setValue(true);
        apiService.getUserCommunityPosts(page, limit, uid, fid, token).enqueue(new Callback<CommunityResponse>() {
            @Override
            public void onResponse(Call<CommunityResponse> call, Response<CommunityResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    diaryPosts.setValue(response.body().getData().getRecords());
                }
            }

            @Override
            public void onFailure(Call<CommunityResponse> call, Throwable t) {
                isLoading.setValue(false);
            }
        });
    }
}


