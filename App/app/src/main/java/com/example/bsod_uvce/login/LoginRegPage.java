package com.example.bsod_uvce.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bsod_uvce.MainActivity;
import com.example.bsod_uvce.R;
import com.example.bsod_uvce.mainpage.EmployerJobs;
import com.example.bsod_uvce.mainpage.viewJobs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginRegPage extends AppCompatActivity {
    private EditText phoneNumber;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    boolean labourer=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_reg_page);
        phoneNumber = findViewById(R.id.phoneText);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (user != null) {
            updateUI(user);
        }
        findViewById(R.id.regNextButton).setOnClickListener(v -> {

            String mobile = phoneNumber.getText().toString().trim();

            if(mobile.isEmpty() || mobile.length() < 10){
                phoneNumber.setError("Enter a valid mobile");
                phoneNumber.requestFocus();
                return;
            }

            Intent intent = new Intent(LoginRegPage.this, OtpActivity.class);
            intent.putExtra("mobile", mobile);
            startActivity(intent);
        });
    }
    private void updateUI(FirebaseUser user)
    {
        db.collection("Labourer")
                .whereEqualTo("Id", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            labourer= task.getResult().getDocuments().size() > 0;
                        } else {
                            Log.e("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });

        Intent intent;
        if(labourer)
            intent = new Intent(LoginRegPage.this, viewJobs.class);
        else
            intent = new Intent(LoginRegPage.this, EmployerJobs.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}