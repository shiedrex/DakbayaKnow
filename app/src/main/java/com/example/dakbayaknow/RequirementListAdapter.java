package com.example.dakbayaknow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RequirementListAdapter extends BaseAdapter {
    List<RequirementModel> requirements;
    Context context;

    public RequirementListAdapter(List<RequirementModel> requirements, Context context) {
        this.requirements = requirements;
        this.context = context;
    }

    @Override
    public int getCount() {
        return requirements.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.requirements_list_items,null);
        TextView fVaccReq1,fVaccReq1Required;
        Button fVaccReq1Button;
        ImageView fVaccReq1Image;
        fVaccReq1 = view.findViewById(R.id.fVaccReq1);
        fVaccReq1Required = view.findViewById(R.id.fVaccReq1Required);
        fVaccReq1Button = view.findViewById(R.id.fVaccReq1Button);
        fVaccReq1Image = view.findViewById(R.id.fVaccReq1Image);

        fVaccReq1.setText(requirements.get(i).getName());


        return view;
    }
}
