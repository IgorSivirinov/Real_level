package com.example.changelevel.CustomAdapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.API.Firebase.Firestor.UserFS;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTaskCompletedTape;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;


import java.util.ArrayList;

public class CustomAdapterTaskCompleted extends RecyclerView.Adapter<CustomAdapterTaskCompleted.MyViewHolder>
implements PopupMenu.OnMenuItemClickListener{

    private Context context;
    private int mPosition;
    private User user;
    private User userCreator;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

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

        mPosition = position;
        updateUser();
        userCreator = dataSet.get(position).getUser();

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
            case  R.id.delete_menu_task_completed_additional_action:
                dialogDeleteTaskCompleted();
                return true;
            case R.id.fine_menu_task_completed_additional_action:
                dialogDeleteTaskCompletedAndFineUser();
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

    private void deleteTaskCompleted(){
        firestore.collection("taskCompletedTape").document(dataSet.get(mPosition).getId()).delete();
    }

    private void deleteTaskCompletedAndFineUser(){
        firestore.collection("taskCompletedTape").document(dataSet.get(mPosition).getId()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firestore.collection("users").document(userCreator.getIdUser()).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        firestore.collection("users").document(userCreator.getIdUser())
                                                .update("xp",
                                                        documentSnapshot.toObject(UserFS.class).getXp()-
                                                                (dataSet.get(mPosition).getTask().getTaskXP()+(dataSet.get(mPosition).getTask().getTaskXP()/10)));
                                    }
                                });
                    }
                }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void dialogDeleteTaskCompletedAndFineUser(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_delete);
        TextView name = dialog.findViewById(R.id.tv_name_dialog_delete);
        name.setText("Вы уверены, что хотите удалить и оштрафовать пользователя на "+
                (dataSet.get(mPosition).getTask().getTaskXP()+(dataSet.get(mPosition).getTask().getTaskXP()/10))+
                " XP?");
        Button delete = dialog.findViewById(R.id.b_deletion_dialog_delete);
        delete.setText("Удалить и оштафовать");
        Button cancel = dialog.findViewById(R.id.b_cancel_dialog_delete);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }});
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTaskCompletedAndFineUser();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
