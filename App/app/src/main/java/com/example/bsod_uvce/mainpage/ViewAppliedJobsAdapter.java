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


public class ViewAppliedJobsAdapter extends RecyclerView.Adapter<ViewAppliedJobsAdapter.ExampleViewHolder> {
    private ArrayList<Job> mExampleList;
    RecyclerView rv;
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public Button expandBtn;
        public View constraintLayout;
        CardView cv;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.jobCategoryIcon);
            mTextView1 = itemView.findViewById(R.id.jobTitle);
            mTextView2 = itemView.findViewById(R.id.categories);
            mTextView3 = itemView.findViewById(R.id.duration);
            mTextView4 = itemView.findViewById(R.id.amount);
            mTextView5 = itemView.findViewById(R.id.description);
            expandBtn = itemView.findViewById(R.id.expandBtn);
            cv = itemView.findViewById(R.id.cardView);
            constraintLayout = itemView.findViewById(R.id.expandableView);
        }
    }

    public ViewAppliedJobsAdapter(ArrayList<Job> exampleList, RecyclerView rv) {
        mExampleList = exampleList;
        this.rv = rv;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_card_layout_apply_layout, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        evh.expandBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Toast.makeText(parent.getContext(),"OnClick Jobs", Toast.LENGTH_LONG).show();
                if(evh.constraintLayout.getVisibility() == View.GONE){
                    TransitionManager.beginDelayedTransition(evh.cv, new AutoTransition());
                    evh.constraintLayout.setVisibility(View.VISIBLE);
                    evh.expandBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_up_black_24dp, 0, 0, 0);
                }
                else{
                    TransitionManager.beginDelayedTransition(rv, new AutoTransition());
                    evh.constraintLayout.setVisibility(View.GONE);
                    evh.expandBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_down_black_24dp, 0, 0, 0);
                }
            }
        });

        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Job currentItem = mExampleList.get(position);

        holder.mTextView1.setText(currentItem.getJobTitle());
        holder.mTextView2.setText(currentItem.getJobCategory());
        holder.mTextView3.setText(currentItem.getJobDuration());
        holder.mTextView4.setText(currentItem.getAmount());
        holder.mTextView5.setText(currentItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}