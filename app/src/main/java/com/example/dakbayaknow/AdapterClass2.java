package com.example.dakbayaknow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterClass2 extends RecyclerView.Adapter<AdapterClass2.myViewHolder> {
    ArrayList<Restrictions> list;

    public AdapterClass2(ArrayList<Restrictions> list){
        this.list = list;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder2,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.ct.setText(list.get(position).getCity());
        holder.al.setText(list.get(position).getAlert());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView ct, al;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            ct = itemView.findViewById(R.id.city);
            al = itemView.findViewById(R.id.alert);
        }
    }
}
