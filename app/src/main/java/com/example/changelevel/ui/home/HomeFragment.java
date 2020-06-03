package com.example.changelevel.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.API.Firebase.Firestor.TaskCompletedTapeFS;
import com.example.changelevel.CustomAdapters.CustomAdapterHistory;
import com.example.changelevel.CustomAdapters.CustomAdapterTaskCompleted;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelTaskCompletedTape;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Query historySort;
    private ProgressBar pbLoadingHistory;
    private SwipeRefreshLayout srlRecyclerViewHistory;
    private RecyclerView.Adapter adapter;
    private static RecyclerView recyclerViewTaskCompleted;
    private LinearLayoutManager layoutManager;
    private static ArrayList<DataModelTaskCompletedTape> data;

    private TextView name_toolbar_home, tv_xp, tv_level;
    private ProgressBar progressBarXP;
    private ImageButton imageButton;
    private User user;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        init();


        srlRecyclerViewHistory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateTaskHistory();
            }
        });

        recyclerViewTaskCompleted.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }



    private void init(){
        updateUser();
        name_toolbar_home = root.findViewById(R.id.tv_name_toolbar_fragment_home);
        tv_xp = root.findViewById(R.id.tv_xp_fragment_home);
        tv_level = root.findViewById(R.id.tv_level_fragment_home);
        progressBarXP = root.findViewById(R.id.pb_xp_fragment_home);
        imageButton = root.findViewById(R.id.ib_icon_user_toolbar_home);

        historySort = firestore.collection("users").document(user.getIdUser()).collection("history")
                .orderBy("time", Query.Direction.DESCENDING);
        srlRecyclerViewHistory = root.findViewById(R.id.swipeRefreshLayout_fragment_home);
        pbLoadingHistory = root.findViewById(R.id.pb_loading_fragment_home);
        recyclerViewTaskCompleted = root.findViewById(R.id.history_recycler_view);
        recyclerViewTaskCompleted.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerViewTaskCompleted.setLayoutManager(layoutManager);
        recyclerViewTaskCompleted.setItemAnimator(new DefaultItemAnimator());
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
                        recyclerViewTaskCompleted.setAdapter(adapter);
                    }
                });

    }

    private void addTaskHistory(){
        historySort.startAfter(lastVisible)
                .limit(2).get()
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

    private void updateUser(){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getContext()
                .getSharedPreferences(user.APP_PREFERENCES_USER, getContext().MODE_PRIVATE);
        user = gson.fromJson(sharedPreferences.getString(user.APP_PREFERENCES_USER,""),User.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        name_toolbar_home.setText(user.getName());
        tv_level.setText("Уровень "+user.checkLevel());
        tv_xp.setText(user.getXp()+"/"+user.getMaxXp(user.checkLevel()));

        progressBarXP.setMax(user.getMaxXp(user.checkLevel()));
        progressBarXP.setProgress((int) user.getXp());
        updateTaskHistory();
    }
}
