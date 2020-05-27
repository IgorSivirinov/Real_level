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
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private ImageButton imageButtonFiltersFragmentHome, ibAddTaskType;
    private static TaskFS task;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View root;
    private ProgressBar progressBar_tasksTape;
    private FloatingActionButton fabStartNewTaskActivity;
    LinearLayoutManager layoutManager;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private Query taskSort;

    private Gson gson = new Gson();
    private User user;
    private boolean isUpdateChips = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tasks, container, false);

        init();

        if(user.isAdmin()||user.isWriter()){
            fabStartNewTaskActivity.setVisibility(View.VISIBLE);
            fabStartNewTaskActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), NewTaskActivity.class);
                    startActivity(intent);
                }
            });
        }

        imageButtonFiltersFragmentHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startBottomSheetDialog(); }});

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateTasksList();
            }
        });

        data.clear();
        recyclerView.setAdapter(adapter);

        progressBar_tasksTape.setVisibility(View.VISIBLE);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if ((layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition()) >= layoutManager.getItemCount()-3) {
                        addTasksToList();
                    }
                }
            }
        });
        return root;
    }

    private void init(){
        updateUser();
        myOnClickListener=new MyOnClickListener(getActivity());
        recyclerView = root.findViewById(R.id.tasks_recycler_view);
        fabStartNewTaskActivity = root.findViewById(R.id.fab_newTask_fragment_task);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data = new ArrayList<DataModelTask>();
        taskSort = firestore.collection("tasks").whereLessThanOrEqualTo("minLevel", user.checkLevel());
        progressBar_tasksTape = root.findViewById(R.id.pb_tasksTape_fragment_tasks);
        imageButtonFiltersFragmentHome = root.findViewById(R.id.imageButton_filters_fragment_home);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout_fragment_tasks);
        updateTasksList();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUser();
        if (isUpdateChips) {
            startBottomSheetDialog();
            isUpdateChips = false;
        }
    }

    private void updateUser(){
        SharedPreferences sharedPreferences = getContext()
                .getSharedPreferences(user.APP_PREFERENCES_USER, getContext().MODE_PRIVATE);
        user = gson.fromJson(sharedPreferences.getString(user.APP_PREFERENCES_USER,""),User.class);
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
            if (!task.isBlocked()) {
                Intent intent = new Intent(context, TaskActivity.class);
                intent.putExtra("task", gson.toJson(task));
                context.startActivity(intent);
            }else Toast.makeText(getContext(), "Вам нужно повысить уровень чтобы выполнить заданее", Toast.LENGTH_LONG);

        }
    }

    private DocumentSnapshot lastVisible;
    private void updateTasksList(){
        data.clear();
        taskSort.limit(10).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            task = document.toObject(TaskFS.class);
                            data.add(new DataModelTask(document.getId(),
                                    task.getTaskName(),
                                    task.getTaskOverview(),
                                    task.getTaskType(),
                                    (int) task.getTaskXP(),
                                    (int) task.getMinLevel(),
                                    CheckTaskCompleted(document.getId()),
                                    (task.getMinLevel() > user.checkLevel())));
                        }
                        try {
                            lastVisible = queryDocumentSnapshots.getDocuments()
                                    .get(queryDocumentSnapshots.size() - 1);
                            adapter = new CustomAdapterTask(data);
                            progressBar_tasksTape.setVisibility(View.GONE);
                            recyclerView.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Заданий нет", Toast.LENGTH_LONG).show();
                        }
                        progressBar_tasksTape.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        recyclerView.setAdapter(adapter);
                    }
                });

                    }

                    private void addTasksToList(){
                        progressBar_tasksTape.setVisibility(View.VISIBLE);
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
                                                    (int)task.getTaskXP(),
                                                    (int)task.getMinLevel(),
                                                    CheckTaskCompleted(document.getId()),
                                                    (task.getMinLevel()>user.checkLevel())));
                                            document.getReference();
                                        }
                                        try {
                                            lastVisible = queryDocumentSnapshots.getDocuments()
                                                    .get(queryDocumentSnapshots.size() - 1);
                                            adapter.notifyDataSetChanged();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        progressBar_tasksTape.setVisibility(View.GONE);
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
    private BottomSheetDialog dialog;
    public void startBottomSheetDialog(){
        dialog = new BottomSheetDialog(getContext());
        View view = getLayoutInflater().inflate(R.layout.filters_task_list_dialog_bottom_sheet_fragment, null);
        progressLoading = view.findViewById(R.id.pb_loading_bottom_sheet_filters);
        chipGroup = view.findViewById(R.id.cg_taskType_bottom_sheet_filters);
        ibAddTaskType = view.findViewById(R.id.ib_addTaskType_bottom_sheet_filters);
        dialog.setContentView(view);
        updateChips();

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = chipGroup.findViewById(checkedId);
                if (chip!=null) {
                    if (chip.getText().toString().equals("Все"))
                        taskSort = firestore.collection("tasks");
                    else
                        taskSort = firestore.collection("tasks").whereEqualTo("taskType", chip.getText().toString());
                }else taskSort = firestore.collection("tasks").whereLessThanOrEqualTo("minLevel", user.checkLevel());
                updateTasksList();
            }
        });
        dialog.show();


    }

    private void updateChips(){
        progressLoading.setVisibility(View.GONE);
        addChipFilter("Все");
        firestore.collection("taskTypes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                addChipFilter(document.toObject(TaskTypeFS.class).getNameType());
                            }
                            stopLoading();

                        }
                    }
                });
    }

    private void addChipFilter(String name){
        chip = (Chip) getLayoutInflater().inflate(R.layout.chip_task_type,
                null, false);
        chip.setText(name);
        chipGroup.addView(chip);
    }

    private void stopLoading(){
        chipGroup.setVisibility(View.VISIBLE);
        if(user.isAdmin()){
            ibAddTaskType.setVisibility(View.VISIBLE);
            ibAddTaskType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isUpdateChips = true;
                    Intent intent = new Intent(getActivity(), AddNewTaskTypeActivity.class);
                    dialog.dismiss();
                    startActivity(intent);
                }
            });
        }
    }
}
