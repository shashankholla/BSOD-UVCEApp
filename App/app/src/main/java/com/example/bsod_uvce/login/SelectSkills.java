package com.example.bsod_uvce.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.bsod_uvce.R;

import com.example.bsod_uvce.mainpage.viewJobs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectSkills extends AppCompatActivity {
    boolean painting, carpentry, electrical, plumbing, construction;
    Button finishButton;
    FirebaseUser mUser;
    FirebaseAuth auth;
    String name;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_skills);
        name = getIntent().getStringExtra("Name");
        String location = getIntent().getStringExtra("Location");
        String type = getIntent().getStringExtra("Type");
        db = FirebaseFirestore.getInstance();
        CompoundButton.OnCheckedChangeListener multiListener = (v, isChecked) -> {
            switch (v.getId()){
                case R.id.switchCarpentry:
                    carpentry=isChecked;
                    break;
                case R.id.switchConstruction:
                    construction=isChecked;
                    break;
                case R.id.switchElectricWork:
                    electrical=isChecked;
                    break;
                case R.id.switchPainting:
                    painting=isChecked;
                    break;
                case R.id.switchPlumbing:
                    plumbing=isChecked;break;
            }
        };
        ((SwitchCompat) findViewById(R.id.switchConstruction)).setOnCheckedChangeListener(multiListener);
        ((SwitchCompat) findViewById(R.id.switchCarpentry)).setOnCheckedChangeListener(multiListener);
        ((SwitchCompat) findViewById(R.id.switchElectricWork)).setOnCheckedChangeListener(multiListener);
        ((SwitchCompat) findViewById(R.id.switchPainting)).setOnCheckedChangeListener(multiListener);
        ((SwitchCompat) findViewById(R.id.switchPlumbing)).setOnCheckedChangeListener(multiListener);
        finishButton=findViewById(R.id.finishButton);
        auth = FirebaseAuth.getInstance();
        mUser =auth.getCurrentUser();
        finishButton.setOnClickListener(view -> {
            registerUser(name, location, type);
        });
    }

    private void registerUser(String name, String location, String type)
    {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("Name", name);
            dataMap.put("Location", location);
            dataMap.put("Phone Number", mUser.getPhoneNumber());
            List<String> skillList = new ArrayList<>();
            if (carpentry)
                skillList.add("Carpentry");
            if (construction)
                skillList.add("Construction");
            if (plumbing)
                skillList.add("Plumbing");
            if (electrical)
                skillList.add("Electrical");
            if (painting)
                skillList.add("Painting");
            dataMap.put("Skills", skillList);
            dataMap.put("Rating", 0);
            dataMap.put("Certificates", null);
            db.collection("Labourer").document(mUser.getUid()).set(dataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Success", "DocumentSnapshot successfully written!");
            }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Failure", "Error writing document", e);
            }
            });
            Intent intent = new Intent(SelectSkills.this, viewJobs.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
    }


}