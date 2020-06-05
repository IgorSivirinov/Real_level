package com.example.changelevel.CustomAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTaskCompletedTape;


import java.util.ArrayList;

public class CustomAdapterHistory extends RecyclerView.Adapter<CustomAdapterHistory.MyViewHolder> {


    private ArrayList<DataModelTaskCompletedTape> dataSet;

    @NonNull
    @Override
    public CustomAdapterHistory.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_me_history, parent, false);
        CustomAdapterHistory.MyViewHolder myViewHolder = new CustomAdapterHistory.MyViewHolder(view);
        return myViewHolder;
    }

    public CustomAdapterHistory(ArrayList<DataModelTaskCompletedTape> dataSet){
        this.dataSet = dataSet;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapterHistory.MyViewHolder holder, int position) {
        TextView nameTask, plusXpUser, userComments;

        nameTask = holder.nameTask;
        plusXpUser = holder.plusXpUser;
        userComments = holder.userComments;

        nameTask.setText(dataSet.get(position).getTask().getTaskName());
        plusXpUser.setText("+"+(int) dataSet.get(position).getTask().getTaskXP()+" XP");
        userComments.setText(dataSet.get(position).getComment());

    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameTask, plusXpUser, userComments;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameTask = itemView.findViewById(R.id.tv_nameTask_layout_card_me_history);
            this.plusXpUser = itemView.findViewById(R.id.tv_xp_layout_card_me_history);
            this.userComments = itemView.findViewById(R.id.tv_userComments_layout_card_me_history);
        }
    }



}
