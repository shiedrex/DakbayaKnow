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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterClass3 extends RecyclerView.Adapter<AdapterClass3.myViewHolder> {
    ArrayList<Requirements> list;
    Dialog dialog;
    Context context;

    public AdapterClass3(ArrayList<Requirements> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder3, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.ct.setText(list.get(position).getCity());
        holder.po.setText(list.get(position).getPolicy());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView ct, po;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            ct = itemView.findViewById(R.id.city);
            po = itemView.findViewById(R.id.policy);

            dialog = new Dialog(itemView.getContext());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.setContentView(R.layout.requirements_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    TextView lgu = dialog.findViewById(R.id.lgu);
                    Button full = dialog.findViewById(R.id.fullyVaccinatedButton);
                    Button unvac = dialog.findViewById(R.id.unvaccinatedButton);
                    Button recover = dialog.findViewById(R.id.recoveredButton);
                    ImageButton close = dialog.findViewById(R.id.closeButton);
                    lgu.setText(ct.getText());
                    dialog.show();

                    if (lgu.getText().equals("Cebu") || lgu.getText().equals("Cebu City")) {
                        recover.setVisibility(View.GONE);
                        full.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, FullyVaccinated2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Intent.EXTRA_TEXT, lgu.getText().toString());
                                intent.putExtra("requirement", "For COVID-19 Fully Vaccinated Individuals");
                                context.startActivity(intent);

                            }
                        });
                        unvac.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, Unvaccinated2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Intent.EXTRA_TEXT, lgu.getText().toString());
                                intent.putExtra("requirement", "For Unvaccinated or Incompletely Vaccinated Individuals");
                                context.startActivity(intent);
                            }
                        });
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    } else if (lgu.getText().equals("Cagayan De Oro City")) {
                        recover.setVisibility(View.GONE);
                        full.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, FullyVaccinated2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Intent.EXTRA_TEXT, lgu.getText().toString());
                                intent.putExtra("requirement", "For COVID-19 Fully Vaccinated Individuals");
                                context.startActivity(intent);

                            }
                        });
                        unvac.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, Unvaccinated2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Intent.EXTRA_TEXT, lgu.getText().toString());
                                intent.putExtra("requirement", "For Unvaccinated or Incompletely Vaccinated Individuals");
                                context.startActivity(intent);
                            }
                        });
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    } else if (lgu.getText().equals("City of Manila")) {
                        full.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, FullyVaccinated2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Intent.EXTRA_TEXT, lgu.getText().toString());
                                intent.putExtra("requirement", "For COVID-19 Fully Vaccinated Individuals");
                                context.startActivity(intent);

                            }
                        });
                        unvac.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, Unvaccinated2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Intent.EXTRA_TEXT, lgu.getText().toString());
                                intent.putExtra("requirement", "For Unvaccinated or Incompletely Vaccinated Individuals");
                                context.startActivity(intent);
                            }
                        });
                        recover.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, Recovered.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Intent.EXTRA_TEXT, lgu.getText().toString());
                                intent.putExtra("requirement", "For Recovered COVID-19 positive travelers within 3 months from positive swab test results");
                                context.startActivity(intent);
                            }
                        });
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    } else {
                        full.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, FullyVaccinated.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Intent.EXTRA_TEXT, lgu.getText().toString());
                                intent.putExtra("requirement", "For COVID-19 Fully Vaccinated Individuals");
                                context.startActivity(intent);

                            }
                        });
                        unvac.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, Unvaccinated.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Intent.EXTRA_TEXT, lgu.getText().toString());
                                intent.putExtra("requirement", "For Unvaccinated or Incompletely Vaccinated Individuals");
                                context.startActivity(intent);
                            }
                        });
                        recover.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, Recovered.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Intent.EXTRA_TEXT, lgu.getText().toString());
                                intent.putExtra("requirement", "For Recovered COVID-19 positive travelers within 3 months from positive swab test results");
                                context.startActivity(intent);
                            }
                        });
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
