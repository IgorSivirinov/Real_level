package com.example.changelevel.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.changelevel.CustomAdapters.CustomAdapterTask;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTask;

import java.util.ArrayList;

public class TasksFragment extends Fragment {

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private ArrayList<DataModelTask> data;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        recyclerView = root.findViewById(R.id.tasks_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        Bundle bundle = getArguments();
//        data = (ArrayList<DataModelTask>) bundle.getSerializable("task");
        data = new ArrayList<DataModelTask>();
        adapter = new CustomAdapterTask(data);
        recyclerView.setAdapter(adapter);
        return root;
    }
}
