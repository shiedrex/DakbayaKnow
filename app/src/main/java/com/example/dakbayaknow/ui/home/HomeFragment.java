package com.example.dakbayaknow.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.dakbayaknow.CheckRestrictions;
import com.example.dakbayaknow.HealthDeclarationForm;
import com.example.dakbayaknow.LGUContacts;
import com.example.dakbayaknow.MyApplications;
import com.example.dakbayaknow.R;
import com.example.dakbayaknow.TravelPermit;
import com.example.dakbayaknow.TravelRequirements;
import com.example.dakbayaknow.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    Activity context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;

    }

    public void onStart() {
        super.onStart();

        ImageButton myApplicationsButton = (ImageButton) context.findViewById(R.id.myApplicationsButton);
        myApplicationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyApplications.class);
                startActivity(intent);
            }
        });
        ImageButton HDFButton = (ImageButton) context.findViewById(R.id.HDFButton);
        HDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HealthDeclarationForm.class);
                startActivity(intent);
            }
        });
        ImageButton travelPermitButton = (ImageButton) context.findViewById(R.id.travelPermitButton);
        travelPermitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TravelPermit.class);
                startActivity(intent);
            }
        });
        ImageButton usersFeedbackButton = (ImageButton) context.findViewById(R.id.usersFeedbackButton);
        usersFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps"));
                startActivity(browserIntent);
            }
        });
        ImageButton travelRequirementsButton = (ImageButton) context.findViewById(R.id.travelRequirementsButton);
        travelRequirementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TravelRequirements.class);
                startActivity(intent);
            }
        });
        ImageButton restrictionsButton = (ImageButton) context.findViewById(R.id.restrictionsButton);
        restrictionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CheckRestrictions.class);
                startActivity(intent);
            }
        });
        ImageButton LGUButton = (ImageButton) context.findViewById(R.id.LGUButton);
        LGUButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LGUContacts.class);
                startActivity(intent);
            }
        });
    }

}