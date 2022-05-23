package com.example.dakbayaknow;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterClassApplications extends FirebaseRecyclerAdapter<Applications, AdapterClassApplications.myViewHolder> {

    public AdapterClassApplications(@NonNull FirebaseRecyclerOptions<Applications> options) {
        super(options);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder_myapp, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Applications model) {
        holder.des.setText(model.getDestination());
        holder.stat.setText(model.getStatus());
        holder.heal.setText(model.getHealth());

        if (holder.heal.getText().toString().contains("Good Condition")) {
            holder.heal.setTextColor(Color.parseColor("#008000"));
        } else if (holder.heal.getText().toString().contains("Stay at Home")) {
            holder.heal.setTextColor(Color.parseColor("#FF0000"));
        } else if (holder.heal.getText().toString().contains("")) {
            holder.heal.setText("Fill up HDF");
        }

        if (holder.stat.getText().toString().contains("Please upload required requirements (vaccinated)")) {
            holder.stat.setTextColor(Color.parseColor("#008000"));
        } else if (holder.stat.getText().toString().contains("Please upload required requirements (unvaccinated)")) {
            holder.stat.setTextColor(Color.parseColor("#FFA500"));
        } else if (holder.stat.getText().toString().contains("Fill up HDF")) {
            holder.stat.setTextColor(Color.parseColor("#FFFF00"));
        } else if (holder.stat.getText().toString().contains("Pending")) {
            holder.stat.setTextColor(Color.parseColor("#FFFF00"));
        } else if (holder.stat.getText().toString().contains("Approved")) {
            holder.stat.setTextColor(Color.parseColor("#008000"));
        } else if (holder.stat.getText().toString().contains("Declined")) {
            holder.stat.setTextColor(Color.parseColor("#FF0000"));
        } else if (holder.stat.getText().toString().contains("")) {
            holder.stat.setText("No Status");
        }

        holder.permit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.stat.getText().toString().contains("No Status")) {
                    Toast.makeText(holder.permit.getContext(), "No Status Yet", Toast.LENGTH_SHORT).show();
                }
                if (holder.stat.getText().toString().contains("Please upload required requirements (vaccinated)")) {
                    Toast.makeText(holder.permit.getContext(), "Please upload required requirements (vaccinated). Click the Status to proceed to Upload Docs Form.", Toast.LENGTH_SHORT).show();
                }
                if (holder.stat.getText().toString().contains("Please upload required requirements (unvaccinated)")) {
                    Toast.makeText(holder.permit.getContext(), "Please upload required requirements (unvaccinated). Click the Status to proceed to Upload Docs Form.", Toast.LENGTH_SHORT).show();
                }
                if (holder.stat.getText().toString().contains("Fill up HDF")) {
                    Toast.makeText(holder.permit.getContext(), "Please fill up Health Declaration Form.", Toast.LENGTH_SHORT).show();
                }
                if (holder.stat.getText().toString().contains("Pending")) {
                    Toast.makeText(holder.permit.getContext(), "Your application is on process. Please wait for 3-5 days", Toast.LENGTH_SHORT).show();
                }
                if (holder.stat.getText().toString().contains("Approved")) {
                    Intent intent = new Intent(holder.permit.getContext(), TravelPermit.class);
                    holder.permit.getContext().startActivity(intent);
                }
                if (holder.stat.getText().toString().contains("Declined")) {
                    Toast.makeText(holder.permit.getContext(), "Sorry your application is declined", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.stat.getText().toString().contains("Please upload required requirements (vaccinated)")) {
                    Intent intent = new Intent(holder.permit.getContext(), UploadDocxFullyVacc.class);
                    holder.permit.getContext().startActivity(intent);
                }
                if (holder.stat.getText().toString().contains("Please upload required requirements (unvaccinated)")) {
                    Intent intent = new Intent(holder.permit.getContext(), UploadDocxUnvacc.class);
                    holder.permit.getContext().startActivity(intent);
                }
                if (holder.stat.getText().toString().contains("Fill up HDF")) {
                    Intent intent = new Intent(holder.permit.getContext(), HealthDeclarationForm.class);
                    holder.permit.getContext().startActivity(intent);
                }
            }
        });
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView des, stat, heal;
        ImageButton permit;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            //textview
            des = itemView.findViewById(R.id.destination);
            stat = itemView.findViewById(R.id.status);
            heal = itemView.findViewById(R.id.health);
            //button
            permit = itemView.findViewById(R.id.travelPermit);
        }
    }
}


