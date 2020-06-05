package com.example.changelevel.ui.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.API.Firebase.Firestor.TaskCompletedTapeFS;
import com.example.changelevel.CustomAdapters.CustomAdapterHistory;
import com.example.changelevel.CustomAdapters.CustomAdapterTaskCompleted;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTaskCompletedTape;
import com.example.changelevel.ui.home.SettingsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity{
    private static final String TAG = "UserActivity";
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
    private User iUser;
    private Gson gson = new Gson();
    private boolean isAddTask = true;
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
                        if (isAddTask) {
                            isAddTask = false;
                            addTaskHistory();
                        }
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
        updateUser();
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
        final ImageView iconUser = findViewById(R.id.iv_icon_user_activity_user);
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("users_avatar");
        if(user.getUserAvatar()!=null)
            mStorageRef.child(user.getUserAvatar()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(UserActivity.this)
                            .load(uri)
                            .into(iconUser);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(UserActivity.this, "Ошибка получения иконки", Toast.LENGTH_SHORT).show();
                }
            });
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

                        if(iUser.getIdUser().equals(user.getIdUser()))
                        adapter = new CustomAdapterHistory(data);
                        else adapter = new CustomAdapterTaskCompleted(data);

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
                        isAddTask = true;
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

    private void updateUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(user.APP_PREFERENCES_USER, MODE_PRIVATE);
        iUser = gson.fromJson(sharedPreferences.getString(user.APP_PREFERENCES_USER,""),User.class);
    }


}