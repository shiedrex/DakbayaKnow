package com.example.dakbayaknow;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class UploadDocxUnvacc extends AppCompatActivity {
    private Button govIdButton, submitButton, uVaccReq1Button, uVaccReq2Button, uVaccReq3Button, uVaccReq4Button, uVaccReq5Button;

    String SITE_KEY = "6LeQMXkeAAAAAOmnUZ2R7k0AV-FLhnOWQj3HyriO";
    String SECRET_KEY = "6LeQMXkeAAAAAC-iU02tSyfJsxo7xhYRCuVaB0Zl";
    RequestQueue queue;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, travelRef, govIdRef, reqRef, appref, unvaccRef;

    FirebaseAuth fAuth;
    StorageReference storageReference;
    FirebaseUser firebaseUser;

    TextView fullname, currentAddress, destinationAddress, departure, arrival,
            govIDRequired, govIdImageRequired, uVaccReq1Required, uVaccReq2Required, uVaccReq3Required, uVaccReq4Required, uVaccReq5Required,
            uVaccReq1, uVaccReq2, uVaccReq3, uVaccReq4, uVaccReq5;

    LinearLayout req1L, req2L, req3L, req4L, req5L;

    ImageView govIdImage, uVaccReq1Image, uVaccReq2Image, uVaccReq3Image, uVaccReq4Image, uVaccReq5Image;

    TextInputEditText govIdNumber;
    AutoCompleteTextView spinner_govId;

    Docx value;
    Applications value2;

    Dialog dialog;
    ProgressDialog progressDialog;

    Uri govIdImageUri, uVaccReq1ImageUri, uVaccReq2ImageUri, uVaccReq3ImageUri, uVaccReq4ImageUri, uVaccReq5ImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaddocxunvacc);

        getSupportActionBar().setTitle("Upload Docx");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        // getting current user data
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users").child(firebaseUser.getUid()).child("uploadDocx");

        travelRef = firebaseDatabase.getReference("travelform");
        govIdRef = firebaseDatabase.getReference("uploadDocx").child(firebaseAuth.getCurrentUser().getUid());
        reqRef = firebaseDatabase.getReference("uploadDocx").child(firebaseAuth.getCurrentUser().getUid());
        appref = FirebaseDatabase.getInstance().getReference("applications");
        unvaccRef = firebaseDatabase.getReference("lgu");

        queue = Volley.newRequestQueue(getApplicationContext());
        //button
        govIdButton = findViewById((R.id.govIdButton));
        uVaccReq1Button = findViewById(R.id.uVaccReq1Button);
        uVaccReq2Button = findViewById(R.id.uVaccReq2Button);
        uVaccReq3Button = findViewById(R.id.uVaccReq3Button);
        uVaccReq4Button = findViewById(R.id.uVaccReq4Button);
        uVaccReq5Button = findViewById(R.id.uVaccReq5Button);
        submitButton = findViewById(R.id.submitButton);
        //spinner
        spinner_govId = findViewById(R.id.spinner_govId);
        //text input
        govIdNumber = findViewById(R.id.govIdNumber);
        //imageview
        govIdImage = findViewById(R.id.govIdImage);
        uVaccReq1Image = findViewById(R.id.uVaccReq1Image);
        uVaccReq2Image = findViewById(R.id.uVaccReq2Image);
        uVaccReq3Image = findViewById(R.id.uVaccReq3Image);
        uVaccReq4Image = findViewById(R.id.uVaccReq4Image);
        uVaccReq5Image = findViewById(R.id.uVaccReq5Image);
        //required
        govIDRequired = findViewById(R.id.govIDRequired);
        govIdImageRequired = findViewById(R.id.govIdImageRequired);
        uVaccReq1Required = findViewById(R.id.uVaccReq1Required);
        uVaccReq2Required = findViewById(R.id.uVaccReq2Required);
        uVaccReq3Required = findViewById(R.id.uVaccReq3Required);
        uVaccReq4Required = findViewById(R.id.uVaccReq4Required);
        uVaccReq5Required = findViewById(R.id.uVaccReq5Required);

        //retrieve from database
        fullname = findViewById(R.id.fullnameText);
        currentAddress = findViewById(R.id.currentAddressText);
        destinationAddress = findViewById(R.id.destinationText);
        departure = findViewById(R.id.departureText);
        arrival = findViewById(R.id.arrivalText);

        uVaccReq1 = findViewById(R.id.uVaccReq1);
        uVaccReq2 = findViewById(R.id.uVaccReq2);
        uVaccReq3 = findViewById(R.id.uVaccReq3);
        uVaccReq4 = findViewById(R.id.uVaccReq4);
        uVaccReq5 = findViewById(R.id.uVaccReq5);
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
                        Toast.makeText(UploadDocxUnvacc.this, "Under Development", Toast.LENGTH_SHORT).show();
