package com.example.changelevel.CustomAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.R;

import com.example.changelevel.models.DataModels.DataModelListSettings;
import com.example.changelevel.ui.uiMain.tasks.Settings.SettingsActivity;


import java.util.ArrayList;

public class CustomAdapterListSettings extends RecyclerView.Adapter<CustomAdapterListSettings.MyViewHolder> {

    private ArrayList<DataModelListSettings> dataSet;

    public CustomAdapterListSettings(ArrayList<DataModelListSettings> dataSet){
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_settings, parent, false);
        view.setOnClickListener(SettingsActivity.myOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ImageView image = holder.image;
        TextView name = holder.name;

        name.setText(dataSet.get(position).getName());
        image.setImageResource(dataSet.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.iconListSettings);
            this.name = itemView.findViewById(R.id.nameListSettings);
        }
    }
}
