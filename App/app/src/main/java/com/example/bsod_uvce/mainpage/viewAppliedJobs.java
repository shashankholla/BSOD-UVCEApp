package com.example.bsod_uvce.mainpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.bsod_uvce.R;

import java.util.ArrayList;

public class viewAppliedJobs extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_applied_jobs);

        ArrayList<Job> exampleList = new ArrayList<>();
        exampleList.add(new Job("Painting", "Line 1", "Generic, House, Construction", "3 Days", "50 Rs",true, true, "Bangalore"));
        exampleList.add(new Job("Pipe Repair", "Line 3", "Plumbing", "3 Days", "50 Rs", false, false, "Bangalore"));
        exampleList.add(new Job("Construction", "Line 5", "Construction, House", "3 Days", "50 Rs", false, false, "Bangalore"));
        exampleList.add(new Job("TV Repair", "Line 7", "Electricals, Electronics", "3 Days", "50 Rs", true, false, "Bangalore"));

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ViewAppliedJobsAdapter(exampleList, mRecyclerView);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}