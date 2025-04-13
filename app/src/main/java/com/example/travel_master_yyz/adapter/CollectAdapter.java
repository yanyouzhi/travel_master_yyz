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
import com.example.travel_master_yyz.DiaryDetailActivity;
import com.example.travel_master_yyz.LandscapeDetailActivity2;
import com.example.travel_master_yyz.R;
import com.example.travel_master_yyz.api.LandscapeCollect;

import java.util.List;

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> {

    private List<LandscapeCollect> list;

    public CollectAdapter(List<LandscapeCollect> list) {
        this.list = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, collectTime;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            description = itemView.findViewById(R.id.tv_desc);
            collectTime = itemView.findViewById(R.id.tv_time);
            image = itemView.findViewById(R.id.iv_image);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LandscapeCollect item = list.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());
        holder.collectTime.setText(item.getCollectTime());
        if(!TextUtils.isEmpty(item.getImage())){
            Glide.with(holder.itemView.getContext())
                    .load(item.getImage())
                    .into(holder.image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), LandscapeDetailActivity2.class);
                intent.putExtra("fid", item.getId());  // 传递动态 ID
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
