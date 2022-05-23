package com.example.dakbayaknow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterClassHDFHistory extends FirebaseRecyclerAdapter<Applications, AdapterClassHDFHistory.myViewHolder> {

    public AdapterClassHDFHistory(@NonNull FirebaseRecyclerOptions<Applications> options) {
        super(options);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder_hdfhistory, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Applications model) {
        holder.date.setText(model.getDateToday());
        holder.des.setText(model.getDestination());
        holder.heal.setText(model.getHealth());

        if (holder.heal.getText().toString().contains("Good Condition")) {
            holder.heal.setTextColor(Color.parseColor("#008000"));
        } else if (holder.heal.getText().toString().contains("Stay at Home")) {
            holder.heal.setTextColor(Color.parseColor("#FF0000"));
        }

        holder.heal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.heal.getText().toString().contains("Fill up HDF")) {
                    Intent intent = new Intent(holder.heal.getContext(), HealthDeclarationForm.class);
                    holder.heal.getContext().startActivity(intent);
                }
            }
        });
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView des, heal, date;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            //textview
            des = itemView.findViewById(R.id.destination);
            heal = itemView.findViewById(R.id.health);
            date = itemView.findViewById(R.id.datetoday);
        }
    }
}


