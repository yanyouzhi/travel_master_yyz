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
import com.example.travel_master_yyz.R;
import com.example.travel_master_yyz.UserInfoActivity;
import com.example.travel_master_yyz.api.CommentResponse;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context context;
    private List<CommentResponse.CommentData> commentList = new ArrayList<>();

    public void setComments(Context context,List<CommentResponse.CommentData> comments) {
        this.context = context;
        this.commentList = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentResponse.CommentData comment = commentList.get(position);
        holder.tvUserName.setText(comment.getName());
        holder.tvComment.setText(comment.getComment());
        holder.tvTime.setText(comment.getTime());

        // 设置背景颜色，依次为 绿色 -> 黄色 -> 灰色
        int[] colors = {R.color.comment1, R.color.comment2,R.color.comment3,R.color.comment4,R.color.comment5,R.color.comment6};
        holder.itemView.setBackgroundResource(colors[position % colors.length]);

        if (comment.getPhoto() != null && !comment.getPhoto().isEmpty()) {
            Glide.with(context)
                    .load(comment.getPhoto())
                    .transform(new CircleCrop()) // 设置圆形
                    .into(holder.img);
        } else {
            holder.img.setVisibility(View.VISIBLE);
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
        TextView tvUserName, tvComment, tvTime;
        ImageView img;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_username);
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvTime = itemView.findViewById(R.id.tv_time);
            img = itemView.findViewById(R.id.iv_avatar);
        }
    }
}
