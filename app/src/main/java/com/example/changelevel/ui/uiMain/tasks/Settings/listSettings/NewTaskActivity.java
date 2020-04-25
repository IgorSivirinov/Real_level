package com.example.changelevel.ui.uiMain.tasks.Settings.listSettings;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.changelevel.MainActivity;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTask;
import com.example.changelevel.models.DatabaseClient;

public class NewTaskActivity extends AppCompatActivity {

    private EditText newTaskName, newTaskOverview,
            newTaskSportXP,newTaskMindXP,newTaskCreativityXP;

    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        newTaskName = findViewById(R.id.newTaskName);
        newTaskOverview = findViewById(R.id.newTaskOverview);
        newTaskSportXP = findViewById(R.id.newTaskSportXP);
        newTaskMindXP = findViewById(R.id.newTaskMindXP);
        newTaskCreativityXP = findViewById(R.id.newTaskCreativityXP);

        saveButton = findViewById(R.id.saveButton);



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dataTask = new Intent();
                 DataModelTask task = new DataModelTask(
                        newTaskName.getText().toString(),
                        newTaskOverview.getText().toString(),
                        Integer.parseInt(newTaskSportXP.getText().toString()),
                        Integer.parseInt(newTaskMindXP.getText().toString()),
                        Integer.parseInt(newTaskCreativityXP.getText().toString()));
                dataTask.putExtra(MainActivity.ACCESS_MESSAGE, task);
                setResult(RESULT_OK, dataTask);
            }
        });
    }

    private void saveTask() {
        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DataModelTask dataModelTask = new DataModelTask(
                        newTaskName.getText().toString(),
                        newTaskOverview.getText().toString(),
                        Integer.parseInt(newTaskSportXP.getText().toString()),
                        Integer.parseInt(newTaskMindXP.getText().toString()),
                        Integer.parseInt(newTaskCreativityXP.getText().toString()));
                //creating a task


                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .ModelDao().insert(dataModelTask);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }
}
