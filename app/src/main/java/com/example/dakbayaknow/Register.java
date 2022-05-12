package com.example.dakbayaknow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private TextView loginNowButton, genderText, clientTypeText;
    private Button registerButton;
    private TextInputEditText firstnameText, lastnameText, ageText, emailText, passwordText, confirmPasswordText, phoneText, regionText,
            provinceText, municipalityText, addressText;

    private android.widget.Spinner spinner_province, spinner_region, spinner_municipality;

    ArrayList<String> arrayList_Region, arrayList_Province, arrayList_Municipality;
    ArrayAdapter<String> arrayAdapter_Region, arrayAdapter_Province, arrayAdapter_Municipality;

    TextView gender, clientType;
    private android.widget.Spinner spinner, spinner2;
    FirebaseDatabase database;
    DatabaseReference reference;
    User value;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        loginNowButton = findViewById(R.id.loginNowButton);
        loginNowButton.setOnClickListener(this);

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        firstnameText = findViewById(R.id.firstname);
        lastnameText = findViewById(R.id.lastname);
        ageText = findViewById(R.id.age);
        emailText = findViewById(R.id.emailAddress);
        passwordText = findViewById(R.id.password);
        confirmPasswordText = findViewById(R.id.confirmPassword);
        phoneText = findViewById(R.id.phone);
        regionText = findViewById(R.id.region);
        provinceText = findViewById(R.id.province);
        municipalityText = findViewById(R.id.municipality);
        addressText = findViewById(R.id.address);

        //spinner
        clientTypeText = findViewById(R.id.clientType);
        genderText = findViewById(R.id.gender);
        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);

        spinner_region = findViewById(R.id.spinner_region);
        spinner_province= findViewById(R.id.spinner_province);
        spinner_municipality = findViewById(R.id.spinner_municipality);

        dialog = new Dialog(this);

        List<String> Categories = new ArrayList<>();
        Categories.add(0,"Gender");
        Categories.add("Male");
        Categories.add("Female");
        Categories.add("Rather not say");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).equals("Gender")){

                }else{
                    genderText.setText(adapterView.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        List<String> Categories2 = new ArrayList<>();
        Categories2.add(0,"Client Type");
        Categories2.add("Student");
        Categories2.add("Local Tourist");
        Categories2.add("Filipino Local Workers");
        Categories2.add("Stranded Individual");
        Categories2.add("Others please specify");

        ArrayAdapter<String> dataAdapter2;
        dataAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Categories2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(dataAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).equals("Client Type")){

                }else{
                    clientTypeText.setText(adapterView.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        init();

        spinner_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i , long l) {
                int position = (int)adapterView.getItemIdAtPosition(i);
                switch (position) {
                    case 0:
                        arrayList_Province = new ArrayList<>();
                        arrayList_Province.add("Aklan");
                        arrayList_Province.add("Antique");
                        arrayList_Province.add("Capiz");
                        arrayList_Province.add("Guimaras");
                        arrayList_Province.add("Iloilo");
                        arrayList_Province.add("Negros Occidental");
                        loadProvince(arrayList_Province);
                        break;
                    case 1:
                        arrayList_Province = new ArrayList<>();
                        arrayList_Province.add("Bohol");
                        arrayList_Province.add("Cebu");
                        arrayList_Province.add("Negros Oriental");
                        arrayList_Province.add("Siquijor");
                        loadProvince(arrayList_Province);
                        break;
                    case 2:
                        arrayList_Province = new ArrayList<>();
                        arrayList_Province.add("Biliran");
                        arrayList_Province.add("Eastern Samar");
                        arrayList_Province.add("Leyte");
                        arrayList_Province.add("Northern Samar");
                        arrayList_Province.add("Samar");
                        arrayList_Province.add("Southern Leyte");
                        loadProvince(arrayList_Province);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i , long l) {
                String item = adapterView.getSelectedItem().toString();
                switch (item) {
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
                        arrayList_Municipality.add("Iloilo City");
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
                        arrayList_Municipality.add("Cebu City");
                        arrayList_Municipality.add("Compostela");
                        arrayList_Municipality.add("Consolacion");
                        arrayList_Municipality.add("Cordova");
                        arrayList_Municipality.add("Daanbantayan");
                        arrayList_Municipality.add("Dalaguete");
                        arrayList_Municipality.add("Danao");
                        arrayList_Municipality.add("Dumanjug");
                        arrayList_Municipality.add("Ginatilan");
                        arrayList_Municipality.add("Lapu-Lapu");
                        arrayList_Municipality.add("Liloan");
                        arrayList_Municipality.add("Madridejos");
                        arrayList_Municipality.add("Malabuyoc");
                        arrayList_Municipality.add("Mandaue");
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
                        arrayList_Municipality.add("Ormoc");
                        arrayList_Municipality.add("Palo");
                        arrayList_Municipality.add("Palompon");
                        arrayList_Municipality.add("Pastrana");
                        arrayList_Municipality.add("San Isidro");
                        arrayList_Municipality.add("San Miguel");
                        arrayList_Municipality.add("Santa Fe");
                        arrayList_Municipality.add("Tabango");
                        arrayList_Municipality.add("Tabontabon");
                        arrayList_Municipality.add("Tacloban");
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginNowButton:
                finish();
                break;

            case R.id.registerButton:
                submit();
                break;
        }
    }

    private void submit() {
        String firstname = firstnameText.getText().toString().trim();
        String lastname = lastnameText.getText().toString().trim();
        String gender = genderText.getText().toString().trim();
        String age = ageText.getText().toString().trim();
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String confirmPassword = confirmPasswordText.getText().toString().trim();
        String phone = phoneText.getText().toString().trim();
        String region = spinner_region.getSelectedItem().toString();
        String province = spinner_province.getSelectedItem().toString();
        String municipality = spinner_municipality.getSelectedItem().toString();
        String address = addressText.getText().toString().trim();
        String clientType = clientTypeText.getText().toString().trim();

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
        if (gender.isEmpty()) {
            genderText.setError("Gender is required!");
            genderText.requestFocus();
            return;
        }
        if (age.isEmpty()) {
            ageText.setError("Age is required!");
            ageText.requestFocus();
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
        if (password.isEmpty()) {
            passwordText.setError("Password is required!");
            passwordText.requestFocus();
            return;
        }
        if (password.length() < 6) {
            passwordText.setError("Min password length should be 6 characters!");
            passwordText.requestFocus();
            return;
        }
        if (confirmPassword.isEmpty()) {
            confirmPasswordText.setError("Confirm your password!");
            confirmPasswordText.requestFocus();
            return;
        }
        if (!confirmPassword.matches(password)) {
            confirmPasswordText.setError("Password doesn't match!");
            confirmPasswordText.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            phoneText.setError("Phone Number is required!");
            phoneText.requestFocus();
            return;
        }
        if (region.isEmpty()) {
            regionText.setError("Region is required!");
            regionText.requestFocus();
            return;
        }
        if (province.isEmpty()) {
            provinceText.setError("Province is required!");
            provinceText.requestFocus();
            return;
        }
        if (municipality.isEmpty()) {
            municipalityText.setError("Municipality is required!");
            municipalityText.requestFocus();
            return;
        }
        if (address.isEmpty()) {
            addressText.setError("Address is required!");
            addressText.requestFocus();
            return;
        }
        if (clientType.isEmpty()) {
            clientTypeText.setError("Client Type is required!");
            clientTypeText.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(firstname, lastname, gender, age, email, phone, region, province, municipality, address, clientType);

                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();

                                        dialog.setContentView(R.layout.terms_conditions_dialog);
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                        Button agree = dialog.findViewById(R.id.agreeButton);
                                        dialog.show();

                                        agree.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(Register.this, "Failed to register! Try Again!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            try
                            {
                                throw task.getException();
                            }
                            catch (FirebaseAuthUserCollisionException existEmail)
                            {
                                Log.d("TAG", "onComplete: exist_email");
                                emailText.setError("Email already exists");
                                emailText.requestFocus();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(Register.this, "Failed to register! Try Again!", Toast.LENGTH_LONG).show();
                                Log.d("TAG", "onComplete: " + e.getMessage());
                            }
                        }
                    }
                });
    }

    public void init() {
        arrayList_Region = new ArrayList<>();
        arrayList_Region.add("Western Visayas (Region VI)");
        arrayList_Region.add("Central Visayas (Region VII)");
        arrayList_Region.add("Eastern Visayas (Region VIII)");
        loadRegion(arrayList_Region);
    }
    public void loadRegion(ArrayList<String> arrayList_Region) {
        arrayAdapter_Region = new ArrayAdapter<>(this, R.layout.textview_gray, arrayList_Region);
        arrayAdapter_Region.setDropDownViewResource(R.layout.textview_gray);
        spinner_region.setAdapter(arrayAdapter_Region);
    }
    public void loadProvince(ArrayList<String> arrayList_Province) {
        arrayAdapter_Province = new ArrayAdapter<>(this, R.layout.textview_gray, arrayList_Province);
        arrayAdapter_Province.setDropDownViewResource(R.layout.textview_gray);
        spinner_province.setAdapter(arrayAdapter_Province);
    }
    public void loadMunicipality(ArrayList<String> arrayList_Municipality) {
        arrayAdapter_Municipality = new ArrayAdapter<>(this, R.layout.textview_gray, arrayList_Municipality);
        arrayAdapter_Municipality.setDropDownViewResource(R.layout.textview_gray);
        spinner_municipality.setAdapter(arrayAdapter_Municipality);
    }
}