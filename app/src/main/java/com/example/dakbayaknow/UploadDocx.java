package com.example.dakbayaknow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UploadDocx extends AppCompatActivity {
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
                openUploadDocxFullyVacc();
            }
        });
    }
    public void openUploadDocxFullyVacc() {
        Intent intent = new Intent(this, UploadDocxFullyVacc.class);
        startActivity(intent);
    }
}
