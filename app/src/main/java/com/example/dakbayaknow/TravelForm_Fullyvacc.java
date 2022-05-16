package com.example.dakbayaknow;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TravelForm_Fullyvacc extends AppCompatActivity {

    private RadioButton male, female, rather;

    private TextInputEditText firstnameText, middleInitialText, lastnameText, suffixnameText, emailText, contactNumberText,
            emergencyContactPersonText, emergencyContactNumberText,
            cAddressText, dAddressText,
            departureText, arrivalText;

    private Button submitButton;

    FirebaseDatabase database;
    DatabaseReference reference, ref2;
    FirebaseAuth fAuth;

    TravelFormDetails value;
    Applications value2;

    int maxid = 1;

    AutoCompleteTextView spinner_travellerType, spinner_title;

    private android.widget.Spinner spinner_cProvince, spinner_cRegion, spinner_cMunicipality,
                                   spinner_dProvince, spinner_dRegion, spinner_dMunicipality;

    ArrayList<String> arrayList_Region, arrayList_Province, arrayList_Municipality;
    ArrayAdapter<String> arrayAdapter_Region, arrayAdapter_Province, arrayAdapter_Municipality;

    DatePickerDialog.OnDateSetListener setListener, setListener2;

    Dialog dialog;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travelform_fullyvacc);

        getSupportActionBar().setTitle("Travel Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //edit text
        firstnameText = findViewById(R.id.firstname);
        middleInitialText = findViewById(R.id.middleInitial);
        lastnameText = findViewById(R.id.lastname);
        suffixnameText = findViewById(R.id.suffixname);
        emailText = findViewById(R.id.emailAddress);
        contactNumberText = findViewById(R.id.contactNumber);
        emergencyContactPersonText = findViewById(R.id.emergencyContactPerson);
        emergencyContactNumberText = findViewById(R.id.emergencyContactNumber);

        cAddressText = findViewById(R.id.cAddress);
        dAddressText = findViewById(R.id.dAddress);
        departureText = findViewById(R.id.departure);
        arrivalText = findViewById(R.id.arrival);

        //spinner
        spinner_travellerType = findViewById(R.id.spinner_travellerType);
        spinner_title = findViewById(R.id.spinner_title);

        spinner_cRegion = findViewById(R.id.spinner_cRegion);
        spinner_cProvince= findViewById(R.id.spinner_cProvince);
        spinner_cMunicipality = findViewById(R.id.spinner_cMunicipality);
        spinner_dRegion = findViewById(R.id.spinner_dRegion);
        spinner_dProvince= findViewById(R.id.spinner_dProvince);
        spinner_dMunicipality = findViewById(R.id.spinner_dMunicipality);

        //radiobutton
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        rather = findViewById(R.id.rather);

        submitButton = findViewById(R.id.submitButton);
        fAuth = FirebaseAuth.getInstance();

        value = new TravelFormDetails();
        value2 = new Applications();

        reference = database.getInstance().getReference("users").child(fAuth.getCurrentUser().getUid()).child("travelform");
        ref2 = database.getInstance().getReference("applications");

        dialog = new Dialog(this);
        progressDialog = new ProgressDialog(this);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int year2 = calendar.get(Calendar.YEAR);
        final int month2 = calendar.get(Calendar.MONTH);
        final int day2 = calendar.get(Calendar.DAY_OF_MONTH);

        departureText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        TravelForm_Fullyvacc.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                month = month+1;
                String date = month+"/"+dayofMonth+"/"+year;
                departureText.setText(date);
            }
        };
        arrivalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog2 = new DatePickerDialog(
                        TravelForm_Fullyvacc.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener2, year2, month2, day2);
                datePickerDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog2.show();
            }
        });
        setListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                month = month+1;
                String date = month+"/"+dayofMonth+"/"+year;
                arrivalText.setText(date);
            }
        };

        List<String> Categories = new ArrayList<>();
        Categories.add("Student");
        Categories.add("Local Tourist");
        Categories.add("Filipino Local Workers");
        Categories.add("Stranded Individual");
        Categories.add("Others please specify");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this, R.layout.textview_gray, Categories);
        dataAdapter.setDropDownViewResource(R.layout.textview_gray);

        spinner_travellerType.setAdapter(dataAdapter);
        spinner_travellerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> Categories2 = new ArrayList<>();
        Categories2.add("Mr.");
        Categories2.add("Mrs.");
        Categories2.add("Ms.");

        ArrayAdapter<String> dataAdapter2;
        dataAdapter2 = new ArrayAdapter<>(this, R.layout.textview_gray, Categories2);
        dataAdapter2.setDropDownViewResource(R.layout.textview_gray);

        spinner_title.setAdapter(dataAdapter2);
        spinner_title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        init();

        spinner_cRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i , long l) {
                int position = (int)adapterView.getItemIdAtPosition(i);
                switch (position) {
                    case 0:
                        arrayList_Province = new ArrayList<>();
                        arrayList_Province.add("City of Manila");
                        loadProvince(arrayList_Province);
                        break;
                    case 1:
                        arrayList_Province = new ArrayList<>();
                        arrayList_Province.add("Aklan");
                        arrayList_Province.add("Antique");
                        arrayList_Province.add("Capiz");
                        arrayList_Province.add("Guimaras");
                        arrayList_Province.add("Iloilo");
                        arrayList_Province.add("Negros Occidental");
                        arrayList_Province.add("Bacolod City");
                        arrayList_Province.add("Iloilo City");
                        loadProvince(arrayList_Province);
                        break;
                    case 2:
                        arrayList_Province = new ArrayList<>();
                        arrayList_Province.add("Bohol");
                        arrayList_Province.add("Cebu");
                        arrayList_Province.add("Negros Oriental");
                        arrayList_Province.add("Siquijor");
                        arrayList_Province.add("Cebu City");
                        arrayList_Province.add("Lapu-Lapu City");
                        arrayList_Province.add("Mandaue City");
                        loadProvince(arrayList_Province);
                        break;
                    case 3:
                        arrayList_Province = new ArrayList<>();
                        arrayList_Province.add("Biliran");
                        arrayList_Province.add("Eastern Samar");
                        arrayList_Province.add("Leyte");
                        arrayList_Province.add("Northern Samar");
                        arrayList_Province.add("Samar");
                        arrayList_Province.add("Southern Leyte");
                        arrayList_Province.add("Tacloban City");
                        arrayList_Province.add("Ormoc City");
                        loadProvince(arrayList_Province);
                        break;
                    case 4:
                        arrayList_Province = new ArrayList<>();
                        arrayList_Province.add("Cagayan De Oro City");
                        loadProvince(arrayList_Province);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_cProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i , long l) {
                String item = adapterView.getSelectedItem().toString();
                switch (item) {
                    //NCR
                    case "City of Manila":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("City of Manila");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    //region 6
                    case "Aklan":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Altavas");
                        arrayList_Municipality.add("Balete");
                        arrayList_Municipality.add("Banga");
                        arrayList_Municipality.add("Batan");
                        arrayList_Municipality.add("Buruanga");
                        arrayList_Municipality.add("Ibajay");
                        arrayList_Municipality.add("Kalibo");
                        arrayList_Municipality.add("Lezo");
                        arrayList_Municipality.add("Libacao");
                        arrayList_Municipality.add("Madalag");
                        arrayList_Municipality.add("Makato");
                        arrayList_Municipality.add("Malay");
                        arrayList_Municipality.add("Malinao");
                        arrayList_Municipality.add("Nabas");
                        arrayList_Municipality.add("New Washington");
                        arrayList_Municipality.add("Numancia");
                        arrayList_Municipality.add("Tangalan");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Antique":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Anini-y");
                        arrayList_Municipality.add("Barbaza");
                        arrayList_Municipality.add("Belison");
                        arrayList_Municipality.add("Bugasong");
                        arrayList_Municipality.add("Caluya");
                        arrayList_Municipality.add("Culasi");
                        arrayList_Municipality.add("Hamtic");
                        arrayList_Municipality.add("Laua-an");
                        arrayList_Municipality.add("Libertad");
                        arrayList_Municipality.add("Pandan");
                        arrayList_Municipality.add("Patnongon");
                        arrayList_Municipality.add("San Jose de Buenavista");
                        arrayList_Municipality.add("San Remigio");
                        arrayList_Municipality.add("Sebaste");
                        arrayList_Municipality.add("Sibalom");
                        arrayList_Municipality.add("Tibiao");
                        arrayList_Municipality.add("Tobias Fornier");
                        arrayList_Municipality.add("Valderrama");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Capiz":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Cuartero");
                        arrayList_Municipality.add("Dao");
                        arrayList_Municipality.add("Dumalag");
                        arrayList_Municipality.add("Dumarao");
                        arrayList_Municipality.add("Ivisan");
                        arrayList_Municipality.add("Jamindan");
                        arrayList_Municipality.add("Ma-ayon");
                        arrayList_Municipality.add("Mambusao");
                        arrayList_Municipality.add("Panay");
                        arrayList_Municipality.add("Panitan");
                        arrayList_Municipality.add("Pilar");
                        arrayList_Municipality.add("Pontevedra");
                        arrayList_Municipality.add("President Roxas");
                        arrayList_Municipality.add("Roxas");
                        arrayList_Municipality.add("Sapian");
                        arrayList_Municipality.add("Sigma");
                        arrayList_Municipality.add("Tapaz");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Guimaras":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Buenavista");
                        arrayList_Municipality.add("Jordan");
                        arrayList_Municipality.add("Nueva Valencia");
                        arrayList_Municipality.add("San Lorenzo");
                        arrayList_Municipality.add("Sibunag");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Iloilo":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Ajuy");
                        arrayList_Municipality.add("Alimodian");
                        arrayList_Municipality.add("Anilao");
                        arrayList_Municipality.add("Badiangan");
                        arrayList_Municipality.add("Balasan");
                        arrayList_Municipality.add("Banate");
                        arrayList_Municipality.add("Barotac Nuevo");
                        arrayList_Municipality.add("Barotac Viejo");
                        arrayList_Municipality.add("Batad");
                        arrayList_Municipality.add("Bingawan");
                        arrayList_Municipality.add("Cabatuan");
                        arrayList_Municipality.add("Calinog");
                        arrayList_Municipality.add("Carles");
                        arrayList_Municipality.add("Concepcion");
                        arrayList_Municipality.add("Dingle");
                        arrayList_Municipality.add("Dueñas");
                        arrayList_Municipality.add("Dumangas");
                        arrayList_Municipality.add("Estancia");
                        arrayList_Municipality.add("Guimbal");
                        arrayList_Municipality.add("Igbaras");
                        arrayList_Municipality.add("Janiuay");
                        arrayList_Municipality.add("Lambunao");
                        arrayList_Municipality.add("Leganes");
                        arrayList_Municipality.add("Lemery");
                        arrayList_Municipality.add("Leon");
                        arrayList_Municipality.add("Maasin");
                        arrayList_Municipality.add("Miagao");
                        arrayList_Municipality.add("Mina");
                        arrayList_Municipality.add("New Lucena");
                        arrayList_Municipality.add("Oton");
                        arrayList_Municipality.add("Passi");
                        arrayList_Municipality.add("Pavia");
                        arrayList_Municipality.add("Pototan");
                        arrayList_Municipality.add("San Dionisio");
                        arrayList_Municipality.add("San Enrique");
                        arrayList_Municipality.add("San Joaquin");
                        arrayList_Municipality.add("San Miguel");
                        arrayList_Municipality.add("San Rafael");
                        arrayList_Municipality.add("Santa Barbara");
                        arrayList_Municipality.add("Sara");
                        arrayList_Municipality.add("Tigbauan");
                        arrayList_Municipality.add("Tubungan");
                        arrayList_Municipality.add("Zarraga");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Negros Occidental":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Bacolod");
                        arrayList_Municipality.add("Bago");
                        arrayList_Municipality.add("Binalbagan");
                        arrayList_Municipality.add("Cadiz");
                        arrayList_Municipality.add("Calatrava");
                        arrayList_Municipality.add("Candoni");
                        arrayList_Municipality.add("Cauayan");
                        arrayList_Municipality.add("Enrique B. Magalona");
                        arrayList_Municipality.add("Escalante");
                        arrayList_Municipality.add("Himamaylan");
                        arrayList_Municipality.add("Hinigaran");
                        arrayList_Municipality.add("Hinoba-an");
                        arrayList_Municipality.add("Ilog");
                        arrayList_Municipality.add("Isabela");
                        arrayList_Municipality.add("Kabankalan");
                        arrayList_Municipality.add("La Carlota");
                        arrayList_Municipality.add("La Castellana");
                        arrayList_Municipality.add("Manapla");
                        arrayList_Municipality.add("Moises Padilla");
                        arrayList_Municipality.add("Murcia");
                        arrayList_Municipality.add("Pontevedra");
                        arrayList_Municipality.add("Pulupandan");
                        arrayList_Municipality.add("Sagay");
                        arrayList_Municipality.add("Salvador Benedicto");
                        arrayList_Municipality.add("San Carlos");
                        arrayList_Municipality.add("San Enrique");
                        arrayList_Municipality.add("Silay");
                        arrayList_Municipality.add("Sipalay");
                        arrayList_Municipality.add("Talisay");
                        arrayList_Municipality.add("Toboso");
                        arrayList_Municipality.add("Valladolid");
                        arrayList_Municipality.add("Victorias");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Bacolod City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Bacolod City");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Iloilo City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Iloilo City");
                        loadMunicipality(arrayList_Municipality);
                        break;

                    //region 7
                    case "Bohol":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Alburquerque");
                        arrayList_Municipality.add("Alicia");
                        arrayList_Municipality.add("Anda");
                        arrayList_Municipality.add("Antequera");
                        arrayList_Municipality.add("Baclayon");
                        arrayList_Municipality.add("Balilihan");
                        arrayList_Municipality.add("Batuan");
                        arrayList_Municipality.add("Bien Unido");
                        arrayList_Municipality.add("Bilar");
                        arrayList_Municipality.add("Buenavista");
                        arrayList_Municipality.add("Calape");
                        arrayList_Municipality.add("Candijay");
                        arrayList_Municipality.add("Carmen");
                        arrayList_Municipality.add("Catigbian");
                        arrayList_Municipality.add("Clarin");
                        arrayList_Municipality.add("Corella");
                        arrayList_Municipality.add("Cortes");
                        arrayList_Municipality.add("Dagohoy");
                        arrayList_Municipality.add("Danao");
                        arrayList_Municipality.add("Dauis");
                        arrayList_Municipality.add("Dimiao");
                        arrayList_Municipality.add("Duero");
                        arrayList_Municipality.add("Garcia Hernandez");
                        arrayList_Municipality.add("Getafe");
                        arrayList_Municipality.add("Guindulman");
                        arrayList_Municipality.add("Inabanga");
                        arrayList_Municipality.add("Jagna");
                        arrayList_Municipality.add("Lila");
                        arrayList_Municipality.add("Loay");
                        arrayList_Municipality.add("Loboc");
                        arrayList_Municipality.add("Loon");
                        arrayList_Municipality.add("Mabini");
                        arrayList_Municipality.add("Maribojoc");
                        arrayList_Municipality.add("Panglao");
                        arrayList_Municipality.add("Pilar");
                        arrayList_Municipality.add("President Carlos P. Garcia");
                        arrayList_Municipality.add("Sagbayan");
                        arrayList_Municipality.add("San Isidro");
                        arrayList_Municipality.add("San Miguel");
                        arrayList_Municipality.add("Sevilla");
                        arrayList_Municipality.add("Sierra Bullones");
                        arrayList_Municipality.add("Sikatuna");
                        arrayList_Municipality.add("Tagbilaran");
                        arrayList_Municipality.add("Talibon");
                        arrayList_Municipality.add("Trinidad");
                        arrayList_Municipality.add("Tubigon");
                        arrayList_Municipality.add("Ubay");
                        arrayList_Municipality.add("Valencia");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Cebu":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Alcantara");
                        arrayList_Municipality.add("Alcoy");
                        arrayList_Municipality.add("Alegria");
                        arrayList_Municipality.add("Aloguinsan");
                        arrayList_Municipality.add("Argao");
                        arrayList_Municipality.add("Asturias");
                        arrayList_Municipality.add("Badian");
                        arrayList_Municipality.add("Balamban");
                        arrayList_Municipality.add("Bantayan");
                        arrayList_Municipality.add("Barili");
                        arrayList_Municipality.add("Bogo");
                        arrayList_Municipality.add("Boljoon");
                        arrayList_Municipality.add("Borbon");
                        arrayList_Municipality.add("Carcar");
                        arrayList_Municipality.add("Carmen");
                        arrayList_Municipality.add("Catmon");
                        arrayList_Municipality.add("Compostela");
                        arrayList_Municipality.add("Consolacion");
                        arrayList_Municipality.add("Cordova");
                        arrayList_Municipality.add("Daanbantayan");
                        arrayList_Municipality.add("Dalaguete");
                        arrayList_Municipality.add("Danao");
                        arrayList_Municipality.add("Dumanjug");
                        arrayList_Municipality.add("Ginatilan");
                        arrayList_Municipality.add("Liloan");
                        arrayList_Municipality.add("Madridejos");
                        arrayList_Municipality.add("Malabuyoc");
                        arrayList_Municipality.add("Medellin");
                        arrayList_Municipality.add("Minglanilla");
                        arrayList_Municipality.add("Moalboal");
                        arrayList_Municipality.add("Naga");
                        arrayList_Municipality.add("Oslob");
                        arrayList_Municipality.add("Pilar");
                        arrayList_Municipality.add("Pinamungajan");
                        arrayList_Municipality.add("Poro");
                        arrayList_Municipality.add("Ronda");
                        arrayList_Municipality.add("Samboan");
                        arrayList_Municipality.add("San Fernando");
                        arrayList_Municipality.add("San Francisco");
                        arrayList_Municipality.add("San Remigio");
                        arrayList_Municipality.add("Santa Fe");
                        arrayList_Municipality.add("Santander");
                        arrayList_Municipality.add("Sibonga");
                        arrayList_Municipality.add("Sogod");
                        arrayList_Municipality.add("Tabogon");
                        arrayList_Municipality.add("Tabuelan");
                        arrayList_Municipality.add("Talisay");
                        arrayList_Municipality.add("Toledo");
                        arrayList_Municipality.add("Tuburan");
                        arrayList_Municipality.add("Tudela");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Negros Oriental":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Amlan");
                        arrayList_Municipality.add("Ayungon");
                        arrayList_Municipality.add("Bacong");
                        arrayList_Municipality.add("Bais");
                        arrayList_Municipality.add("Basay");
                        arrayList_Municipality.add("Bayawan");
                        arrayList_Municipality.add("Bindoy");
                        arrayList_Municipality.add("Canlaon");
                        arrayList_Municipality.add("Dauin");
                        arrayList_Municipality.add("Dumaguete");
                        arrayList_Municipality.add("Guihulngan");
                        arrayList_Municipality.add("Jimalalud");
                        arrayList_Municipality.add("La Libertad");
                        arrayList_Municipality.add("Mabinay");
                        arrayList_Municipality.add("Manjuyod");
                        arrayList_Municipality.add("Pamplona");
                        arrayList_Municipality.add("San Jose");
                        arrayList_Municipality.add("Santa Catalina");
                        arrayList_Municipality.add("Siaton");
                        arrayList_Municipality.add("Sibulan");
                        arrayList_Municipality.add("Tanjay");
                        arrayList_Municipality.add("Tayasan");
                        arrayList_Municipality.add("Valencia");
                        arrayList_Municipality.add("Vallehermoso");
                        arrayList_Municipality.add("Zamboanguita");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Siquijor":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Enrique Villanueva");
                        arrayList_Municipality.add("Larena");
                        arrayList_Municipality.add("Lazi");
                        arrayList_Municipality.add("Maria");
                        arrayList_Municipality.add("San Juan");
                        arrayList_Municipality.add("Siquijor");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Cebu City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Cebu City");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Lapu-Lapu City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Lapu-Lapu City");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Mandaue City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Mandaue City");
                        loadMunicipality(arrayList_Municipality);
                        break;

                    //region 8
                    case "Biliran":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Almeria");
                        arrayList_Municipality.add("Biliran");
                        arrayList_Municipality.add("Cabucgayan");
                        arrayList_Municipality.add("Caibiran");
                        arrayList_Municipality.add("Culaba");
                        arrayList_Municipality.add("Kawayan");
                        arrayList_Municipality.add("Maripipi");
                        arrayList_Municipality.add("Naval");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Eastern Samar":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Arteche");
                        arrayList_Municipality.add("Balangiga");
                        arrayList_Municipality.add("Balangkayan");
                        arrayList_Municipality.add("Borongan");
                        arrayList_Municipality.add("Can-avid");
                        arrayList_Municipality.add("Dolores");
                        arrayList_Municipality.add("General MacArthur");
                        arrayList_Municipality.add("Giporlos");
                        arrayList_Municipality.add("Guiuan");
                        arrayList_Municipality.add("Hernani");
                        arrayList_Municipality.add("Jipapad");
                        arrayList_Municipality.add("Lawaan");
                        arrayList_Municipality.add("Llorente");
                        arrayList_Municipality.add("Maslog");
                        arrayList_Municipality.add("Maydolong");
                        arrayList_Municipality.add("Mercedes");
                        arrayList_Municipality.add("Oras");
                        arrayList_Municipality.add("Quinapondan");
                        arrayList_Municipality.add("Salcedo");
                        arrayList_Municipality.add("San Julian");
                        arrayList_Municipality.add("San Policarpo");
                        arrayList_Municipality.add("Sulat");
                        arrayList_Municipality.add("Taft");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Leyte":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Abuyog");
                        arrayList_Municipality.add("Alangalang");
                        arrayList_Municipality.add("Albuera");
                        arrayList_Municipality.add("Babatngon");
                        arrayList_Municipality.add("Barugo");
                        arrayList_Municipality.add("Bato");
                        arrayList_Municipality.add("Baybay");
                        arrayList_Municipality.add("Burauen");
                        arrayList_Municipality.add("Calubian");
                        arrayList_Municipality.add("Capoocan");
                        arrayList_Municipality.add("Carigara");
                        arrayList_Municipality.add("Dagami");
                        arrayList_Municipality.add("Dulag");
                        arrayList_Municipality.add("Hilongos");
                        arrayList_Municipality.add("Hindang");
                        arrayList_Municipality.add("Inopacan");
                        arrayList_Municipality.add("Isabel");
                        arrayList_Municipality.add("Jaro");
                        arrayList_Municipality.add("Javier");
                        arrayList_Municipality.add("Julita");
                        arrayList_Municipality.add("Kananga");
                        arrayList_Municipality.add("La Paz");
                        arrayList_Municipality.add("Leyte");
                        arrayList_Municipality.add("MacArthur");
                        arrayList_Municipality.add("Mahaplag");
                        arrayList_Municipality.add("Matag-ob");
                        arrayList_Municipality.add("Matalom");
                        arrayList_Municipality.add("Mayorga");
                        arrayList_Municipality.add("Merida");
                        arrayList_Municipality.add("Palo");
                        arrayList_Municipality.add("Palompon");
                        arrayList_Municipality.add("Pastrana");
                        arrayList_Municipality.add("San Isidro");
                        arrayList_Municipality.add("San Miguel");
                        arrayList_Municipality.add("Santa Fe");
                        arrayList_Municipality.add("Tabango");
                        arrayList_Municipality.add("Tabontabon");
                        arrayList_Municipality.add("Tanauan");
                        arrayList_Municipality.add("Tolosa");
                        arrayList_Municipality.add("Tunga");
                        arrayList_Municipality.add("Villaba");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Northern Samar":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Allen");
                        arrayList_Municipality.add("Biri");
                        arrayList_Municipality.add("Bobon");
                        arrayList_Municipality.add("Capul");
                        arrayList_Municipality.add("Catarman");
                        arrayList_Municipality.add("Catubig");
                        arrayList_Municipality.add("Gamay");
                        arrayList_Municipality.add("Laoang");
                        arrayList_Municipality.add("Lapinig");
                        arrayList_Municipality.add("Las Navas");
                        arrayList_Municipality.add("Lavezares");
                        arrayList_Municipality.add("Lope de Vega");
                        arrayList_Municipality.add("Mapanas");
                        arrayList_Municipality.add("Mondragon");
                        arrayList_Municipality.add("Palapag");
                        arrayList_Municipality.add("Pambujan");
                        arrayList_Municipality.add("Rosario");
                        arrayList_Municipality.add("San Antonio");
                        arrayList_Municipality.add("San Isidro");
                        arrayList_Municipality.add("San Jose");
                        arrayList_Municipality.add("San Roque");
                        arrayList_Municipality.add("San Vicente");
                        arrayList_Municipality.add("Silvino Lobos");
                        arrayList_Municipality.add("Victoria");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Samar":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Almagro");
                        arrayList_Municipality.add("Basey");
                        arrayList_Municipality.add("Calbayog");
                        arrayList_Municipality.add("Calbiga");
                        arrayList_Municipality.add("Catbalogan");
                        arrayList_Municipality.add("Daram");
                        arrayList_Municipality.add("Gandara");
                        arrayList_Municipality.add("Hinabangan");
                        arrayList_Municipality.add("Jiabong");
                        arrayList_Municipality.add("Marabut");
                        arrayList_Municipality.add("Matuguinao");
                        arrayList_Municipality.add("Motiong");
                        arrayList_Municipality.add("Pagsanghan");
                        arrayList_Municipality.add("Paranas");
                        arrayList_Municipality.add("Pinabacdao");
                        arrayList_Municipality.add("San Jorge");
                        arrayList_Municipality.add("San Jose de Buan");
                        arrayList_Municipality.add("San Sebastian");
                        arrayList_Municipality.add("Santa Margarita");
                        arrayList_Municipality.add("Santa Rita");
                        arrayList_Municipality.add("Santo Niño");
                        arrayList_Municipality.add("Tagapul-an");
                        arrayList_Municipality.add("Talalora");
                        arrayList_Municipality.add("Tarangnan");
                        arrayList_Municipality.add("Villareal");
                        arrayList_Municipality.add("Zumarraga");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Southern Leyte":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Anahawan");
                        arrayList_Municipality.add("Bontoc");
                        arrayList_Municipality.add("Hinunangan");
                        arrayList_Municipality.add("Hinundayan");
                        arrayList_Municipality.add("Libagon");
                        arrayList_Municipality.add("Liloan");
                        arrayList_Municipality.add("Limasawa");
                        arrayList_Municipality.add("Maasin");
                        arrayList_Municipality.add("Macrohon");
                        arrayList_Municipality.add("Malitbog");
                        arrayList_Municipality.add("Padre Burgos");
                        arrayList_Municipality.add("Pintuyan");
                        arrayList_Municipality.add("Saint Bernard");
                        arrayList_Municipality.add("San Francisco");
                        arrayList_Municipality.add("San Juan");
                        arrayList_Municipality.add("San Ricardo");
                        arrayList_Municipality.add("Silago");
                        arrayList_Municipality.add("Sogod");
                        arrayList_Municipality.add("Tomas Oppus");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Tacloban City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Tacloban City");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    case "Ormoc City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Ormoc City");
                        loadMunicipality(arrayList_Municipality);
                        break;
                    //Region 10
                    case "Cagayan De Oro City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Cagayan De Oro City");
                        loadMunicipality(arrayList_Municipality);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        init2();

        spinner_dRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i , long l) {
                int position = (int)adapterView.getItemIdAtPosition(i);
                switch (position) {
                    case 0:
                        arrayList_Province = new ArrayList<>();
                        arrayList_Province.add("City of Manila");
                        load_dProvince(arrayList_Province);
                        break;
                    case 1:
                        arrayList_Province = new ArrayList<>();
                        arrayList_Province.add("Aklan");
                        arrayList_Province.add("Antique");
                        arrayList_Province.add("Capiz");
                        arrayList_Province.add("Guimaras");
                        arrayList_Province.add("Iloilo");
                        arrayList_Province.add("Negros Occidental");
                        arrayList_Province.add("Bacolod City");
                        arrayList_Province.add("Iloilo City");
                        load_dProvince(arrayList_Province);
                        break;
                    case 2:
                        arrayList_Province = new ArrayList<>();
                        arrayList_Province.add("Bohol");
                        arrayList_Province.add("Cebu");
                        arrayList_Province.add("Negros Oriental");
                        arrayList_Province.add("Siquijor");
                        arrayList_Province.add("Cebu City");
                        arrayList_Province.add("Lapu-Lapu City");
                        arrayList_Province.add("Mandaue City");
                        load_dProvince(arrayList_Province);
                        break;
                    case 3:
                        arrayList_Province = new ArrayList<>();
                        arrayList_Province.add("Biliran");
                        arrayList_Province.add("Eastern Samar");
                        arrayList_Province.add("Leyte");
                        arrayList_Province.add("Northern Samar");
                        arrayList_Province.add("Samar");
                        arrayList_Province.add("Southern Leyte");
                        arrayList_Province.add("Tacloban City");
                        arrayList_Province.add("Ormoc City");
                        load_dProvince(arrayList_Province);
                        break;
                    case 4:
                        arrayList_Province = new ArrayList<>();
                        arrayList_Province.add("Cagayan De Oro City");
                        load_dProvince(arrayList_Province);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_dProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i , long l) {
                String item = adapterView.getSelectedItem().toString();
                switch (item) {
                    //NCR
                    case "City of Manila":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("City of Manila");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    //region 6
                    case "Aklan":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Altavas");
                        arrayList_Municipality.add("Balete");
                        arrayList_Municipality.add("Banga");
                        arrayList_Municipality.add("Batan");
                        arrayList_Municipality.add("Buruanga");
                        arrayList_Municipality.add("Ibajay");
                        arrayList_Municipality.add("Kalibo");
                        arrayList_Municipality.add("Lezo");
                        arrayList_Municipality.add("Libacao");
                        arrayList_Municipality.add("Madalag");
                        arrayList_Municipality.add("Makato");
                        arrayList_Municipality.add("Malay");
                        arrayList_Municipality.add("Malinao");
                        arrayList_Municipality.add("Nabas");
                        arrayList_Municipality.add("New Washington");
                        arrayList_Municipality.add("Numancia");
                        arrayList_Municipality.add("Tangalan");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Antique":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Anini-y");
                        arrayList_Municipality.add("Barbaza");
                        arrayList_Municipality.add("Belison");
                        arrayList_Municipality.add("Bugasong");
                        arrayList_Municipality.add("Caluya");
                        arrayList_Municipality.add("Culasi");
                        arrayList_Municipality.add("Hamtic");
                        arrayList_Municipality.add("Laua-an");
                        arrayList_Municipality.add("Libertad");
                        arrayList_Municipality.add("Pandan");
                        arrayList_Municipality.add("Patnongon");
                        arrayList_Municipality.add("San Jose de Buenavista");
                        arrayList_Municipality.add("San Remigio");
                        arrayList_Municipality.add("Sebaste");
                        arrayList_Municipality.add("Sibalom");
                        arrayList_Municipality.add("Tibiao");
                        arrayList_Municipality.add("Tobias Fornier");
                        arrayList_Municipality.add("Valderrama");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Capiz":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Cuartero");
                        arrayList_Municipality.add("Dao");
                        arrayList_Municipality.add("Dumalag");
                        arrayList_Municipality.add("Dumarao");
                        arrayList_Municipality.add("Ivisan");
                        arrayList_Municipality.add("Jamindan");
                        arrayList_Municipality.add("Ma-ayon");
                        arrayList_Municipality.add("Mambusao");
                        arrayList_Municipality.add("Panay");
                        arrayList_Municipality.add("Panitan");
                        arrayList_Municipality.add("Pilar");
                        arrayList_Municipality.add("Pontevedra");
                        arrayList_Municipality.add("President Roxas");
                        arrayList_Municipality.add("Roxas");
                        arrayList_Municipality.add("Sapian");
                        arrayList_Municipality.add("Sigma");
                        arrayList_Municipality.add("Tapaz");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Guimaras":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Buenavista");
                        arrayList_Municipality.add("Jordan");
                        arrayList_Municipality.add("Nueva Valencia");
                        arrayList_Municipality.add("San Lorenzo");
                        arrayList_Municipality.add("Sibunag");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Iloilo":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Ajuy");
                        arrayList_Municipality.add("Alimodian");
                        arrayList_Municipality.add("Anilao");
                        arrayList_Municipality.add("Badiangan");
                        arrayList_Municipality.add("Balasan");
                        arrayList_Municipality.add("Banate");
                        arrayList_Municipality.add("Barotac Nuevo");
                        arrayList_Municipality.add("Barotac Viejo");
                        arrayList_Municipality.add("Batad");
                        arrayList_Municipality.add("Bingawan");
                        arrayList_Municipality.add("Cabatuan");
                        arrayList_Municipality.add("Calinog");
                        arrayList_Municipality.add("Carles");
                        arrayList_Municipality.add("Concepcion");
                        arrayList_Municipality.add("Dingle");
                        arrayList_Municipality.add("Dueñas");
                        arrayList_Municipality.add("Dumangas");
                        arrayList_Municipality.add("Estancia");
                        arrayList_Municipality.add("Guimbal");
                        arrayList_Municipality.add("Igbaras");
                        arrayList_Municipality.add("Janiuay");
                        arrayList_Municipality.add("Lambunao");
                        arrayList_Municipality.add("Leganes");
                        arrayList_Municipality.add("Lemery");
                        arrayList_Municipality.add("Leon");
                        arrayList_Municipality.add("Maasin");
                        arrayList_Municipality.add("Miagao");
                        arrayList_Municipality.add("Mina");
                        arrayList_Municipality.add("New Lucena");
                        arrayList_Municipality.add("Oton");
                        arrayList_Municipality.add("Passi");
                        arrayList_Municipality.add("Pavia");
                        arrayList_Municipality.add("Pototan");
                        arrayList_Municipality.add("San Dionisio");
                        arrayList_Municipality.add("San Enrique");
                        arrayList_Municipality.add("San Joaquin");
                        arrayList_Municipality.add("San Miguel");
                        arrayList_Municipality.add("San Rafael");
                        arrayList_Municipality.add("Santa Barbara");
                        arrayList_Municipality.add("Sara");
                        arrayList_Municipality.add("Tigbauan");
                        arrayList_Municipality.add("Tubungan");
                        arrayList_Municipality.add("Zarraga");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Negros Occidental":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Bacolod");
                        arrayList_Municipality.add("Bago");
                        arrayList_Municipality.add("Binalbagan");
                        arrayList_Municipality.add("Cadiz");
                        arrayList_Municipality.add("Calatrava");
                        arrayList_Municipality.add("Candoni");
                        arrayList_Municipality.add("Cauayan");
                        arrayList_Municipality.add("Enrique B. Magalona");
                        arrayList_Municipality.add("Escalante");
                        arrayList_Municipality.add("Himamaylan");
                        arrayList_Municipality.add("Hinigaran");
                        arrayList_Municipality.add("Hinoba-an");
                        arrayList_Municipality.add("Ilog");
                        arrayList_Municipality.add("Isabela");
                        arrayList_Municipality.add("Kabankalan");
                        arrayList_Municipality.add("La Carlota");
                        arrayList_Municipality.add("La Castellana");
                        arrayList_Municipality.add("Manapla");
                        arrayList_Municipality.add("Moises Padilla");
                        arrayList_Municipality.add("Murcia");
                        arrayList_Municipality.add("Pontevedra");
                        arrayList_Municipality.add("Pulupandan");
                        arrayList_Municipality.add("Sagay");
                        arrayList_Municipality.add("Salvador Benedicto");
                        arrayList_Municipality.add("San Carlos");
                        arrayList_Municipality.add("San Enrique");
                        arrayList_Municipality.add("Silay");
                        arrayList_Municipality.add("Sipalay");
                        arrayList_Municipality.add("Talisay");
                        arrayList_Municipality.add("Toboso");
                        arrayList_Municipality.add("Valladolid");
                        arrayList_Municipality.add("Victorias");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Bacolod City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Bacolod City");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Iloilo City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Iloilo City");
                        load_dMunicipality(arrayList_Municipality);
                        break;

                    //region 7
                    case "Bohol":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Alburquerque");
                        arrayList_Municipality.add("Alicia");
                        arrayList_Municipality.add("Anda");
                        arrayList_Municipality.add("Antequera");
                        arrayList_Municipality.add("Baclayon");
                        arrayList_Municipality.add("Balilihan");
                        arrayList_Municipality.add("Batuan");
                        arrayList_Municipality.add("Bien Unido");
                        arrayList_Municipality.add("Bilar");
                        arrayList_Municipality.add("Buenavista");
                        arrayList_Municipality.add("Calape");
                        arrayList_Municipality.add("Candijay");
                        arrayList_Municipality.add("Carmen");
                        arrayList_Municipality.add("Catigbian");
                        arrayList_Municipality.add("Clarin");
                        arrayList_Municipality.add("Corella");
                        arrayList_Municipality.add("Cortes");
                        arrayList_Municipality.add("Dagohoy");
                        arrayList_Municipality.add("Danao");
                        arrayList_Municipality.add("Dauis");
                        arrayList_Municipality.add("Dimiao");
                        arrayList_Municipality.add("Duero");
                        arrayList_Municipality.add("Garcia Hernandez");
                        arrayList_Municipality.add("Getafe");
                        arrayList_Municipality.add("Guindulman");
                        arrayList_Municipality.add("Inabanga");
                        arrayList_Municipality.add("Jagna");
                        arrayList_Municipality.add("Lila");
                        arrayList_Municipality.add("Loay");
                        arrayList_Municipality.add("Loboc");
                        arrayList_Municipality.add("Loon");
                        arrayList_Municipality.add("Mabini");
                        arrayList_Municipality.add("Maribojoc");
                        arrayList_Municipality.add("Panglao");
                        arrayList_Municipality.add("Pilar");
                        arrayList_Municipality.add("President Carlos P. Garcia");
                        arrayList_Municipality.add("Sagbayan");
                        arrayList_Municipality.add("San Isidro");
                        arrayList_Municipality.add("San Miguel");
                        arrayList_Municipality.add("Sevilla");
                        arrayList_Municipality.add("Sierra Bullones");
                        arrayList_Municipality.add("Sikatuna");
                        arrayList_Municipality.add("Tagbilaran");
                        arrayList_Municipality.add("Talibon");
                        arrayList_Municipality.add("Trinidad");
                        arrayList_Municipality.add("Tubigon");
                        arrayList_Municipality.add("Ubay");
                        arrayList_Municipality.add("Valencia");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Cebu":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Alcantara");
                        arrayList_Municipality.add("Alcoy");
                        arrayList_Municipality.add("Alegria");
                        arrayList_Municipality.add("Aloguinsan");
                        arrayList_Municipality.add("Argao");
                        arrayList_Municipality.add("Asturias");
                        arrayList_Municipality.add("Badian");
                        arrayList_Municipality.add("Balamban");
                        arrayList_Municipality.add("Bantayan");
                        arrayList_Municipality.add("Barili");
                        arrayList_Municipality.add("Bogo");
                        arrayList_Municipality.add("Boljoon");
                        arrayList_Municipality.add("Borbon");
                        arrayList_Municipality.add("Carcar");
                        arrayList_Municipality.add("Carmen");
                        arrayList_Municipality.add("Catmon");
                        arrayList_Municipality.add("Compostela");
                        arrayList_Municipality.add("Consolacion");
                        arrayList_Municipality.add("Cordova");
                        arrayList_Municipality.add("Daanbantayan");
                        arrayList_Municipality.add("Dalaguete");
                        arrayList_Municipality.add("Danao");
                        arrayList_Municipality.add("Dumanjug");
                        arrayList_Municipality.add("Ginatilan");
                        arrayList_Municipality.add("Liloan");
                        arrayList_Municipality.add("Madridejos");
                        arrayList_Municipality.add("Malabuyoc");
                        arrayList_Municipality.add("Medellin");
                        arrayList_Municipality.add("Minglanilla");
                        arrayList_Municipality.add("Moalboal");
                        arrayList_Municipality.add("Naga");
                        arrayList_Municipality.add("Oslob");
                        arrayList_Municipality.add("Pilar");
                        arrayList_Municipality.add("Pinamungajan");
                        arrayList_Municipality.add("Poro");
                        arrayList_Municipality.add("Ronda");
                        arrayList_Municipality.add("Samboan");
                        arrayList_Municipality.add("San Fernando");
                        arrayList_Municipality.add("San Francisco");
                        arrayList_Municipality.add("San Remigio");
                        arrayList_Municipality.add("Santa Fe");
                        arrayList_Municipality.add("Santander");
                        arrayList_Municipality.add("Sibonga");
                        arrayList_Municipality.add("Sogod");
                        arrayList_Municipality.add("Tabogon");
                        arrayList_Municipality.add("Tabuelan");
                        arrayList_Municipality.add("Talisay");
                        arrayList_Municipality.add("Toledo");
                        arrayList_Municipality.add("Tuburan");
                        arrayList_Municipality.add("Tudela");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Negros Oriental":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Amlan");
                        arrayList_Municipality.add("Ayungon");
                        arrayList_Municipality.add("Bacong");
                        arrayList_Municipality.add("Bais");
                        arrayList_Municipality.add("Basay");
                        arrayList_Municipality.add("Bayawan");
                        arrayList_Municipality.add("Bindoy");
                        arrayList_Municipality.add("Canlaon");
                        arrayList_Municipality.add("Dauin");
                        arrayList_Municipality.add("Dumaguete");
                        arrayList_Municipality.add("Guihulngan");
                        arrayList_Municipality.add("Jimalalud");
                        arrayList_Municipality.add("La Libertad");
                        arrayList_Municipality.add("Mabinay");
                        arrayList_Municipality.add("Manjuyod");
                        arrayList_Municipality.add("Pamplona");
                        arrayList_Municipality.add("San Jose");
                        arrayList_Municipality.add("Santa Catalina");
                        arrayList_Municipality.add("Siaton");
                        arrayList_Municipality.add("Sibulan");
                        arrayList_Municipality.add("Tanjay");
                        arrayList_Municipality.add("Tayasan");
                        arrayList_Municipality.add("Valencia");
                        arrayList_Municipality.add("Vallehermoso");
                        arrayList_Municipality.add("Zamboanguita");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Siquijor":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Enrique Villanueva");
                        arrayList_Municipality.add("Larena");
                        arrayList_Municipality.add("Lazi");
                        arrayList_Municipality.add("Maria");
                        arrayList_Municipality.add("San Juan");
                        arrayList_Municipality.add("Siquijor");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Cebu City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Cebu City");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Lapu-Lapu City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Lapu-Lapu City");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Mandaue City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Mandaue City");
                        load_dMunicipality(arrayList_Municipality);
                        break;

                    //region 8
                    case "Biliran":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Almeria");
                        arrayList_Municipality.add("Biliran");
                        arrayList_Municipality.add("Cabucgayan");
                        arrayList_Municipality.add("Caibiran");
                        arrayList_Municipality.add("Culaba");
                        arrayList_Municipality.add("Kawayan");
                        arrayList_Municipality.add("Maripipi");
                        arrayList_Municipality.add("Naval");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Eastern Samar":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Arteche");
                        arrayList_Municipality.add("Balangiga");
                        arrayList_Municipality.add("Balangkayan");
                        arrayList_Municipality.add("Borongan");
                        arrayList_Municipality.add("Can-avid");
                        arrayList_Municipality.add("Dolores");
                        arrayList_Municipality.add("General MacArthur");
                        arrayList_Municipality.add("Giporlos");
                        arrayList_Municipality.add("Guiuan");
                        arrayList_Municipality.add("Hernani");
                        arrayList_Municipality.add("Jipapad");
                        arrayList_Municipality.add("Lawaan");
                        arrayList_Municipality.add("Llorente");
                        arrayList_Municipality.add("Maslog");
                        arrayList_Municipality.add("Maydolong");
                        arrayList_Municipality.add("Mercedes");
                        arrayList_Municipality.add("Oras");
                        arrayList_Municipality.add("Quinapondan");
                        arrayList_Municipality.add("Salcedo");
                        arrayList_Municipality.add("San Julian");
                        arrayList_Municipality.add("San Policarpo");
                        arrayList_Municipality.add("Sulat");
                        arrayList_Municipality.add("Taft");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Leyte":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Abuyog");
                        arrayList_Municipality.add("Alangalang");
                        arrayList_Municipality.add("Albuera");
                        arrayList_Municipality.add("Babatngon");
                        arrayList_Municipality.add("Barugo");
                        arrayList_Municipality.add("Bato");
                        arrayList_Municipality.add("Baybay");
                        arrayList_Municipality.add("Burauen");
                        arrayList_Municipality.add("Calubian");
                        arrayList_Municipality.add("Capoocan");
                        arrayList_Municipality.add("Carigara");
                        arrayList_Municipality.add("Dagami");
                        arrayList_Municipality.add("Dulag");
                        arrayList_Municipality.add("Hilongos");
                        arrayList_Municipality.add("Hindang");
                        arrayList_Municipality.add("Inopacan");
                        arrayList_Municipality.add("Isabel");
                        arrayList_Municipality.add("Jaro");
                        arrayList_Municipality.add("Javier");
                        arrayList_Municipality.add("Julita");
                        arrayList_Municipality.add("Kananga");
                        arrayList_Municipality.add("La Paz");
                        arrayList_Municipality.add("Leyte");
                        arrayList_Municipality.add("MacArthur");
                        arrayList_Municipality.add("Mahaplag");
                        arrayList_Municipality.add("Matag-ob");
                        arrayList_Municipality.add("Matalom");
                        arrayList_Municipality.add("Mayorga");
                        arrayList_Municipality.add("Merida");
                        arrayList_Municipality.add("Palo");
                        arrayList_Municipality.add("Palompon");
                        arrayList_Municipality.add("Pastrana");
                        arrayList_Municipality.add("San Isidro");
                        arrayList_Municipality.add("San Miguel");
                        arrayList_Municipality.add("Santa Fe");
                        arrayList_Municipality.add("Tabango");
                        arrayList_Municipality.add("Tabontabon");
                        arrayList_Municipality.add("Tanauan");
                        arrayList_Municipality.add("Tolosa");
                        arrayList_Municipality.add("Tunga");
                        arrayList_Municipality.add("Villaba");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Northern Samar":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Allen");
                        arrayList_Municipality.add("Biri");
                        arrayList_Municipality.add("Bobon");
                        arrayList_Municipality.add("Capul");
                        arrayList_Municipality.add("Catarman");
                        arrayList_Municipality.add("Catubig");
                        arrayList_Municipality.add("Gamay");
                        arrayList_Municipality.add("Laoang");
                        arrayList_Municipality.add("Lapinig");
                        arrayList_Municipality.add("Las Navas");
                        arrayList_Municipality.add("Lavezares");
                        arrayList_Municipality.add("Lope de Vega");
                        arrayList_Municipality.add("Mapanas");
                        arrayList_Municipality.add("Mondragon");
                        arrayList_Municipality.add("Palapag");
                        arrayList_Municipality.add("Pambujan");
                        arrayList_Municipality.add("Rosario");
                        arrayList_Municipality.add("San Antonio");
                        arrayList_Municipality.add("San Isidro");
                        arrayList_Municipality.add("San Jose");
                        arrayList_Municipality.add("San Roque");
                        arrayList_Municipality.add("San Vicente");
                        arrayList_Municipality.add("Silvino Lobos");
                        arrayList_Municipality.add("Victoria");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Samar":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Almagro");
                        arrayList_Municipality.add("Basey");
                        arrayList_Municipality.add("Calbayog");
                        arrayList_Municipality.add("Calbiga");
                        arrayList_Municipality.add("Catbalogan");
                        arrayList_Municipality.add("Daram");
                        arrayList_Municipality.add("Gandara");
                        arrayList_Municipality.add("Hinabangan");
                        arrayList_Municipality.add("Jiabong");
                        arrayList_Municipality.add("Marabut");
                        arrayList_Municipality.add("Matuguinao");
                        arrayList_Municipality.add("Motiong");
                        arrayList_Municipality.add("Pagsanghan");
                        arrayList_Municipality.add("Paranas");
                        arrayList_Municipality.add("Pinabacdao");
                        arrayList_Municipality.add("San Jorge");
                        arrayList_Municipality.add("San Jose de Buan");
                        arrayList_Municipality.add("San Sebastian");
                        arrayList_Municipality.add("Santa Margarita");
                        arrayList_Municipality.add("Santa Rita");
                        arrayList_Municipality.add("Santo Niño");
                        arrayList_Municipality.add("Tagapul-an");
                        arrayList_Municipality.add("Talalora");
                        arrayList_Municipality.add("Tarangnan");
                        arrayList_Municipality.add("Villareal");
                        arrayList_Municipality.add("Zumarraga");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Southern Leyte":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Anahawan");
                        arrayList_Municipality.add("Bontoc");
                        arrayList_Municipality.add("Hinunangan");
                        arrayList_Municipality.add("Hinundayan");
                        arrayList_Municipality.add("Libagon");
                        arrayList_Municipality.add("Liloan");
                        arrayList_Municipality.add("Limasawa");
                        arrayList_Municipality.add("Maasin");
                        arrayList_Municipality.add("Macrohon");
                        arrayList_Municipality.add("Malitbog");
                        arrayList_Municipality.add("Padre Burgos");
                        arrayList_Municipality.add("Pintuyan");
                        arrayList_Municipality.add("Saint Bernard");
                        arrayList_Municipality.add("San Francisco");
                        arrayList_Municipality.add("San Juan");
                        arrayList_Municipality.add("San Ricardo");
                        arrayList_Municipality.add("Silago");
                        arrayList_Municipality.add("Sogod");
                        arrayList_Municipality.add("Tomas Oppus");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Tacloban City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Tacloban City");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    case "Ormoc City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Ormoc City");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                    //Region 10
                    case "Cagayan De Oro City":
                        arrayList_Municipality = new ArrayList<>();
                        arrayList_Municipality.add("Cagayan De Oro City");
                        load_dMunicipality(arrayList_Municipality);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
                String g1 = male.getText().toString();
                String g2 = female.getText().toString();
                String g3 = rather.getText().toString();

                //text
                value.setFirstname(firstnameText.getText().toString().trim());
                value.setMiddleinitial(middleInitialText.getText().toString().trim());
                value.setLastname(lastnameText.getText().toString().trim());
                value.setSuffixname(suffixnameText.getText().toString().trim());
                value.setEmail(emailText.getText().toString().trim());
                value.setContactNumber(contactNumberText.getText().toString().trim());
                value.setEmergencyContactPerson(emergencyContactPersonText.getText().toString().trim());
                value.setEmergencyContactNumber(emergencyContactNumberText.getText().toString().trim());
                value.setcAddress(cAddressText.getText().toString().trim());
                value.setdAddress(dAddressText.getText().toString().trim());
                value.setDeparture(departureText.getText().toString().trim());
                value.setArrival(arrivalText.getText().toString().trim());
                //spinner
                value.setTravellerType(spinner_travellerType.getText().toString());
                value.setTitle(spinner_title.getText().toString());

                value.setcRegion(spinner_cRegion.getSelectedItem().toString());
                value.setcProvince(spinner_cProvince.getSelectedItem().toString());
                value.setcMunicipality(spinner_cMunicipality.getSelectedItem().toString());
                value.setdRegion(spinner_dRegion.getSelectedItem().toString());
                value.setdProvince(spinner_dProvince.getSelectedItem().toString());
                value.setdMunicipality(spinner_dMunicipality.getSelectedItem().toString());

                if(male.isChecked()){
                    value.setGender(g1);
                }else if (female.isChecked()){
                    value.setGender(g2);
                }else{
                    value.setGender((g3));
                }

                String firstname = firstnameText.getText().toString().trim();
                String lastname = lastnameText.getText().toString().trim();
                String email = emailText.getText().toString().trim();
                String contactNumber = contactNumberText.getText().toString().trim();
                String emergencyContactPerson = emergencyContactPersonText.getText().toString().trim();
                String emergencyContactNumber = emergencyContactNumberText.getText().toString().trim();
                String cAddress = cAddressText.getText().toString().trim();
                String dAddress = dAddressText.getText().toString().trim();
                String departure = departureText.getText().toString().trim();
                String arrival = arrivalText.getText().toString().trim();

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
                if (contactNumber.isEmpty()) {
                    contactNumberText.setError("Contact Number is required!");
                    contactNumberText.requestFocus();
                    return;
                }
                if (emergencyContactPerson.isEmpty()) {
                    emergencyContactPersonText.setError("Emergency Contact Person is required!");
                   emergencyContactPersonText.requestFocus();
                    return;
                }
                if (emergencyContactNumber.isEmpty()) {
                    emergencyContactNumberText.setError("Emergency Contact Number is required!");
                    emergencyContactNumberText.requestFocus();
                    return;
                }
                if (cAddress.isEmpty()) {
                    cAddressText.setError("Current Address is required!");
                    cAddressText.requestFocus();
                    return;
                }
                if (dAddress.isEmpty()) {
                    dAddressText.setError("Destination Address is required!");
                    dAddressText.requestFocus();
                    return;
                }
                if (departure.isEmpty()) {
                    departureText.setError("Departure Date is required!");
                    departureText.requestFocus();
                    return;
                }
                if (arrival.isEmpty()) {
                    arrivalText.setError("Arrival Date is required!");
                    arrivalText.requestFocus();
                    return;
                }

                progressDialog.setMessage("Submitting...Please Wait");
                progressDialog.show();

                String vax = "vaccinated";
                value.setVaccineStatus(vax);

                reference.child(String.valueOf(fAuth.getCurrentUser().getUid())).setValue(value);

                String stat = "Please upload required requirements (vaccinated)";
                value.setStatus(stat);

                String travType = spinner_travellerType.getText().toString();
                String orig = cAddressText.getText().toString().trim() + ", " +
                        spinner_cMunicipality.getSelectedItem().toString() + ", " +
                        spinner_cProvince.getSelectedItem().toString();
                String des = dAddressText.getText().toString().trim() + ", " +
                        spinner_dMunicipality.getSelectedItem().toString() + ", " +
                        spinner_dProvince.getSelectedItem().toString();
                String travDate = departureText.getText().toString();
                String arrivDate = arrivalText.getText().toString();

                updateStatus(stat, travType, orig, des, travDate, arrivDate, email);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        dialog.setContentView(R.layout.travelform_success_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        Button ok = dialog.findViewById(R.id.okButton);
                        dialog.show();

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                startActivity(new Intent(TravelForm_Fullyvacc.this, TravelForm_Submit_FullyVacc.class));
                            }
                        });
                    }
                }, 3000);
            }
        });

    }

    public void init() {
        arrayList_Region = new ArrayList<>();
        arrayList_Region.add("National Capital Region (NCR)");
        arrayList_Region.add("Western Visayas (Region VI)");
        arrayList_Region.add("Central Visayas (Region VII)");
        arrayList_Region.add("Eastern Visayas (Region VIII)");
        arrayList_Region.add("Northern Mindanao (Region X)");
        loadRegion(arrayList_Region);
    }
    public void loadRegion(ArrayList<String> arrayList_Region) {
        arrayAdapter_Region = new ArrayAdapter<>(this, R.layout.textview_gray, arrayList_Region);
        arrayAdapter_Region.setDropDownViewResource(R.layout.textview_gray);
        spinner_cRegion.setAdapter(arrayAdapter_Region);
    }
    public void loadProvince(ArrayList<String> arrayList_Province) {
        arrayAdapter_Province = new ArrayAdapter<>(this, R.layout.textview_gray, arrayList_Province);
        arrayAdapter_Province.setDropDownViewResource(R.layout.textview_gray);
        spinner_cProvince.setAdapter(arrayAdapter_Province);
    }
    public void loadMunicipality(ArrayList<String> arrayList_Municipality) {
        arrayAdapter_Municipality = new ArrayAdapter<>(this, R.layout.textview_gray, arrayList_Municipality);
        arrayAdapter_Municipality.setDropDownViewResource(R.layout.textview_gray);
        spinner_cMunicipality.setAdapter(arrayAdapter_Municipality);
    }

    public void init2() {
        arrayList_Region = new ArrayList<>();
        arrayList_Region.add("National Capital Region (NCR)");
        arrayList_Region.add("Western Visayas (Region VI)");
        arrayList_Region.add("Central Visayas (Region VII)");
        arrayList_Region.add("Eastern Visayas (Region VIII)");
        arrayList_Region.add("Northern Mindanao (Region X)");
        load_dRegion(arrayList_Region);
    }
    public void load_dRegion(ArrayList<String> arrayList_Region) {
        arrayAdapter_Region = new ArrayAdapter<>(this, R.layout.textview_gray, arrayList_Region);
        arrayAdapter_Region.setDropDownViewResource(R.layout.textview_gray);
        spinner_dRegion.setAdapter(arrayAdapter_Region);
    }
    public void load_dProvince(ArrayList<String> arrayList_Province) {
        arrayAdapter_Province = new ArrayAdapter<>(this, R.layout.textview_gray, arrayList_Province);
        arrayAdapter_Province.setDropDownViewResource(R.layout.textview_gray);
        spinner_dProvince.setAdapter(arrayAdapter_Province);
    }
    public void load_dMunicipality(ArrayList<String> arrayList_Municipality) {
        arrayAdapter_Municipality = new ArrayAdapter<>(this, R.layout.textview_gray, arrayList_Municipality);
        arrayAdapter_Municipality.setDropDownViewResource(R.layout.textview_gray);
        spinner_dMunicipality.setAdapter(arrayAdapter_Municipality);
    }

    private void updateStatus(String stat, String travType, String orig, String des, String travDate, String arrivDate, String email) {
        HashMap user = new HashMap();
        user.put("status", stat);
        user.put("travellerType", travType);
        user.put("origin", orig);
        user.put("destination", des);
        user.put("departure", travDate);
        user.put("arrival", arrivDate);
        user.put("email", email);

        ref2.child(fAuth.getCurrentUser().getUid()).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(TravelForm_Fullyvacc.this, "Success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(TravelForm_Fullyvacc.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}