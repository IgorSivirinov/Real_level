package com.example.changelevel.ui.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.API.Firebase.Firestor.TaskCompletedTapeFS;
import com.example.changelevel.API.Firebase.Firestor.UserTasksCompletedFS;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTask;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class TaskActivity extends AppCompatActivity {
    private Button completed;
    private TextView name, overview;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private User user;
    private DataModelTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);



        final Gson gson = new Gson();
        final SharedPreferences sharedPreferences = getSharedPreferences(user.APP_PREFERENCES_USER, MODE_PRIVATE);
        user = gson.fromJson(sharedPreferences.getString(user.APP_PREFERENCES_USER,""),User.class);

        name = findViewById(R.id.nameTask_activity_task);
        overview = findViewById(R.id.overviewTask_activity_task);
        completed = findViewById(R.id.b_task_completed_activity_task);

        name.setText(getIntent().getStringExtra("name"));
        overview.setText(getIntent().getStringExtra("overview"));

        task = gson.fromJson(getIntent().getStringExtra("task"), DataModelTask.class);



        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("taskCompletedTape")
                        .add(new TaskCompletedTapeFS(
                                task.getId(),
                                    task.getOverview(),
                                        task.getXp()))
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        firestore.collection("users").document(user.getEmail())
                                .update("tasksCompleted", FieldValue.arrayUnion(task.getId()));
                        user.addTaskCompleted(task.getId());
                        SharedPreferences.Editor editorUser = sharedPreferences.edit();
                        editorUser.putString(user.APP_PREFERENCES_USER, gson.toJson(user));
                        editorUser.apply();
                    }
                });
            }
        });
    }


}