//                            pick_camera_vacccard();
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
        uVaccReq1Button.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(UploadDocxUnvacc.this, "Under Development", Toast.LENGTH_SHORT).show();
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
        uVaccReq2Button.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(UploadDocxUnvacc.this, "Under Development", Toast.LENGTH_SHORT).show();
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
        uVaccReq3Button.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(UploadDocxUnvacc.this, "Under Development", Toast.LENGTH_SHORT).show();
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
        uVaccReq4Button.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(UploadDocxUnvacc.this, "Under Development", Toast.LENGTH_SHORT).show();
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
        uVaccReq5Button.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(UploadDocxUnvacc.this, "Under Development", Toast.LENGTH_SHORT).show();
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

                if (uVaccReq1Image.getDrawable() == null && req1L.isShown()) {
                    uVaccReq1Required.setText("This is required");
                    uVaccReq1Required.requestFocus();
                    return;
                } else {
                    uVaccReq1Required.setText(null);
                }

                if (uVaccReq2Image.getDrawable() == null && req2L.isShown()) {
                    uVaccReq2Required.setText("This is required");
                    uVaccReq2Required.requestFocus();
                    return;
                } else {
                    uVaccReq2Required.setText(null);
                }

                if (uVaccReq3Image.getDrawable() == null && req3L.isShown()) {
                    uVaccReq3Required.setText("This is required");
                    uVaccReq3Required.requestFocus();
                    return;
                } else {
                    uVaccReq3Required.setText(null);
                }

                if (uVaccReq4Image.getDrawable() == null && req4L.isShown()) {
                    uVaccReq4Required.setText("This is required");
                    uVaccReq4Required.requestFocus();
                    return;
                } else {
                    uVaccReq4Required.setText(null);
                }

                if (uVaccReq5Image.getDrawable() == null && req5L.isShown()) {
                    uVaccReq5Required.setText("This is required");
                    uVaccReq5Required.requestFocus();
                    return;
                }  else {
                    uVaccReq5Required.setText(null);
                }

                verifyGoogleReCAPTCHA();
            }
        });

        Query query = travelRef.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    // Retrieving Data from firebase
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

                    Query query2 = unvaccRef.orderByChild("city").equalTo(dMuni);
                    query2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String req1 = "" + dataSnapshot1.child("uVaccReq1").getValue();
                                String req2 = "" + dataSnapshot1.child("uVaccReq2").getValue();
                                String req3 = "" + dataSnapshot1.child("uVaccReq3").getValue();
                                String req4 = "" + dataSnapshot1.child("uVaccReq4").getValue();
                                String req5 = "" + dataSnapshot1.child("uVaccReq5").getValue();

                                // setting data to our text view
                                if (dataSnapshot.exists()) {
                                    uVaccReq1.setText(req1);
                                    uVaccReq2.setText(req2);
                                    uVaccReq3.setText(req3);
                                    uVaccReq4.setText(req4);
                                    uVaccReq5.setText(req5);
                                }
                                //requirement 1
                                if (!dataSnapshot1.hasChild("uVaccReq1") || req1.contains("APPROVED DakbayaKnow Travel Permit")) {
                                    req1L.setVisibility(View.GONE);
                                } else {
                                    req1L.setVisibility(View.VISIBLE);
                                }
                                //requirement 2
                                if (!dataSnapshot1.hasChild("uVaccReq2") || req2.contains("APPROVED DakbayaKnow Travel Permit")) {
                                    req2L.setVisibility(View.GONE);
                                } else {
                                    req2L.setVisibility(View.VISIBLE);
                                }
                                //requirement 3
                                if (!dataSnapshot1.hasChild("uVaccReq3") || req3.contains("APPROVED DakbayaKnow Travel Permit")) {
                                    req3L.setVisibility(View.GONE);
                                } else {
                                    req3L.setVisibility(View.VISIBLE);
                                }
                                //requirement 4
                                if (!dataSnapshot1.hasChild("uVaccReq4") || req4.contains("APPROVED DakbayaKnow Travel Permit")) {
                                    req4L.setVisibility(View.GONE);
                                } else {
                                    req4L.setVisibility(View.VISIBLE);
                                }
                                //requirement 5
                                if (!dataSnapshot1.hasChild("uVaccReq5") || req5.contains("APPROVED DakbayaKnow Travel Permit")) {
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
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                            Toast.makeText(UploadDocxUnvacc.this, "Error found is : " + e, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(UploadDocxUnvacc.this, "User verified with reCAPTCHA", Toast.LENGTH_SHORT).show();

                                value.setGovId(spinner_govId.getText().toString().trim());
                                value.setGovIdNumber(govIdNumber.getText().toString().trim());

                                if(uploadToFirebase()==true || uploadToFirebase2()==true || uploadToFirebase3()==true || uploadToFirebase4()==true || uploadToFirebase5()==true || uploadToFirebase6()==true) {
                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(value);
                                    String stat = "Fill up HDF";
                                    String govId = spinner_govId.getText().toString().trim();

                                    HashMap user = new HashMap();
                                    user.put("status", stat);
                                    user.put("govId", govId);

                                    appref.child(fAuth.getCurrentUser().getUid()).updateChildren(user);

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
                                                    startActivity(new Intent(UploadDocxUnvacc.this, HealthDeclarationForm.class));
                                                }
                                            });
                                        }
                                    }, 3000);
                                } else {
                                    Toast.makeText(UploadDocxUnvacc.this, "Failed", Toast.LENGTH_SHORT).show();
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
                uVaccReq1ImageUri = data.getData();
                uVaccReq1Image.setImageURI(uVaccReq1ImageUri);
                dialog.dismiss();
            }
        }
        if (requestCode == 3000) {
            if (resultCode == Activity.RESULT_OK) {
                uVaccReq2ImageUri = data.getData();
                uVaccReq2Image.setImageURI(uVaccReq2ImageUri);
                dialog.dismiss();
            }
        }
        if (requestCode == 4000) {
            if (resultCode == Activity.RESULT_OK) {
                uVaccReq3ImageUri = data.getData();
                uVaccReq3Image.setImageURI(uVaccReq3ImageUri);
                dialog.dismiss();
            }
        }
        if (requestCode == 5000) {
            if (resultCode == Activity.RESULT_OK) {
                uVaccReq4ImageUri = data.getData();
                uVaccReq4Image.setImageURI(uVaccReq4ImageUri);
                dialog.dismiss();
            }
        }
        if (requestCode == 6000) {
            if (resultCode == Activity.RESULT_OK) {
                uVaccReq5ImageUri = data.getData();
                uVaccReq5Image.setImageURI(uVaccReq5ImageUri);
                dialog.dismiss();
            }
        }

    }

    private boolean uploadToFirebase() {
        final StorageReference fileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "govId" + getFileExtension(govIdImageUri));
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
                Toast.makeText(UploadDocxUnvacc.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean uploadToFirebase2() {

        StorageReference fileRef = storageReference.child("users/" + firebaseUser.getUid() + "uVaccReq1Image" + getFileExtension(uVaccReq1ImageUri));
        fileRef.putFile(uVaccReq1ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Requirement image = new Requirement(uri.toString());
                        reqRef.child("uVaccReq1Image").setValue(image);

                        String requirementImage = uri.toString();

                        HashMap user = new HashMap();
                        user.put("uVaccReq1Image", requirementImage);

                        appref.child(firebaseUser.getUid()).updateChildren(user);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDocxUnvacc.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean uploadToFirebase3() {

        StorageReference fileRef = storageReference.child("users/" + firebaseUser.getUid() + "uVaccReq2Image" + getFileExtension(uVaccReq2ImageUri));
        fileRef.putFile(uVaccReq2ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Requirement image = new Requirement(uri.toString());
                        reqRef.child("uVaccReq2Image").setValue(image);

                        String requirementImage = uri.toString();

                        HashMap user = new HashMap();
                        user.put("uVaccReq2Image", requirementImage);

                        appref.child(firebaseUser.getUid()).updateChildren(user);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDocxUnvacc.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean uploadToFirebase4() {

        StorageReference fileRef = storageReference.child("users/" + firebaseUser.getUid() + "uVaccReq3Image" + getFileExtension(uVaccReq3ImageUri));
        fileRef.putFile(uVaccReq3ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Requirement image = new Requirement(uri.toString());
                        reqRef.child("uVaccReq3Image").setValue(image);

                        String requirementImage = uri.toString();

                        HashMap user = new HashMap();
                        user.put("uVaccReq3Image", requirementImage);

                        appref.child(firebaseUser.getUid()).updateChildren(user);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDocxUnvacc.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean uploadToFirebase5() {

        StorageReference fileRef = storageReference.child("users/" + firebaseUser.getUid() + "uVaccReq4Image" + getFileExtension(uVaccReq4ImageUri));
        fileRef.putFile(uVaccReq4ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Requirement image = new Requirement(uri.toString());
                        reqRef.child("uVaccReq4Image").setValue(image);

                        String requirementImage = uri.toString();

                        HashMap user = new HashMap();
                        user.put("uVaccReq4Image", requirementImage);

                        appref.child(firebaseUser.getUid()).updateChildren(user);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDocxUnvacc.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private boolean uploadToFirebase6() {

        StorageReference fileRef = storageReference.child("users/" + firebaseUser.getUid() + "uVaccReq5Image" + getFileExtension(uVaccReq5ImageUri));
        fileRef.putFile(uVaccReq5ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Requirement image = new Requirement(uri.toString());
                        reqRef.child("uVaccReq5Image").setValue(image);

                        String requirementImage = uri.toString();

                        HashMap user = new HashMap();
                        user.put("uvaccReq5Image", requirementImage);

                        appref.child(firebaseUser.getUid()).updateChildren(user);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadDocxUnvacc.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
        return true;
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

}
