package com.example.travel_master_yyz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.travel_master_yyz.dao.SessionManager;

public class SplashActivity extends AppCompatActivity {

    TextView textView;
    LottieAnimationView lottieAnimationView;
    private boolean isNavigated = false; // 防止重复跳转
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        View rootView = findViewById(android.R.id.content); // 获取整个界面的根布局
        textView = findViewById(R.id.tv_splash);
        lottieAnimationView = findViewById(R.id.lottie);
        // 5秒后自动跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigateToNext();
            }
        }, 5000);

        // 设置点击跳过
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToNext();
            }
        });

    }

    private void navigateToNext() {
        if (isNavigated) return; // 防止多次跳转
        isNavigated = true;

        SessionManager sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish(); // 关闭当前页面
    }

}