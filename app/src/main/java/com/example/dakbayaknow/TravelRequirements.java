package com.example.dakbayaknow;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TravelRequirements extends AppCompatActivity {

    private Button fullyVaccinatedButton, unvaccinatedButton, recoveredButton, addbutton;

    DatabaseReference ref;
    ArrayList<Requirements> list;
    RecyclerView recyclerView;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelrequirements);

        getSupportActionBar().setTitle("Travel Requirements");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ref = FirebaseDatabase.getInstance().getReference().child("lgu");
        recyclerView = findViewById(R.id.rv);
        searchView = findViewById(R.id.searchView);

        fullyVaccinatedButton = (Button) findViewById(R.id.fullyVaccinatedButton);
        fullyVaccinatedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFullyVaccinated();
            }
        });
        unvaccinatedButton = (Button) findViewById(R.id.unvaccinatedButton);
        unvaccinatedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUnvaccinated();
            }
        });
        recoveredButton = (Button) findViewById(R.id.recoveredButton);
        recoveredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecovered();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ref!=null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        list = new ArrayList<>();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            list.add(ds.getValue(Requirements.class));
                        }
                        AdapterClass3 adapterClass = new AdapterClass3(list);
                        recyclerView.setAdapter(adapterClass);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(TravelRequirements.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(searchView!=null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return false;
                }
            });
        }
    }
    private void search(String str) {
        ArrayList<Requirements> myList = new ArrayList<>();
        for (Requirements object : list) {
            if(object.getCity().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
        }
        AdapterClass3 adapterClass = new AdapterClass3(myList);
        recyclerView.setAdapter(adapterClass);
    }

    public void openFullyVaccinated(){
        Intent intent = new Intent(this, FullyVaccinated.class);
        startActivity(intent);
    }

    public void openUnvaccinated() {
        Intent intent = new Intent(this, Unvaccinated.class);
        startActivity(intent);
    }

    public void openRecovered() {
        Intent intent = new Intent(this, Recovered.class);
        startActivity(intent);
    }

}