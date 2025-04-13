package com.example.travel_master_yyz.page_fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.travel_master_yyz.R;
import com.example.travel_master_yyz.adapter.LandscapeAdapter;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.LandscapeResponse;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.dao.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SceneFragment extends Fragment {

    private RecyclerView recyclerView;
    private LandscapeAdapter adapter;
    private String token,id;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    public SceneFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLandscapes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scene, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        progressBar = view.findViewById(R.id.progressBar);
        //swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        SessionManager sessionManager = new SessionManager(getContext());
        token = sessionManager.getToken();
        id = sessionManager.getId();
        loadLandscapes();

        return view;
    }

    /**
     * todo:下拉刷新逻辑
     */
    private void refreshLandscapes() {
        loadLandscapes(); // 重新请求数据
    }
    private void loadLandscapes() {
        // 显示加载动画
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<LandscapeResponse> call = apiService.getLandscapes(token, "1", "20", id, "");

        call.enqueue(new Callback<LandscapeResponse>() {
            @Override
            public void onResponse(Call<LandscapeResponse> call, Response<LandscapeResponse> response) {
                progressBar.setVisibility(View.GONE); // 隐藏加载动画
                if (response.body() != null && response.body().getData() != null) {
                    List<LandscapeResponse.Landscape> landscapes = response.body().getData().getRecords();
                    adapter = new LandscapeAdapter(getContext(),landscapes);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), "数据加载失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LandscapeResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}