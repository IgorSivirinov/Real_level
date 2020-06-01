package com.example.changelevel.CustomAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.R;
import com.example.changelevel.ui.community.UsersFragment;

import java.util.ArrayList;

public class CustomAdapterUser  extends RecyclerView.Adapter<CustomAdapterUser.MyViewHolder>{



    private ArrayList<User> dataSet;

    public CustomAdapterUser(ArrayList<User> dataSet){
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public CustomAdapterUser.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_user, parent, false);
        view.setOnClickListener(UsersFragment.myOnClickListener);
        CustomAdapterUser.MyViewHolder myViewHolder = new CustomAdapterUser.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView name = holder.name;
        TextView level = holder.level;
        name.setText(dataSet.get(position).getName());
        level.setText("Уровень "+dataSet.get(position).checkLevel());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, level;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.tv_name_layout_card_user);
            this.level = itemView.findViewById(R.id.tv_level_layout_card_user);
        }
    }

}
