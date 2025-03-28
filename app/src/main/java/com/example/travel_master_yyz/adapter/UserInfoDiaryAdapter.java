package com.example.travel_master_yyz.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.travel_master_yyz.DiaryDetailActivity;
import com.example.travel_master_yyz.R;
import com.example.travel_master_yyz.api.DiaryPost;

import java.util.ArrayList;
import java.util.List;

public class UserInfoDiaryAdapter extends RecyclerView.Adapter<UserInfoDiaryAdapter.ViewHolder> {
    private List<DiaryPost> diaryList = new ArrayList<>();
    private Context context;

    public UserInfoDiaryAdapter(Context context) {
        this.context = context;
    }

    public void setDiaryList(List<DiaryPost> list) {
        this.diaryList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_diaries, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DiaryPost diary = diaryList.get(position);
        holder.tvDescription.setText(diary.getDescription());
        holder.tvTime.setText(diary.getTime());
        holder.tvPraise.setText(String.valueOf(diary.getPraise()));
        holder.tvComment.setText(String.valueOf(diary.getComment()));

        // 加载文章图片
        if (!diary.getImage().isEmpty()) {
            Glide.with(context)
                    .load(diary.getImage())
                    .into(holder.imgDiary);
            holder.imgDiary.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DiaryDetailActivity.class);
            intent.putExtra("diary_id", diary.getId());  // 传递动态 ID
            intent.putExtra("diary_uid", diary.getUid());  // 传递用户 ID
            context.startActivity(intent);
        });

        int colorIndex = position % 6;
        switch (colorIndex) {
            case 0:
                holder.itemView.setBackgroundResource(R.drawable.shape_n_pink);
                break;
            case 1:
                holder.itemView.setBackgroundResource(R.drawable.shape_n_blue);
                break;
            case 2:
                holder.itemView.setBackgroundResource(R.drawable.shape_n_orange);
                break;
            case 3:
                holder.itemView.setBackgroundResource(R.drawable.shape_n_green);
                break;
            case 4:
                holder.itemView.setBackgroundResource(R.drawable.shape_n_purple);
                break;
            case 5:
                holder.itemView.setBackgroundResource(R.drawable.shape_n_yellow);
                break;
            default:
                holder.itemView.setBackgroundResource(R.drawable.item_shape_new);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvTime, tvPraise, tvComment;
        ImageView imgPhoto, imgDiary;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPraise = itemView.findViewById(R.id.textViewPraise);
            tvComment = itemView.findViewById(R.id.textViewComment);
            imgDiary = itemView.findViewById(R.id.img_f);
        }
    }
}

