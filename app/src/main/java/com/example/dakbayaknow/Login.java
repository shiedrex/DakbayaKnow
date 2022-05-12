package com.example.dakbayaknow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dakbayaknow.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView registerNowButton, forgotPasswordButton;
    private Button loginButton;
    private TextInputEditText emailText, passwordText;

    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;
    ProgressBar progressbar;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerNowButton = findViewById(R.id.registerNowButton);
        registerNowButton.setOnClickListener(this);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        forgotPasswordButton.setOnClickListener(this);

        emailText = findViewById(R.id.emailAddress);
        passwordText = findViewById(R.id.password);

        progressbar = findViewById(R.id.progressBar);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        dialog = new Dialog(this);

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerNowButton:
                startActivity(new Intent(this, Register.class));
                break;

            case R.id.loginButton:
                userLogin();
                break;

            case R.id.forgotPasswordButton:
                forgotPassword();
                break;
        }
    }

    private void userLogin() {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (email.isEmpty()) {
            emailText.setError("Email is required!");
            emailText.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Please provide valid Email!");
            emailText.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (password.isEmpty()) {
            passwordText.setError("Password is required!");
            passwordText.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (password.length() < 6) {
            passwordText.setError("Min password length should be 6 characters!");
            passwordText.requestFocus();
            progressDialog.dismiss();
            return;
        }

//        mAuth.fetchSignInMethodsForEmail(email)
//                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
//
//                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
//
//                        if (isNewUser) {
//                            Log.e("TAG", "User does not exist!");
//                            emailText.setError("Email is not registered. Register Now!");
//                            emailText.requestFocus();
//                            progressDialog.dismiss();
//                            return;
//                        } else {
//                            Log.e("TAG", "User Exist!");
//                        }
//
//                    }
//                });
        progressDialog.setMessage("Logging in...Please Wait");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Login.this, WelcomeUser.class));
                            progressDialog.dismiss();

                        } else {
                            try
                            {
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthInvalidUserException invalidEmail)
                            {
                                Toast.makeText(Login.this, "Invalid Email not registered", Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "onComplete: invalid_email");
                                progressDialog.dismiss();
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                            {
                                Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                Log.d("TAG", "onComplete: wrong_password");
                                progressDialog.dismiss();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(Login.this, "Failed to login!", Toast.LENGTH_LONG).show();
                                Log.d("TAG", "onComplete: " + e.getMessage());
                                progressDialog.dismiss();
                            }
                        }
                    }
                });
    }

    private void forgotPassword() {
        dialog.setContentView(R.layout.forgotpass_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText emailText = dialog.findViewById(R.id.emailAddress);
        Button send = dialog.findViewById(R.id.sendButton);
        dialog.show();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString();
                mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Login.this, "Reset link sent to your email.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Error! Reset link not sent.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}