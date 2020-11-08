package com.example.bsod_uvce.mainpage;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.example.bsod_uvce.R;

import java.util.ArrayList;


public class chatAdapter extends RecyclerView.Adapter<chatAdapter.ExampleViewHolder> {
    private ArrayList<String> mExampleList;
    RecyclerView rv;
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView1;


        public ExampleViewHolder(View itemView) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.text);

        }
    }

    public chatAdapter(ArrayList<String> exampleList, RecyclerView rv) {
        mExampleList = exampleList;
        this.rv = rv;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_bubble_dark, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);


        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        String currentItem = mExampleList.get(position);
        holder.mTextView1.setText(currentItem);

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}