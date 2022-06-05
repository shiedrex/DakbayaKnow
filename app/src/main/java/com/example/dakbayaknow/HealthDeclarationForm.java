package com.example.dakbayaknow;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.StringValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HealthDeclarationForm extends AppCompatActivity {
    private EditText countryText, cityText;
    private RadioButton sYes, sNo, cYes, cNo, aYes, aNo;

    private TextInputEditText ageText, nationalityText, arrivalText;

    private Button submitButton;
    FirebaseDatabase database;
    DatabaseReference reference, ref2, travelref;
    FirebaseAuth fAuth;
    HDFDetails value;
    Applications value2;
    int maxid = 1;

    AutoCompleteTextView spinner_gender, spinner_symptoms;

    Dialog dialog;
    ProgressDialog progressDialog;

    TextView sickRequired, symptomsRequired, covidRequired, animalRequired,
            fullname, currentAddress, destinationAddress, arrival, gender, contactNumber, emailAddress;
    RadioGroup sick, covid, animal;
    int checkgroup_sick, checkgroup_covid, checkgroup_animal;

    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hdf);

        getSupportActionBar().setTitle("Health Declaration Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //edit text
        ageText = findViewById(R.id.age);
        nationalityText = findViewById(R.id.nationality);
        countryText = findViewById(R.id.country);
        cityText = findViewById(R.id.city);
        arrivalText = findViewById(R.id.arrival);

        //spinner
        spinner_symptoms = findViewById(R.id.spinner_symptoms);

        //radiobutton
        sYes = findViewById(R.id.sickYes);
        sNo = findViewById(R.id.sickNo);
        cYes = findViewById(R.id.covidYes);
        cNo = findViewById(R.id.covidNo);
        aYes = findViewById(R.id.animalsYes);
        aNo = findViewById(R.id.animalsNo);

        //radiogroup
        sick = findViewById(R.id.sick);
        covid = findViewById(R.id.covid);
        animal = findViewById(R.id.animal);

        //retrieve from database
        fullname = findViewById(R.id.fullnameText);
        gender = findViewById(R.id.genderText);
        contactNumber = findViewById(R.id.contactNumberText);
        emailAddress = findViewById(R.id.emailText);
        currentAddress = findViewById(R.id.currentAddressText);
        destinationAddress = findViewById(R.id.destinationText);
        arrival = findViewById(R.id.arrivalText);

        sick.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkgroup_sick = i;
            }
        });

        covid.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkgroup_covid = i;
            }
        });

        animal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkgroup_animal = i;
            }
        });

        //button
        submitButton = findViewById(R.id.submitButton);

        //textview required
        sickRequired = findViewById(R.id.sickRequired);
        symptomsRequired = findViewById(R.id.symptomsRequired);
        covidRequired = findViewById(R.id.covidRequired);
        animalRequired = findViewById(R.id.animalRequired);

        fAuth = FirebaseAuth.getInstance();

        value = new HDFDetails();
        value2 = new Applications();

        reference = database.getInstance().getReference("hdf");
        travelref = database.getInstance().getReference("travelform");
        ref2 = database.getInstance().getReference("applications");

        dialog = new Dialog(this);
        progressDialog = new ProgressDialog(this);

        List<String> Categories2 = new ArrayList<>();
        Categories2.add("Fever");
        Categories2.add("Colds");
        Categories2.add("Cough");
        Categories2.add("Sore Throat");
        Categories2.add("Loss of smell and taste");
        Categories2.add("Muscle Pain");
        Categories2.add("Headache");
        Categories2.add("Difficulty in breathing");
        Categories2.add("NO");

        ArrayAdapter<String> dataAdapter2;
        dataAdapter2 = new ArrayAdapter<>(this, R.layout.textview_gray, Categories2);
        dataAdapter2.setDropDownViewResource(R.layout.textview_gray);
        spinner_symptoms.setAdapter(dataAdapter2);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxid = (int) snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        arrivalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        HealthDeclarationForm.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                month = month + 1;
                String date = month + "/" + dayofMonth + "/" + year;
                arrivalText.setText(date);
            }
        };

        Query query = travelref.orderByChild("email").equalTo(fAuth.getCurrentUser().getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String fn = "" + dataSnapshot1.child("firstname").getValue();
                    String mi = "" + dataSnapshot1.child("middleinitial").getValue();
                    String ln = "" + dataSnapshot1.child("lastname").getValue();
                    String sn = "" + dataSnapshot1.child("suffixname").getValue();
                    String gn = "" + dataSnapshot1.child("gender").getValue();
                    String contact = "" + dataSnapshot1.child("contactNumber").getValue();
                    String em = "" + dataSnapshot1.child("email").getValue();
                    String cAdd = "" + dataSnapshot1.child("cAddress").getValue();
                    String cMuni = "" + dataSnapshot1.child("cMunicipality").getValue();
                    String cProv = "" + dataSnapshot1.child("cProvince").getValue();
                    String dAdd = "" + dataSnapshot1.child("dAddress").getValue();
                    String dMuni = "" + dataSnapshot1.child("dMunicipality").getValue();
                    String dProv = "" + dataSnapshot1.child("dProvince").getValue();
                    String arriv = "" + dataSnapshot1.child("arrival").getValue();

                    // setting data to our text view
                    fullname.setText(fn + " " + mi + " " + ln + " " + sn);
                    gender.setText(gn);
                    contactNumber.setText(contact);
                    emailAddress.setText(em);
                    currentAddress.setText(cAdd + ", " + cMuni + ", " + cProv);
                    destinationAddress.setText(dAdd + ", " + dMuni + ", " + dProv);
                    arrivalText.setText(arriv);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //radiobutton
                String s1 = sYes.getText().toString();
                String s2 = sNo.getText().toString();
                String c1 = cYes.getText().toString();
                String c2 = cNo.getText().toString();
                String a1 = aYes.getText().toString();
                String a2 = aNo.getText().toString();

                String nationality = nationalityText.getText().toString().trim();
                String country = countryText.getText().toString().trim();
                String city = cityText.getText().toString().trim();
                String symptoms = spinner_symptoms.getText().toString().trim();
                String arrival = arrivalText.getText().toString().trim();

                //required
                if (nationality.isEmpty()) {
                    nationalityText.setError("Nationality is required!");
                    nationalityText.requestFocus();
                    return;
                }
                if (country.isEmpty()) {
                    countryText.setError("This is required! Put NA if Not Applicable");
                    countryText.requestFocus();
                    return;
                }
                if (city.isEmpty()) {
                    cityText.setError("This is required!");
                    cityText.requestFocus();
                    return;
                }
                if (arrival.isEmpty()) {
                    arrivalText.setError("Arrival Date is Required");
                    arrivalText.requestFocus();
                    return;
                }

                if (checkgroup_sick <= 0) {
                    sickRequired.setVisibility(View.VISIBLE);
                    sickRequired.requestFocus();
                    return;
                } else {
                    sickRequired.setVisibility(View.INVISIBLE);
                }

                if (symptoms.isEmpty()) {
                    symptomsRequired.setVisibility(View.VISIBLE);
                    symptomsRequired.requestFocus();
                    spinner_symptoms.requestFocus();
                    return;
                } else {
                    symptomsRequired.setVisibility(View.INVISIBLE);
                }

                if (checkgroup_covid <= 0) {
                    covidRequired.setVisibility(View.VISIBLE);
                    covidRequired.requestFocus();
                    return;
                } else {
                    covidRequired.setVisibility(View.INVISIBLE);
                }

                if (checkgroup_animal <= 0) {
                    animalRequired.setVisibility(View.VISIBLE);
                    animalRequired.requestFocus();
                    return;
                } else {
                    animalRequired.setVisibility(View.INVISIBLE);
                }

                Date today = Calendar.getInstance().getTime();//getting date
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");//formating according to my need
                String date = formatter.format(today);

                //submit values to database
                //textview
                value.setEmail(emailAddress.getText().toString().trim());
                value.setNationality(nationalityText.getText().toString().trim());
                value.setCountry(countryText.getText().toString().trim());
                value.setCity(cityText.getText().toString().trim());
                value.setArrival(arrivalText.getText().toString().trim());
                //spinner
                value.setSymptoms(spinner_symptoms.getText().toString());
                value.setDateToday(date);

                if (sYes.isChecked()) {
                    value.setSick(s1);
                }
                if (sNo.isChecked()) {
                    value.setSick(s2);
                }

                if (cYes.isChecked()) {
                    value.setCovid(c1);
                }
                if (cNo.isChecked()) {
                    value.setCovid(c2);
                }

                if (aYes.isChecked()) {
                    value.setAnimal(a1);
                }
                if (aNo.isChecked()) {
                    value.setAnimal(a2);
                }

                progressDialog.setMessage("Submitting...Please Wait");
                progressDialog.show();

                reference.child(fAuth.getCurrentUser().getUid()).setValue(value);

                if (symptoms.contains("NO") && sNo.isChecked() && cNo.isChecked() && aNo.isChecked()) {
                    String health = "Good Condition";
                    String stat = "Pending";
                    updateStatus(health, date, stat);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            dialog.setContentView(R.layout.hdf_success_dialog);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            Button ok = dialog.findViewById(R.id.okButton);
                            dialog.show();

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    startActivity(new Intent(HealthDeclarationForm.this, MainActivity.class));
                                }
                            });
                        }
                    }, 3000);

                } else if (!symptoms.contains("NO") || sYes.isChecked() || cYes.isChecked() || aYes.isChecked()) {
                    String health = "Severe Condition";
                    String stat = "Pending";
                    updateStatus(health, date, stat);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            dialog.setContentView(R.layout.hdf_failed_dialog);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            Button ok = dialog.findViewById(R.id.okButton);

                            dialog.show();

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(HealthDeclarationForm.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }, 3000);
            }
        }
    });
}

    private void updateStatus(String health, String date, String stat) {
        HashMap user = new HashMap();
        user.put("health", health);
        user.put("dateToday", date);
        user.put("status", stat);

        ref2.child(fAuth.getCurrentUser().getUid()).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(HealthDeclarationForm.this, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HealthDeclarationForm.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && progressDialog != null)
            dialog.dismiss();
        progressDialog.dismiss();
    }
}