package com.example.bsod_uvce.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bsod_uvce.MainActivity;
import com.example.bsod_uvce.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UpdateDetails extends AppCompatActivity implements LocationListener{
    EditText nameText;
    boolean labourer=false;
    TextView locationLabel;
    Button locationButton;
    TextInputLayout nameInput;
    ImageView profilePicture;
    Uri profilePhotoUri;
    int REQUEST_IMAGE_CAPTURE = 69;
    int REQUEST_IMAGE_GALLERY = 420;
    StorageReference storageReference;
    FirebaseFirestore db;
    FirebaseUser mUser;
    FirebaseAuth auth;
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        setContentView(R.layout.activity_update_details);
        locationLabel = findViewById(R.id.locationLabel);
        nameText = findViewById(R.id.nameTextBox);
        nameInput = findViewById(R.id.nameInput);
        auth = FirebaseAuth.getInstance();
        mUser =auth.getCurrentUser();
        locationButton = findViewById(R.id.locationButton);
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });
        (findViewById(R.id.nextDetails)).setOnClickListener(view -> {
            if(validate())
            {
                String name = nameText.getText().toString();
                String location = locationLabel.getText().toString();
                updateUserProfile(mUser);
                String type="Labourer";
                if(!labourer)
                    type="Employer";
                Intent intent = new Intent(UpdateDetails.this, SelectSkills.class);
                intent.putExtra("Name", name);
                intent.putExtra("Location", location);
                intent.putExtra("Type", type);
                startActivity(intent);
            }
        });

        profilePicture = findViewById(R.id.profilePictureSignUp);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }
    public void onRadioButtonClicked(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId())
        {
            case R.id.labourerRadio:
                if(checked)
                    labourer=true;
                break;
            case R.id.employerRadio:
                if(checked)
                    labourer=false;
                break;
        }
    }
    public boolean validate()
    {
        boolean flag=true;
        String name = nameText.getText().toString();
        String location = locationLabel.getText().toString();
        if (name.isEmpty()) {
            nameInput.setError("Please enter a valid name");
            flag = false;
        } else
            nameInput.setError(null);
        if(location.equals("0 0"))
        {
            Toast.makeText(this, "Please select your location", Toast.LENGTH_LONG).show();
            flag = true; // Change after fixing location
        }
        return flag;
    }

    private void selectImage() {
        String[] options = {"Gallery", "Camera", "Use Default Picture", "Cancel"};
        AlertDialog.Builder dpBuilder = new AlertDialog.Builder(UpdateDetails.this);
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
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK)
            profilePhotoUri = data.getData();
        Log.e("Profile Photo Uri", profilePhotoUri.toString());
        Glide.with(this).load(profilePhotoUri).apply(RequestOptions.circleCropTransform()).into(profilePicture);
    }
    private void takePhotoFromGallery() {
        Intent takePictureFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takePictureFromGallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(takePictureFromGallery, REQUEST_IMAGE_GALLERY);
    }

    private void updateUserProfile(FirebaseUser user) {
        if (profilePhotoUri != null) {
            StorageReference profilePictureUpload = storageReference.child("Images/Profile Pictures/" + user.getUid());
            Log.e("Shit hit the fan", user.getUid());
            profilePictureUpload.putFile(profilePhotoUri).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateDetails.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                    Log.e("Shit hit the fan", e.toString());
                }
            });
        }
        String name = nameText.getText().toString();
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).setPhotoUri(profilePhotoUri).build();
        user.updateProfile(profileUpdate).addOnCompleteListener(task -> Toast.makeText(UpdateDetails.this, "Registration Successful", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateDetails.this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = getLastKnownLocation();

        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {

        }
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return null;
            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
    @Override
    public void onLocationChanged(Location location) {
        //You had this as int. It is advised to have Lat/Loing as double.
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);

            String mCountry = address.get(0).getCountryName();
            String mLocality = address.get(0).getLocality();
            String mArea = address.get(0).getSubLocality();
            String fnialAddress = builder.toString(); //This is the complete address.

            locationLabel.setText(mLocality);

        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException e) {
            // Handle NullPointerException
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates((LocationListener) this);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}