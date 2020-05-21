package com.example.changelevel.ui.home.Act.listAct;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.changelevel.API.Firebase.Firestor.TaskFS;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTask;
import com.example.changelevel.ui.tasks.TasksFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NewTaskActivity extends AppCompatActivity {
    private TextInputLayout taskName, taskOverview,
            nameTaskType,minRankTaskType
            ,taskXP;
    private Button buttonSave;
    private ImageButton imageButton;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        progressBar = findViewById(R.id.progressSave_activity_new_task);
        taskName = findViewById(R.id.til_nameTask_activity_new_task);
        taskOverview = findViewById(R.id.til_overviewTask_activity_new_task);
        nameTaskType = findViewById(R.id.til_nameTaskType_activity_new_task);
        minRankTaskType = findViewById(R.id.til_minRankTaskType_activity_new_task);
        taskXP = findViewById(R.id.til_xpTask_activity_new_task);
            buttonSave = findViewById(R.id.b_saveTask_activity_new_task);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                TaskFS taskFS = new TaskFS(taskName.getEditText().getText().toString().trim(),
                        taskOverview.getEditText().getText().toString().trim(),
                        new TaskFS.TaskType(
                                nameTaskType.getEditText().getText().toString().trim(),
                                Integer.parseInt(minRankTaskType.getEditText().getText().toString().trim())),
                        Integer.parseInt(taskXP.getEditText().getText().toString().trim()));

                firestore.collection("tasks")
                        .add(taskFS).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(NewTaskActivity.this, "Задание опубликовано",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(NewTaskActivity.this, "Error",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        imageButton = findViewById(R.id.imageButton_back_toolbar_activity_new_task);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
