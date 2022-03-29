package com.example.dakbayaknow.ui.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.dakbayaknow.Image;
import com.example.dakbayaknow.MainActivity;
import com.example.dakbayaknow.R;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, reference;

    TextView firstnameText, lastnameText, genderText, ageText, emailAddressText, regionText, provinceText, municipalityText, addressText, clientTypeText;
    ProgressDialog pd;

    FirebaseAuth fAuth;
    ImageView profileImage;
    StorageReference storageReference;
    FirebaseUser firebaseUser;

    ImageButton editProfile, editLastname, editGender, editAge, editEmail, editRegion, editProvince, editMunicipality, editAddress, editClientType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        ((AppCompatActivity)getContext()).getSupportActionBar().setTitle("Profile Information");

        // getting current user data
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        reference = firebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("profileImage");

        // Initialising the text view and imageview
        firstnameText = view.findViewById(R.id.firstname);
        lastnameText = view.findViewById(R.id.lastname);
        genderText = view.findViewById(R.id.gender);
        ageText = view.findViewById(R.id.age);
        emailAddressText = view.findViewById(R.id.userEmail);
        regionText = view.findViewById(R.id.region);
        provinceText = view.findViewById(R.id.province);
        municipalityText = view.findViewById(R.id.municipality);
        addressText = view.findViewById(R.id.address);
        clientTypeText = view.findViewById(R.id.clientType);
        profileImage = view.findViewById(R.id.image);

        editProfile = view.findViewById(R.id.editProfile);
        editLastname = view.findViewById(R.id.editLastname);
        editGender = view.findViewById(R.id.editGender);
        editAge = view.findViewById(R.id.editAge);
        editEmail = view.findViewById(R.id.editEmail);
        editRegion = view.findViewById(R.id.editRegion);
        editProvince = view.findViewById(R.id.editProvince);
        editMunicipality = view.findViewById(R.id.editMunicipality);
        editAddress = view.findViewById(R.id.editAddress);
        editClientType = view.findViewById(R.id.editClientType);

        pd = new ProgressDialog(getActivity());
        pd.setCanceledOnTouchOutside(false);

        fAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Changing First Name");
                showFirstnameUpdate("firstname");
            }
        });

        editLastname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Changing Last Name");
                showLastnameUpdate("lastname");
            }
        });

        editGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Changing Gender");
                showGenderUpdate("gender");
            }
        });

        editAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Changing Age");
                showAgeUpdate("age");
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Changing Email Address");
                showEmailUpdate("email");
            }
        });

        editRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Changing Region");
                showRegionUpdate("region");
            }
        });

        editProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Changing Province");
                showProvinceUpdate("province");
            }
        });

        editMunicipality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Changing Municipality");
                showMunicipalityUpdate("municipality");
            }
        });

        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Changing Address");
                showAddressUpdate("address");
            }
        });

        editClientType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Changing Client Type");
                showClientTypeUpdate("clientType");
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
                    String image = "" + dataSnapshot1.child("profileImage").child("imageUrl").getValue();

                    // setting data to our text view
                    firstnameText.setText(fn);
                    lastnameText.setText(ln);
                    genderText.setText(Gender);
                    ageText.setText(Age);
                    emailAddressText.setText(emailAdd);
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
                Uri imageUri = data.getData();
                profileImage.setImageURI(imageUri);
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
                        Image image = new Image(uri.toString());
