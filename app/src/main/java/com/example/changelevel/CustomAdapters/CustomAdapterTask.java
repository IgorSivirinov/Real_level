package com.example.changelevel.CustomAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTask;
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
        TextView type = holder.type;
        TextView minLevel = holder.minLevel;
        ImageView completed = holder.completed;
        CardView blocked = holder.blocked;

        name.setText(dataSet.get(position).getName());
        xp.setText("+"+(dataSet.get(position).getXp())+" XP");
        type.setText(dataSet.get(position).getType());
        minLevel.setText("Доступен с "+dataSet.get(position).getMinLevel()+" уровня");
        if (!dataSet.get(position).isBlocked()){
            blocked.setVisibility(View.GONE);
        }else blocked.setVisibility(View.VISIBLE);

        if (checkTaskCompleted(dataSet.get(position).getTasksCompleted(), dataSet.get(position).getId())){
            completed.setVisibility(View.VISIBLE);
            xp.setVisibility(View.GONE);
        }else{
            completed.setVisibility(View.GONE);
            xp.setVisibility(View.VISIBLE);
        }

    }

    private boolean checkTaskCompleted(ArrayList<String> tasksCompleted, String idTask){
        for (int i = 0; i<tasksCompleted.size(); i++){
            if (tasksCompleted.get(i).equals(idTask)) return true;
        }
        return false;
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, xp, type, minLevel;
        ImageView completed;
        CardView blocked;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name_task);
            this.xp = itemView.findViewById(R.id.layout_tasks_xp);
            this.completed = itemView.findViewById(R.id.iv_taskComplete_task);
            this.type = itemView.findViewById(R.id.tv_taskType_task);
            this.blocked = itemView.findViewById(R.id.cv_blocked_task);
            this.minLevel = itemView.findViewById(R.id.tv_minLevel_task);
        }
    }

}
