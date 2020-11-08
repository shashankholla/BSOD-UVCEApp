package com.example.bsod_uvce.mainpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.bsod_uvce.R;
import com.example.bsod_uvce.createJob;
import com.example.bsod_uvce.login.Settings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployerJobs extends AppCompatActivity implements EmployerSubmittedJobsAdapter.OnClickInMyAdapterListener {
    Toolbar toolbar;
    FirebaseFirestore db;
    FirebaseUser user;
    FirebaseAuth auth;
    ArrayList<Job> exampleList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_jobs);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        db=FirebaseFirestore.getInstance();
       exampleList  = new ArrayList<>();

        CollectionReference docRef = db.collection("jobs");


        db.collection("jobs")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {


                      exampleList = getJobs();
                    }
                });
        exampleList = getJobs();
        Toast.makeText(this, "EmployerJobs", Toast.LENGTH_SHORT).show();

//        exampleList.add(new Job("Painting", "Line 1", "Generic, House, Construction", "3 Days", "50 Rs",true, true, "Bangalore"));
//        exampleList.add(new Job("Pipe Repair", "Line 3", "Plumbing", "3 Days", "50 Rs", false, false,"Bangalore"));
//        exampleList.add(new Job("Construction", "Line 5", "Construction, House", "3 Days", "50 Rs", false, false,"Bangalore"));
//        exampleList.add(new Job("TV Repair", "Line 7", "Electricals, Electronics", "3 Days", "50 Rs", true, false,"Bangalore"));
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new EmployerSubmittedJobsAdapter(this, exampleList, mRecyclerView, this);
        db = FirebaseFirestore.getInstance();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

	fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmployerJobs.this, createJob.class);
                startActivity(i);
            }
        });
    }

 @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings:
                intent = new Intent(EmployerJobs.this, Settings.class);
                startActivity(intent);
                break;
            case R.id.chat:
                Toast.makeText(this, "Chat Box", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    private ArrayList<Job> getJobs()
    {
        ArrayList<Job> jobList = new ArrayList<>();
        db.collection("jobs")
                .whereEqualTo("employerId", user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document: task.getResult())
                    {
                        Job newJob = new Job();
                        newJob.jobId = document.getId();
                        newJob.location=(String)document.getData().get("location");
                        newJob.jobTitle=(String)document.getData().get("jobTitle");
                        newJob.description=(String)document.getData().get("description");
                        newJob.amount=(String)document.getData().get("amount");
                        newJob.jobDuration=(String)document.getData().get("jobDuration");
                        newJob.jobCategory=document.getData().get("jobCategory").toString();
                        newJob.ifApplied=(boolean)document.getData().get("ifApplied");
                        newJob.ifAccepted=(boolean)document.getData().get("ifAccepted");
                        newJob.employerId=(String)document.getData().get("employerId");
                        newJob.acceptedLabourerId=(String)document.getData().get("acceptedLabourerId");
                        newJob.jobCategory=document.getData().get("jobCategory").toString();
                        Log.e("Result", document.getId()+"+>"+newJob.toString());
                        jobList.add(newJob);
                    }
                    mAdapter = new EmployerSubmittedJobsAdapter(getApplicationContext(), jobList, mRecyclerView, EmployerJobs.this);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                }
                else
                {
                    Log.e("Result", "Fail");
                }
            }
        });
        return jobList;
    }

    @Override
    public void onAccepted(String jobId) {
        Map<String, Boolean> userMap = new HashMap<>();
        userMap.put("ifApplied", true);
        Toast.makeText(this, jobId, Toast.LENGTH_SHORT).show();
        this.db.collection("jobs").document(jobId).update("ifAccepted", true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
}
