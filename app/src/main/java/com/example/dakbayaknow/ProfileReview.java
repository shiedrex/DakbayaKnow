package com.example.dakbayaknow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileReview extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView firstname, lastname, gender, age, emailAddress, region, province, municipality, address, clientType;
    ProgressDialog pd;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;
    ImageView profileImage;
    Button changeProfileImage;
    StorageReference storageReference;
    Button doneButton;

    FirebaseUser firebaseUser;

//    ActivityResultLauncher<Intent> selectPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_review);

        // getting current user data
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        // Initialising the text view and imageview
        firstname = findViewById(R.id.userName);
        lastname = findViewById(R.id.lastname);
        gender = findViewById(R.id.gender);
        age = findViewById(R.id.age);
        emailAddress = findViewById(R.id.userEmail);
        region = findViewById(R.id.region);
        province = findViewById(R.id.province);
        municipality = findViewById(R.id.municipality);
        address = findViewById(R.id.address);
        clientType = findViewById(R.id.clientType);
        profileImage = findViewById(R.id.image);

        changeProfileImage = findViewById(R.id.changeProfileImage);
        doneButton = findViewById(R.id.doneButton);

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

        fAuth = FirebaseAuth.getInstance();
        storageReference =FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileReview.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    // Retrieving Data from firebase
                    String fn = "" + dataSnapshot1.child("firstname").getValue();
                    String ln = "" + dataSnapshot1.child("lastname").getValue();
                    String Gender = "" + dataSnapshot1.child("gender").getValue();
                    String Age = "" + dataSnapshot1.child("age").getValue();
                    String emailAdd = "" + dataSnapshot1.child("email").getValue();
                    String Region = "" + dataSnapshot1.child("region").getValue();
                    String prov = "" + dataSnapshot1.child("province").getValue();
                    String muni = "" + dataSnapshot1.child("municipality").getValue();
                    String house = "" + dataSnapshot1.child("address").getValue();
                    String client = "" + dataSnapshot1.child("clientType").getValue();

                    // setting data to our text view
                    firstname.setText(fn);
                    lastname.setText(ln);
                    gender.setText(Gender);
                    age.setText(Age);
                    emailAddress.setText(emailAdd);
                    region.setText(Region);
                    province.setText(prov);
                    municipality.setText(muni);
                    address.setText(house);
                    clientType.setText(client);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
//                profileImage.setImageURI(imageUri);
                uploadToFirebase(imageUri);
            }
        }
    }

    private void uploadToFirebase(Uri imageUri) {

        final StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileReview.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

}