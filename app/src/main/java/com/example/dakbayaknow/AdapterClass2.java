package com.example.dakbayaknow;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterClass2 extends RecyclerView.Adapter<AdapterClass2.myViewHolder> {
    ArrayList<Restrictions> list;
    Dialog dialog;
    Context context;

    public AdapterClass2(ArrayList<Restrictions> list, Context context){
        this.list = list;
        this.context = context;
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

            dialog = new Dialog(itemView.getContext());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(al.getText().toString().contains("1")){
                        dialog.setContentView(R.layout.alert1_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        ImageButton close = dialog.findViewById(R.id.closeButton);
                        TextView c = dialog.findViewById(R.id.cityalert);

                        c.setText(ct.getText());
                        dialog.show();

                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                    if(al.getText().toString().contains("2")){
                        dialog.setContentView(R.layout.alert2_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        ImageButton close = dialog.findViewById(R.id.closeButton);
                        TextView c = dialog.findViewById(R.id.cityalert);

                        c.setText(ct.getText());
                        dialog.show();

                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                    if(al.getText().toString().contains("3")){
                        dialog.setContentView(R.layout.alert3_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        ImageButton close = dialog.findViewById(R.id.closeButton);
                        TextView c = dialog.findViewById(R.id.cityalert);

                        c.setText(ct.getText());
                        dialog.show();

                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                    if(al.getText().toString().contains("4")){
                        dialog.setContentView(R.layout.alert4_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        ImageButton close = dialog.findViewById(R.id.closeButton);
                        TextView c = dialog.findViewById(R.id.cityalert);

                        c.setText(ct.getText());
                        dialog.show();

                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                    if(al.getText().toString().contains("5")){
                        dialog.setContentView(R.layout.alert5_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        ImageButton close = dialog.findViewById(R.id.closeButton);
                        TextView c = dialog.findViewById(R.id.cityalert);

                        c.setText(ct.getText());
                        dialog.show();

                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                }
            });
        }
    }
}
