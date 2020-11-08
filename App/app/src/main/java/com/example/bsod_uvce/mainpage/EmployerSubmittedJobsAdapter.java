package com.example.bsod_uvce.mainpage;

import android.content.Context;
import android.content.Intent;
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


public class EmployerSubmittedJobsAdapter extends RecyclerView.Adapter<EmployerSubmittedJobsAdapter.ExampleViewHolder> {
    private ArrayList<Job> mExampleList;
    RecyclerView rv;
    Context context;
    public OnClickInMyAdapterListener myActivityInterface;
    public interface OnClickInMyAdapterListener {
        void onAccepted(String jobId);
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public Button expandBtn;
        public TextView mStatus;
        public Button acceptJob;
        public Button chatBtn;
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
            expandBtn = itemView.findViewById(R.id.expandBtn);
            acceptJob = itemView.findViewById(R.id.acceptBtn);
            mStatus = itemView.findViewById(R.id.emplyorStatus);
            chatBtn = itemView.findViewById(R.id.startChat);
            constraintLayout = itemView.findViewById(R.id.expandableView);
        }
    }

    public EmployerSubmittedJobsAdapter(Context context, ArrayList<Job> exampleList, RecyclerView rv, EmployerSubmittedJobsAdapter.OnClickInMyAdapterListener myAdapterListener) {
        mExampleList = exampleList;
        this.rv = rv;
        this.context = context;
        Toast.makeText(context,"OnClick workshere1", Toast.LENGTH_LONG).show();
        this.myActivityInterface = myAdapterListener;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_card_layout_apply_layout, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        evh.expandBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Toast.makeText(parent.getContext(),"OnClick workshere2", Toast.LENGTH_LONG).show();
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

        holder.mTextView1.setText(context.getString(R.string.title)+ ":" + currentItem.getJobTitle());
        holder.mTextView2.setText(context.getString(R.string.skills_needed) + ":" + currentItem.getJobCategory());
        holder.mTextView3.setText(context.getString(R.string.job_duration) + ":" + currentItem.getJobDuration());
        holder.mTextView4.setText(context.getString(R.string.salary_for_job) + ":" + currentItem.getAmount());
        holder.mTextView5.setText(context.getString(R.string.job_desc) + ":" + currentItem.getDescription());
        holder.acceptJob.setVisibility(View.GONE);
        holder.chatBtn.setVisibility(View.GONE);
        if(currentItem.ifApplied){
            holder.mStatus.setText("User:" + currentItem.acceptedLabourerId + String.valueOf(R.string.has_applied));
            holder.acceptJob.setVisibility(View.VISIBLE);
        }

        if(currentItem.ifAccepted){
            holder.mStatus.setText(String.valueOf(R.string.great_talk));
            holder.chatBtn.setVisibility(View.VISIBLE);
            holder.acceptJob.setVisibility(View.GONE);
        }

        holder.acceptJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept(currentItem.jobId);

            }
        });

        holder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChatBox.class);
                context.startActivity(i);
            }
        });
    }
    public void accept(String jobId){
        this.myActivityInterface.onAccepted(jobId);
    }


    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}