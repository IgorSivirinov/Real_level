package com.example.changelevel.CustomAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.MainActivity;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTask;
import com.example.changelevel.ui.home.HomeFragment;
import com.example.changelevel.ui.home.SettingsActivity;
import com.example.changelevel.ui.tasks.TasksFragment;

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
        view.setOnClickListener(TasksFragment.myOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView name = holder.name;
        TextView xp = holder.xp;
        ImageView completed = holder.completed;

        name.setText(dataSet.get(position).getName());
        xp.setText("+"+(dataSet.get(position).getXp())+" XP");
        if (dataSet.get(position).isCompleted()){
            completed.setVisibility(View.VISIBLE);
            xp.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,
                xp;
        ImageView completed;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name_task);
            this.xp = itemView.findViewById(R.id.layout_tasks_xp);
            this.completed = itemView.findViewById(R.id.iv_taskComplete_task);
        }
    }

}
