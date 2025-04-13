package com.example.travel_master_yyz.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.travel_master_yyz.DiaryDetailActivity;
import com.example.travel_master_yyz.R;
import com.example.travel_master_yyz.api.ApiService;
import com.example.travel_master_yyz.api.BaseResponse;
import com.example.travel_master_yyz.api.RetrofitClient;
import com.example.travel_master_yyz.dao.SessionManager;
import com.example.travel_master_yyz.model_mvvm.DiaryPost;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDiaryAdapter extends RecyclerView.Adapter<UserDiaryAdapter.ViewHolder> {
    private Context context;
    private List<DiaryPost> diaryList;
    private boolean isLiked = false;
    private String token,loginId;

    public UserDiaryAdapter(Context context, List<DiaryPost> diaryList) {
        this.context = context;
        this.diaryList = diaryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_diary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SessionManager sessionManager = new SessionManager(context);
        token = sessionManager.getToken();
        loginId = sessionManager.getId();
        DiaryPost diary = diaryList.get(position);
        holder.tvUsername.setText(diary.getName());
        holder.tvContent.setText(diary.getDescription());
        holder.tvTime.setText(diary.getTime());
        holder.tvLikes.setText(String.valueOf(diary.getPraise()));
        holder.tvComments.setText(String.valueOf(diary.getComment()));

        // 处理 Base64 图片
        if (diary.getImage() != null && !diary.getImage().isEmpty()) {
            Glide.with(context)
                    .load(diary.getImage())
                    .into(holder.ivImage);
            holder.ivImage.setVisibility(View.VISIBLE);
        } else {
            holder.ivImage.setVisibility(View.GONE);
        }

        if (diary.getPhoto() != null && !diary.getPhoto().isEmpty()) {
            Glide.with(context)
                    .load(diary.getPhoto())
                    .transform(new CircleCrop()) // 设置圆形
                    .into(holder.avaImage);
        } else{
            Glide.with(context)
                    .load("https://yanyouzhi8758.oss-cn-guangzhou.aliyuncs.com/%E9%BB%91%E7%8C%AB.jpg")
                    .transform(new CircleCrop())
                    .into(holder.avaImage);
        }

        // 处理点赞状态
        isLiked = diary.getPraise_be()> 0; // 获取点赞状态（0 = 未点赞，>0 = 已点赞）
        holder.imgLike.setSelected(false);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DiaryDetailActivity.class);
            intent.putExtra("diary_id", diary.getId());  // 传递动态 ID
            intent.putExtra("diary_uid", diary.getUid());  // 传递用户 ID
            context.startActivity(intent);
        });

        /*holder.imgLike.setOnClickListener(v -> {
            if (isLiked) {
                cancelLike(diary.getUid(),diary.getId(),holder);
            } else {
                addLike(diary.getUid(),diary.getId(),holder);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvJob, tvContent, tvTime, tvLikes, tvComments;
        ImageView ivImage,imgLike,avaImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvLikes = itemView.findViewById(R.id.tv_likes);
            tvComments = itemView.findViewById(R.id.tv_comments);
            ivImage = itemView.findViewById(R.id.iv_image);
            imgLike = itemView.findViewById(R.id.img_like);
            avaImage = itemView.findViewById(R.id.imgAva);
        }
    }

    //todo：点赞逻辑
    private void addLike(String diaryUid,String diaryId,ViewHolder holder) {

        RetrofitClient.getInstance().create(ApiService.class)
                .addPraise(token, loginId, diaryId)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null && response.body().getCode() == 200) {
                            isLiked = true;
                            // 获取当前点赞数并 +1
                            int currentPraiseCount = Integer.parseInt(holder.tvLikes.getText().toString());
                            holder.tvLikes.setText(String.valueOf(currentPraiseCount + 1));
                            holder.imgLike.setSelected(isLiked);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.e("Error", "点赞失败：" + t.getMessage());
                    }
                });
    }

    //todo：取消赞逻辑
    private void cancelLike(String diaryUid,String diaryId,ViewHolder holder) {
        RetrofitClient.getInstance().create(ApiService.class)
                .delPraise(token, loginId, diaryId)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null && response.body().getCode() == 200) {
                            isLiked = false;
                            // -1
                            int currentPraiseCount = Integer.parseInt(holder.tvLikes.getText().toString());
                            holder.tvLikes.setText(String.valueOf(currentPraiseCount - 1));
                            holder.imgLike.setSelected(isLiked);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.e("Error", "取消点赞失败：" + t.getMessage());
                    }
                });
    }

}
