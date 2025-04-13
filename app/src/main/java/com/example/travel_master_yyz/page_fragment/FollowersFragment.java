package com.example.travel_master_yyz.page_fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.travel_master_yyz.R;
import com.example.travel_master_yyz.adapter.FollowAdapter;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.FollowDataResponse;
import com.example.travel_master_yyz.api.FollowRecord;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.dao.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersFragment extends Fragment {
    private RecyclerView recyclerView;
    RetrofitClient retrofitClient;
    private FollowAdapter adapter;
    private List<FollowRecord> recordList = new ArrayList<>();
    ApiService api;
    private String id,token; // 动态设置
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);
        SessionManager sessionManager = new SessionManager(getContext());
        id = sessionManager.getId();
        token = sessionManager.getToken();
        api = RetrofitClient.getInstance().create(ApiService.class);
        loadData(1, 30);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FollowAdapter(recordList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void loadData(int page, int limit) {
        api.getFollow(token,id, page, limit)
                .enqueue(new Callback<FollowDataResponse>() {
                    @Override
                    public void onResponse(Call<FollowDataResponse> call, Response<FollowDataResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<FollowRecord> records = response.body().getData().getRecords();
                            recordList.clear();
                            recordList.addAll(records);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<FollowDataResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "加载失败：" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}