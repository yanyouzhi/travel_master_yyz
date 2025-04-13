package com.example.travel_master_yyz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.travel_master_yyz.adapter.LandscapeAdapter;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.LandscapeResponse;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.dao.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindLandscapeActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private LandscapeAdapter adapter;
    private String token,id,key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_landscape);
        SessionManager sessionManager = new SessionManager(FindLandscapeActivity.this);
        token = sessionManager.getToken();
        id = sessionManager.getId();
        Intent intent = getIntent();
        if (intent != null) {
            key = intent.getStringExtra("key");
        }
        recyclerView = findViewById(R.id.rc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadLandscapes();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindLandscapeActivity.this, HomeActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    private void loadLandscapes() {
        // 显示加载动画
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<LandscapeResponse> call = apiService.getLandscapesSearch(token, "1", "20", id, key,"");

        call.enqueue(new Callback<LandscapeResponse>() {
            @Override
            public void onResponse(Call<LandscapeResponse> call, Response<LandscapeResponse> response) {
                if (response.body() != null && response.body().getData() != null) {
                    List<LandscapeResponse.Landscape> landscapes = response.body().getData().getRecords();
                    adapter = new LandscapeAdapter(FindLandscapeActivity.this,landscapes);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(FindLandscapeActivity.this, "数据加载失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LandscapeResponse> call, Throwable t) {
                Toast.makeText(FindLandscapeActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}