package com.example.bsod_uvce.profiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bsod_uvce.R;

import java.util.ArrayList;

public class CertiAdapter extends RecyclerView.Adapter<CertiAdapter.MyViewHolder> {
    Context context;
    ArrayList<jobAward>  items;

    private LayoutInflater inflater;

    public CertiAdapter(Context context,  ArrayList<jobAward>  items) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.items = items;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.certfi_listview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.awardName.setText(items.get(position).awardName);
        holder.awardDate.setText(items.get(position).dateAwarded);
        if(items.get(position).medal.compareTo("gold") == 0){
            holder.medal.setImageResource(R.drawable.gold);
        } else {
            holder.medal.setImageResource(R.drawable.silver);
        }

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView awardName;
        TextView awardDate;
        ImageView medal;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            awardName = itemView.findViewById(R.id.awardName);
            awardDate = itemView.findViewById(R.id.awardDate);
            medal = itemView.findViewById(R.id.medal_image);
        }
    }
}
