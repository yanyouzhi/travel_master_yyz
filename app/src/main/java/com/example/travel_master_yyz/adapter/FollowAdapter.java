package com.example.travel_master_yyz.adapter;

import android.content.Intent;
import android.text.TextUtils;
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
import com.example.travel_master_yyz.api.FollowRecord;

import java.util.List;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    private List<FollowRecord> recordList;

    public FollowAdapter(List<FollowRecord> list) {
        this.recordList = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, time;
        ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            time = itemView.findViewById(R.id.tv_time);
            photo = itemView.findViewById(R.id.iv_photo);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_follow_record, parent, false);
        return new ViewHolder(view);
    }

    //https://yanyouzhi8758.oss-cn-guangzhou.aliyuncs.com/%E9%BB%91%E7%8C%AB.png
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FollowRecord record = recordList.get(position);
        holder.name.setText(record.getName());
        if(!TextUtils.isEmpty(record.getPhoto())){
            Glide.with(holder.itemView.getContext())
                    .load(record.getPhoto())
                    .transform(new CircleCrop())
                    .into(holder.photo);
        }else{
            Glide.with(holder.itemView.getContext())
                    .load("https://yanyouzhi8758.oss-cn-guangzhou.aliyuncs.com/%E9%BB%91%E7%8C%AB.jpg")
                    .transform(new CircleCrop())
                    .into(holder.photo);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), UserInfoActivity.class);
            intent.putExtra("search_id", record.getId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }
}
