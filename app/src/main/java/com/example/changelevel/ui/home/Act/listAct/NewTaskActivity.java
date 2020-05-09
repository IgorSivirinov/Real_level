package com.example.changelevel.ui.home.Act.listAct;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTask;
import com.example.changelevel.ui.tasks.TasksFragment;

import java.util.ArrayList;

public class NewTaskActivity extends AppCompatActivity {

    private EditText newTaskName, newTaskOverview, xp;

    private Button saveButton;

    private static ArrayList<DataModelTask> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        newTaskName = findViewById(R.id.newTaskName);
        newTaskOverview = findViewById(R.id.newTaskOverview);
        xp = findViewById(R.id.newTaskXP);
        data=new ArrayList<DataModelTask>();
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 data.add(new DataModelTask(
                        newTaskName.getText().toString(),
                        newTaskOverview.getText().toString(),
                        Integer.parseInt(xp.getText().toString())));
                Toast.makeText(getApplicationContext(),
                        "Naw Task:"
                                +"\n   TaskName: "+newTaskName.getText().toString()
                                +"\n   TaskOverview: "+newTaskOverview.getText().toString()
                                +"\n   TaskSportXP: "+xp.getText().toString()
                        ,Toast.LENGTH_LONG).show();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                TasksFragment tasksFragment = new TasksFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("task", data);
                tasksFragment.setArguments(bundle);
                transaction.commit();
            }
        });
    }
}
