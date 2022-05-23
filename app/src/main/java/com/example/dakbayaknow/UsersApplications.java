package com.example.dakbayaknow;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UsersApplications extends AppCompatActivity {

    DatabaseReference ref;
    ArrayList<Applications> list;
    RecyclerView recyclerView;
    SearchView searchView;
    AdapterClassApplications adapterClassApplications;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersapplications);

        getSupportActionBar().setTitle("My Applications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        // getting current user data
        firebaseUser = firebaseAuth.getCurrentUser();

        ref = FirebaseDatabase.getInstance().getReference("applications");
        ref.keepSynced(true);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchView);

        FirebaseRecyclerOptions<Applications> options = new FirebaseRecyclerOptions.Builder<Applications>()
                .setQuery(ref.orderByChild("email").equalTo(firebaseUser.getEmail()), Applications.class)
                .build();

        adapterClassApplications = new AdapterClassApplications(options);
        recyclerView.setAdapter(adapterClassApplications);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterClassApplications.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterClassApplications.stopListening();
    }
}