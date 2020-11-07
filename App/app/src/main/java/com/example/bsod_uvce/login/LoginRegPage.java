package com.example.bsod_uvce.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.bsod_uvce.MainActivity;
import com.example.bsod_uvce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegPage extends AppCompatActivity {
    private EditText phoneNumber;
    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg_page);
        phoneNumber = findViewById(R.id.phoneText);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
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
    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(LoginRegPage.this, UpdateDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}