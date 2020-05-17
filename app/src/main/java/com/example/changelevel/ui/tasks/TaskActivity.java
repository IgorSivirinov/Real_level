package com.example.changelevel.ui.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.changelevel.R;

public class TaskActivity extends AppCompatActivity {
    private Button button;
    private TextView name, overview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        name = findViewById(R.id.nameTask_activity_task);
        overview = findViewById(R.id.overviewTask_activity_task);
        button = findViewById(R.id.b_);
    }
}
