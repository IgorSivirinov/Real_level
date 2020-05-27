package com.example.changelevel.ui.tasks;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.changelevel.API.Firebase.Firestor.TaskFS;
import com.example.changelevel.API.Firebase.Firestor.TaskTypeFS;
import com.example.changelevel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NewTaskActivity extends AppCompatActivity {
    private TextInputLayout taskName, taskOverview,
            nameTaskType,minLevelTaskType
            ,taskXP;
    private AutoCompleteTextView taskType;
    private Button buttonSave;
    private ImageButton imageButton;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ProgressBar progressBar;
    private ArrayList<String> taskTypes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        init();
        loadingTaskType();
        clearErrorEditText();
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEmptyString()) {
                    progressBar.setVisibility(View.VISIBLE);
                    TaskFS taskFS = new TaskFS(taskName.getEditText().getText().toString().trim(),
                            taskOverview.getEditText().getText().toString().trim(),
                            nameTaskType.getEditText().getText().toString().trim(),
                            Integer.parseInt(taskXP.getEditText().getText().toString().trim()),
                            Integer.parseInt(minLevelTaskType.getEditText().getText().toString().trim()));

                    firestore.collection("tasks")
                            .add(taskFS).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(NewTaskActivity.this, "Задание опубликовано", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(NewTaskActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    });
                }
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

    private void init(){
        progressBar = findViewById(R.id.progressSave_activity_new_task);
        taskName = findViewById(R.id.til_nameTask_activity_new_task);
        taskOverview = findViewById(R.id.til_overviewTask_activity_new_task);
        nameTaskType = findViewById(R.id.til_nameTaskType_activity_new_task);
        minLevelTaskType = findViewById(R.id.til_minRankTaskType_activity_new_task);
        taskXP = findViewById(R.id.til_xpTask_activity_new_task);
        taskType = findViewById(R.id.actv_nameTaskType_activity_new_task);
        buttonSave = findViewById(R.id.b_saveTask_activity_new_task);
    }

    private void loadingTaskType(){
        firestore.collection("taskTypes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                taskTypes.add(document.toObject(TaskTypeFS.class).getNameType());
                            }
                            ArrayAdapter <String> adapter =
                                    new ArrayAdapter<String>(NewTaskActivity.this, android.R.layout.simple_list_item_1, taskTypes);
                            taskType.setAdapter(adapter);
                        }
                    }
                });
    }

    private void clearErrorEditText(){
        taskName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                taskName.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        taskOverview.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                taskOverview.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        nameTaskType.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                nameTaskType.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        minLevelTaskType.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                minLevelTaskType.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        taskXP.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                taskXP.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean checkEmptyString(){
        boolean check = true;
        if (taskName.getEditText().getText().toString().trim().isEmpty()){ taskName.setError("Пустое поле"); check=false;}
        if (taskOverview.getEditText().getText().toString().trim().isEmpty()){ taskOverview.setError("Пустое поле"); check=false;}
        if (nameTaskType.getEditText().getText().toString().trim().isEmpty()){ nameTaskType.setError("Пустое поле"); check=false;}
        if (minLevelTaskType.getEditText().getText().toString().trim().isEmpty()){ minLevelTaskType.setError("Пустое поле"); check=false;}
        if (taskXP.getEditText().getText().toString().trim().isEmpty()){ taskXP.setError("Пустое поле"); check=false;}
        return check;
    }
}
