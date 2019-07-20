package com.example.ttapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ttapp.R;
import com.example.ttapp.databinding.RowItemStatsBinding;
import com.example.ttapp.db.room.entity.ProfileEntity;

import java.util.List;



public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder> {

    private Context context;
    List<ProfileEntity>profileEntityList;

    public StatsAdapter(Context context,List<ProfileEntity>profileEntityList){
        this.context=context;
        this.profileEntityList=profileEntityList;
    }

    @NonNull
    @Override
    public StatsAdapter.StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.row_item_stats,parent,false);
        StatsViewHolder statsViewHolder=new StatsViewHolder(view);
        return statsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StatsAdapter.StatsViewHolder holder, int position) {
        holder.rowItemStatsBinding.valueDay.setText(profileEntityList.get(position).getDay());
        holder.rowItemStatsBinding.valueMatches.setText(profileEntityList.get(position).getMatches());
        holder.rowItemStatsBinding.valueWins.setText(profileEntityList.get(position).getWins());
    }

    @Override
    public int getItemCount() {
        return profileEntityList.size();
    }

    public class StatsViewHolder extends RecyclerView.ViewHolder{
        private RowItemStatsBinding rowItemStatsBinding;
        public StatsViewHolder(View view){
            super(view);
            rowItemStatsBinding=RowItemStatsBinding.bind(view);
        }
    }
}
