package com.example.dakbayaknow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Unvaccinated extends AppCompatActivity {

    private Button travelFormButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unvacc);

        travelFormButton = (Button) findViewById(R.id.travelFormButton);
        travelFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTravelForm();
            }
        });
}

    public void openTravelForm() {
        Intent intent = new Intent(this, TravelForm_Unvacc.class);
        startActivity(intent);
    }
}