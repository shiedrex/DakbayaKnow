package com.example.dakbayaknow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FullyVaccinated2 extends AppCompatActivity {

    Button travelFormButton;
    TextView lgu, requirement;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    FirebaseUser firebaseUser;

    RecyclerView recyclerView;
    AdapterClassFullyVacc adapterClassFullyVacc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullyvaccinated);

        firebaseAuth = FirebaseAuth.getInstance();

        // getting current user data
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("lgu");
        ref.keepSynced(true);

        travelFormButton = findViewById(R.id.travelFormButton);
        lgu = findViewById(R.id.lgu);
        requirement = findViewById(R.id.requirement);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String lguText, reqText;
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            lguText = null;
            reqText = null;
        } else {
            lguText = extras.getString(Intent.EXTRA_TEXT);
            reqText = extras.getString("requirement");
        }

        lgu.setText(lguText);
        requirement.setText(reqText);

        FirebaseRecyclerOptions<Requirements> options = new FirebaseRecyclerOptions.Builder<Requirements>()
                .setQuery(ref.orderByChild("city").equalTo(lguText), Requirements.class)
                .build();

        adapterClassFullyVacc = new AdapterClassFullyVacc(options);
        recyclerView.setAdapter(adapterClassFullyVacc);


        travelFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTravelForm();
            }
        });
    }

    public void openTravelForm() {
        Intent intent = new Intent(this, TravelForm_Fullyvacc.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterClassFullyVacc.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterClassFullyVacc.stopListening();
    }
}
