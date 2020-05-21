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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.changelevel.API.Firebase.Firestor.TaskFS;
import com.example.changelevel.CustomAdapters.CustomAdapterTask;
import com.example.changelevel.MainActivity;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelListAct;
import com.example.changelevel.models.DataModels.DataModelTask;
import com.example.changelevel.ui.home.Act.DataListAct;
import com.example.changelevel.ui.home.Act.listAct.NewTaskActivity;
import com.example.changelevel.ui.home.SettingsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.changelevel.ui.tasks.DataListTest.nameArray;
import static com.example.changelevel.ui.tasks.DataListTest.xp;

public class TasksFragment extends Fragment {

    private RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private static ArrayList<DataModelTask> data;
    private static ArrayList<TaskFS> tasks;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View root;
    private ProgressBar progressBar_tasksTape;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tasks, container, false);

        myOnClickListener=new MyOnClickListener(getActivity());
        recyclerView = root.findViewById(R.id.tasks_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data = new ArrayList<DataModelTask>();
        tasks = new ArrayList<TaskFS>();

        progressBar_tasksTape = root.findViewById(R.id.pb_tasksTape_fragment_tasks);

        ImageButton imageButtonFiltersFragmentHome = root.findViewById(R.id.imageButton_filters_fragment_home);
        imageButtonFiltersFragmentHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { StartBottomSheetDialog(); }});

        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout_fragment_tasks);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UpdateTasksList();
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateTasksList();
    }


    private class UpdateTapeTasksThread extends Thread{
        private DocumentSnapshot lastVisible;
        private int indexTask = -1;
        @Override
        public void run() {
            swipeRefreshLayout.setRefreshing(true);
            firestore.collection("tasks").limit(1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                            indexTask++;
                            tasks.add(document.toObject(TaskFS.class));
                            data.add(new DataModelTask(tasks.get(indexTask).getTaskName(), (int)tasks.get(indexTask).getTaskXP()));
                        }
                        lastVisible = queryDocumentSnapshots.getDocuments()
                                .get(queryDocumentSnapshots.size() -1);
                        adapter = new CustomAdapterTask(data);
                        swipeRefreshLayout.setRefreshing(false);
                        recyclerView.setAdapter(adapter);
                        Button button = root.findViewById(R.id.addTask_fragment_tasks);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progressBar_tasksTape.setVisibility(View.VISIBLE);
                                try {
                                    firestore.collection("tasks").startAfter(lastVisible).limit(1).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                                                        indexTask++;
                                                        tasks.add(document.toObject(TaskFS.class));
                                                        data.add(new DataModelTask(tasks.get(indexTask).getTaskName(), (int)tasks.get(indexTask).getTaskXP()));
                                                    }
                                                    try {
                                                        lastVisible = queryDocumentSnapshots.getDocuments()
                                                                .get(queryDocumentSnapshots.size() -1);

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
//            intent.putExtra("task",dmt);
            context.startActivity(intent);
        }
    }

    public void UpdateTasksList(){
        data.clear();
        tasks.clear();
        UpdateTapeTasksThread updateTapeTasks = new UpdateTapeTasksThread();
        updateTapeTasks.start();
    }

    public void StartBottomSheetDialog(){
        View view = getLayoutInflater().inflate(R.layout.filters_task_list_dialog_bottom_sheet_fragment, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(view);
        dialog.show();
    }

}
