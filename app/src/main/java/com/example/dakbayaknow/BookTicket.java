package com.example.dakbayaknow;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class BookTicket extends AppCompatActivity {
    private ImageButton cokaliongButton, transAsiaButton, toGoButton, liteFerriesButton, backButtonBookTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookticket);

        getSupportActionBar().setTitle("Book Ticket");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cokaliongButton = (ImageButton) findViewById(R.id.cokaliongButton);
        cokaliongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cokaliongshipping.com/"));
                startActivity(browserIntent);
            }
        });
        transAsiaButton = (ImageButton) findViewById(R.id.transAsiaButton);
        transAsiaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.transasiashipping.com/"));
                startActivity(browserIntent);
            }
        });
        toGoButton = (ImageButton) findViewById(R.id.toGoButton);
        toGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://travel.2go.com.ph/"));
                startActivity(browserIntent);
            }
        });
        liteFerriesButton = (ImageButton) findViewById(R.id.liteFerriesButton);
        liteFerriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://liteferries.com.ph/"));
                startActivity(browserIntent);
            }
        });

    }
}
