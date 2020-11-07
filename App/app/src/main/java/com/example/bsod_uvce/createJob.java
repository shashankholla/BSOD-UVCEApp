package com.example.bsod_uvce;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsod_uvce.mainpage.EmployerJobs;
import com.example.bsod_uvce.mainpage.Job;
import com.example.bsod_uvce.profiles.labourProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class createJob extends AppCompatActivity implements LocationListener {

    private AutoCompleteTextView addTagsET;
    private EditText jobTitle;
    private EditText jobDesc;
    private EditText addressField;
    private EditText salary;
    private EditText location;
    private LocationManager locationManager;
    private String provider;
    private TagContainerLayout mTagContainerLayout;
    private Button addSkillBtn;
    private Button getLocation;
    private int MAX_WORDS = 1;
    private Button addJob;
    private FirebaseAuth auth;
    FirebaseFirestore db;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job);
        mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tagcontainerLayout);
        addSkillBtn = findViewById(R.id.add_tag_btn);
        addTagsET = findViewById(R.id.add_tag_et);
        addJob = findViewById(R.id.add_job);
        getLocation = findViewById(R.id.get_location);
        List<String> list2 = new ArrayList<String>();
        mTagContainerLayout.setTags(list2);
        db = FirebaseFirestore.getInstance();
        jobTitle = findViewById(R.id.job_title);
        jobDesc = findViewById(R.id.job_desc);
        addressField = findViewById(R.id.my_location);
        salary = findViewById(R.id.salary);
        location = findViewById(R.id.my_location);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = getLastKnownLocation();


                if (location != null) {
                    System.out.println("Provider " + provider + " has been selected.");
                    onLocationChanged(location);
                } else {

                }

            }
        });
        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                Toast.makeText(createJob.this, "click-position:" + position + ", text:" + text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTagLongClick(final int position, String text) {

            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                AlertDialog dialog = new AlertDialog.Builder(createJob.this)
                        .setTitle("long click")
                        .setMessage("You will delete this tag!")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (position < mTagContainerLayout.getChildCount()) {
                                    list2.remove(list2.indexOf(mTagContainerLayout.getTagText(position)));
                                    mTagContainerLayout.removeTag(position);

                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();

            }
        });
        addTagsET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int wordsLength = countWords(s.toString());// words.length;
                // count == 0 means a new word is going to start
                if (count == 0 && wordsLength >= MAX_WORDS) {
                    setCharLimit(addTagsET, addTagsET.getText().length());
                } else {
                    removeFilter(addTagsET);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        addSkillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable skill = addTagsET.getText();
                String skillname = skill.toString().toLowerCase();
                if(!list2.contains(skillname)) {
                    mTagContainerLayout.addTag(skillname);
                    addTagsET.getText().clear();
                    list2.add(skillname);
                    Toast.makeText(createJob.this, list2.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(createJob.this, R.string.skill_exits, Toast.LENGTH_SHORT).show();
                }
            }
        });


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }

        addJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = jobTitle.getText().toString();
                String desc = jobDesc.getText().toString();
                String jobSalary = salary.getText().toString();
                String skills = list2.toString();
                String mylocation = location.getText().toString();



                //exampleList.add(new Job("Painting", "Line 1", "Generic, House, Construction", "3 Days", "50 Rs",true, true));
                Job thisJob = new Job(title, desc, skills, "5", jobSalary,false, false, FirebaseAuth.getInstance().getCurrentUser().getUid(), null, mylocation);
                db.collection(getString(R.string.db_jobs)).add(thisJob).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(createJob.this, "Job Was Added", Toast.LENGTH_SHORT).show();
                        Intent act = new Intent(createJob.this, EmployerJobs.class);
                        startActivity(act);
                    }
                });
            }
        });



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

            addressField.setText(mLocality); //This will display the final address.

        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException e) {
            // Handle NullPointerException
        }
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

    private int countWords(String s) {
        String trim = s.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length; // separate string around spaces
    }

    private InputFilter filter;

    private void setCharLimit(EditText et, int max) {
        filter = new InputFilter.LengthFilter(max);
        et.setFilters(new InputFilter[] { filter });
    }

    private void removeFilter(EditText et) {
        if (filter != null) {
            et.setFilters(new InputFilter[0]);
            filter = null;
        }
    }
}