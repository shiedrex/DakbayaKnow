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

public class Recovered extends AppCompatActivity {

    private Button travelFormButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovered);

        travelFormButton = (Button) findViewById(R.id.travelFormButton);
        travelFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("hdf");
                ref.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            openTravelForm();
                        }
                        else{
                            Toast.makeText(Recovered.this, "Please fill up HDF first", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Recovered.this, HealthDeclarationForm.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed, how to handle?
                    }
                });
            }
        });
    }
    public void openTravelForm(){
        Intent intent = new Intent(this, TravelForm_Unvacc.class);
        startActivity(intent);
    }
}