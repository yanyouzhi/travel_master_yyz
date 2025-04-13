package com.example.travel_master_yyz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.travel_master_yyz.adapter.HistoryAdapter;
import com.example.travel_master_yyz.dao.LandscapeCacheHelper;
import com.example.travel_master_yyz.dao.LandscapeCacheItem;
import com.example.travel_master_yyz.dao.SessionManager;

import java.util.List;

public class HistoryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private LandscapeCacheHelper cacheHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        SessionManager sessionManager = new SessionManager(HistoryActivity.this);
        String token = sessionManager.getToken();
        String id = sessionManager.getId();

        recyclerView = findViewById(R.id.rc);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cacheHelper = new LandscapeCacheHelper(this);
        List<LandscapeCacheItem> historyList = cacheHelper.getAllLandscapeItems(id);

        adapter = new HistoryAdapter(this, historyList);
        recyclerView.setAdapter(adapter);

        Log.d("CacheDebug", "获取缓存记录数 = " + historyList.size());

    }
}
