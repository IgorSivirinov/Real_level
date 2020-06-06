package com.example.changelevel.ui.community;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.API.Firebase.Firestor.UserFS;
import com.example.changelevel.CustomAdapters.CustomAdapterUser;
import com.example.changelevel.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UsersFragment extends Fragment {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Query userSort;

    private ProgressBar pbLoadingUser;
    private SwipeRefreshLayout srlRecyclerViewUsers;
    private RecyclerView.Adapter adapter;
    private static RecyclerView recyclerViewUsers;
    public static View.OnClickListener myOnClickListener;
    private LinearLayoutManager layoutManager;
    private boolean isUpdateUsersList = true;
    private Gson gson = new Gson();

    private static ArrayList<User> data;
    private View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_users, container, false);
        init();

        srlRecyclerViewUsers.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUsersList();
            }
        });

        recyclerViewUsers.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if ((layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition())
                            >= layoutManager.getItemCount() - 3)
                        if (isUpdateUsersList) {
                            isUpdateUsersList = false;
                            addUsersToList();
                        }
                    if ((layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition())
                            > layoutManager.getItemCount())
                    pbLoadingUser.setVisibility(View.VISIBLE);
                }


            }
        });
        return root;
    }

    private void init(){
        userSort = firestore.collection("users").orderBy("xp", Query.Direction.DESCENDING);
        srlRecyclerViewUsers = root.findViewById(R.id.swipeRefreshLayout_fragment_users);
        pbLoadingUser = root.findViewById(R.id.pb_usersLoading_fragment_users);
        myOnClickListener=new UsersFragment.MyOnClickListener(getActivity());
        recyclerViewUsers = root.findViewById(R.id.rv_users_fragment_users);
        recyclerViewUsers.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerViewUsers.setLayoutManager(layoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        data = new ArrayList<User>();
        updateUsersList();
    }

    private DocumentSnapshot lastVisible;
    private void updateUsersList(){
        data.clear();
        userSort.limit(10).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            data.add(new User(document.getId(),null,document.toObject(UserFS.class)));
                        }
                        try {
                            lastVisible = queryDocumentSnapshots.getDocuments()
                                    .get(queryDocumentSnapshots.size() - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pbLoadingUser.setVisibility(View.GONE);
                        adapter = new CustomAdapterUser(data);
                        srlRecyclerViewUsers.setRefreshing(false);
                        recyclerViewUsers.setAdapter(adapter);
                    }
                });

    }

    private void addUsersToList(){
        userSort.startAfter(lastVisible)
                .limit(3).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            data.add(new User(document.getId(),null,document.toObject(UserFS.class)));
                        }
                        try {
                            lastVisible = queryDocumentSnapshots.getDocuments()
                                    .get(queryDocumentSnapshots.size() - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pbLoadingUser.setVisibility(View.GONE);
                        isUpdateUsersList = true;
                        adapter.notifyDataSetChanged();
                    }
                });
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
            try {
                int selectedItemPosition = recyclerViewUsers.getChildPosition(view);
                RecyclerView.ViewHolder viewHolder = recyclerViewUsers.findViewHolderForPosition(selectedItemPosition);
                User user = UsersFragment.data.get(selectedItemPosition);
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    Intent intent = new Intent(context, UserActivity.class);
                    intent.putExtra("user", gson.toJson(user));
                    context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
