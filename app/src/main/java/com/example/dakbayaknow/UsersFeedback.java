package com.example.dakbayaknow;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersFeedback extends AppCompatActivity {

    FloatingActionButton addFeedback;
    Button submitButton;
    RatingBar ratingBar;
    TextView ratingNum;
    float rateValue;

    FirebaseDatabase database;
    DatabaseReference ref, ref2, userRef;
    FirebaseAuth fAuth;
    FirebaseUser firebaseUser;

    Dialog dialog;

    ArrayList<Feedback> list;
    RecyclerView recyclerView;

    Feedback value;

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
        firebaseUser = fAuth.getCurrentUser();

        dialog = new Dialog(this);

        recyclerView = findViewById(R.id.rv);

        value = new Feedback();

        ref = database.getInstance().getReference("userFeedback").child(fAuth.getCurrentUser().getUid());
        ref2 = FirebaseDatabase.getInstance().getReference().child("userFeedback");
        userRef = database.getInstance().getReference("users");
        ref.keepSynced(true);
        ref2.keepSynced(true);
        userRef.keepSynced(true);

        addFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ref2 != null) {
            ref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        list = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            list.add(ds.getValue(Feedback.class));
                        }
                        AdapterClass4 adapterClass = new AdapterClass4(list);
                        recyclerView.setAdapter(adapterClass);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UsersFeedback.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void submit() {
        dialog.setContentView(R.layout.rating_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        TextView textView = dialog.findViewById(R.id.ratingNum);
        TextView fullname = dialog.findViewById(R.id.fullname);
        Button submit = dialog.findViewById(R.id.submitButton);
        ImageView profImage = dialog.findViewById(R.id.profImage);
        EditText comment = dialog.findViewById(R.id.comment);
        dialog.show();

        Query query = userRef.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    // Retrieving Data from firebase
                    String fn = "" + dataSnapshot1.child("firstname").getValue();
                    String ln = "" + dataSnapshot1.child("lastname").getValue();

                    // setting data to our text view
                    fullname.setText(fn + " " + ln);
                    String image = "" + dataSnapshot1.child("profileImage").child("imageUrl").getValue();

                    try {
                        Glide.with(getApplicationContext()).load(image).placeholder(R.drawable.ic_profilepic).into(profImage);
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateValue = ratingBar.getRating();

                if(rateValue==0) {
                    textView.setText("0.0");
                }
                else if(rateValue<=1 && rateValue>0) {
                    textView.setText("Bad: " + rateValue + "/5.0");
                }
                else if(rateValue<=2 && rateValue>1){
                    textView.setText("OK: " + rateValue + "/5.0");
                }
                else if(rateValue<=3 && rateValue>2){
                    textView.setText("Good: " + rateValue + "/5.0");
                }
                else if(rateValue<=4 && rateValue>3){
                    textView.setText("Very Good: " + rateValue + "/5.0");
                }
                else if(rateValue<=5 && rateValue>4){
                    textView.setText("Best: " + rateValue + "/5.0");
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value.setRating(textView.getText().toString().trim());
                value.setUsername(fullname.getText().toString().trim());
                value.setComment(comment.getText().toString().trim());
                ratingBar.setRating(0);

                ref.setValue(value);

                dialog.setContentView(R.layout.rating_success_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button ok = dialog.findViewById(R.id.okButton);
                dialog.show();

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
