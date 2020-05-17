package com.example.changelevel.ui.tasks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.changelevel.CustomAdapters.CustomAdapterTask;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelListAct;
import com.example.changelevel.models.DataModels.DataModelTask;
import com.example.changelevel.ui.home.Act.DataListAct;
import com.example.changelevel.ui.home.Act.listAct.NewTaskActivity;
import com.example.changelevel.ui.home.SettingsActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import static com.example.changelevel.ui.tasks.DataListTest.nameArray;
import static com.example.changelevel.ui.tasks.DataListTest.xp;

public class TasksFragment extends Fragment {

    private Button test;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    public static ArrayList<DataModelTask> data;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton imageButtonFiltersFragmentHome;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        recyclerView = root.findViewById(R.id.tasks_recycler_view);
        recyclerView.setHasFixedSize(true);
        myOnClickListener=new MyOnClickListener(getActivity());
        layoutManager=new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data = new ArrayList<DataModelTask>();

        imageButtonFiltersFragmentHome=root.findViewById(R.id.imageButton_filters_fragment_home);
        imageButtonFiltersFragmentHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { StartBottomSheetDialog(); }});

        UpdateTasksList();
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout_fragment_tasks);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UpdateTasksList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;
    }

    private static class MyOnClickListener implements View.OnClickListener {
        private final Context context;
        RecyclerView.ViewHolder viewHolder;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            removeItem(v);
        }

        private void removeItem(View view) {
            int selectedItemPosition = recyclerView.getChildPosition(view);
            viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);

            DataModelTask dmt = TasksFragment.data.get(selectedItemPosition);
            Intent intent = new Intent(context, TaskActivity.class);
            intent.putExtra("task",dmt);
            context.startActivity(intent);
        }
    }

    public void UpdateTasksList(){
        data.clear();
        for (int i = 0; i< DataListTest.id.length; i++)
        {
            data.add(new DataModelTask(DataListTest.nameArray[i], DataListTest.overviewArray[i], DataListTest.xp[i]));
        }

        adapter = new CustomAdapterTask(data);
        recyclerView.setAdapter(adapter);
    }

    public void StartBottomSheetDialog(){
        View view = getLayoutInflater().inflate(R.layout.filters_task_list_dialog_bottom_sheet_fragment, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(view);
        dialog.show();
        test = dialog.findViewById(R.id.b_test);
    }
}
