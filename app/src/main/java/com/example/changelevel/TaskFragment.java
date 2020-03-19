package com.example.changelevel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment{
    private EditText overview, iconView, complexity, sportXP, mindXP, creativityXP;
    private Button newTask;
    public TaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        overview = view.findViewById(R.id.overview);
        iconView = view.findViewById(R.id.iconView);
        complexity = view.findViewById(R.id.complexity);
        sportXP = view.findViewById(R.id.sportXP);
        mindXP = view.findViewById(R.id.mindXP);
        creativityXP = view.findViewById(R.id.creativityXP);
        newTask = view.findViewById(R.id.newTask);
        newTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        
        return view;
    }
}
