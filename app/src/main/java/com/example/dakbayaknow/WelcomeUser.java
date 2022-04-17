package com.example.dakbayaknow;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WelcomeUser extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference userRef;
    FirebaseAuth fAuth;
    FirebaseUser firebaseUser;

    Button getstarted;
    TextView firstname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomeuser);

        fAuth = FirebaseAuth.getInstance();
        firebaseUser = fAuth.getCurrentUser();

        userRef = database.getInstance().getReference("users");

        firstname = findViewById(R.id.firstname);
        getstarted = findViewById(R.id.getstarted);

        Animation animation = AnimationUtils.loadAnimation(WelcomeUser.this, R.anim.slide_in_left);
        firstname.startAnimation(animation);

        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeUser.this, MainActivity.class));
                finish();
            }
        });

        Query query = userRef.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    // Retrieving Data from firebase
                    String fn = "" + dataSnapshot1.child("firstname").getValue();
                    String ln = "" + dataSnapshot1.child("lastname").getValue();

                    // setting data to our text view
                    firstname.setText("Hi, " + fn + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
