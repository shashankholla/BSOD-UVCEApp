package com.example.bsod_uvce.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bsod_uvce.MainActivity;
import com.example.bsod_uvce.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.Date;

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
        findViewById(R.id.nextButton).setOnClickListener(v -> {

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
        Intent intent = new Intent(LoginRegPage.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}