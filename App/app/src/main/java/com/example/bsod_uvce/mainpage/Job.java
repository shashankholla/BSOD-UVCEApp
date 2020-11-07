package com.example.bsod_uvce.mainpage;

import android.graphics.drawable.Drawable;


public class Job {
    public String jobTitle;
    public Drawable jobCategoryIcon;
    public String jobCategory;
    public String description;
    public String jobDuration;
    public String amount;

    public Job(String jobTitle, String desctription, String jobCategory, String jobDuration, String amount

    ){
        this.jobTitle = jobTitle;
        this.jobCategory = jobCategory;
        this.description = desctription;
        this.jobDuration = jobDuration;
        this.amount = amount;
    }
    public Job(String jobTitle, String desctription, String jobCategory, String jobDuration, String amount, Drawable jobCategoryIcon){
        this.jobTitle = jobTitle;
        this.jobCategory = jobCategory;
        this.description = desctription;
        this.jobDuration = jobDuration;
        this.amount = amount;
        this.jobCategoryIcon = jobCategoryIcon;
    }
    public String getJobTitle() {
        return jobTitle;
    }
    public String getJobCategory() {
        return jobCategory;
    }
    public String getDescription() {
        return description;
    }
    public String getJobDuration() {
        return jobDuration;
    }
    public String getAmount() {
        return amount;
    }
}
