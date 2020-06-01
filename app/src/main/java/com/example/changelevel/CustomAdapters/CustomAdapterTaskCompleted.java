package com.example.changelevel.CustomAdapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTaskCompletedTape;
import com.google.gson.Gson;


import java.util.ArrayList;

public class CustomAdapterTaskCompleted extends RecyclerView.Adapter<CustomAdapterTaskCompleted.MyViewHolder>
implements PopupMenu.OnMenuItemClickListener{

    private Context context;

    private User user;

    private ArrayList<DataModelTaskCompletedTape> dataSet;

    @NonNull
    @Override
    public CustomAdapterTaskCompleted.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_task_completed, parent, false);
        CustomAdapterTaskCompleted.MyViewHolder myViewHolder = new CustomAdapterTaskCompleted.MyViewHolder(view);
        return myViewHolder;
    }

    public CustomAdapterTaskCompleted(ArrayList<DataModelTaskCompletedTape> dataSet){
        this.dataSet = dataSet;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView nameTask, nameUser, plusXpUser, userComments;
        ImageButton additionalAction;

        updateUser();

        nameTask = holder.nameTask;
        nameUser = holder.nameUser;
        plusXpUser = holder.plusXpUser;
        userComments = holder.userComments;
        additionalAction = holder.additionalAction;

        nameTask.setText(dataSet.get(position).getTask().getTaskName());
        nameUser.setText(dataSet.get(position).getUser().getName());
        plusXpUser.setText("+"+(int) dataSet.get(position).getTask().getTaskXP()+" XP");
        userComments.setText(dataSet.get(position).getComment());
        additionalAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context,v);
                popup.setOnMenuItemClickListener(CustomAdapterTaskCompleted.this);
                popup.inflate(R.menu.menu_task_completed_additional_action);
                if(user.isAdmin()){
                    popup.getMenu().getItem(0).setVisible(true);
                    popup.getMenu().getItem(1).setVisible(true);
                }
                popup.show();
            }
        });

    }

    private void updateUser(){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(user.APP_PREFERENCES_USER, context.MODE_PRIVATE);
        user = gson.fromJson(sharedPreferences.getString(user.APP_PREFERENCES_USER,""),User.class);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.complaint_menu_task_completed_additional_action:
                return true;
            case  R.id.delete_menu_task_completed_additional_action:
                return true;
            case R.id.fine_menu_task_completed_additional_action:
                return true;
            default:
                return false;
        }
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }




    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameTask, nameUser, plusXpUser, userComments;
        ImageButton additionalAction;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameTask = itemView.findViewById(R.id.tv_nameTask_layout_card_task_completed);
            this.nameUser = itemView.findViewById(R.id.tv_name_layout_card_task_completed);
            this.plusXpUser = itemView.findViewById(R.id.tv_xp_layout_card_task_completed);
            this.userComments = itemView.findViewById(R.id.tv_userComments_layout_card_task_completed);
            this.additionalAction = itemView.findViewById(R.id.ib_additionalAction_layout_card_task_completed);

        }
    }
}
