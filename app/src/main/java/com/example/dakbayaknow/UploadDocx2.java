package com.example.dakbayaknow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UploadDocx2 extends AppCompatActivity {
    private Button checkRequirementsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaddocx);

        getSupportActionBar().setTitle("Upload Docx");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkRequirementsButton = findViewById(R.id.checkRequirementsButton);
        checkRequirementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUploadDocxUnVacc();
            }
        });
    }
    public void openUploadDocxUnVacc() {
        Intent intent = new Intent(this, UploadDocxUnvacc.class);
        startActivity(intent);
    }
}
