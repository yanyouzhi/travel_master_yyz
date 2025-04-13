package com.example.travel_master_yyz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travel_master_yyz.DiaryDetailActivity;
import com.example.travel_master_yyz.LandscapeDetailActivity2;
import com.example.travel_master_yyz.R;
import com.example.travel_master_yyz.dao.LandscapeCacheItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<LandscapeCacheItem> data;
    private Context context;

    public HistoryAdapter(Context context, List<LandscapeCacheItem> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lc, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        LandscapeCacheItem item = data.get(position);
        holder.tvName.setText(item.name);
        holder.tvDesc.setText(item.description);

        @SuppressLint("SimpleDateFormat")
        String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(item.timestamp));
        holder.tvTime.setText("查看时间：" + timeStr);

        Glide.with(context)
                .load(item.image)
                .into(holder.ivImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), LandscapeDetailActivity2.class);
                intent.putExtra("fid", item.id);  // 传递动态 ID
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvDesc, tvTime;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
