package com.example.dakbayaknow;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TravelForm_Submit_FullyVacc extends AppCompatActivity {

    private Button bookTicketButton, HDFButton, uploadDocxButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelform_submit);

        getSupportActionBar().setTitle("Travel Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bookTicketButton = (Button) findViewById(R.id.bookTicketButton);
        bookTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBookTicket();
            }
        });
        HDFButton = (Button) findViewById(R.id.HDFButton);
        HDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHDF();
            }
        });
        uploadDocxButton = (Button) findViewById(R.id.uploadDocxButton);
        uploadDocxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUploadDocx();
            }
        });
    }
    public void openBookTicket() {
        Intent intent = new Intent(this, BookTicket.class);
        startActivity(intent);
    }
    public void openHDF() {
        Intent intent = new Intent(this, HealthDeclarationForm.class);
        startActivity(intent);
    }
    public void openUploadDocx() {
        Intent intent = new Intent(this, UploadDocx.class);
        startActivity(intent);
    }
}