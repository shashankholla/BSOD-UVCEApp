package com.example.bsod_uvce.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bsod_uvce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Date;

public class Settings extends AppCompatActivity {
    int REQUEST_IMAGE_CAPTURE = 69;
    int REQUEST_IMAGE_GALLERY = 420;
    Uri profilePhotoUri;
    ImageView profilePicture;
    Button logoutButton, saveButton;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    AlertDialog progressDialog;
    Toolbar toolbar;
    StorageReference storageReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        logoutButton = findViewById(R.id.logoutButton);
        saveButton = findViewById(R.id.saveButton);
        profilePicture = findViewById(R.id.profilePictureSettings);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        toolbar = findViewById(R.id.toolbar);
        AlertDialog.Builder progressBuilder = new AlertDialog.Builder(Settings.this);
        progressBuilder.setCancelable(false);
        progressBuilder.setView(R.layout.progress_bar);
        progressDialog = progressBuilder.create();
        if (user.getPhotoUrl() != null)
            Glide.with(this).load(user.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(profilePicture);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogout();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


    }

    private void save() {
        progressDialog.show();
        if (profilePhotoUri != null) {
            StorageReference profilePictureUpload = storageReference.child("Images/Profile Pictures/" + user.getUid());
            profilePictureUpload.putFile(profilePhotoUri).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Settings.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                }
            });
        }
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().
                setPhotoUri(profilePhotoUri).build();
        user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Settings.this, "User profile updated successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void logout() {
        firebaseAuth.signOut();
        Intent intent = new Intent(this, LoginRegPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void confirmLogout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Logout?");
        builder.setTitle("Confirm Logout");
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void takePhotoFromCamera() {
        boolean hasCamera = this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
        if (!hasCamera) {
            Toast.makeText(this, "A camera is required to use these features", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent takePictureFromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureFromCamera.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            photoFile = createImageFile();
            profilePhotoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", photoFile);
            takePictureFromCamera.putExtra(MediaStore.EXTRA_OUTPUT, profilePhotoUri);
            startActivityForResult(takePictureFromCamera, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File createImageFile() {
        long timeStamp = new Date().getTime();
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(storageDir, imageFileName + ".jpg");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            profilePhotoUri = data.getData();
            assert profilePhotoUri != null;
        }
        Glide.with(this).load(profilePhotoUri).apply(RequestOptions.circleCropTransform()).into(profilePicture);
    }

    private void takePhotoFromGallery() {
        Intent takePictureFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takePictureFromGallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(takePictureFromGallery, REQUEST_IMAGE_GALLERY);
    }

    private void selectImage() {
        String[] options = {"Gallery", "Camera", "Use Default Picture", "Cancel"};
        AlertDialog.Builder dpBuilder = new AlertDialog.Builder(this);
        dpBuilder.setTitle("Add Image");
        dpBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        takePhotoFromGallery();
                        break;
                    case 1:
                        takePhotoFromCamera();
                        break;
                    case 2:
                        profilePicture.setImageResource(R.mipmap.default_profile_picture);
                        break;
                    case 3:
                        dialog.dismiss();
                        break;
                }
            }
        });
        dpBuilder.show();
    }
}