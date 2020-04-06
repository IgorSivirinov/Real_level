package com.example.changelevel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private ArrayList<Task> tasksSet;

    public CustomAdapter(ArrayList<Task> tasksSet){
        this.tasksSet = tasksSet;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
        //view.setOnClickListener(MainActivity.myOnClickListener);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView overview = holder.overview;
        TextView cell_xp1 = holder.cell_xp1;
        TextView cell_xp2 = holder.cell_xp2;
        TextView cell_xp3 = holder.cell_xp3;
        Button taskAction = holder.taskAction;
    }

    @Override
    public int getItemCount() {
        return tasksSet.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView overview,
                cell_xp1,cell_xp2,cell_xp3;
        Button taskAction;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.overview=itemView.findViewById(R.id.overview);
            this.cell_xp1=itemView.findViewById(R.id.cell_xp1);
            this.cell_xp2=itemView.findViewById(R.id.cell_xp2);
            this.cell_xp3=itemView.findViewById(R.id.cell_xp3);
            this.taskAction=itemView.findViewById(R.id.taskAction);
        }
    }
}
