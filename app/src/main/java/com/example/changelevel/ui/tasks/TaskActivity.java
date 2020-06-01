package com.example.changelevel.ui.tasks;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.API.Firebase.Firestor.TaskCompletedTapeFS;
import com.example.changelevel.API.Firebase.Firestor.TaskFS;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Date;


public class TaskActivity extends AppCompatActivity {
    private Button bCompleted, bDelete;
    private ImageButton ibBack;
    private TextView name, overview;
    private ImageView ivTaskComplete;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private User user;
    private DataModelTask task;
    private Gson gson = new Gson();
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        init();
        name.setText(task.getName());
        overview.setText(task.getOverview());

        if (user.isAdmin()) bDelete.setVisibility(View.VISIBLE);

        if (task.isCompleted()){
            bCompleted.setVisibility(View.GONE);
            ivTaskComplete.setVisibility(View.VISIBLE);
        }

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialogDeleteWarning();
            }
        });

        bCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.addTaskCompleted(task.getId());
                user.setXp(user.getXp()+task.getXp());

                firestore.collection("taskCompletedTape")
                        .add(new TaskCompletedTapeFS(
                                new TaskFS(task.getName(),null,null,    task.getXp(), 0),
                                user,
                                null,
                                new Timestamp(new Date())))
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        firestore.collection("users").document(user.getIdUser())
                                .update("tasksCompleted", FieldValue.arrayUnion(task.getId()));
                        firestore.collection("users").document(user.getIdUser())
                                .update("xp", user.getXp()+task.getXp());
                        SharedPreferences.Editor editorUser = sharedPreferences.edit();
                        editorUser.putString(user.APP_PREFERENCES_USER, gson.toJson(user));
                        editorUser.apply();
                        finish();
                    }
                });
            }
        });
    }
    private void init(){
        sharedPreferences = getSharedPreferences(user.APP_PREFERENCES_USER, MODE_PRIVATE);
        user = gson.fromJson(sharedPreferences.getString(user.APP_PREFERENCES_USER,""),User.class);

        name = findViewById(R.id.nameTask_activity_task);
        overview = findViewById(R.id.overviewTask_activity_task);
        ivTaskComplete = findViewById(R.id.iv_taskComplete_activity_task);

        ibBack = findViewById(R.id.ib_back_toolbar_activity_task);
        bCompleted = findViewById(R.id.b_task_completed_activity_task);
        bDelete = findViewById(R.id.b_deleteTask_activity_task);

        task = gson.fromJson(getIntent().getStringExtra("task"), DataModelTask.class);
    }

    private void startDialogDeleteWarning(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_delete_warning);
        Button delete = dialog.findViewById(R.id.b_confirmDeletion_dialog_delete_warning);
        Button cancel = dialog.findViewById(R.id.b_cancelDeletion_dialog_delete_warning);
        cancel.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          dialog.dismiss();
                                      }});
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("tasks").document(task.getId()).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TaskActivity.this, "Ошибка удаления", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        dialog.show();
    }


}
