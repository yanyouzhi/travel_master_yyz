package com.example.travel_master_yyz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.api.UserInfoRequest;
import com.example.travel_master_yyz.api.UserResponse;
import com.example.travel_master_yyz.dao.SessionManager;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.engine.CompressFileEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class EditInfoActivity extends BaseActivity {
    private EditText edtUserName, edtDesc, edtJob, edtLocate, edtPhone;
    private static final int REQUEST_IMAGE_PICK = 1;
    private ImageView imgEditAva;
    private TextView tvMan, tvWoman;
    private Button btnSave;
    private int selectedSex; // 默认男
    private String base64Photo = "";  // 用于上传的 Base64 图片
    private String photoUrl = "";  // 用户头像的 URL（从后端获取）
    private String token, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        SessionManager sessionManager = new SessionManager(EditInfoActivity.this);
        token = sessionManager.getToken();
        id = sessionManager.getId();
        initViews();
        loadUserInfo();
        setupListeners();
    }

    private void initViews() {
        imgEditAva = findViewById(R.id.img_editava);
        edtUserName = findViewById(R.id.edt_userName);
        edtDesc = findViewById(R.id.edt_desc);
        edtJob = findViewById(R.id.edt_job);
        edtLocate = findViewById(R.id.edt_locate);
        edtPhone = findViewById(R.id.edt_phone);
        tvMan = findViewById(R.id.tv_man);
        tvWoman = findViewById(R.id.tv_woman);
        btnSave = findViewById(R.id.btnSave);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
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

    private void updateUI(UserResponse.UserData userData) {
        edtUserName.setText(userData.getName());
        edtDesc.setText(userData.getDescription());
        edtJob.setText(userData.getOccupation());
        edtLocate.setText(userData.getAddr());
        selectedSex = userData.getSex();
        edtPhone.setText(userData.getEmail());
        if(selectedSex == 1){
            tvMan.setSelected(true);
            tvWoman.setSelected(false);
        }else{
            tvMan.setSelected(false);
            tvWoman.setSelected(true);
        }

        if (userData.getPhoto() != null && !userData.getPhoto().isEmpty()) {
            Glide.with(this)
                    .load(userData.getPhoto())
                    .transform(new CircleCrop()) // 设置圆形
                    .into(imgEditAva);
        }else{
            Glide.with(this)
                    .load("https://yanyouzhi8758.oss-cn-guangzhou.aliyuncs.com/%E9%BB%91%E7%8C%AB.jpg")
                    .transform(new CircleCrop())
                    .into(imgEditAva);
        }
    }

    private void setupListeners() {
        imgEditAva.setOnClickListener(v -> selectImageFromGallery());

        tvMan.setOnClickListener(v -> {
            selectedSex = 1;
            tvMan.setSelected(true);
            tvWoman.setSelected(false);
        });

        tvWoman.setOnClickListener(v -> {
            selectedSex = 2;
            tvWoman.setSelected(true);
            tvMan.setSelected(false);
        });

        btnSave.setOnClickListener(v -> saveUserInfo());
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            // Luban 进行压缩
            Luban.with(this)
                    .load(imageUri)
                    .ignoreBy(100) // 小于 100KB 不压缩
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                            // 压缩开始
                        }
                        @Override
                        public void onSuccess(int index, File compressFile) {
                            // 读取压缩后的图片
                            Bitmap bitmap = BitmapFactory.decodeFile(compressFile.getAbsolutePath());

                            // 使用 Glide 显示
                            Glide.with(EditInfoActivity.this)
                                    .load(bitmap)
                                    .transform(new CircleCrop()) // 圆形头像
                                    .into(imgEditAva);

                            // 转换为 Base64
                            base64Photo = encodeImageToBase64(bitmap);
                            Log.i("yyz",base64Photo);
                        }

                        @Override
                        public void onError(int index,Throwable e) {
                            Toast.makeText(EditInfoActivity.this, "图片压缩失败", Toast.LENGTH_SHORT).show();
                        }
                    }).launch();
        }
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream); // 70% 质量
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    private void saveUserInfo() {
        String name = edtUserName.getText().toString().trim();
        Log.i("yyz",name);
        String addr = edtLocate.getText().toString().trim();
        String description = edtDesc.getText().toString().trim();
        String occupation = edtJob.getText().toString().trim();
        Log.i("yyz",occupation);
        String contact = edtPhone.getText().toString().trim();

        Log.i("yyz",base64Photo);
        UserInfoRequest request = new UserInfoRequest(id, name, base64Photo, selectedSex, addr, description, occupation, contact);

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<ResponseBody> call = apiService.editUserInfo(token, request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditInfoActivity.this, "资料更新成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditInfoActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditInfoActivity.this, "网络错误：" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
