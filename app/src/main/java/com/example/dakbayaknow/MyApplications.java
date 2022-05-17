package com.example.dakbayaknow;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MyApplications extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, appref;

    TextView destination, status, hdfStatus;
    ProgressDialog pd;

    FirebaseUser firebaseUser;
    ImageButton travelPermitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myapplications);

        getSupportActionBar().setTitle("My Applications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        // getting current user data
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("travelform");
        appref = firebaseDatabase.getReference("applications");

        // Initialising the text view
        destination = findViewById(R.id.destination);
        status = findViewById(R.id.status);
        hdfStatus = findViewById(R.id.hdfStatus);

        travelPermitButton = findViewById(R.id.travelPermitButton);

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(destination.getText().toString().contains("Fill up travel form")){
                    Intent intent = new Intent(MyApplications.this, TravelRequirements.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.getText().toString().contains("Please upload required requirements (vaccinated)")){
                    Intent intent = new Intent(MyApplications.this, UploadDocxFullyVacc.class);
                    startActivity(intent);
                    finish();
                }
                if(status.getText().toString().contains("Please upload required requirements (unvaccinated)")){
                    Intent intent = new Intent(MyApplications.this, UploadDocxUnvacc.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        hdfStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hdfStatus.getText().toString().contains("Fill up HDF")){
                    Intent intent = new Intent(MyApplications.this, HealthDeclarationForm.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        travelPermitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.getText().toString().contains("Pending")){
                    Toast.makeText(MyApplications.this, "Your application is on process. Please wait for 3-5 days", Toast.LENGTH_SHORT).show();
                }
                if(status.getText().toString().contains("Approved")){
                    openTravelPermit();
                    finish();
                }
                if(status.getText().toString().contains("Declined")){
                    Toast.makeText(MyApplications.this, "Sorry your application is declined", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    // Retrieving Data from firebase
                    String dAdd = "" + dataSnapshot1.child("dAddress").getValue();
                    String dMuni = "" + dataSnapshot1.child("dMunicipality").getValue();
                    String dProv = "" + dataSnapshot1.child("dProvince").getValue();
                    // setting data to our text view
                    destination.setText(dAdd + ", " + dMuni + ", " + dProv);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query query2 = appref.orderByChild("email").equalTo(firebaseUser.getEmail());
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    // Retrieving Data from firebase
                    String stat = "" + dataSnapshot1.child("status").getValue();
                    // setting data to our text view
                    status.setText(stat);

                    if(stat.contains("Approved")){
                        status.setTextColor(Color.parseColor("#008000"));
                    } else if(stat.contains("Declined")){
                        status.setTextColor(Color.parseColor("#FF0000"));
                    } else if(stat.contains("Please upload required requirements (vaccinated)")){
                        status.setTextColor(Color.parseColor("#FF0000"));
                    } else if(stat.contains("Please upload required requirements (unvaccinated)")){
                        status.setTextColor(Color.parseColor("#FFA500"));
                    } else if(stat.contains("Pending")){
                        status.setTextColor(Color.parseColor("#FFFF00"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("hdf");
        rootRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Boolean found, found2, found3, found4;
                String search = "NO", search2 = "No";

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String symptoms = ds.child("symptoms").getValue(String.class);
                    String sick = ds.child("sick").getValue(String.class);
                    String covid = ds.child("covid").getValue(String.class);
                    String animal = ds.child("animal").getValue(String.class);

                    found = symptoms.contains(search);
                    found2 = sick.contains(search2);
                    found3 = covid.contains(search2);
                    found4 = animal.contains(search2);

                    if (found == true && found2 == true && found3 == true && found4 == true) {
                        hdfStatus.setText("Safe");
                        hdfStatus.setTextColor(Color.parseColor("#008000"));
                    } else {
                        hdfStatus.setText("Stay at Home");
                        hdfStatus.setTextColor(Color.parseColor("#FF0000"));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?
            }
        });

    }

    public void openTravelPermit() {
        Intent intent = new Intent(this, TravelPermit.class);
        startActivity(intent);
    }
}