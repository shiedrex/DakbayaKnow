package com.example.dakbayaknow;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadDocxFullyVacc extends AppCompatActivity {
    private Button govIdButton, fVaccReq1Button, fVaccReq2Button, fVaccReq3Button, fVaccReq4Button, fVaccReq5Button, submitButton;

    String SITE_KEY = "6LeQMXkeAAAAAOmnUZ2R7k0AV-FLhnOWQj3HyriO";
    String SECRET_KEY = "6LeQMXkeAAAAAC-iU02tSyfJsxo7xhYRCuVaB0Zl";
    RequestQueue queue;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, govIdRef, reqRef, travelRef, appref, fullyvaccRef;

    FirebaseAuth fAuth;
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    ListView requirementLV;
    RequirementListAdapter requirementListAdapter;

    ImageView govIdImage, fVaccReq1Image, fVaccReq2Image, fVaccReq3Image, fVaccReq4Image, fVaccReq5Image;

    TextView fullname, currentAddress, destinationAddress, departure, arrival,
            govIDRequired, govIdImageRequired, fVaccReq1Required, fVaccReq2Required, fVaccReq3Required, fVaccReq4Required, fVaccReq5Required,
            fVaccReq1, fVaccReq2, fVaccReq3, fVaccReq4, fVaccReq5;

    LinearLayout req1L, req2L, req3L, req4L, req5L;

    TextInputEditText govIdNumber;
    AutoCompleteTextView spinner_govId;

    Docx value;
    Applications value2;
    Uri govIdImageUri, fVaccReq1ImageUri, fVaccReq2ImageUri, fVaccReq3ImageUri, fVaccReq4ImageUri, fVaccReq5ImageUri;

    Dialog dialog;
    ProgressDialog progressDialog;
    List<RequirementModel> requirementModelList;
    byte[] bb, bb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaddocxfullyvacc);

        getSupportActionBar().setTitle("Upload Docx");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        // getting current user data
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("uploadDocx");

        requirementLV = findViewById(R.id.requirementLV);
        requirementModelList = new ArrayList<>();
        requirementListAdapter = new RequirementListAdapter(requirementModelList, this);
        requirementLV.setAdapter(requirementListAdapter);

        travelRef = firebaseDatabase.getReference("travelform");
        govIdRef = firebaseDatabase.getReference("uploadDocx").child(firebaseAuth.getCurrentUser().getUid());
        reqRef = firebaseDatabase.getReference("uploadDocx").child(firebaseAuth.getCurrentUser().getUid());
        appref = FirebaseDatabase.getInstance().getReference("applications");
        fullyvaccRef = firebaseDatabase.getReference("lgu");

        queue = Volley.newRequestQueue(getApplicationContext());
        //button
        govIdButton = findViewById((R.id.govIdButton));
        fVaccReq1Button = findViewById(R.id.fVaccReq1Button);
        fVaccReq2Button = findViewById(R.id.fVaccReq2Button);
        fVaccReq3Button = findViewById(R.id.fVaccReq3Button);
        fVaccReq4Button = findViewById(R.id.fVaccReq4Button);
        fVaccReq5Button = findViewById(R.id.fVaccReq5Button);
        submitButton = findViewById(R.id.submitButton);
        //spinner
        spinner_govId = findViewById(R.id.spinner_govId);
        //text input
        govIdNumber = findViewById(R.id.govIdNumber);
        //imageview
        govIdImage = findViewById(R.id.govIdImage);
        fVaccReq1Image = findViewById(R.id.fVaccReq1Image);
        fVaccReq2Image = findViewById(R.id.fVaccReq2Image);
        fVaccReq3Image = findViewById(R.id.fVaccReq3Image);
        fVaccReq4Image = findViewById(R.id.fVaccReq4Image);
        fVaccReq5Image = findViewById(R.id.fVaccReq5Image);
        //required
        govIDRequired = findViewById(R.id.govIDRequired);
        govIdImageRequired = findViewById(R.id.govIdImageRequired);
        fVaccReq1Required = findViewById(R.id.fVaccReq1Required);
        fVaccReq2Required = findViewById(R.id.fVaccReq2Required);
        fVaccReq3Required = findViewById(R.id.fVaccReq3Required);
        fVaccReq4Required = findViewById(R.id.fVaccReq4Required);
        fVaccReq5Required = findViewById(R.id.fVaccReq5Required);

        //retrieve from database
        fullname = findViewById(R.id.fullnameText);
        currentAddress = findViewById(R.id.currentAddressText);
        destinationAddress = findViewById(R.id.destinationText);
        departure = findViewById(R.id.departureText);
        arrival = findViewById(R.id.arrivalText);

        fVaccReq1 = findViewById(R.id.fVaccReq1);
        fVaccReq2 = findViewById(R.id.fVaccReq2);
        fVaccReq3 = findViewById(R.id.fVaccReq3);
        fVaccReq4 = findViewById(R.id.fVaccReq4);
        fVaccReq5 = findViewById(R.id.fVaccReq5);
        //LinearLayout
        req1L = findViewById(R.id.req1);
        req2L = findViewById(R.id.req2);
        req3L = findViewById(R.id.req3);
        req4L = findViewById(R.id.req4);
        req5L = findViewById(R.id.req5);

        value = new Docx();
        value2 = new Applications();

        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        dialog = new Dialog(this);
        progressDialog = new ProgressDialog(this);

        List<String> Categories = new ArrayList<>();
        Categories.add("UMID");
        Categories.add("Driver's License");
        Categories.add("Philhealth Card");
        Categories.add("SSS ID");
        Categories.add("Passport");
        Categories.add("TIN ID");
        Categories.add("Voter's ID");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this, R.layout.textview_gray, Categories);
        dataAdapter.setDropDownViewResource(R.layout.textview_gray);
        spinner_govId.setAdapter(dataAdapter);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.CAMERA};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            govIdButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.setContentView(R.layout.pick_image_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Button cam = dialog.findViewById(R.id.camera);
                    Button gall = dialog.findViewById(R.id.gallery);
                    ImageButton close = dialog.findViewById(R.id.closeButton);
                    dialog.show();

                    cam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            pick_camera_govid();
                            Toast.makeText(UploadDocxFullyVacc.this, "Under Development", Toast.LENGTH_SHORT).show();
                        }
                    });

                    gall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pick_gallery_govid();
                        }
                    });

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });
            fVaccReq1Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.setContentView(R.layout.pick_image_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Button cam = dialog.findViewById(R.id.camera);
                    Button gall = dialog.findViewById(R.id.gallery);
                    ImageButton close = dialog.findViewById(R.id.closeButton);
                    dialog.show();

                    cam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(UploadDocxFullyVacc.this, "Under Development", Toast.LENGTH_SHORT).show();
//                            pick_camera_vacccard();
                        }
                    });

                    gall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pick_gallery_req1();
                        }
                    });

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });
            fVaccReq2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.setContentView(R.layout.pick_image_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Button cam = dialog.findViewById(R.id.camera);
                    Button gall = dialog.findViewById(R.id.gallery);
                    ImageButton close = dialog.findViewById(R.id.closeButton);
                    dialog.show();

                    cam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(UploadDocxFullyVacc.this, "Under Development", Toast.LENGTH_SHORT).show();
//                            pick_camera_vacccard();
                        }
                    });

                    gall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pick_gallery_req2();
                        }
                    });

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });
            fVaccReq3Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.setContentView(R.layout.pick_image_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Button cam = dialog.findViewById(R.id.camera);
                    Button gall = dialog.findViewById(R.id.gallery);
                    ImageButton close = dialog.findViewById(R.id.closeButton);
                    dialog.show();

                    cam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(UploadDocxFullyVacc.this, "Under Development", Toast.LENGTH_SHORT).show();
//                            pick_camera_vacccard();
                        }
                    });

                    gall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pick_gallery_req3();
                        }
                    });

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });
            fVaccReq4Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.setContentView(R.layout.pick_image_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Button cam = dialog.findViewById(R.id.camera);
                    Button gall = dialog.findViewById(R.id.gallery);
                    ImageButton close = dialog.findViewById(R.id.closeButton);
                    dialog.show();

                    cam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(UploadDocxFullyVacc.this, "Under Development", Toast.LENGTH_SHORT).show();
//                            pick_camera_vacccard();
                        }
                    });

                    gall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pick_gallery_req4();
                        }
                    });

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });
            fVaccReq5Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.setContentView(R.layout.pick_image_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    Button cam = dialog.findViewById(R.id.camera);
                    Button gall = dialog.findViewById(R.id.gallery);
                    ImageButton close = dialog.findViewById(R.id.closeButton);
                    dialog.show();

                    cam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(UploadDocxFullyVacc.this, "Under Development", Toast.LENGTH_SHORT).show();
//                            pick_camera_vacccard();
                        }
                    });

                    gall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pick_gallery_req5();
                        }
                    });

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String govId = spinner_govId.getText().toString().trim();
                String govIdNum = govIdNumber.getText().toString().trim();

                if (govId.isEmpty()) {
                    govIDRequired.setText("Government ID is required!");
                    spinner_govId.requestFocus();
                    return;
                } else {
                    govIDRequired.setText(null);
                }

                if (govIdNum.isEmpty()) {
                    govIdNumber.setError("Government ID Number is required!");
                    govIdNumber.requestFocus();
                    return;
                }

                if (govIdImage.getDrawable() == null) {
                    govIdImageRequired.setText("Government ID Photo is required");
                    govIdImageRequired.requestFocus();
                    return;
                } else {
                    govIdImageRequired.setText(null);
                }

                if (fVaccReq1Image.getDrawable() == null && req1L.isShown()) {
                    fVaccReq1Required.setText("This is required");
                    fVaccReq1Required.requestFocus();
                    return;
                } else {
                    fVaccReq1Required.setText(null);
                }


                if (fVaccReq2Image.getDrawable() == null && req2L.isShown()) {
                    fVaccReq2Required.setText("This is required");
                    fVaccReq2Required.requestFocus();
                    return;
                } else {
                    fVaccReq2Required.setText(null);
                }


                if (fVaccReq3Image.getDrawable() == null && req3L.isShown()) {
                    fVaccReq3Required.setText("This is required");
                    fVaccReq3Required.requestFocus();
                    return;
                } else {
                    fVaccReq3Required.setText(null);
                }


                if (fVaccReq4Image.getDrawable() == null && req4L.isShown()) {
                    fVaccReq4Required.setText("This is required");
                    fVaccReq4Required.requestFocus();
                    return;
                } else {
                    fVaccReq4Required.setText(null);
                }


                if (fVaccReq5Image.getDrawable() == null && req5L.isShown()) {
                    fVaccReq5Required.setText("This is required");
                    fVaccReq5Required.requestFocus();
                    return;
                }  else {
                    fVaccReq5Required.setText(null);
                }


                verifyGoogleReCAPTCHA();
            }
        });

        Query query = travelRef.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String fn = "" + dataSnapshot1.child("firstname").getValue();
                    String mi = "" + dataSnapshot1.child("middleinitial").getValue();
                    String ln = "" + dataSnapshot1.child("lastname").getValue();
                    String sn = "" + dataSnapshot1.child("suffixname").getValue();
                    String cAdd = "" + dataSnapshot1.child("cAddress").getValue();
                    String cMuni = "" + dataSnapshot1.child("cMunicipality").getValue();
                    String cProv = "" + dataSnapshot1.child("cProvince").getValue();
                    String dAdd = "" + dataSnapshot1.child("dAddress").getValue();
                    String dMuni = "" + dataSnapshot1.child("dMunicipality").getValue();
                    String dProv = "" + dataSnapshot1.child("dProvince").getValue();
                    String depart = "" + dataSnapshot1.child("departure").getValue();
                    String arriv = "" + dataSnapshot1.child("arrival").getValue();

                    // setting data to our text view
                    fullname.setText(fn + " " + mi + " " + ln + " " + sn);
                    currentAddress.setText(cAdd + ", " + cMuni + ", " + cProv);
                    destinationAddress.setText(dAdd + ", " + dMuni + ", " + dProv);
                    departure.setText(depart);
                    arrival.setText(arriv);

                    Query query2 = fullyvaccRef.orderByChild("city").equalTo(dMuni);
                    query2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            requirementModelList.clear();
