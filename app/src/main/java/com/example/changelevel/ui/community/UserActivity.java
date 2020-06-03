package com.example.changelevel.ui.community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.API.Firebase.Firestor.TaskCompletedTapeFS;
import com.example.changelevel.CustomAdapters.CustomAdapterHistory;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTaskCompletedTape;
import com.example.changelevel.ui.home.SettingsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Query historySort;
    private ProgressBar pbLoadingHistory;
    private SwipeRefreshLayout srlRecyclerViewHistory;
    private RecyclerView.Adapter adapter;
    private static RecyclerView recyclerViewHistory;
    private LinearLayoutManager layoutManager;
    private static ArrayList<DataModelTaskCompletedTape> data;

    private TextView name_toolbar_home, tv_level;
    private ImageButton back;
    private User user;
    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        srlRecyclerViewHistory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateTaskHistory();
            }
        });

        recyclerViewHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if ((layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition())
                            >= layoutManager.getItemCount()-3) {
                        addTaskHistory();
                    }
                }
                if (dy > 0) {
                    if ((layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition()) > layoutManager.getItemCount()) {
                        pbLoadingHistory.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


    }


    private void init(){
        user = gson.fromJson(getIntent().getStringExtra("user"), User.class);
        name_toolbar_home = findViewById(R.id.tv_name_activity_user);
        tv_level = findViewById(R.id.tv_level_activity_user);
        back = findViewById(R.id.ib_back_toolbar_activity_user);

        historySort = firestore.collection("users").document(user.getIdUser()).collection("history")
                .orderBy("time", Query.Direction.DESCENDING);
        srlRecyclerViewHistory = findViewById(R.id.swipeRefreshLayout_activity_user);
        pbLoadingHistory = findViewById(R.id.pb_loading_activity_user);
        recyclerViewHistory = findViewById(R.id.history_recycler_view_activity_user);
        recyclerViewHistory.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(UserActivity.this, 1);
        recyclerViewHistory.setLayoutManager(layoutManager);
        recyclerViewHistory.setItemAnimator(new DefaultItemAnimator());
        data = new ArrayList<DataModelTaskCompletedTape>();
    }
    private DocumentSnapshot lastVisible;
    private void updateTaskHistory(){
        data.clear();
        historySort.limit(10).get()
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
                        pbLoadingHistory.setVisibility(View.GONE);
                        adapter = new CustomAdapterHistory(data);
                        srlRecyclerViewHistory.setRefreshing(false);
                        recyclerViewHistory.setAdapter(adapter);
                    }
                });

    }

    private void addTaskHistory(){
        historySort.startAfter(lastVisible)
                .limit(3).get()
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
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pbLoadingHistory.setVisibility(View.GONE);

                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        name_toolbar_home.setText(user.getName());
        tv_level.setText("Уровень "+user.checkLevel());
        updateTaskHistory();
    }
}