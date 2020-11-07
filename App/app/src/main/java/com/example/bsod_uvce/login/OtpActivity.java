package com.example.bsod_uvce.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bsod_uvce.MainActivity;
import com.example.bsod_uvce.R;
import com.example.bsod_uvce.mainpage.EmployerJobs;
import com.example.bsod_uvce.mainpage.viewJobs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    String verificationCode;
    private EditText otpCode;
    AlertDialog progressDialog;
    FirebaseUser user;
    FirebaseFirestore db;
    boolean labourer=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Intent intent = getIntent();
        otpCode=findViewById(R.id.otpText);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(OtpActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_bar);
        progressDialog = builder.create();
        String mobile = intent.getStringExtra("mobile");
        sendOTP(mobile);
        findViewById(R.id.verifyButton).setOnClickListener(view -> {
            String otp = otpCode.getText().toString().trim();
            verifyCode(otp);
        });

    }
    private void sendOTP(String mobile)
    {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+mobile)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential)
        {
            String code = credential.getSmsCode();
            if(code!=null)
            {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token)
        {
            verificationCode = verificationId;
        }
    };

    private void verifyCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, code);
        signInTheUserByCredential(credential);
    }

    private void signInTheUserByCredential(PhoneAuthCredential credential) {
        progressDialog.show();
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                user=mAuth.getCurrentUser();
                Toast.makeText(OtpActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                Intent intent;
                if(isNew)
                {
                    intent = new Intent(OtpActivity.this, UpdateDetails.class);
                }
                else
                {
                    db.collection("Labourer")
                            .whereEqualTo("Id", user.getUid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        labourer= task.getResult().getDocuments().size() != 0;
                                    } else {
                                        Log.e("Error", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                    if(labourer)
                        intent = new Intent(OtpActivity.this, viewJobs.class);
                    else
                        intent = new Intent(OtpActivity.this, EmployerJobs.class);
                }
                progressDialog.dismiss();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else
                Toast.makeText(OtpActivity.this, "Failed to login. Please check OTP", Toast.LENGTH_SHORT).show();
        });
    }
}




