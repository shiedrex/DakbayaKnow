package com.example.dakbayaknow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TravelForm_Submit_Unvacc extends AppCompatActivity {

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
        Intent intent = new Intent(this, UploadDocx2.class);
        startActivity(intent);
    }
}