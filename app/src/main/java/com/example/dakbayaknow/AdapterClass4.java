package com.example.dakbayaknow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterClass4 extends RecyclerView.Adapter<AdapterClass4.myViewHolder> {
    ArrayList<Feedback> list;

    public AdapterClass4(ArrayList<Feedback> list){
        this.list = list;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder4,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.rate.setText(list.get(position).getRating());
        holder.user.setText(list.get(position).getUsername());
        holder.comm.setText(list.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView rate, user;
        RatingBar star;
        EditText comm;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            rate = itemView.findViewById(R.id.rating);
            user = itemView.findViewById(R.id.username);
            comm = itemView.findViewById(R.id.comment);
        }
    }
}
