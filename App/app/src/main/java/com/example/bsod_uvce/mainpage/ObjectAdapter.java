package com.example.bsod_uvce.mainpage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bsod_uvce.R;

import java.util.ArrayList;


public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.ExampleViewHolder> {
    private ArrayList<Job> mExampleList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.jobCategoryIcon);
            mTextView1 = itemView.findViewById(R.id.jobTitle);
            mTextView2 = itemView.findViewById(R.id.categories);
            mTextView3 = itemView.findViewById(R.id.duration);
            mTextView4 = itemView.findViewById(R.id.amount);
        }
    }

    public ObjectAdapter(ArrayList<Job> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_card_layout, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Job currentItem = mExampleList.get(position);

        holder.mTextView1.setText(currentItem.getJobTitle());
        holder.mTextView2.setText(currentItem.getJobCategory());
        holder.mTextView3.setText(currentItem.getJobDuration());
        holder.mTextView4.setText(currentItem.getAmount());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}