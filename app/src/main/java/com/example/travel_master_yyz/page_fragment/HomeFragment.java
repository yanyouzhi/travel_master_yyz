package com.example.travel_master_yyz.page_fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.travel_master_yyz.R;
import com.example.travel_master_yyz.adapter.HomeLandscapeAdapter;
import com.example.travel_master_yyz.adapter.ImageAdapter;
import com.example.travel_master_yyz.adapter.LandscapeAdapter;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.LandscapeResponse;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.dao.DataBean;
import com.example.travel_master_yyz.dao.SessionManager;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private Banner banner;
    private View view;

    private RecyclerView recyclerView;
    private HomeLandscapeAdapter adapter;
    private String token,id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        banner = view.findViewById(R.id.banner);
        ImageAdapter imageAdapter = new ImageAdapter(DataBean.getTestData2());
        banner.setAdapter(imageAdapter)
                .addBannerLifecycleObserver(getActivity())
                .setIndicator(new CircleIndicator(getActivity()));
        recyclerView = view.findViewById(R.id.re);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        SessionManager sessionManager = new SessionManager(getContext());
        token = sessionManager.getToken();
        id = sessionManager.getId();
        loadLandscapes();

        return view;
    }

    private void loadLandscapes() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<LandscapeResponse> call = apiService.getLandscapes(token, "1", "5", id, "1");

        call.enqueue(new Callback<LandscapeResponse>() {
            @Override
            public void onResponse(Call<LandscapeResponse> call, Response<LandscapeResponse> response) {
                if (response.body() != null && response.body().getData() != null) {
                    List<LandscapeResponse.Landscape> landscapes = response.body().getData().getRecords();
                    adapter = new HomeLandscapeAdapter(getContext(),landscapes);
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