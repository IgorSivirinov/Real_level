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
        TextView xp = holder.xp;


        name.setText(dataSet.get(position).getName());
        xp.setText(dataSet.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,
                xp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.name = itemView.findViewById(R.id.name_task);
            this.xp=itemView.findViewById(R.id.layout_tasks_xp);
        }
    }
}
