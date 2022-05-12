package com.example.dakbayaknow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HealthDeclarationForm extends AppCompatActivity {
    private EditText countryText, cityText;
    private RadioButton sYes, sNo, cYes, cNo, aYes, aNo;

    private TextInputEditText firstnameText, middlenameText, lastnameText, nationalityText, ageText, contactNumberText, emailText, presentAddressText;
    private TextInputLayout firstnameInput;

    private Button submitButton;
    FirebaseDatabase database;
    DatabaseReference reference, ref2;
    FirebaseAuth fAuth;
    HDFDetails value;
    Applications value2;
    int maxid = 1;

    AutoCompleteTextView spinner_gender, spinner_symptoms;

    Dialog dialog;
    ProgressDialog progressDialog;

    TextView genderRequired, sickRequired, symptomsRequired, covidRequired, animalRequired;
    RadioGroup sick, covid, animal;
    int checkgroup_sick, checkgroup_covid, checkgroup_animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hdf);

        getSupportActionBar().setTitle("Health Declaration Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //edit text
        firstnameText = findViewById(R.id.firstname);
        middlenameText = findViewById(R.id.middlename);
        lastnameText = findViewById(R.id.lastname);
        nationalityText = findViewById(R.id.nationality);
        ageText = findViewById(R.id.age);
        contactNumberText = findViewById(R.id.contactNumber);
        emailText = findViewById(R.id.emailAddress);
        presentAddressText = findViewById(R.id.presentAddress);
        countryText = findViewById(R.id.country);
        cityText = findViewById(R.id.city);

        firstnameInput = findViewById(R.id.firstnameInput);

        //spinner
        spinner_gender = findViewById(R.id.spinner_gender);
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
        genderRequired = findViewById(R.id.genderRequired);
        sickRequired = findViewById(R.id.sickRequired);
        symptomsRequired = findViewById(R.id.symptomsRequired);
        covidRequired = findViewById(R.id.covidRequired);
        animalRequired = findViewById(R.id.animalRequired);

        fAuth = FirebaseAuth.getInstance();

        value = new HDFDetails();
        value2 = new Applications();

        reference = database.getInstance().getReference("users").child(fAuth.getCurrentUser().getUid()).child("hdf");
        ref2 = database.getInstance().getReference("applications");

        dialog = new Dialog(this);
        progressDialog = new ProgressDialog(this);

        List<String> Categories = new ArrayList<>();
        Categories.add("Male");
        Categories.add("Female");
        Categories.add("Rather not say");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this, R.layout.textview_gray, Categories);
        dataAdapter.setDropDownViewResource(R.layout.textview_gray);
        spinner_gender.setAdapter(dataAdapter);

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

                String firstname = firstnameText.getText().toString().trim();
                String lastname = lastnameText.getText().toString().trim();
                String nationality = nationalityText.getText().toString().trim();
                String gender = spinner_gender.getText().toString().trim();
                String age = ageText.getText().toString().trim();
                String contactNumber = contactNumberText.getText().toString().trim();
                String email = emailText.getText().toString().trim();
                String presentAddress = presentAddressText.getText().toString().trim();
                String country = countryText.getText().toString().trim();
                String city = cityText.getText().toString().trim();
                String symptoms = spinner_symptoms.getText().toString().trim();

                //required
                if (firstname.isEmpty()) {
                    firstnameText.setError("First Name is required!");
                    firstnameText.requestFocus();
                    return;
                }
                if (lastname.isEmpty()) {
                    lastnameText.setError("Last Name is required!");
                    lastnameText.requestFocus();
                    return;
                }
                if (nationality.isEmpty()) {
                    nationalityText.setError("Nationality is required!");
                    nationalityText.requestFocus();
                    return;
                }
                if (gender.isEmpty()) {
                    genderRequired.setVisibility(View.VISIBLE);
                    genderRequired.requestFocus();
                    spinner_gender.requestFocus();
                    return;
                }else{
                    genderRequired.setVisibility(View.INVISIBLE);
                }
                if (age.isEmpty()) {
                    ageText.setError("Age is required!");
                    ageText.requestFocus();
                    return;
                }
                if (contactNumber.isEmpty()) {
                    contactNumberText.setError("Contact Number is required!");
                    contactNumberText.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    emailText.setError("Email is required!");
                    emailText.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailText.setError("Please provide valid Email!");
                    emailText.requestFocus();
                    return;
                }
                if(!email.matches(fAuth.getCurrentUser().getEmail())){
                    emailText.setError("Incorrect Email!");
                    emailText.requestFocus();
                    return;
                }
                if (presentAddress.isEmpty()) {
                    presentAddressText.setError("Present Adress is required!");
                    presentAddressText.requestFocus();
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

                if (checkgroup_sick<=0) {
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
                }else{
                    symptomsRequired.setVisibility(View.INVISIBLE);
                }

                if (checkgroup_covid<=0) {
                    covidRequired.setVisibility(View.VISIBLE);
                    covidRequired.requestFocus();
                    return;
                } else {
                    covidRequired.setVisibility(View.INVISIBLE);
                }

                if (checkgroup_animal<=0) {
                    animalRequired.setVisibility(View.VISIBLE);
                    animalRequired.requestFocus();
                    return;
                } else {
                    animalRequired.setVisibility(View.INVISIBLE);
                }

                //submit values to database
                //text
                value.setFirstname(firstnameText.getText().toString().trim());
                value.setMiddlename(middlenameText.getText().toString().trim());
                value.setLastname(lastnameText.getText().toString().trim());
                value.setNationality(nationalityText.getText().toString().trim());
                value.setAge(ageText.getText().toString().trim());
                value.setContactNumber(contactNumberText.getText().toString().trim());
                value.setEmail(emailText.getText().toString().trim());
                value.setPresentAddress(presentAddressText.getText().toString().trim());
                value.setCountry(countryText.getText().toString().trim());
                value.setCity(cityText.getText().toString().trim());
                //spinner
                value.setGender(spinner_gender.getText().toString());
                value.setSymptoms(spinner_symptoms.getText().toString());

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

                reference.child(String.valueOf(fAuth.getCurrentUser().getUid())).setValue(value);

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users").child(fAuth.getCurrentUser().getUid()).child("hdf");
                rootRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Boolean found, found2, found3, found4;
                        String search = "NO", search2 = "No";

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String symptoms = ds.child("symptoms").getValue(String.class);
                            String sick = ds.child("sick").getValue(String.class);
                            String covid = ds.child("covid").getValue(String.class);
                            String animal = ds.child("animal").getValue(String.class);

                            found = symptoms.contains(search);
                            found2 = sick.contains(search2);
                            found3 = covid.contains(search2);
                            found4 = animal.contains(search2);

                            if (found == true && found2 == true && found3 == true && found4 == true) {
                                String fullname = firstnameText.getText().toString().trim() + " " + lastnameText.getText().toString().trim();
                                String health = "Safe";
                                updateStatus(fullname, health);

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
                                                finish();
                                            }
                                        });
                                    }
                                }, 3000);

                            } else {
                                String fullname = firstnameText.getText().toString().trim() + " " + lastnameText.getText().toString().trim();
                                String health = "Stay at Home";
                                updateStatus(fullname, health);

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
                                                finish();
                                            }
                                        });
                                    }
                                }, 3000);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed, how to handle?
                    }
                });
            }
        });
    }
    private void updateStatus(String fullname, String health) {
        HashMap user = new HashMap();
        user.put("fullname", fullname);
        user.put("health", health);

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
}