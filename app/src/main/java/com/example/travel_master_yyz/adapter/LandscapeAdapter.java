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

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.travel_master_yyz.DiaryDetailActivity;
import com.example.travel_master_yyz.LandscapeDetailActivity2;
import com.example.travel_master_yyz.R;
import com.bumptech.glide.Glide;
import com.example.travel_master_yyz.api.LandscapeResponse;

import java.util.List;

public class LandscapeAdapter extends RecyclerView.Adapter<LandscapeAdapter.ViewHolder> {
    private Context context;
    private List<LandscapeResponse.Landscape> landscapeList;

    public LandscapeAdapter(Context context,List<LandscapeResponse.Landscape> landscapeList) {
        this.context = context;
        this.landscapeList = landscapeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_landscape, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LandscapeResponse.Landscape landscape = landscapeList.get(position);
        holder.name.setText(landscape.getName());
        holder.score.setText(String.format("%.1f 分", landscape.getAverageScore()));
        holder.description.setText(landscape.getDescription().length() > 50 ?
                landscape.getDescription().substring(0, 50) + "..." : landscape.getDescription());
        holder.praise.setText(String.valueOf(landscape.getPraise()));
        holder.comment.setText(String.valueOf(landscape.getComment()));

        // 解析 Base64 图片
        try {
            Glide.with(context)
                    .load(landscape.getImage())
                    .into(holder.image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, LandscapeDetailActivity2.class);
            intent.putExtra("fid", landscape.getId());  // 传递动态 ID
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return landscapeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, score, description, praise, comment;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.textViewName);
            score = itemView.findViewById(R.id.textViewScore);
            description = itemView.findViewById(R.id.textViewDescription);
            praise = itemView.findViewById(R.id.textViewPraise);
            comment = itemView.findViewById(R.id.textViewComment);
        }
    }
}
