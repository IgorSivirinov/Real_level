package com.example.changelevel.ui.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class TasksFragment extends Fragment
        implements PopupMenu.OnMenuItemClickListener{

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
    private LinearLayoutManager layoutManager;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private Query taskSort;

    private Gson gson = new Gson();
    private User user;
    private boolean isUpdateChips = false;
    private boolean isAddTask = true;

    private boolean isUpdateTaskList = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tasks, container, false);

        init();

        if(user.isWriter()){
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



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if ((layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition()) >= layoutManager.getItemCount()-3) {
                        if (isAddTask) {
                            isAddTask = false;
                            addTasksToList();
                        }
                    }
                }
                if (dy > 0) {
                    if ((layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition()) > layoutManager.getItemCount()-0.01) {
                        progressBar_tasksTape.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void onResume() {
        super.onResume();

            updateTasksList();

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
        updateUser();
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
                                    user.getTasksCompleted(),
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
                        updateUser();
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
                                                    user.getTasksCompleted(),
                                                    (task.getMinLevel()>user.checkLevel())));
                                            document.getReference();
                                        }
                                        try {
                                            lastVisible = queryDocumentSnapshots.getDocuments()
                                                    .get(queryDocumentSnapshots.size() - 1);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        adapter.notifyDataSetChanged();
                                        progressBar_tasksTape.setVisibility(View.GONE);
                                        isAddTask = true;
                                    }
                                });
                    }






    private ChipGroup chipGroup;
    private BottomSheetDialog dialog;
    public void startBottomSheetDialog(){
        dialog = new BottomSheetDialog(getContext());
        View view = getLayoutInflater().inflate(R.layout.filters_task_list_dialog_bottom_sheet_fragment, null);
        chipGroup = view.findViewById(R.id.cg_taskType_bottom_sheet_filters);
        ibAddTaskType = view.findViewById(R.id.ib_addTaskType_bottom_sheet_filters);
        if (user.isAdmin()) ibAddTaskType.setVisibility(View.VISIBLE);
        dialog.setContentView(view);
        updateChips();
        dialog.show();


    }

    private void updateChips(){
        Chip chip2 = (Chip) getLayoutInflater().inflate(R.layout.chip_task_type,
                null, false);
        chip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskSort = firestore.collection("tasks").whereLessThanOrEqualTo("minLevel", user.checkLevel());
                updateTasksList();
            }
        });
        chip2.setText("Все доступные");
        chipGroup.addView(chip2);


        Chip chip1 = (Chip) getLayoutInflater().inflate(R.layout.chip_task_type,
                null, false);
        chip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskSort = firestore.collection("tasks").orderBy("time", Query.Direction.DESCENDING);
                updateTasksList();
            }
        });
        chip1.setText("Все");
        chipGroup.addView(chip1);
        firestore.collection("taskTypes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                addChipFilter(document.toObject(TaskTypeFS.class).getNameType(), document.getId());
                            }
                        }
                    }
                });
    }

    private void addChipFilter(final String name, final String id){
        final Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_task_type,
                null, false);
        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskSort = firestore.collection("tasks").whereEqualTo("taskType", name);
                updateTasksList();
            }
        });
        chip.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(),v);
                popup.setOnMenuItemClickListener(TasksFragment.this);
                popup.inflate(R.menu.menu_delete);
                popup.show();
                whatToDeleteChip = id;
                return false;
            }
        });
        chip.setText(name);
        chipGroup.addView(chip);
    }
    private String whatToDeleteChip;
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId()==R.id.delete_menu_delete){
            firestore.collection("taskTypes").document(whatToDeleteChip).delete();
            updateChips();
            return true;
        }
        return false;
    }

}
