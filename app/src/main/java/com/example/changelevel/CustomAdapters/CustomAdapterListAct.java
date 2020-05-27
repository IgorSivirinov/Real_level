package com.example.changelevel.CustomAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelListAct;
import com.example.changelevel.ui.home.SettingsActivity;

import java.util.ArrayList;

public class CustomAdapterListAct extends RecyclerView.Adapter<CustomAdapterListAct.MyViewHolder> {

    private ArrayList<DataModelListAct> dataSet;

    public CustomAdapterListAct(ArrayList<DataModelListAct> dataSet){
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_settings, parent, false);
        view.setOnClickListener(SettingsActivity.myOnClickListener);
        return new MyViewHolder(view);
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

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.iconListSettings);
            this.name = itemView.findViewById(R.id.nameListSettings);
        }
    }
}
