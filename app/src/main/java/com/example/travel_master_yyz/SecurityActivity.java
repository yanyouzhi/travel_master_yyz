package com.example.travel_master_yyz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.api.UserResponse;
import com.example.travel_master_yyz.dao.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecurityActivity extends AppCompatActivity {

    TextView phone,email,time,loginOut;
    String token,id;

    LinearLayout ly1,ly2,ly3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        SessionManager sessionManager = new SessionManager(SecurityActivity.this);
        id = sessionManager.getId();
        token = sessionManager.getToken();
        initView();
        loadUserInfo();
        ly1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecurityActivity.this, ForgetPasswordActivity.class);
                intent.putExtra("isChange", 0);
                startActivity(intent);
            }
        });

        ly2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecurityActivity.this, ChangePhoneActivity.class);
                startActivity(intent);
            }
        });

        ly3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecurityActivity.this, ChangeEmailActivity.class);
                startActivity(intent);
            }
        });
        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.clearSession();
                Intent intent = new Intent(SecurityActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }

    public void initView(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        phone = findViewById(R.id.tv_phone);
        email = findViewById(R.id.tv_email);
        time = findViewById(R.id.time);
        loginOut = findViewById(R.id.out);
        ly1 = findViewById(R.id.ly1);
        ly2 = findViewById(R.id.ly2);
        ly3 = findViewById(R.id.ly3);
    }

    private void loadUserInfo() {
        ApiService userApi = RetrofitClient.getInstance().create(ApiService.class);
        Call<UserResponse> call = userApi.getUserInfo(id, id, token);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();
                    if (userResponse.getCode() == 200) {
                        updateUI(userResponse.getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // 处理请求失败
            }
        });
    }

    public String maskMiddle(String text, int start, int end) {
        if (text == null || text.length() < end) return text; // 防止越界
        StringBuilder maskedText = new StringBuilder(text);
        for (int i = start; i < end; i++) {
            maskedText.setCharAt(i, '*');
        }
        return maskedText.toString();
    }

    public String maskEmail(String email) {
        return email.replaceAll("(?<=.{3}).(?=.*@)", "*");
    }

    private void updateUI(UserResponse.UserData userData) {
        phone.setText( maskMiddle(userData.getPhone(), 3, 7));
        email.setText(maskEmail(userData.getEmail()));
        time.setText("上次登录时间："+ userData.getTime());
    }

}