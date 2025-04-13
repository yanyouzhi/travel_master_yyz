package com.example.travel_master_yyz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.travel_master_yyz.adapter.CollectAdapter;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.CollectResponse;
import com.example.travel_master_yyz.api.LandscapeCollect;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.dao.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectLandscapeActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private CollectAdapter adapter;
    private List<LandscapeCollect> collectList = new ArrayList<>();
    ApiService api;
    Toolbar toolbar;
    private String id,token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_landscape);
        SessionManager sessionManager = new SessionManager(CollectLandscapeActivity.this);
        id = sessionManager.getId();
        token = sessionManager.getToken();
        api = RetrofitClient.getInstance().create(ApiService.class);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getCollectData();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CollectAdapter(collectList);
        recyclerView.setAdapter(adapter);

    }

    private void getCollectData(){
        api = RetrofitClient.getInstance().create(ApiService.class);

        api.getCollectList(token, "1", "30", id).enqueue(new Callback<CollectResponse>() {
            @Override
            public void onResponse(Call<CollectResponse> call, Response<CollectResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LandscapeCollect> list = response.body().getData().getRecords();
                    collectList.addAll(list);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(CollectLandscapeActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CollectResponse> call, Throwable t) {
                Toast.makeText(CollectLandscapeActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }
}