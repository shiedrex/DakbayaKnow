package com.example.dakbayaknow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class TravelPermit extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView fullname, travellerType, origin, destination, dateTravel, expectedArrival, status;
    ProgressDialog pd;

    FirebaseUser firebaseUser;
    Button generateQRButton;
    ImageView qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelpermit);

        getSupportActionBar().setTitle("Travel Permit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        // getting current user data
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("travelform");

        // Initialising the text view and imageview
        fullname = findViewById(R.id.fullnameText);
        travellerType = findViewById(R.id.travellerTypeText);
        origin = findViewById(R.id.originText);
        destination = findViewById(R.id.destinationText);
        dateTravel = findViewById(R.id.dateTravelText);
        expectedArrival = findViewById(R.id.expectedArrivalText);
        status = findViewById(R.id.statusText);

        generateQRButton = findViewById(R.id.generateQRCodeButton);
        qrCode = findViewById(R.id.qrCode);

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    // Retrieving Data from firebase
                    String fn = "" + dataSnapshot1.child("firstname").getValue();
                    String ln = "" + dataSnapshot1.child("lastname").getValue();
                    String traveller = "" + dataSnapshot1.child("travellerType").getValue();
                    String cAdd = "" + dataSnapshot1.child("cAddress").getValue();
                    String cMuni = "" + dataSnapshot1.child("cMunicipality").getValue();
                    String cProv = "" + dataSnapshot1.child("cProvince").getValue();
                    String dAdd = "" + dataSnapshot1.child("dAddress").getValue();
                    String dMuni = "" + dataSnapshot1.child("dMunicipality").getValue();
                    String dProv = "" + dataSnapshot1.child("dProvince").getValue();
                    String depart = "" + dataSnapshot1.child("departure").getValue();
                    String arriv = "" + dataSnapshot1.child("arrival").getValue();

                    // setting data to our text view
                    fullname.setText(fn+" "+ln);
                    travellerType.setText(traveller);
                    origin.setText(cAdd+", "+cMuni+", "+cProv);
                    destination.setText(dAdd+", "+dMuni+", "+dProv);
                    dateTravel.setText(depart);
                    expectedArrival.setText(arriv);
                    status.setText("Pending");

                    MultiFormatWriter writer = new MultiFormatWriter();
                    try {

                        BitMatrix matrix = writer.encode("Full Name: "+fn+" "+ln+
                                                                 "\n\nOrigin: " +cAdd+", "+cMuni+", "+cProv+
                                                                 "\n\nDestination: " +dAdd+", "+dMuni+", "+dProv+
                                                                 "\n\nDeparture: " +depart+
                                                                 "\n\nArrival: " +arriv,
                                BarcodeFormat.QR_CODE,350, 350);
                        BarcodeEncoder encoder = new BarcodeEncoder();
                        Bitmap bitmap = encoder.createBitmap(matrix);
                        qrCode.setImageBitmap(bitmap);

                        InputMethodManager manager = (InputMethodManager) getSystemService(
                                Context.INPUT_METHOD_SERVICE
                        );
                        manager.hideSoftInputFromWindow(fullname.getApplicationWindowToken(),0);
                        manager.hideSoftInputFromWindow(travellerType.getApplicationWindowToken(),0);
                    }catch (WriterException e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        generateQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}