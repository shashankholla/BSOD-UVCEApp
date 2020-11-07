package com.example.bsod_uvce.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.bsod_uvce.R;
import com.example.bsod_uvce.profiles.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SelectSkills extends AppCompatActivity {
    boolean painting, carpentry, electrical, plumbing, construction;
    Button finishButton;
    FirebaseUser mUser;
    FirebaseAuth auth;
    Uri profilePhotoUri;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_skills);
        name = getIntent().getStringExtra("Name");
        String location = getIntent().getStringExtra("Location");
        String type = getIntent().getStringExtra("Type");

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
            User user = new User();
            user.setName(name);
            user.setLocation(location);
            user.setPhoneNumber(mUser.getPhoneNumber());
            if (carpentry)
                user.addNewSkill("Carpentry");
            if (construction)
                user.addNewSkill("Construction");
            if (plumbing)
                user.addNewSkill("Plumbing");
            if (electrical)
                user.addNewSkill("Electrical");
            if (painting)
                user.addNewSkill("Painting");
    }


}