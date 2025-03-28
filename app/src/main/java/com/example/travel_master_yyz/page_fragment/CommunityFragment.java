package com.example.travel_master_yyz.page_fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.travel_master_yyz.PostActivity;
import com.example.travel_master_yyz.R;
import com.example.travel_master_yyz.adapter.DiaryAdapter;
import com.example.travel_master_yyz.dao.SessionManager;
import com.example.travel_master_yyz.view_model.CommunityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CommunityFragment extends Fragment {
    private CommunityViewModel viewModel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FloatingActionButton fab;
    String id,token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        init(view);
        SessionManager sessionManager = new SessionManager(getContext());
        id = sessionManager.getId();
        token = sessionManager.getToken();


        //todo:viewModel操作
        viewModel = new ViewModelProvider(this).get(CommunityViewModel.class);
        viewModel.fetchNewCommunityPosts(progressBar,1, 50, id,  token);
        viewModel.getDiaryPosts().observe(getViewLifecycleOwner(), posts -> {
            recyclerView.setAdapter(new DiaryAdapter(requireContext(), posts));
        });

        //todo：悬浮按钮操作
        fab.setOnClickListener(v -> {
            // 跳转到发布动态界面（自行实现）
            Intent intent = new Intent(getActivity(), PostActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 重新请求数据，刷新 RecyclerView
        String id = new SessionManager(getContext()).getId();
        String token = new SessionManager(getContext()).getToken();
        viewModel.fetchNewCommunityPosts(progressBar,1, 50, id,  token);
    }


    public void init(View view){
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = view.findViewById(R.id.progress_bar);
        fab = view.findViewById(R.id.fab_add);
    }
}