//                        String imageId = reference.push().toString();
                        reference.setValue(image);
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

    private void showFirstnameUpdate(final String fn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update" + fn);

        // creating a layout to write the new name
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        final EditText editText = new EditText(getContext());
        editText.setHint("Enter " + fn);
        layout.addView(editText);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    pd.show();

                    // Here we are updating the new name
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(fn, value);
                    databaseReference.child(firebaseUser.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();

                            // after updated we will show updated
                            Toast.makeText(getContext(), " updated ", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                        }
                    });
                    if (fn.equals("firstname")) {
                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                        Query query = database.orderByChild("email").equalTo(firebaseUser.getEmail());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String child = database.getKey();
                                    dataSnapshot1.getRef().child("firstname").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });
        builder.create().show();
    }

    private void showLastnameUpdate(final String ln) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update" + ln);

        // creating a layout to write the new name
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        final EditText editText = new EditText(getContext());
        editText.setHint("Enter " + ln);
        layout.addView(editText);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    pd.show();

                    // Here we are updating the new name
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(ln, value);
                    databaseReference.child(firebaseUser.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();

                            // after updated we will show updated
                            Toast.makeText(getContext(), " updated ", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                        }
                    });
                    if (ln.equals("lastname")) {
                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                        Query query = database.orderByChild("email").equalTo(firebaseUser.getEmail());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String child = database.getKey();
                                    dataSnapshot1.getRef().child("lastname").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });
        builder.create().show();
    }

    private void showGenderUpdate(final String g) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update" + g);

        // creating a layout to write the new name
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        final EditText editText = new EditText(getContext());
        editText.setHint("Enter " + g);
        layout.addView(editText);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    pd.show();

                    // Here we are updating the new name
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(g, value);
                    databaseReference.child(firebaseUser.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();

                            // after updated we will show updated
                            Toast.makeText(getContext(), " updated ", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                        }
                    });
                    if (g.equals("gender")) {
                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                        Query query = database.orderByChild("email").equalTo(firebaseUser.getEmail());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String child = database.getKey();
                                    dataSnapshot1.getRef().child("gender").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });
        builder.create().show();
    }

    private void showAgeUpdate(final String a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update" + a);

        // creating a layout to write the new name
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        final EditText editText = new EditText(getContext());
        editText.setHint("Enter " + a);
        layout.addView(editText);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    pd.show();

                    // Here we are updating the new name
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(a, value);
                    databaseReference.child(firebaseUser.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();

                            // after updated we will show updated
                            Toast.makeText(getContext(), " updated ", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                        }
                    });
                    if (a.equals("age")) {
                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                        Query query = database.orderByChild("email").equalTo(firebaseUser.getEmail());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String child = database.getKey();
                                    dataSnapshot1.getRef().child("age").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });
        builder.create().show();
    }

    private void showEmailUpdate(final String e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update" + e);

        // creating a layout to write the new name
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        final EditText editText = new EditText(getContext());
        editText.setHint("Enter " + e);
        layout.addView(editText);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    pd.show();

                    // Here we are updating the new name
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(e, value);
                    databaseReference.child(firebaseUser.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();

                            // after updated we will show updated
                            Toast.makeText(getContext(), " updated ", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                        }
                    });
                    if (e.equals("email")) {
                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                        Query query = database.orderByChild("email").equalTo(firebaseUser.getEmail());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String child = database.getKey();
                                    dataSnapshot1.getRef().child("email").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });
        builder.create().show();
    }

    private void showRegionUpdate(final String r) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update" + r);

        // creating a layout to write the new name
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        final EditText editText = new EditText(getContext());
        editText.setHint("Enter " + r);
        layout.addView(editText);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    pd.show();

                    // Here we are updating the new name
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(r, value);
                    databaseReference.child(firebaseUser.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();

                            // after updated we will show updated
                            Toast.makeText(getContext(), " updated ", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                        }
                    });
                    if (r.equals("region")) {
                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                        Query query = database.orderByChild("email").equalTo(firebaseUser.getEmail());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String child = database.getKey();
                                    dataSnapshot1.getRef().child("region").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });
        builder.create().show();
    }

    private void showProvinceUpdate(final String p) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update" + p);

        // creating a layout to write the new name
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        final EditText editText = new EditText(getContext());
        editText.setHint("Enter " + p);
        layout.addView(editText);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    pd.show();

                    // Here we are updating the new name
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(p, value);
                    databaseReference.child(firebaseUser.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();

                            // after updated we will show updated
                            Toast.makeText(getContext(), " updated ", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                        }
                    });
                    if (p.equals("province")) {
                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                        Query query = database.orderByChild("email").equalTo(firebaseUser.getEmail());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String child = database.getKey();
                                    dataSnapshot1.getRef().child("province").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });
        builder.create().show();
    }

    private void showMunicipalityUpdate(final String m) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update" + m);

        // creating a layout to write the new name
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        final EditText editText = new EditText(getContext());
        editText.setHint("Enter " + m);
        layout.addView(editText);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    pd.show();

                    // Here we are updating the new name
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(m, value);
                    databaseReference.child(firebaseUser.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();

                            // after updated we will show updated
                            Toast.makeText(getContext(), " updated ", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                        }
                    });
                    if (m.equals("municipality")) {
                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                        Query query = database.orderByChild("email").equalTo(firebaseUser.getEmail());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String child = database.getKey();
                                    dataSnapshot1.getRef().child("municipality").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });
        builder.create().show();
    }

    private void showAddressUpdate(final String add) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update" + add);

        // creating a layout to write the new name
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        final EditText editText = new EditText(getContext());
        editText.setHint("Enter " + add);
        layout.addView(editText);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    pd.show();

                    // Here we are updating the new name
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(add, value);
                    databaseReference.child(firebaseUser.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();

                            // after updated we will show updated
                            Toast.makeText(getContext(), " updated ", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                        }
                    });
                    if (add.equals("address")) {
                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                        Query query = database.orderByChild("email").equalTo(firebaseUser.getEmail());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String child = database.getKey();
                                    dataSnapshot1.getRef().child("address").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });
        builder.create().show();
    }

    private void showClientTypeUpdate(final String ct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update" + ct);

        // creating a layout to write the new name
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);
        final EditText editText = new EditText(getContext());
        editText.setHint("Enter " + ct);
        layout.addView(editText);
        builder.setView(layout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    pd.show();

                    // Here we are updating the new name
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(ct, value);
                    databaseReference.child(firebaseUser.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();

                            // after updated we will show updated
                            Toast.makeText(getContext(), " updated ", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                        }
                    });
                    if (ct.equals("clientType")) {
                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
                        Query query = database.orderByChild("email").equalTo(firebaseUser.getEmail());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String child = database.getKey();
                                    dataSnapshot1.getRef().child("clientType").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Unable to update", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd.dismiss();
            }
        });
        builder.create().show();
    }
}