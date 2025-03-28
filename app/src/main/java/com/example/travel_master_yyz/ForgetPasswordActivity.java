package com.example.travel_master_yyz;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travel_master_yyz.api.ApiResponse;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.ResetPasswordRequest;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    private TextInputEditText inputEmail, inputVerificationCode, inputPassword, inputConfirmPassword;
    private TextInputLayout tilPasswordLayout, tilConfirmPasswordLayout;
    private boolean isPasswordVisible = false, isConfirmPasswordVisible = false;
    private TextView tvGetVerificationCode, tvLogin;
    private Button btnReset;
    private ApiService apiService;
    private int bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        Intent intent = getIntent();
        bool = intent.getIntExtra("isChange",2);
        init();
    }

    private void init() {
        inputEmail = findViewById(R.id.input_email);
        inputVerificationCode = findViewById(R.id.input_verification_code);
        inputPassword = findViewById(R.id.input_password);
        inputConfirmPassword = findViewById(R.id.input_confirm_password);
        tvGetVerificationCode = findViewById(R.id.tv_get_verification_code);
        tvLogin = findViewById(R.id.tv_login);
        btnReset = findViewById(R.id.btn_register);
        tilPasswordLayout = findViewById(R.id.til_passwordLayout);

        if(bool == 1){
            tvLogin.setVisibility(View.VISIBLE);
        }else{
            tvLogin.setVisibility(View.GONE);
        }

        tilConfirmPasswordLayout = findViewById(R.id.til_confirmPasswordLayout);

        tvGetVerificationCode.setOnClickListener(v -> sendVerificationCode());

        btnReset.setOnClickListener(v -> resetPassword());

        tvLogin.setOnClickListener(v -> finish());

        // 密码可见性切换
        tilPasswordLayout.setEndIconOnClickListener(v -> togglePasswordVisibility(inputPassword, tilPasswordLayout));
        tilConfirmPasswordLayout.setEndIconOnClickListener(v -> togglePasswordVisibility(inputConfirmPassword, tilConfirmPasswordLayout));
    }

    //todo:发送验证码
    private void sendVerificationCode() {
        String email = inputEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.sendVerifyCode(email).enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == 200) {
                        Toast.makeText(ForgetPasswordActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i("tahh",t.getMessage());
                Toast.makeText(ForgetPasswordActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //todo:重置密码
    private void resetPassword() {
        String email = inputEmail.getText().toString().trim();
        String verificationCode = inputVerificationCode.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirmPassword = inputConfirmPassword.getText().toString().trim();

        if (email.isEmpty() || verificationCode.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        ResetPasswordRequest request = new ResetPasswordRequest(email, password, verificationCode);
        apiService.resetPassword(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == 200) {
                        Toast.makeText(ForgetPasswordActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(ForgetPasswordActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //todo:隐藏显示密码
    private void togglePasswordVisibility(TextInputEditText editText, TextInputLayout layout) {
        int inputType = editText.getInputType();
        boolean isPasswordVisible = (inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        if (isPasswordVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            layout.setEndIconDrawable(R.drawable.baseline_visibility_off_24); // 眼睛关闭图标
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            layout.setEndIconDrawable(R.drawable.baseline_visibility_24); // 眼睛打开图标
        }

        editText.setSelection(editText.getText().length()); // 保持光标位置
    }
}
