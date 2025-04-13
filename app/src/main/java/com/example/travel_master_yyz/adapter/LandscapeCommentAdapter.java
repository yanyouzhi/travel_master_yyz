package com.example.travel_master_yyz.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
import com.example.travel_master_yyz.UserInfoActivity;
import com.example.travel_master_yyz.api.CommentListResponse;

import java.util.List;

public class LandscapeCommentAdapter extends RecyclerView.Adapter<LandscapeCommentAdapter.ViewHolder> {
    private List<CommentListResponse.Comment> commentList;
    private Context context;

    public LandscapeCommentAdapter(Context context,List<CommentListResponse.Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentListResponse.Comment comment = commentList.get(position);
        holder.tvUsername.setText(comment.getName());
        holder.tvComment.setText(comment.getComment());
        holder.tvTime.setText(comment.getTime());
        if (comment.getPhoto() != null && !comment.getPhoto().isEmpty()) {
            Glide.with(context)
                    .load(comment.getPhoto())
                    .transform(new CircleCrop()) // 设置圆形
                    .into(holder.ava);
        }else{
            Glide.with(context)
                    .load("https://yanyouzhi8758.oss-cn-guangzhou.aliyuncs.com/%E9%BB%91%E7%8C%AB.jpg")
                    .transform(new CircleCrop())
                    .into(holder.ava);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserInfoActivity.class);
            intent.putExtra("search_id", comment.getUid());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvComment, tvTime;
        ImageView ava;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvTime = itemView.findViewById(R.id.tv_time);
            ava = itemView.findViewById(R.id.iv_avatar);
        }
    }
}
