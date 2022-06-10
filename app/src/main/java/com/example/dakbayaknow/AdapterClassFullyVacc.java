package com.example.dakbayaknow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterClassFullyVacc extends FirebaseRecyclerAdapter<Requirements, AdapterClassFullyVacc.myViewHolder> {

    public AdapterClassFullyVacc(@NonNull FirebaseRecyclerOptions<Requirements> options) {
        super(options);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder_fullyvacc, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Requirements model) {
        holder.Req1.setText(model.getfVaccReq1());
        holder.Req2.setText(model.getfVaccReq2());
        holder.Req3.setText(model.getfVaccReq3());
        holder.Req4.setText(model.getfVaccReq4());
        holder.Req5.setText(model.getfVaccReq5());

        if(holder.Req1.getText().equals("")) {
            holder.Req1.setVisibility(View.GONE);
        } else {
            holder.Req1.setVisibility(View.VISIBLE);
        }
        if(holder.Req2.getText().equals("")) {
            holder.Req2.setVisibility(View.GONE);
        } else {
            holder.Req2.setVisibility(View.VISIBLE);
        }
        if(holder.Req3.getText().equals("")) {
            holder.Req3.setVisibility(View.GONE);
        } else {
            holder.Req3.setVisibility(View.VISIBLE);
        }
        if(holder.Req4.getText().equals("")) {
            holder.Req4.setVisibility(View.GONE);
        } else {
            holder.Req4.setVisibility(View.VISIBLE);
        }
        if(holder.Req5.getText().equals("")) {
            holder.Req5.setVisibility(View.GONE);
        } else {
            holder.Req5.setVisibility(View.VISIBLE);
        }
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView Req1, Req2, Req3, Req4, Req5;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            //textview
            Req1 = itemView.findViewById(R.id.req1);
            Req2 = itemView.findViewById(R.id.req2);
            Req3 = itemView.findViewById(R.id.req3);
            Req4 = itemView.findViewById(R.id.req4);
            Req5 = itemView.findViewById(R.id.req5);
        }
    }
}