//                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
//                                Log.d("Log", dataSnapshot2.getKey());
//                                for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
//                                    Log.d("Log", dataSnapshot3.getKey());
//                                    for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
//                                        Log.d("Log", dataSnapshot4.getValue().toString());
//                                        RequirementModel model = new RequirementModel();
//                                        model.setName(dataSnapshot4.getValue().toString());
//                                        requirementModelList.add(model);
//                                        requirementListAdapter.notifyDataSetChanged();
//                                    }
//                                }
//                            }

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String req1 = "" + dataSnapshot1.child("fVaccReq1").getValue();
                                String req2 = "" + dataSnapshot1.child("fVaccReq2").getValue();
                                String req3 = "" + dataSnapshot1.child("fVaccReq3").getValue();
                                String req4 = "" + dataSnapshot1.child("fVaccReq4").getValue();
                                String req5 = "" + dataSnapshot1.child("fVaccReq5").getValue();

                                // setting data to our text view
                                if (dataSnapshot.exists()) {
                                    fVaccReq1.setText(req1);
                                    fVaccReq2.setText(req2);
                                    fVaccReq3.setText(req3);
                                    fVaccReq4.setText(req4);
                                    fVaccReq5.setText(req5);
                                }
                                //requirement 1
                                if (!dataSnapshot1.hasChild("fVaccReq1") || req1.toLowerCase().contains("valid id") || req1.toLowerCase().contains("dakbayaknow travel permit")) {
                                    req1L.setVisibility(View.GONE);
                                } else {
                                    req1L.setVisibility(View.VISIBLE);
                                }
                                //requirement 2
                                if (!dataSnapshot1.hasChild("fVaccReq2") || req2.toLowerCase().contains("valid id") || req2.toLowerCase().contains("dakbayaknow travel permit")) {
                                    req2L.setVisibility(View.GONE);
                                } else {
                                    req2L.setVisibility(View.VISIBLE);
                                }
                                //requirement 3
                                if (!dataSnapshot1.hasChild("fVaccReq3") || req3.toLowerCase().contains("valid id") || req3.toLowerCase().contains("dakbayaknow travel permit")) {
                                    req3L.setVisibility(View.GONE);
                                } else {
                                    req3L.setVisibility(View.VISIBLE);
                                }
                                //requirement 4
                                if (!dataSnapshot1.hasChild("fVaccReq4") || req4.toLowerCase().contains("valid id") || req3.toLowerCase().contains("dakbayaknow travel permit")) {
                                    req4L.setVisibility(View.GONE);
                                } else {
                                    req4L.setVisibility(View.VISIBLE);
                                }
                                //requirement 5
                                if (!dataSnapshot1.hasChild("fVaccReq5") || req5.toLowerCase().contains("valid id") || req3.toLowerCase().contains("dakbayaknow travel permit")) {
                                    req5L.setVisibility(View.GONE);
                                } else {
                                    req5L.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verifyGoogleReCAPTCHA() {

        // below line is use for getting our safety
        // net client and verify with reCAPTCHA
        SafetyNet.getClient(this).verifyWithRecaptcha(SITE_KEY)
                // after getting our client we have
                // to add on success listener.
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        // in below line we are checking the response token.
                        if (!response.getTokenResult().isEmpty()) {
                            // if the response token is not empty then we
                            // are calling our verification method.
                            handleVerification(response.getTokenResult());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // this method is called when we get any error.
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            // below line is use to display an error message which we get.
                            Log.d("TAG", "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                        } else {
                            // below line is use to display a toast message for any error.
                            Toast.makeText(UploadDocxFullyVacc.this, "Error found is : " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    protected void handleVerification(final String responseToken) {
        // inside handle verification method we are
        // verifying our user with response token.
        // url to sen our site key and secret key
        // to below url using POST method.
        String url = "https://www.google.com/recaptcha/api/siteverify";

        // in this we are making a string request and
        // using a post method to pass the data.
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // inside on response method we are checking if the
                        // response is successful or not.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                // if the response is successful then we are
                                // showing below toast message.
                                Toast.makeText(UploadDocxFullyVacc.this, "User verified with reCAPTCHA", Toast.LENGTH_SHORT).show();

                                progressDialog.setMessage("Submitting...Please Wait");
                                progressDialog.show();

                                value.setGovId(spinner_govId.getText().toString().trim());
                                value.setGovIdNumber(govIdNumber.getText().toString().trim());

//                              uploadToFirebaseFromCamera();
//                              uploadToFirebaseFromCamera2();

                                if(uploadToFirebase()==true || uploadToFirebase2()==true || uploadToFirebase3()==true || uploadToFirebase4()==true || uploadToFirebase5()==true || uploadToFirebase6()==true) {
                                    databaseReference.child((firebaseAuth.getCurrentUser().getUid())).setValue(value);

                                    String stat = "Fill up HDF";
                                    String govID = spinner_govId.getText().toString().trim();

                                    HashMap user = new HashMap();
                                    user.put("status", stat);
                                    user.put("govId", govID);
                                    appref.child(firebaseUser.getUid()).updateChildren(user);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            dialog.setContentView(R.layout.uploaddocx_success_dialog);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                            Button ok = dialog.findViewById(R.id.okButton);

                                            dialog.show();

                                            ok.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog.dismiss();
                                                    startActivity(new Intent(UploadDocxFullyVacc.this, HealthDeclarationForm.class));
                                                }
                                            });
                                        }
                                    }, 3000);
                                } else {
                                    Toast.makeText(UploadDocxFullyVacc.this, "Failed", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            } else {
                                // if the response if failure we are displaying
                                // a below toast message.
                                Toast.makeText(getApplicationContext(), String.valueOf(jsonObject.getString("error-codes")), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                            // if we get any exception then we are
                            // displaying an error message in logcat.
                            Log.d("TAG", "JSON exception: " + ex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // inside error response we are displaying
                        // a log message in our logcat.
                        Log.d("TAG", "Error message: " + error.getMessage());
                    }
                }) {
            // below is the getParamns method in which we will
            // be passing our response token and secret key to the above url.
            @Override
            protected Map<String, String> getParams() {
                // we are passing data using hashmap
                // key and value pair.
                Map<String, String> params = new HashMap<>();
                params.put("secret", SECRET_KEY);
                params.put("response", responseToken);
                return params;
            }
        };
        // below line of code is use to set retry
        // policy if the api fails in one try.
        request.setRetryPolicy(new DefaultRetryPolicy(
                // we are setting time for retry is 5 seconds.
                50000,

                // below line is to perform maximum retries.
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // at last we are adding our request to queue.
        queue.add(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                govIdImageUri = data.getData();
                govIdImage.setImageURI(govIdImageUri);
                dialog.dismiss();
            }
        }
        if (requestCode == 2000) {
            if (resultCode == Activity.RESULT_OK) {
                fVaccReq1ImageUri = data.getData();
                fVaccReq1Image.setImageURI(fVaccReq1ImageUri);
                dialog.dismiss();
            }
        }
        if (requestCode == 3000) {
            if (resultCode == Activity.RESULT_OK) {
                fVaccReq2ImageUri = data.getData();
                fVaccReq2Image.setImageURI(fVaccReq2ImageUri);
                dialog.dismiss();
            }
        }
        if (requestCode == 4000) {
            if (resultCode == Activity.RESULT_OK) {
                fVaccReq3ImageUri = data.getData();
                fVaccReq3Image.setImageURI(fVaccReq3ImageUri);
                dialog.dismiss();
            }
        }
        if (requestCode == 5000) {
            if (resultCode == Activity.RESULT_OK) {
                fVaccReq4ImageUri = data.getData();
                fVaccReq4Image.setImageURI(fVaccReq4ImageUri);
                dialog.dismiss();
            }
        }
        if (requestCode == 6000) {
            if (resultCode == Activity.RESULT_OK) {
                fVaccReq5ImageUri = data.getData();
                fVaccReq5Image.setImageURI(fVaccReq5ImageUri);
                dialog.dismiss();
            }
        }
//        if (requestCode == 3000) {
//            if (resultCode == Activity.RESULT_OK) {
//                govIdImageUri = data.getData();
//                govIdImage.setImageURI(govIdImageUri);
//                dialog.dismiss();
//                onCaptureImageResult(data);
//
//            }
//        }
//        if (requestCode == 4000) {
//            if (resultCode == Activity.RESULT_OK) {
//                vaccCardImageUri = data.getData();
//                vaccCardImage.setImageURI(vaccCardImageUri);
//                dialog.dismiss();
//                onCaptureImageResult2(data);
//
//            }
//        }
    }

    private boolean uploadToFirebase() {
        final StorageReference fileRef = storageReference.child("users/" + firebaseUser.getUid() + "govId" + getFileExtension(govIdImageUri));
        fileRef.putFile(govIdImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        GovId image = new GovId(uri.toString());
                        govIdRef.child("govIdImage").setValue(image);

                        String govIdImage = uri.toString();

                        HashMap user = new HashMap();
                        user.put("govIdImage", govIdImage);

                        appref.child(firebaseUser.getUid()).updateChildren(user);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDocxFullyVacc.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean uploadToFirebase2() {

        StorageReference fileRef = storageReference.child("users/" + firebaseUser.getUid() + "fVaccReq1Image" + getFileExtension(fVaccReq1ImageUri));
        fileRef.putFile(fVaccReq1ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Requirement image = new Requirement(uri.toString());
                        reqRef.child("fVaccReq1Image").setValue(image);

                        String requirementImage = uri.toString();

                        HashMap user = new HashMap();
                        user.put("fVaccReq1Image", requirementImage);

                        appref.child(firebaseUser.getUid()).updateChildren(user);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDocxFullyVacc.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean uploadToFirebase3() {

        StorageReference fileRef = storageReference.child("users/" + firebaseUser.getUid() + "fVaccReq2Image" + getFileExtension(fVaccReq2ImageUri));
        fileRef.putFile(fVaccReq2ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Requirement image = new Requirement(uri.toString());
                        reqRef.child("fVaccReq2Image").setValue(image);

                        String requirementImage = uri.toString();

                        HashMap user = new HashMap();
                        user.put("fvaccReq2Image", requirementImage);

                        appref.child(firebaseUser.getUid()).updateChildren(user);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDocxFullyVacc.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean uploadToFirebase4() {

        StorageReference fileRef = storageReference.child("users/" + firebaseUser.getUid() + "fVaccReq3Image" + getFileExtension(fVaccReq3ImageUri));
        fileRef.putFile(fVaccReq3ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Requirement image = new Requirement(uri.toString());
                        reqRef.child("fVaccReq3Image").setValue(image);

                        String requirementImage = uri.toString();

                        HashMap user = new HashMap();
                        user.put("fVaccReq3Image", requirementImage);

                        appref.child(firebaseUser.getUid()).updateChildren(user);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDocxFullyVacc.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean uploadToFirebase5() {

        StorageReference fileRef = storageReference.child("users/" + firebaseUser.getUid() + "fVaccReq4Image" + getFileExtension(fVaccReq4ImageUri));
        fileRef.putFile(fVaccReq4ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Requirement image = new Requirement(uri.toString());
                        reqRef.child("fVaccReq4Image").setValue(image);

                        String requirementImage = uri.toString();

                        HashMap user = new HashMap();
                        user.put("fVaccReq4Image", requirementImage);

                        appref.child(firebaseUser.getUid()).updateChildren(user);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDocxFullyVacc.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean uploadToFirebase6() {

        StorageReference fileRef = storageReference.child("users/" + firebaseUser.getUid() + "fVaccReq5Image" + getFileExtension(fVaccReq5ImageUri));
        fileRef.putFile(fVaccReq5ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Requirement image = new Requirement(uri.toString());
                        reqRef.child("fVaccReq5Image").setValue(image);

                        String requirementImage = uri.toString();

                        HashMap user = new HashMap();
                        user.put("fVaccReq5Image", requirementImage);

                        appref.child(firebaseUser.getUid()).updateChildren(user);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDocxFullyVacc.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private void uploadToFirebaseFromCamera() {
        final StorageReference fileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "govId.jpg");
        fileRef.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        GovId image = new GovId(uri.toString());
                        govIdRef.child("govIdImage").setValue(image);
//                        GovIdImage image2 = new GovIdImage(uri.toString());
//                        app_govIdRef.setValue(image2);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDocxFullyVacc.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadToFirebaseFromCamera2() {
        final StorageReference fileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "vaccCard.jpg");
        fileRef.putBytes(bb2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Requirement image = new Requirement(uri.toString());
                        reqRef.child("vaccCardImage").setValue(image);
//                        VaccCardImage image2 = new VaccCardImage(uri.toString());
//                        app_vaccCardRef.setValue(image2);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDocxFullyVacc.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String getFileExtension(Uri mUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    public void pick_gallery_govid() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, 1000);
    }

    public void pick_gallery_req1() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, 2000);
    }

    public void pick_gallery_req2() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, 3000);
    }

    public void pick_gallery_req3() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, 4000);
    }

    public void pick_gallery_req4() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, 5000);
    }

    public void pick_gallery_req5() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, 6000);
    }

//    public void pick_camera_govid() {
//        Intent openGalleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(openGalleryIntent, 3000);
//    }
//
//    public void pick_camera_vacccard() {
//        Intent openGalleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(openGalleryIntent, 4000);
//    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

//    private void onCaptureImageResult(Intent data) {
//        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//        bb = bytes.toByteArray();
//        govIdImage.setImageBitmap(bitmap);
//
//        uploadToFirebaseFromCamera();
//    }
//
//    private void onCaptureImageResult2(Intent data) {
//        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//        bb2 = bytes.toByteArray();
//        vaccCardImage.setImageBitmap(bitmap);
//
//        uploadToFirebaseFromCamera2();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && progressDialog != null)
            dialog.dismiss();
        progressDialog.dismiss();
    }
}