package com.example.travel_master_yyz;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travel_master_yyz.api.ApiResponse;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.RegisterRequest;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText inputEmail, inputPhone, inputVerifyCode, inputPassword, inputConfirmPassword;
    private TextView tvGetVerificationCode,backToLogin,tv_agreement,tv_policy;
    private Button btnRegister;
    private TextInputLayout passwordLayout;
    private boolean isPasswordVisible = false; // 密码是否可见
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        tvGetVerificationCode.setOnClickListener(view -> sendVerifyCode());
        btnRegister.setOnClickListener(view -> registerUser());
        passwordLayout.setEndIconOnClickListener(view -> togglePasswordVisibility());
        jumpAgreement();
    }

    private void init(){
        inputEmail = findViewById(R.id.input_email);
        inputPhone = findViewById(R.id.input_phone);
        inputVerifyCode = findViewById(R.id.input_verification_code);
        inputPassword = findViewById(R.id.input_password);
        inputConfirmPassword = findViewById(R.id.input_confirm_password);
        tvGetVerificationCode = findViewById(R.id.tv_get_verification_code);
        btnRegister = findViewById(R.id.btn_register);
        passwordLayout = findViewById(R.id.til_passwordLayout);
        backToLogin = findViewById(R.id.tv_login);
        tv_agreement = findViewById(R.id.tv_xieyi1);
        tv_policy = findViewById(R.id.tv_xieyi2);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //todo:发送验证码
    private void sendVerifyCode() {
        String email = inputEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<ApiResponse> call = apiService.sendVerifyCode(email);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "网络错误：" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // todo:注册用户
    private void registerUser() {
        String email = inputEmail.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String verifyCode = inputVerifyCode.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirmPassword = inputConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(verifyCode)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(email, phone, password, verifyCode);
        Call<ApiResponse> call = apiService.registerUser(request);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish(); // 关闭当前页面
                } else {
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "网络错误：" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // todo:切换密码可见/隐藏
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            inputPassword.setInputType(129);
            passwordLayout.setEndIconDrawable(R.drawable.baseline_visibility_off_24);
        } else {
            inputPassword.setInputType(145);
            passwordLayout.setEndIconDrawable(R.drawable.baseline_visibility_24);
        }
        isPasswordVisible = !isPasswordVisible;
        inputPassword.setSelection(inputPassword.getText().length()); // 让光标保持在最后
    }

    //todo:点击跳转协议或者政策
    public void jumpAgreement(){
        tv_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,AgreetmentActivity.class);
                intent.putExtra("isAgreement", true);
                startActivity(intent);
            }
        });

        tv_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,AgreetmentActivity.class);
                intent.putExtra("isAgreement", false);
                startActivity(intent);
            }
        });
    }
}

