package com.example.travel_master_yyz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.travel_master_yyz.dao.SessionManager;
import com.example.travel_master_yyz.dao.UserData;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.LoginRequest;
import com.example.travel_master_yyz.api.LoginResponse;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TextView tv_register,tv_forget,tv_agreement,tv_policy;
    Button login;
    TextInputLayout textInputLayoutPhone,textInputLayoutEmail,ly_pass_email,ly_pass_phone;
    TextInputEditText edt_phone,edt_email,edt_pass_phone,edt_pass_email;
    ViewFlipper viewFlipper;
    private boolean isPasswordVisible = false;  // 控制密码显示状态
    private boolean isPhoneLogin = true; // 默认使用手机号登录

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        init();
        setPasswordVisibility();
        jumpAgreement();
        jump_login_register_forget();

    }

    //TODO:tablayout和viewFillper相互绑定
    public void setTabLayout(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab != null) {
                    switch (tab.getPosition()) {
                        case 0:
                            viewFlipper.setDisplayedChild(0); // 手机号登录
                            break;
                        case 1:
                            viewFlipper.setDisplayedChild(1); // 邮箱登录
                            break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    //TODO:控件绑定
    public void init(){
        tabLayout = findViewById(R.id.tabLayout);
        viewFlipper = findViewById(R.id.viewFlipper);
        textInputLayoutPhone = findViewById(R.id.til_phone);
        textInputLayoutEmail =findViewById(R.id.til_email);
        ly_pass_email = findViewById(R.id.password_input_email_layout);
        ly_pass_phone = findViewById(R.id.password_input_layout);
        edt_phone = findViewById(R.id.tie_phone);
        edt_email = findViewById(R.id.tie_email);
        edt_pass_phone = findViewById(R.id.password_edit_text);
        edt_pass_email = findViewById(R.id.password_edit_text_email);
        tv_register = findViewById(R.id.tv_register);
        tv_forget = findViewById(R.id.tv_forget);
        tv_agreement = findViewById(R.id.tv_xieyi1);
        tv_policy = findViewById(R.id.tv_xieyi2);
        login = findViewById(R.id.btn_login);
        setTabLayout();
    }
    //todo:置密码输入框的图标点击事件（手机号和邮箱的密码）
    public void setPasswordVisibility(){
        ly_pass_phone.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(edt_pass_phone,ly_pass_phone);
            }
        });

        ly_pass_email.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility(edt_pass_email,ly_pass_email);
            }
        });
    }
    //todo；切换密码显示/隐藏，并修改 icon
    private void togglePasswordVisibility(TextInputEditText editText, TextInputLayout inputLayout) {
        if (isPasswordVisible) {
            // 隐藏密码
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            inputLayout.setEndIconDrawable(R.drawable.baseline_visibility_off_24); // 关闭眼睛
        } else {
            // 显示密码
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            inputLayout.setEndIconDrawable(R.drawable.baseline_visibility_24); // 打开眼睛
        }
        isPasswordVisible = !isPasswordVisible; // 切换状态
        editText.setSelection(editText.getText().length()); // 保持光标位置
    }
    //todo:点击跳转协议或者政策
    public void jumpAgreement(){
        tv_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AgreetmentActivity.class);
                intent.putExtra("isAgreement", true);
                startActivity(intent);
            }
        });

        tv_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AgreetmentActivity.class);
                intent.putExtra("isAgreement", false);
                startActivity(intent);
            }
        });
    }
    //todo:点击登录，注册，重置密码
    public void jump_login_register_forget(){
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ForgetPasswordActivity.class);
                intent.putExtra("isChange", 1);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
    }

    //todo:登录方法
    private void login() {
        String phone = isPhoneLogin ? edt_phone.getText().toString().trim() : "";
        String email = !isPhoneLogin ? edt_email.getText().toString().trim() : "";
        String password = edt_pass_phone.getText().toString().trim();

        if ((isPhoneLogin && phone.isEmpty()) || (!isPhoneLogin && email.isEmpty()) || password.isEmpty()) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(email, phone, password);
        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.getCode() == 200) {
                        UserData userData = loginResponse.getData();
                        Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();

                        // 传递 id 和 token 到下一个页面
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("USER_ID", userData.getId());
                        intent.putExtra("TOKEN", userData.getToken());
                        SessionManager sessionManager = new SessionManager(MainActivity.this);
                        sessionManager.saveUserSession(userData.getId(), userData.getToken());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, loginResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}