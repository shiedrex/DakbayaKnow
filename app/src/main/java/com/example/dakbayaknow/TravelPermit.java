package com.example.dakbayaknow;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TravelPermit extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReference2;

    TextView fullname, travellerType, origin, destination, dateTravel, expectedArrival, status, noTravelPermit, fillUp;
    ProgressDialog pd;

    FirebaseUser firebaseUser;
    Button saveTravelPermit;
    ImageView qrCode;

    LinearLayout linearLayout;

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
        databaseReference = firebaseDatabase.getReference("travelform");
        databaseReference2 = firebaseDatabase.getReference("applications");
        databaseReference.keepSynced(true);
        databaseReference2.keepSynced(true);

        // Initialising the text view and imageview
        fullname = findViewById(R.id.fullnameText);
        travellerType = findViewById(R.id.travellerTypeText);
        origin = findViewById(R.id.originText);
        destination = findViewById(R.id.destinationText);
        dateTravel = findViewById(R.id.dateTravelText);
        expectedArrival = findViewById(R.id.expectedArrivalText);
        status = findViewById(R.id.statusText);
        noTravelPermit = findViewById(R.id.noTravelPermit);
        fillUp = findViewById(R.id.fillUp);

        saveTravelPermit = findViewById(R.id.saveTravelPermitButton);
        qrCode = findViewById(R.id.qrCode);

        linearLayout = findViewById(R.id.layout);

        linearLayout.setVisibility(View.GONE);
        saveTravelPermit.setVisibility(View.GONE);
        noTravelPermit.setVisibility(View.VISIBLE);
        fillUp.setVisibility(View.VISIBLE);

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.getText().toString().contains("Please upload required requirements (vaccinated)")){
                    Intent intent = new Intent(TravelPermit.this, UploadDocxFullyVacc.class);
                    startActivity(intent);
                    finish();
                }
                if(status.getText().toString().contains("Please upload required requirements (unvaccinated)")){
                    Intent intent = new Intent(TravelPermit.this, UploadDocxUnvacc.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        Query query2 = databaseReference2.orderByChild("email").equalTo(firebaseUser.getEmail());
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    // Retrieving Data from firebase
                    String stat = "" + dataSnapshot1.child("status").getValue();
                    // setting data to our text view
                    status.setText(stat);

                    if(stat.contains("Approved")){
                        status.setTextColor(Color.parseColor("#008000"));
                        linearLayout.setVisibility(View.VISIBLE);
                        saveTravelPermit.setVisibility(View.VISIBLE);
                        noTravelPermit.setVisibility(View.GONE);
                        fillUp.setVisibility(View.GONE);
                    } else if(stat.contains("Declined")){
                        status.setTextColor(Color.parseColor("#FF0000"));
                        linearLayout.setVisibility(View.GONE);
                        saveTravelPermit.setVisibility(View.GONE);
                        noTravelPermit.setVisibility(View.VISIBLE);
                        fillUp.setVisibility(View.VISIBLE);
                        fillUp.setText("Application Declined");
                    }

                    String fn = "" + dataSnapshot1.child("fullname").getValue();
                    String traveller = "" + dataSnapshot1.child("travellerType").getValue();
                    String des = "" + dataSnapshot1.child("destination").getValue();
                    String orig = "" + dataSnapshot1.child("origin").getValue();
                    String depart = "" + dataSnapshot1.child("departure").getValue();
                    String arriv = "" + dataSnapshot1.child("arrival").getValue();
                    // setting data to our text view
                    fullname.setText(fn);
                    travellerType.setText(traveller);
                    origin.setText(des);
                    destination.setText(orig);
                    dateTravel.setText(depart);
                    expectedArrival.setText(arriv);

                    MultiFormatWriter writer = new MultiFormatWriter();
                    try {

                        BitMatrix matrix = writer.encode("Full Name: "+fn+
                                        "\n\nOrigin: " +orig+
                                        "\n\nDestination: " +des+
                                        "\n\nDeparture: " +depart+
                                        "\n\nArrival: " +arriv+
                                        "\n\nStatus: " +stat,
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if(!hasPermissions(this,PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else{
            saveTravelPermit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveTP();
                }
            });
        }
    }

    private void saveTP() {
        linearLayout.setDrawingCacheEnabled(true);
        linearLayout.buildDrawingCache();
        linearLayout.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        Bitmap bitmap = linearLayout.getDrawingCache();
        save(bitmap);
    }

    private void save(Bitmap bitmap) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStamp  = dateFormat.format(new Date());

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(root+"/Download");
        String filename = "TravelPermit";
        File myfile = new File(file, filename + timeStamp +".jpg");

        if(myfile.exists()){
            myfile.delete();
        }

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(myfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            Toast.makeText(this, "Travel Permit Saved", Toast.LENGTH_SHORT).show();
            linearLayout.setDrawingCacheEnabled(false);

        } catch (Exception e){
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context!=null && permissions!=null){
            for(String permission : permissions) {
                if(ActivityCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}