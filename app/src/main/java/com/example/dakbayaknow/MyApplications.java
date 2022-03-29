package com.example.dakbayaknow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyApplications extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

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

        // Initialising the text view
        destination = findViewById(R.id.destination);
        status = findViewById(R.id.status);
        hdfStatus = findViewById(R.id.hdfStatus);

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("travelform");
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        status.setText("In-progress");
                        status.setTextColor(Color.parseColor("#008000"));
                    }
                }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?
            }
        });

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("uploadDocx");
        dr.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    status.setText("Checking");
                    status.setTextColor(Color.parseColor("#FFFF00"));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?
            }
        });

        travelPermitButton = (ImageButton) findViewById(R.id.travelPermitButton);
        travelPermitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTravelPermit();
            }
        });

    }

    public void openTravelPermit() {
        Intent intent = new Intent(this, TravelPermit.class);
        startActivity(intent);
    }

}