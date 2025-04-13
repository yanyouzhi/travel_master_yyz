package com.example.travel_master_yyz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.travel_master_yyz.FindLandscapeActivity;
import com.example.travel_master_yyz.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ChengyuViewHolder> {

    private List<String> chengyuList;
    private Context context;

    public SearchAdapter(Context context, List<String> chengyuList) {
        this.context = context;
        this.chengyuList = chengyuList;
    }

    @NonNull
    @Override
    public ChengyuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new ChengyuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChengyuViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String chengyu = chengyuList.get(position);
        holder.chengyuTextView.setText(chengyu);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FindLandscapeActivity.class);
                intent.putExtra("chengyu", chengyuList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chengyuList.size();
    }

    static class ChengyuViewHolder extends RecyclerView.ViewHolder {

        TextView chengyuTextView;

        public ChengyuViewHolder(@NonNull View itemView) {
            super(itemView);
            chengyuTextView = itemView.findViewById(R.id.tv_search);
        }
    }
}
