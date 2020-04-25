package com.example.changelevel.CustomAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTask;

import java.util.ArrayList;

public class CustomAdapterTask extends RecyclerView.Adapter<CustomAdapterTask.MyViewHolder>{

    private ArrayList<DataModelTask> dataSet;

    public CustomAdapterTask(ArrayList<DataModelTask> dataSet){
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView name = holder.name;
        TextView cell_xp1 = holder.cell_xp1;
        TextView cell_xp2 = holder.cell_xp2;
        TextView cell_xp3 = holder.cell_xp3;

        name.setText(dataSet.get(position).getName());
        cell_xp1.setText(dataSet.get(position).getCreativityXP());
        cell_xp2.setText(dataSet.get(position).getMindXP());
        cell_xp3.setText(dataSet.get(position).getSportXP());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,
                cell_xp1,cell_xp2,cell_xp3;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.name = itemView.findViewById(R.id.name);
            this.cell_xp1=itemView.findViewById(R.id.cell_xp1);
            this.cell_xp2=itemView.findViewById(R.id.cell_xp2);
            this.cell_xp3=itemView.findViewById(R.id.cell_xp3);
        }
    }
}
