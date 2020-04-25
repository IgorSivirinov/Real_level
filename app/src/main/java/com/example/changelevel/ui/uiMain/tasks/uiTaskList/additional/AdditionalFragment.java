package com.example.changelevel.ui.uiMain.tasks.uiTaskList.additional;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.CustomAdapters.CustomAdapterTask;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTask;

import java.util.ArrayList;


public class AdditionalFragment extends Fragment {

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private ArrayList<DataModelTask> Data;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_additional, container, false);

        recyclerView = root.findViewById(R.id.additional_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CustomAdapterTask(Data);
        recyclerView.setAdapter(adapter);

        return root;
    }


}
