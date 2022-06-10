package com.example.dakbayaknow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Unvaccinated2 extends AppCompatActivity {

    Button travelFormButton;
    TextView lgu, requirement;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference fullyvaccRef;
    FirebaseUser firebaseUser;

    RecyclerView recyclerView;
    AdapterClassUnVacc adapterClassUnVacc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unvaccinated);

        firebaseAuth = FirebaseAuth.getInstance();

        // getting current user data
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fullyvaccRef = firebaseDatabase.getReference("lgu");
        fullyvaccRef.keepSynced(true);

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
                .setQuery(fullyvaccRef.orderByChild("city").equalTo(lguText), Requirements.class)
                .build();

        adapterClassUnVacc = new AdapterClassUnVacc(options);
        recyclerView.setAdapter(adapterClassUnVacc);


        travelFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lguText.equals("Cagayan De Oro City")) {
                    Toast.makeText(Unvaccinated2.this, "Sorry "+lguText+" is Restricted to Travel", Toast.LENGTH_SHORT).show();
                } else {
                    openTravelForm();
                }
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
        adapterClassUnVacc.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapterClassUnVacc.stopListening();
    }
}
