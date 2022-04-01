package com.example.dakbayaknow;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Rating;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsersFeedback extends AppCompatActivity {

    FloatingActionButton addFeedback;
    Button submitButton;
    RatingBar ratingBar;
    TextView ratingNum;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth fAuth;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersfeedback);

        getSupportActionBar().setTitle("Users Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addFeedback = findViewById(R.id.addFeedback);
        submitButton = findViewById(R.id.submitButton);
        ratingBar = findViewById(R.id.ratingBar);
        ratingNum = findViewById(R.id.ratingNum);

        fAuth = FirebaseAuth.getInstance();
        dialog = new Dialog(this);

        addFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void testRatingBar() {
        final DatabaseReference ref = database.getReference("userFeedback").child("rating");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    float rating = Float.parseFloat(dataSnapshot.getValue().toString());
                    ratingBar.setRating(rating);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) ref.setValue(rating);
            }
        });
    }
    public void submit() {
        dialog.setContentView(R.layout.rating_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        Button button = dialog.findViewById(R.id.submitButton);
        dialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testRatingBar();
            }
        });
    }
}
