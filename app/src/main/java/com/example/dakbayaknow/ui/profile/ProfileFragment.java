package com.example.dakbayaknow.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.dakbayaknow.Image;
import com.example.dakbayaknow.MainActivity;
import com.example.dakbayaknow.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, reference;

    TextView usernameText, firstnameText, lastnameText, genderText, ageText, emailAddressText, phoneText, regionText, provinceText, municipalityText, addressText, clientTypeText;
    ProgressDialog pd;

    FirebaseAuth fAuth;
    ImageView profileImage;
    StorageReference storageReference;
    FirebaseUser firebaseUser;

    ImageButton addprofileimage;
    Button editprofile;
    Dialog dialog;
    Uri camUri, galleryUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        ((AppCompatActivity)getContext()).getSupportActionBar().setTitle("Profile Information");

        // getting current user data
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        databaseReference.keepSynced(true);

        reference = firebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("profileImage");

        // Initialising the text view and imageview
        usernameText = view.findViewById(R.id.username);
        firstnameText = view.findViewById(R.id.firstname);
        lastnameText = view.findViewById(R.id.lastname);
        genderText = view.findViewById(R.id.gender);
        ageText = view.findViewById(R.id.age);
        emailAddressText = view.findViewById(R.id.userEmail);
        phoneText = view.findViewById(R.id.phone);
        regionText = view.findViewById(R.id.region);
        provinceText = view.findViewById(R.id.province);
        municipalityText = view.findViewById(R.id.municipality);
        addressText = view.findViewById(R.id.address);
        clientTypeText = view.findViewById(R.id.clientType);
        profileImage = view.findViewById(R.id.profileimage);
        addprofileimage = view.findViewById(R.id.addprofile);
        editprofile = view.findViewById(R.id.editprofile);

        pd = new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);

        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        dialog = new Dialog(getContext());

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.CAMERA};

        if(!hasPermissions(getActivity(),PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }
        else {
            addprofileimage.setOnClickListener(new View.OnClickListener() {
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
                            pick_camera();
                        }
                    });

                    gall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pick_gallery();
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

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.editprofile_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button save = dialog.findViewById(R.id.saveButton);
                Button cancel = dialog.findViewById(R.id.cancelButton);

                EditText firstname = dialog.findViewById(R.id.firstname);
                EditText lastname = dialog.findViewById(R.id.lastname);
                EditText gender = dialog.findViewById(R.id.gender);
                EditText age = dialog.findViewById(R.id.age);
                EditText phone = dialog.findViewById(R.id.phone);
                EditText email = dialog.findViewById(R.id.email);
                EditText region = dialog.findViewById(R.id.region);
                EditText province = dialog.findViewById(R.id.province);
                EditText municipality = dialog.findViewById(R.id.municipality);
                EditText address = dialog.findViewById(R.id.address);
                EditText clientType = dialog.findViewById(R.id.clientType);

                dialog.show();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String fn = firstname.getText().toString();
                        String ln = lastname.getText().toString();
                        String gen = gender.getText().toString();
                        String Age = age.getText().toString();
                        String phn = phone.getText().toString();
                        String Reg = region.getText().toString();
                        String Prov = province.getText().toString();
                        String muni = municipality.getText().toString();
                        String add = address.getText().toString();
                        String ct = clientType.getText().toString();

                        updateData(fn,ln,gen,Age,phn,Reg,Prov,muni,add,ct);
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
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
                            String Phone = "" + dataSnapshot1.child("phone").getValue();
                            String Email = "" + dataSnapshot1.child("email").getValue();
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
                            phone.setText(Phone);
                            email.setText(Email);
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
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });
        //retrieve profile from database
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
                    String Phone = "" + dataSnapshot1.child("phone").getValue();
                    String Region = "" + dataSnapshot1.child("region").getValue();
                    String prov = "" + dataSnapshot1.child("province").getValue();
                    String muni = "" + dataSnapshot1.child("municipality").getValue();
                    String house = "" + dataSnapshot1.child("address").getValue();
                    String client = "" + dataSnapshot1.child("clientType").getValue();
                    String image = "" + dataSnapshot1.child("profileImage").child("imageUrl").getValue();

                    // setting data to our text view
                    usernameText.setText(fn + " " + ln);
                    firstnameText.setText(fn);
                    lastnameText.setText(ln);
                    genderText.setText(Gender);
                    ageText.setText(Age);
                    emailAddressText.setText(emailAdd);
                    phoneText.setText(Phone);
                    regionText.setText(Region);
                    provinceText.setText(prov);
                    municipalityText.setText(muni);
                    addressText.setText(house);
                    clientTypeText.setText(client);

                    try {
                        Glide.with(getActivity()).load(image).placeholder(R.drawable.ic_profilepic).into(profileImage);
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK) {
                camUri = data.getData();
                pd.setMessage("Changing Profile...Please Wait");
                pd.show();
                dialog.dismiss();
                onCaptureImageResult(data);
            }
        }
        if (requestCode == 2000) {
            if(resultCode == Activity.RESULT_OK) {
                galleryUri = data.getData();
                pd.setMessage("Changing Profile...Please Wait");
                pd.show();
                dialog.dismiss();
                uploadToFirebase2();
            }
        }
    }

    private void uploadToFirebase(byte[] bb) {
        final StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
        fileRef.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Image image = new Image(uri.toString());
//                        String imageId = reference.push().toString();
                        reference.setValue(image);
                        pd.dismiss();
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Profile changed successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadToFirebase2() {
        final StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
        fileRef.putFile(galleryUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Image image = new Image(uri.toString());
//                        String imageId = reference.push().toString();
                        reference.setValue(image);
                        pd.dismiss();
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Profile changed successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    public void pick_camera() {
        Intent openGalleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openGalleryIntent, 1000);
    }
    public void pick_gallery() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openGalleryIntent, 2000);
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

    private void onCaptureImageResult(Intent data){
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byte bb[] = bytes.toByteArray();
//        profileImage.setImageBitmap(bitmap);

        uploadToFirebase(bb);
    }

    private void updateData(String fn, String ln, String gen, String Age, String phn, String Reg, String Prov, String muni, String add, String ct) {
        HashMap user = new HashMap();
        user.put("firstname", fn);
        user.put("lastname", ln);
        user.put("gender", gen);
        user.put("age", Age);
        user.put("phone", phn);
        user.put("region", Reg);
        user.put("province", Prov);
        user.put("municipality", muni);
        user.put("address", add);
        user.put("clientType", ct);

        databaseReference.child(fAuth.getCurrentUser().getUid()).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Failed To Update", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}