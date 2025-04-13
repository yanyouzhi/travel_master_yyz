package com.example.travel_master_yyz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.travel_master_yyz.adapter.SearchHistoryAdapter;
import com.example.travel_master_yyz.dao.SearchHistoryHelper;
import com.example.travel_master_yyz.dao.SessionManager;

import java.util.List;

public class SearchActivity extends BaseActivity {
    private TextView send;
    private EditText edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SessionManager sessionManager = new SessionManager(SearchActivity.this);
        String token = sessionManager.getToken();
        String id = sessionManager.getId();
        SearchHistoryHelper helper = new SearchHistoryHelper(this);
        List<String> historyList = helper.getSearchHistory(id);
        edt = findViewById(R.id.actv_search);
        send = findViewById(R.id.send);

        RecyclerView recyclerView = findViewById(R.id.search_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SearchHistoryAdapter adapter = new SearchHistoryAdapter(this, historyList, content -> {
            // 点击历史记录条目，可用于回填搜索框等操作
            edt.setText(content);
        });
        recyclerView.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchHistoryHelper helper = new SearchHistoryHelper(SearchActivity.this);
                helper.insertSearch(id, edt.getText().toString().trim());
                Intent intent = new Intent(SearchActivity.this, FindLandscapeActivity.class);
                intent.putExtra("key", edt.getText().toString().trim());  // 传递动态 ID
                startActivity(intent);
            }
        });

    }
}