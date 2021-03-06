package com.example.dakbayaknow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FullyVaccinated_CDO extends AppCompatActivity {

    private Button travelFormButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullvacc_cdo);

        travelFormButton = (Button) findViewById(R.id.travelFormButton);
        travelFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTravelForm();
            }
        });
    }

    public void openTravelForm() {
        Intent intent = new Intent(this, TravelForm_Fullyvacc.class);
        startActivity(intent);
    }
}
