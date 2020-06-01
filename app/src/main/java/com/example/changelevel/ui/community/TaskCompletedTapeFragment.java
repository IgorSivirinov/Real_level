package com.example.changelevel.ui.community;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.API.Firebase.Firestor.TaskCompletedTapeFS;
import com.example.changelevel.API.Firebase.Firestor.UserFS;
import com.example.changelevel.API.Firebase.Firestor.UserTasksCompletedFS;
import com.example.changelevel.CustomAdapters.CustomAdapterTaskCompleted;
import com.example.changelevel.CustomAdapters.CustomAdapterUser;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTaskCompletedTape;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class TaskCompletedTapeFragment extends Fragment {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Query taskCompletedSort;
    private User user;
    private ProgressBar pdLoadingTaskCompleted;
    private SwipeRefreshLayout srlRecyclerViewUsers;
    private RecyclerView.Adapter adapter;
    private static RecyclerView recyclerViewTaskCompleted;
    private LinearLayoutManager layoutManager;
    private static ArrayList<DataModelTaskCompletedTape> data;

    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_task_completed_tape, container, false);
        init();
        srlRecyclerViewUsers.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateTaskCompletedList();
            }
        });

        recyclerViewTaskCompleted.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if ((layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition())
                            >= layoutManager.getItemCount()-3) {
                        addTaskCompletedToList();
                    }
                }
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateTaskCompletedList();
    }

    private void init(){
        taskCompletedSort = firestore.collection("taskCompletedTape").orderBy("time", Query.Direction.DESCENDING);
        srlRecyclerViewUsers = root.findViewById(R.id.swipeRefreshLayout_fragment_task_completed_tape);
        pdLoadingTaskCompleted = root.findViewById(R.id.pb_taskCompletedLoading_fragment_task_completed_tape);
        recyclerViewTaskCompleted = root.findViewById(R.id.rv_taskCompleted_fragment_task_completed_tape);
        recyclerViewTaskCompleted.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerViewTaskCompleted.setLayoutManager(layoutManager);
        recyclerViewTaskCompleted.setItemAnimator(new DefaultItemAnimator());
        data = new ArrayList<DataModelTaskCompletedTape>();

    }

    private DocumentSnapshot lastVisible;
    private void updateTaskCompletedList(){
        data.clear();
        pdLoadingTaskCompleted.setVisibility(View.VISIBLE);
        taskCompletedSort.limit(10).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            data.add(new DataModelTaskCompletedTape
                                    (document.getId(),document.toObject(TaskCompletedTapeFS.class)));
                        }
                        try {
                            lastVisible = queryDocumentSnapshots.getDocuments()
                                    .get(queryDocumentSnapshots.size() - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pdLoadingTaskCompleted.setVisibility(View.GONE);
                        adapter = new CustomAdapterTaskCompleted(data);
                        srlRecyclerViewUsers.setRefreshing(false);
                        recyclerViewTaskCompleted.setAdapter(adapter);
                    }
                });

    }

    private void addTaskCompletedToList(){
        pdLoadingTaskCompleted.setVisibility(View.VISIBLE);
        taskCompletedSort.startAfter(lastVisible)
                .limit(3).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            data.add(new DataModelTaskCompletedTape
                                    (document.getId(),document.toObject(TaskCompletedTapeFS.class)));                        }
                        try {
                            lastVisible = queryDocumentSnapshots.getDocuments()
                                    .get(queryDocumentSnapshots.size() - 1);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pdLoadingTaskCompleted.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}