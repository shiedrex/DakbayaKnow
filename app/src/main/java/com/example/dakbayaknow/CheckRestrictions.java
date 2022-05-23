package com.example.dakbayaknow;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckRestrictions extends AppCompatActivity {

    DatabaseReference ref;
    ArrayList<Restrictions> list;
    RecyclerView recyclerView;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkrestrictions);

        getSupportActionBar().setTitle("Check Restrictions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ref = FirebaseDatabase.getInstance().getReference().child("lgu");
        ref.keepSynced(true);
        recyclerView = findViewById(R.id.rv);
        searchView = findViewById(R.id.searchView);
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
                            list.add(ds.getValue(Restrictions.class));
                        }
                        AdapterClass2 adapterClass = new AdapterClass2(list, getApplicationContext());
                        recyclerView.setAdapter(adapterClass);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(CheckRestrictions.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        ArrayList<Restrictions> myList = new ArrayList<>();
        for (Restrictions object : list) {
            if(object.getCity().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
        }
        AdapterClass2 adapterClass = new AdapterClass2(myList, getApplicationContext());
        recyclerView.setAdapter(adapterClass);
    }
}