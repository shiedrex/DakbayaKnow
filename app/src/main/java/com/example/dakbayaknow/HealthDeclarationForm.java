package com.example.dakbayaknow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import java.util.List;

public class HealthDeclarationForm extends AppCompatActivity {
    private EditText  countryText, cityText;
    private RadioButton sYes, sNo, cYes, cNo, aYes, aNo;

    private TextInputEditText firstnameText, middlenameText, lastnameText, nationalityText, ageText, contactNumberText, emailText, presentAddressText;
    private TextInputLayout firstnameInput;

    private Button submitButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth fAuth;
    HDFDetails value;
    int maxid = 1;

    AutoCompleteTextView spinner_gender, spinner_symptoms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hdf);

        getSupportActionBar().setTitle("Health Declaration Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //edit text
        firstnameText = (TextInputEditText) findViewById(R.id.firstname);
        middlenameText = (TextInputEditText) findViewById(R.id.middlename);
        lastnameText = (TextInputEditText) findViewById(R.id.lastname);
        nationalityText = findViewById(R.id.nationality);
        ageText = (TextInputEditText) findViewById(R.id.age);
        contactNumberText = findViewById(R.id.contactNumber);
        emailText = (TextInputEditText) findViewById(R.id.emailAddress);
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

        submitButton = findViewById(R.id.submitButton);
        fAuth = FirebaseAuth.getInstance();

        value = new HDFDetails();
        reference = database.getInstance().getReference("users").child(fAuth.getCurrentUser().getUid()).child("hdf");

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
                if(snapshot.exists()){
                    maxid = (int)snapshot.getChildrenCount();
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

                if(sYes.isChecked()){
                    value.setSick(s1);
                }else{
                    value.setSick(s2);
                }

                if(cYes.isChecked()){
                    value.setCovid(c1);
                }else{
                    value.setCovid(c2);
                }

                if(aYes.isChecked()){
                    value.setAnimal(a1);
                }else{
                    value.setAnimal(a2);
                }

                String firstname = firstnameText.getText().toString().trim();
                String lastname = lastnameText.getText().toString().trim();
                String nationality = nationalityText.getText().toString().trim();
                String age = ageText.getText().toString().trim();
                String contactNumber = contactNumberText.getText().toString().trim();
                String email = emailText.getText().toString().trim();
                String presentAddress = presentAddressText.getText().toString().trim();
                String country = countryText.getText().toString().trim();
                String city = cityText.getText().toString().trim();

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

//                fAuth.fetchSignInMethodsForEmail(email)
//                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
//
//                                boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
//
//                                if (isNewUser) {
//                                    Log.e("TAG", "Email Address do not exist");
//                                    emailText.setError("Email Address must be same with your Registered Email Address!");
//                                    emailText.requestFocus();
//                                    return;
//                                } else {
//                                    Log.e("TAG", "Email Address Exist!");
//                                }
//
//                            }
//                        });

                Toast.makeText(HealthDeclarationForm.this, "Health Declaration Form submitted successfully!", Toast.LENGTH_SHORT).show();
                reference.child(String.valueOf(fAuth.getCurrentUser().getUid())).setValue(value);
                submit();
            }
        });
    }

    private void submit() {
        AlertDialog.Builder builder=new AlertDialog.Builder(HealthDeclarationForm.this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.dklogo2);
        builder.setTitle("Successfully Submitted!");
        builder.setMessage("You are safe to travel.");
        builder.setInverseBackgroundForced(true);

        builder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
                finish();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }
}