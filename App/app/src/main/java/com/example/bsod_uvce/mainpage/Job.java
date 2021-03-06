package com.example.bsod_uvce.mainpage;

import android.graphics.drawable.Drawable;


public class Job {
    public String jobTitle;
    public Drawable jobCategoryIcon;
    public String jobCategory;
    public String description;
    public String jobDuration;
    public String amount;
    public boolean ifApplied;
    public boolean ifAccepted;
    public String employerId;
    public String acceptedLabourerId;
    public String location;
    public String jobId;
    public Job(String jobTitle, String description, String jobCategory, String jobDuration, String amount, boolean ifApplied, boolean ifAccepted, String location

    ){
        this.jobTitle = jobTitle;
        this.jobCategory = jobCategory;
        this.description = description;
        this.jobDuration = jobDuration;
        this.amount = amount;
        this.ifApplied = ifApplied;
        this.ifAccepted = ifAccepted;
        this.location=location;
    }
    public Job(String jobTitle, String description, String jobCategory, String jobDuration, String amount, boolean ifApplied, boolean ifAccepted, String employerId, String acceptedLabourerId,
               String location

    ){
        this.jobTitle = jobTitle;
        this.jobCategory = jobCategory;
        this.description = description;
        this.jobDuration = jobDuration;
        this.amount = amount;
        this.ifApplied = ifApplied;
        this.ifAccepted = ifAccepted;
        this.employerId = employerId;
        this.acceptedLabourerId = acceptedLabourerId;
        this.location=location;
    }

    public Job(String jobTitle, String description, String jobCategory, String jobDuration, String amount, boolean ifApplied, boolean ifAccepted, Drawable jobCategoryIcon, String location){
        this.jobTitle = jobTitle;
        this.jobCategory = jobCategory;
        this.description = description;
        this.jobDuration = jobDuration;
        this.amount = amount;
        this.jobCategoryIcon = jobCategoryIcon;
        this.ifApplied = ifApplied;
        this.ifAccepted = ifAccepted;
        this.location=location;
    }
    public Job()
    {}
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
    public boolean getIfApplied() {
        return ifApplied;
    }
    public boolean getIfAccepted() {
        return ifAccepted;
    }

}
