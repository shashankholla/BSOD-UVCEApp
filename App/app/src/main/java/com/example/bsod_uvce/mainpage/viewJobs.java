package com.example.bsod_uvce.mainpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bsod_uvce.R;
import com.example.bsod_uvce.login.Settings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class viewJobs extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Toolbar toolbar;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    String myLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewjobs);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        ArrayList<Job> exampleList = new ArrayList<>();
        getLocation();
        exampleList.add(new Job("Painting", "Line 1", "Generic, House, Construction", "3 Days", "50 Rs", true, false,"Bangalore"));
        exampleList.add(new Job("Pipe Repair", "Line 3", "Plumbing", "3 Days", "50 Rs", false, false,"Bangalore"));
        exampleList.add(new Job("Construction", "Line 5", "Construction, House", "3 Days", "50 Rs", true, true,"Bangalore"));
        exampleList.add(new Job("TV Repair", "Line 7", "Electricals, Electronics", "3 Days", "50 Rs", false, false,"Bangalore"));

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ViewJobsAdapter(exampleList, mRecyclerView);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        toolbar = findViewById(R.id.toolbar);
    }

    private void getLocation()
    {
        db.collection("Labourer").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    myLocation = document.getData().get("Location").toString();
                    getJobs();
                }
            }
        });
    }

    private void getJobs()
    {
        ArrayList<Job> jobList = new ArrayList<>();
        db.collection("jobs")
                .whereEqualTo("location", myLocation).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document: task.getResult())
                    {
                        Job newJob = new Job();
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

                    Collections.sort(jobList, new Comparator<Job>()
                    {
                        @Override
                        public int compare(Job t1, Job t2)
                        {
                            return t2.amount.compareToIgnoreCase(t1.amount);
                        }
                    });
                    mAdapter = new EmployerSubmittedJobsAdapter(jobList, mRecyclerView);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                }
                else
                {
                    Log.e("Result", "Fail");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings:
                intent = new Intent(viewJobs.this, Settings.class);
                startActivity(intent);
                break;
            case R.id.chat:
                intent = new Intent(viewJobs.this, ChatBox.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }



}