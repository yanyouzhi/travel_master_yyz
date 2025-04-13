package com.example.travel_master_yyz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.travel_master_yyz.adapter.DiaryAdapter;
import com.example.travel_master_yyz.adapter.UserDiaryAdapter;
import com.example.travel_master_yyz.dao.SessionManager;
import com.example.travel_master_yyz.view_model.CommunityViewModel;
import com.example.travel_master_yyz.view_model.UserCommunityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UserDiaryActivity extends BaseActivity {

    private UserCommunityViewModel viewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    String id,token,search_id;

    @Override
    protected void onResume() {
        super.onResume();
        // 重新请求数据，刷新 RecyclerView

        id = new SessionManager(UserDiaryActivity.this).getId();
        token = new SessionManager(UserDiaryActivity.this).getToken();
        Intent intent = getIntent();
        search_id = intent.getStringExtra("search_Did");
        viewModel.fetchDiaryPosts(progressBar,1, 50, id, search_id, token);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_diary);
        Intent intent = getIntent();
        search_id = intent.getStringExtra("search_Did");
        init();
        SessionManager sessionManager = new SessionManager(UserDiaryActivity.this);
        id = sessionManager.getId();
        token = sessionManager.getToken();
        if(id.equals(search_id)){
            fab.setVisibility(View.VISIBLE);
        }else{
            fab.setVisibility(View.GONE);
        }


        //todo:viewModel操作
        viewModel = new ViewModelProvider(this).get(UserCommunityViewModel.class);
        viewModel.fetchDiaryPosts(progressBar,1, 50, id, search_id, token);
        viewModel.getDiaryPosts().observe(UserDiaryActivity.this, posts -> {
            recyclerView.setAdapter(new UserDiaryAdapter(UserDiaryActivity.this, posts));
        });

        //todo：悬浮按钮操作
        fab.setOnClickListener(v -> {
            // 跳转到发布动态界面（自行实现）
            Intent intent1 = new Intent(UserDiaryActivity.this, PostActivity.class);
            startActivity(intent1);
        });
    }

    public void init(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserDiaryActivity.this));
        progressBar = findViewById(R.id.progress_bar);
        fab = findViewById(R.id.fab_add);
    }
}