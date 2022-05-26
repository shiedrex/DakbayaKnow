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
import android.widget.ImageView;
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
    private Button govIdButton, RTPCRButton, otherFilesButton, submitButton;

    String SITE_KEY = "6LeQMXkeAAAAAOmnUZ2R7k0AV-FLhnOWQj3HyriO";
    String SECRET_KEY = "6LeQMXkeAAAAAC-iU02tSyfJsxo7xhYRCuVaB0Zl";
    RequestQueue queue;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, travelRef, govIdRef, rtcprRef, otherfilesRef, appref;

    FirebaseAuth fAuth;
    StorageReference storageReference;
    FirebaseUser firebaseUser;

    TextView fullname, currentAddress, destinationAddress, departure, arrival,
            govIDRequired, govIdImageRequired, rtcprImageRequired, otherfilesImageRequired;

    ImageView govIdImage, rtcprImage, otherfilesImage;

    TextInputEditText govIdNumber;
    AutoCompleteTextView spinner_govId;

    Docx value;
    Applications value2;

    Dialog dialog;
    ProgressDialog progressDialog;

    Uri govIdImageUri, rtcprImageUri, otherfilesImageUri;

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
        rtcprRef = firebaseDatabase.getReference("uploadDocx").child(firebaseAuth.getCurrentUser().getUid());
        appref = FirebaseDatabase.getInstance().getReference("applications");

        queue = Volley.newRequestQueue(getApplicationContext());
        //button
        govIdButton = findViewById((R.id.govIdButton));
        RTPCRButton = findViewById((R.id.RTPCRButton));
        submitButton = findViewById(R.id.submitButton);
        //spinner
        spinner_govId = findViewById(R.id.spinner_govId);
        //text input
        govIdNumber = findViewById(R.id.govIdNumber);
        //imageview
        govIdImage = findViewById(R.id.govIdImage);
        rtcprImage = findViewById(R.id.rtcprImage);
        //required
        govIDRequired = findViewById(R.id.govIDRequired);
        govIdImageRequired = findViewById(R.id.govIdImageRequired);
        rtcprImageRequired = findViewById(R.id.rtpcrImageRequired);

        //retrieve from database
        fullname = findViewById(R.id.fullnameText);
        currentAddress = findViewById(R.id.currentAddressText);
        destinationAddress = findViewById(R.id.destinationText);
        departure = findViewById(R.id.departureText);
        arrival = findViewById(R.id.arrivalText);

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
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        RTPCRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 2000);
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

                if (rtcprImage.getDrawable() == null) {
                    rtcprImageRequired.setText("RTPCR Test Result Photo is required");
                    rtcprImageRequired.requestFocus();
                    return;
                } else {
                    rtcprImageRequired.setText(null);
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

                                if (uploadToFirebase()==true && uploadToFirebase2()==true) {
                                    databaseReference.child(String.valueOf(firebaseAuth.getCurrentUser().getUid())).setValue(value);
                                    String stat = "Fill up HDF";
                                    String govId = spinner_govId.getText().toString().trim();
                                    updateStatus(stat, govId);

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
            }
        }
        if (requestCode == 2000) {
            if (resultCode == Activity.RESULT_OK) {
                rtcprImageUri = data.getData();
                rtcprImage.setImageURI(rtcprImageUri);
            }
        }
    }

    private boolean uploadToFirebase() {
        final StorageReference fileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "govId.jpg");
        fileRef.putFile(govIdImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        GovId image = new GovId(uri.toString());
                        govIdRef.child("govIdImage").setValue(image);

                        String govIdImage = uri.toString();
                        updateStatus2(govIdImage);
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
        final StorageReference fileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "RTPCR.jpg");
        fileRef.putFile(rtcprImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        RTCPR image = new RTCPR(uri.toString());
                        rtcprRef.child("requirementImage").setValue(image);

                        String requirementImage = uri.toString();
                        updateStatus3(requirementImage);
                        Toast.makeText(UploadDocxUnvacc.this, "RT-PCR Test uploaded successfully!", Toast.LENGTH_SHORT).show();
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

    private void updateStatus(String stat, String govId) {
        HashMap user = new HashMap();
        user.put("status", stat);
        user.put("govId", govId);

        appref.child(fAuth.getCurrentUser().getUid()).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(UploadDocxUnvacc.this, "Success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UploadDocxUnvacc.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateStatus2(String govIdImage) {
        HashMap user = new HashMap();
        user.put("govIdImage", govIdImage);

        appref.child(firebaseUser.getUid()).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UploadDocxUnvacc.this, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UploadDocxUnvacc.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateStatus3(String requirementImage) {
        HashMap user = new HashMap();
        user.put("requirementImage", requirementImage);

        appref.child(firebaseUser.getUid()).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UploadDocxUnvacc.this, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UploadDocxUnvacc.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
