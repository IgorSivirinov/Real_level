package com.example.changelevel.CustomAdapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.API.Firebase.Firestor.UserFS;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTaskCompletedTape;
import com.example.changelevel.ui.community.UserActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class CustomAdapterTaskCompleted extends RecyclerView.Adapter<CustomAdapterTaskCompleted.MyViewHolder>
implements PopupMenu.OnMenuItemClickListener{

    private StorageReference mStorageRef;
    private Context context;
    private int mPosition;
    private int mMenuPosition;
    private User user;
    private User userCreator;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Gson gson = new Gson();
    private boolean userComplained;

    private ArrayList<DataModelTaskCompletedTape> dataSet;

    @NonNull
    @Override
    public CustomAdapterTaskCompleted.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mStorageRef = FirebaseStorage.getInstance().getReference("users_avatar");
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_task_completed, parent, false);
        CustomAdapterTaskCompleted.MyViewHolder myViewHolder = new CustomAdapterTaskCompleted.MyViewHolder(view);
        return myViewHolder;
    }

    public CustomAdapterTaskCompleted(ArrayList<DataModelTaskCompletedTape> dataSet){
        this.dataSet = dataSet;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        TextView nameTask, nameUser, plusXpUser, userComments, complaints;
        ImageButton additionalAction;

        mPosition = position;
        updateUser();
        userCreator = dataSet.get(position).getUser();


        nameTask = holder.nameTask;
        nameUser = holder.nameUser;
        complaints = holder.complaints;
        plusXpUser = holder.plusXpUser;
        userComments = holder.userComments;
        final ImageButton iconUser = holder.iconUser;
        additionalAction = holder.additionalAction;

        nameTask.setText(dataSet.get(position).getTask().getTaskName());
        nameUser.setText(userCreator.getName());
        plusXpUser.setText("+"+(int) dataSet.get(position).getTask().getTaskXP()+" XP");
        userComments.setText(dataSet.get(position).getComment());
        if (user.isAdmin()) {
            complaints.setVisibility(View.VISIBLE);
            ArrayList<String> complainingUsersId = dataSet.get(mPosition).getComplainingUsersId();
            complaints.setText(complainingUsersId.size() + " Жалоб");
        }else complaints.setVisibility(View.GONE);



        additionalAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context,v);
                popup.setOnMenuItemClickListener(CustomAdapterTaskCompleted.this);
                popup.inflate(R.menu.menu_task_completed_additional_action);
                if(user.isAdmin()){
                    popup.getMenu().getItem(0).setVisible(true);
                    popup.getMenu().getItem(1).setVisible(true);
                    mMenuPosition = position;
                }
                popup.show();
            }
        });
        if(userCreator.getUserAvatar()!=null)
            mStorageRef.child(userCreator.getUserAvatar()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

        iconUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserActivity();
            }
        });


    }

    private void updateUser(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(user.APP_PREFERENCES_USER, context.MODE_PRIVATE);
        user = gson.fromJson(sharedPreferences.getString(user.APP_PREFERENCES_USER,""),User.class);
    }

    private void openUserActivity(){
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra("user", gson.toJson(user));
        context.startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if (item.getItemId()==R.id.delete_menu_task_completed_additional_action){
            dialogDeleteTaskCompleted();
            return true;
        }
        if ((item.getItemId()==R.id.advertising_complaint_menu_task_completed_additional_action
                ||item.getItemId()==R.id.profanity_complaint_menu_task_completed_additional_action
                ||item.getItemId()==R.id.insult_complaint_menu_task_completed_additional_action)&&!checkUserComplained()){
            firestore.collection("taskCompletedTape").document(dataSet.get(mMenuPosition).getId())
                    .update("complainingUsersId",FieldValue.arrayUnion(user.getIdUser()));
            userComplained = true;
        }


        return false;
    }

    private boolean checkUserComplained(){
        ArrayList<String> complainingUsersId = dataSet.get(mMenuPosition).getComplainingUsersId();
        for(String id : complainingUsersId){
            if (id.equals(dataSet.get(mMenuPosition).getId())) return true;
        }
        return false;
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView nameTask, nameUser, plusXpUser, userComments, complaints;
        ImageButton additionalAction;
        ImageButton iconUser;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameTask = itemView.findViewById(R.id.tv_nameTask_layout_card_task_completed);
            this.nameUser = itemView.findViewById(R.id.tv_name_layout_card_task_completed);
            this.plusXpUser = itemView.findViewById(R.id.tv_xp_layout_card_task_completed);
            this.userComments = itemView.findViewById(R.id.tv_userComments_layout_card_task_completed);
            this.additionalAction = itemView.findViewById(R.id.ib_additionalAction_layout_card_task_completed);
            this.iconUser = itemView.findViewById(R.id.ib_icon_user_layout_card_task_completed);
            this.complaints = itemView.findViewById(R.id.tv_complaint_counter_layout_card_task_completed);
        }
    }

    private void deleteTaskCompleted(){
        firestore.collection("taskCompletedTape").document(dataSet.get(mMenuPosition).getId()).delete();
    }


    private void dialogDeleteTaskCompleted(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_delete);
        TextView name = dialog.findViewById(R.id.tv_name_dialog_delete);
        name.setText("Вы уверены, что хотите удалить это?");
        Button delete = dialog.findViewById(R.id.b_deletion_dialog_delete);
        Button cancel = dialog.findViewById(R.id.b_cancel_dialog_delete);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }});
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTaskCompleted();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
