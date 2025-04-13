package com.example.travel_master_yyz;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.PostRequest;
import com.example.travel_master_yyz.api.PostResponse;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.dao.SessionManager;
import com.example.travel_master_yyz.page_fragment.CommunityFragment;
import com.example.travel_master_yyz.view_model.CommunityViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class PostActivity extends BaseActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_PICK = 1;
    private EditText etDescription;
    private ImageView ivAddImage;
    private CheckBox cbShareCommunity;
    private String imageBase64 = "";  // 存储 Base64 编码的图片
    private String base64Photo = "";  // 用于上传的 Base64 图片
    private String token ; // 从登录获取
    private String uid ; // 从登录获取
    private TextView tvCancel,tvPublish;
    private CommunityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        init();
        // todo:选择图片
        ivAddImage.setOnClickListener(v -> selectImageFromGallery());

        // 取消
        tvCancel.setOnClickListener(v -> finish());

        // todo:发布
        tvPublish.setOnClickListener(v -> publishPost());


    }

    public void init(){
        etDescription = findViewById(R.id.et_description);
        ivAddImage = findViewById(R.id.iv_add_image);
        cbShareCommunity = findViewById(R.id.cb_share_community);
        tvCancel = findViewById(R.id.tv_cancel);
        tvPublish = findViewById(R.id.tv_publish);
        SessionManager sessionManager = new SessionManager(PostActivity.this);
        uid = sessionManager.getId();
        token = sessionManager.getToken();
    }

    // todo:打开相册选择图片
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
                            // 压缩成功后，加载压缩后的图片并显示在 ImageView 中
                            Bitmap compressedBitmap = BitmapFactory.decodeFile(compressFile.getAbsolutePath());
                            //ivAddImage.setImageBitmap(compressedBitmap);
                            // 使用 Glide 显示
                            Glide.with(PostActivity.this)
                                    .load(data.getData())
                                    .into(ivAddImage);


                            // 将压缩后的图片转换为 Base64 编码
                            imageBase64 = encodeImageToBase64(compressedBitmap);
                        }

                        @Override
                        public void onError(int index,Throwable e) {
                            Toast.makeText(PostActivity.this, "图片压缩失败", Toast.LENGTH_SHORT).show();
                        }
                    }).launch();
        }
    }


    // 将 Bitmap 转换为 Base64 编码
    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    // todo:发送发布请求
    private void publishPost() {
        String description = etDescription.getText().toString().trim();
        int community = cbShareCommunity.isChecked() ? 1 : 0;

        if (description.isEmpty()) {
            Toast.makeText(this, "请填写内容", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i("pto",imageBase64);
        PostRequest postRequest = new PostRequest(uid, description, imageBase64, String.valueOf(community));
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);

        apiService.postDiary(token, postRequest).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    Toast.makeText(PostActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PostActivity.this, "发布失败: " + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(PostActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
