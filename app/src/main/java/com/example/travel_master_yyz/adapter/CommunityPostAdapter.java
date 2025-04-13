package com.example.travel_master_yyz.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.travel_master_yyz.DiaryDetailActivity;
import com.example.travel_master_yyz.R;
import com.example.travel_master_yyz.api.CommunityHomePost;
import com.example.travel_master_yyz.api.CommunityPost;
import com.example.travel_master_yyz.utils.DipPx;

import java.util.ArrayList;
import java.util.List;

public class CommunityPostAdapter extends RecyclerView.Adapter<CommunityPostAdapter.CommunityViewHolder> {
    private List<CommunityHomePost> data = new ArrayList<>();
    private Context context;

    public CommunityPostAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<CommunityHomePost> newData) {
        data.clear();
        if (newData != null) data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_diary, parent, false);
        return new CommunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {
        CommunityHomePost post = data.get(position);
        // 设置背景颜色，依次为 绿色 -> 黄色 -> 灰色
        int[] colors = {R.color.comment1, R.color.comment2,R.color.comment3,R.color.comment4,R.color.comment5,R.color.comment6};
        holder.itemView.setBackgroundResource(colors[position % colors.length]);
        holder.tvContent.setText(post.getDescription());
        holder.name.setText(post.getName());
        holder.name.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        holder.time.setText(post.getTime());
        holder.time.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);


        if (!TextUtils.isEmpty(post.getPhoto())) {
            Glide.with(context)
                    .load(post.getPhoto())
                    .transform(new CircleCrop()) // 设置圆形
                    .into(holder.imgAva);
        } else{
            Glide.with(context)
                    .load("https://yanyouzhi8758.oss-cn-guangzhou.aliyuncs.com/%E9%BB%91%E7%8C%AB.jpg")
                    .transform(new CircleCrop())
                    .into(holder.imgAva);
        }



        // 动态图片显示
        if (post.getImage() != null && !post.getImage().isEmpty()) {
            holder.ivImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(post.getImage())
                    .transform(new RoundedCorners(50))
                    .into(holder.ivImage);
        } else {
            holder.ivImage.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DiaryDetailActivity.class);
            intent.putExtra("diary_id", post.getId());  // 传递动态 ID
            intent.putExtra("diary_uid", post.getUid());  // 传递用户 ID
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class CommunityViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent,name,time;
        ImageView ivImage, imgAva;

        public CommunityViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_username);
            time = itemView.findViewById(R.id.tv_time);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivImage = itemView.findViewById(R.id.iv_image);
            imgAva = itemView.findViewById(R.id.imgAva);
        }
    }
}
