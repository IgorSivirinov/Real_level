package com.example.changelevel.ui.tasks;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.changelevel.API.Firebase.Firestor.TaskTypeFS;
import com.example.changelevel.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddNewTaskTypeActivity extends AppCompatActivity {

    private ProgressBar pbLoading;
    private ImageButton ibBack;
    private Button bCreate;
    private TextInputLayout tilNameType;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task_type);
        init();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clearErrorEditText();
        bCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmptyString()) {
                    pbLoading.setVisibility(View.VISIBLE);
                    TaskTypeFS taskTypeFS = new TaskTypeFS(tilNameType.getEditText().getText().toString().trim());
                    firestore.collection("taskTypes").add(taskTypeFS).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            finish();
                        }
                    });
                }
            }
        });

    }
    private void clearErrorEditText(){
        tilNameType.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tilNameType.setError(null);
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
        if (tilNameType.getEditText().getText().toString().trim().isEmpty()) tilNameType.setError("Пустое поле");
        return !tilNameType.getEditText().getText().toString().trim().isEmpty();
    }

    private void init(){
        pbLoading = findViewById(R.id.pb_loading_activity_add_new_task_type);
        ibBack = findViewById(R.id.ib_back_toolbar_activity_add_new_task_type);
        bCreate = findViewById(R.id.b_create_activity_add_new_task_type);
        tilNameType = findViewById(R.id.til_nameType_activity_add_new_task_type);
    }
}
