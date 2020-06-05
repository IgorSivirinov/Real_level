package com.example.changelevel.CustomAdapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.R;
import com.example.changelevel.ui.community.UsersFragment;
import com.example.changelevel.ui.home.SettingsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapterUser  extends RecyclerView.Adapter<CustomAdapterUser.MyViewHolder>{


    private StorageReference mStorageRef;
    private ArrayList<User> dataSet;
    private Context context;

    public CustomAdapterUser(ArrayList<User> dataSet){
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public CustomAdapterUser.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mStorageRef = FirebaseStorage.getInstance().getReference("users_avatar");
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_user, parent, false);
        view.setOnClickListener(UsersFragment.myOnClickListener);
        CustomAdapterUser.MyViewHolder myViewHolder = new CustomAdapterUser.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView name = holder.name;
        TextView level = holder.level;
        final ImageView iconUser = holder.iconUser;
        User user = dataSet.get(position);
        name.setText(user.getName());
        level.setText("Уровень "+user.checkLevel());
        if(user.getUserAvatar()!=null)
            mStorageRef.child(user.getUserAvatar()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context)
                            .load(uri)
                            .into(iconUser);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context, "Ошибка получения иконки", Toast.LENGTH_SHORT).show();
                }
            });
        else iconUser.setImageResource(R.drawable.ic_user_start);
    }



    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, level;
        ImageView iconUser;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.tv_name_layout_card_user);
            this.level = itemView.findViewById(R.id.tv_level_layout_card_user);
            this.iconUser = itemView.findViewById(R.id.iv_icon_user_layout_card_user);
        }
    }

}
