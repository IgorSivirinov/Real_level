package com.example.changelevel.ui.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.API.Firebase.Firestor.TaskFS;
import com.example.changelevel.API.Firebase.Firestor.TaskTypeFS;
import com.example.changelevel.CustomAdapters.CustomAdapterTask;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

public class TasksFragment extends Fragment {

    private RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private static ArrayList<DataModelTask> data;
    private ImageButton imageButtonFiltersFragmentHome;
    private static TaskFS task;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View root;
    private ProgressBar progressBar_tasksTape;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private Query taskSort;
    private String taskSortFilter = "minLevel";
    private Query.Direction taskSortType = Query.Direction.DESCENDING;

    private Gson gson = new Gson();
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tasks, container, false);
        init();


        imageButtonFiltersFragmentHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { StartBottomSheetDialog(); }});

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UpdateTasksList();
            }
        });



        return root;
    }

    private void init(){
        myOnClickListener=new MyOnClickListener(getActivity());
        recyclerView = root.findViewById(R.id.tasks_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data = new ArrayList<DataModelTask>();
        progressBar_tasksTape = root.findViewById(R.id.pb_tasksTape_fragment_tasks);
        imageButtonFiltersFragmentHome = root.findViewById(R.id.imageButton_filters_fragment_home);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout_fragment_tasks);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getContext()
                .getSharedPreferences(user.APP_PREFERENCES_USER, getContext().MODE_PRIVATE);
        user = gson.fromJson(sharedPreferences.getString(user.APP_PREFERENCES_USER,""),User.class);
        taskSort = firestore.collection("tasks").whereLessThanOrEqualTo("minLevel", user.checkLevel())
                .orderBy(taskSortFilter, taskSortType);
        UpdateTasksList();

    }


    private class MyOnClickListener implements View.OnClickListener {
        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            removeItem(v);
        }

        private void removeItem(View view) {
            int selectedItemPosition = recyclerView.getChildPosition(view);
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);
            DataModelTask task = TasksFragment.data.get(selectedItemPosition);
            Intent intent = new Intent(context, TaskActivity.class);

            intent.putExtra("task", gson.toJson(task));
            context.startActivity(intent);

        }
    }

    private DocumentSnapshot lastVisible;
    private void UpdateTasksList(){
        data.clear();
        recyclerView.setAdapter(adapter);

        progressBar_tasksTape.setVisibility(View.VISIBLE);

        taskSort.limit(3).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            task = document.toObject(TaskFS.class);
                                                        data.add(new DataModelTask(document.getId(),
                                    task.getTaskName(),
                                    task.getTaskOverview(),
                                    task.getTaskType(),
                                    (int)task.getTaskXP()
                                    ,CheckTaskCompleted(document.getId())));
                        }
                        lastVisible = queryDocumentSnapshots.getDocuments()
                                .get(queryDocumentSnapshots.size() - 1);
                        adapter = new CustomAdapterTask(data);
                        progressBar_tasksTape.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        recyclerView.setAdapter(adapter);
                        Button button = root.findViewById(R.id.addTask_fragment_tasks);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progressBar_tasksTape.setVisibility(View.VISIBLE);
                                try {
                                    taskSort.startAfter(lastVisible)
                                            .limit(3).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                        task = document.toObject(TaskFS.class);
                                                        data.add(new DataModelTask(document.getId(),
                                                                task.getTaskName(),
                                                                task.getTaskOverview(),
                                                                task.getTaskType(),
                                                                (int)task.getTaskXP()
                                                                ,CheckTaskCompleted(document.getId())));
                                                        document.getReference();
                                                    }
                                                    try {
                                                        lastVisible = queryDocumentSnapshots.getDocuments()
                                                                .get(queryDocumentSnapshots.size() - 1);
                                                        adapter = new CustomAdapterTask(data);
                                                        progressBar_tasksTape.setVisibility(View.GONE);
                                                        recyclerView.setAdapter(adapter);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        progressBar_tasksTape.setVisibility(View.GONE);
                                                        Toast.makeText(getActivity(), "Задания закончились", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    progressBar_tasksTape.setVisibility(View.GONE);
                                }
                            }

                        });
                    }
                });
    }

    private boolean CheckTaskCompleted(String idTask){
        for (int i = 0; i<user.getTasksCompleted().size(); i++){
            if (user.getTasksCompleted().get(i).equals(idTask)) return true;
        }
        return false;
    }



    private Chip chip;
    private ArrayList<Chip> chips = new ArrayList<Chip>();
    private ChipGroup chipGroup;
    private ProgressBar progressLoading;

    public void StartBottomSheetDialog(){
        View view = getLayoutInflater().inflate(R.layout.filters_task_list_dialog_bottom_sheet_fragment, null);
        progressLoading = view.findViewById(R.id.pb_loading_bottom_sheet_filters);
        chipGroup = view.findViewById(R.id.cg_taskType_bottom_sheet_filters);
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(view);
        dialog.show();
        updateChips();
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Toast.makeText(getContext(), chips.get(checkedId).getText().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateChips(){
        firestore.collection("taskTypes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                chip = (Chip) getLayoutInflater().inflate(R.layout.chip_task_type,
                                        null, false);
                                chip.setText(document.toObject(TaskTypeFS.class).getNameType());
                                chips.add(chip);
                                chipGroup.addView(chip);
                            }
                            stopLoading();
                        }
                    }
                });
    }

    private void stopLoading(){
        progressLoading.setVisibility(View.GONE);
        chipGroup.setVisibility(View.VISIBLE);
    }
}
