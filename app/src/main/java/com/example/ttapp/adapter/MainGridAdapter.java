package com.example.ttapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ttapp.R;
import com.example.ttapp.ui.fragment.StatsFragment;
import com.example.ttapp.utils.CustomClickListener;

import java.util.ArrayList;

public class MainGridAdapter extends RecyclerView.Adapter<MainGridAdapter.MainViewHolder> {

    private int mHeight;

    private LayoutInflater layoutInflater;
    private String[] gridItemName;
    private Context context;
    private ArrayList<GridModel>gridModels;
    private int layout;
    private View.OnClickListener onClickListener;
    private FragmentManager fragmentManager;

    private int[] mGridItemIcon;

    private  CustomClickListener customClickListener;

    public MainGridAdapter(Context context, String[]gridItemName, int[]mGridItemIcon, CustomClickListener customClickListener) {
        this.context=context;
        this.layoutInflater = LayoutInflater.from(context);
        this.gridItemName = gridItemName;
        this.mGridItemIcon = mGridItemIcon;
        this.customClickListener=customClickListener;


        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mHeight = metrics.widthPixels / 2 - 20;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_grid,parent,false);
        view.setMinimumHeight(mHeight);
        MainViewHolder mainViewHolder=new MainViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customClickListener.onItemClick(view,mainViewHolder.getAdapterPosition());
            }
        });
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.textView.setText(gridItemName[position]);
        Glide.with(context).load(mGridItemIcon[position]).into(holder.imageView);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"a",Toast.LENGTH_LONG).show();
            }
        });
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               // Bundle bundle=new Bundle();
//                //bundle.putInt("KEY_POSITION",position);
//                StatsFragment statsFragment=new StatsFragment();
//                //statsFragment.setArguments(bundle);
//                AppCompatActivity appCompatActivity=(AppCompatActivity)view.getContext();
//                appCompatActivity.getSupportFragmentManager()
//                        .beginTransaction().replace(R.id.container,statsFragment)
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return gridItemName.length;
    }

    class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textView;
        private ImageView imageView;
        CardView cardView;
        private ItemClickListener itemClickListener;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text_view_grid);
            imageView=itemView.findViewById(R.id.image_view_grid);
            cardView=itemView.findViewById(R.id.cardView);
            //itemView.setTag(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position=getLayoutPosition();
            Log.d("MainGridAdapter","position= "+position);
            Toast.makeText(context,position,Toast.LENGTH_LONG).show();
             itemClickListener.onItemClick(view,getAdapterPosition());
        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
