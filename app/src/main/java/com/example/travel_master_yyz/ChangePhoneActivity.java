package com.example.travel_master_yyz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.BaseResponse;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.dao.SessionManager;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePhoneActivity extends BaseActivity {

    EditText editText;
    TextView textView;
    String id,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        editText = findViewById(R.id.change);
        textView =findViewById(R.id.btn);
        SessionManager sessionManager = new SessionManager(ChangePhoneActivity.this);
        id = sessionManager.getId();
        token = sessionManager.getToken();
        textView.setOnClickListener(v -> showConfirmDialog());
    }

    // todo:显示确认弹窗
    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.toast1))
                .setMessage(getString(R.string.toast2))
                .setPositiveButton(getString(R.string.toast3), (dialog, which) -> editPhone())
                .setNegativeButton(getString(R.string.toast4), null)
                .show();
    }

    // todo:发送修改手机号的请求
    private void editPhone() {
        String newPhone = editText.getText().toString().trim();
        if (TextUtils.isEmpty(newPhone)) {
            Toast.makeText(this, getString(R.string.toast5), Toast.LENGTH_SHORT).show();
            return;
        }

        // 构造请求体
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("uid", id);
        requestBody.addProperty("phone", newPhone);

        // 调用 API
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<BaseResponse> call = apiService.editPhone(token, requestBody);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    Toast.makeText(ChangePhoneActivity.this, getString(R.string.toast7), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChangePhoneActivity.this, getString(R.string.toast8), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(ChangePhoneActivity.this, "网络错误：" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}