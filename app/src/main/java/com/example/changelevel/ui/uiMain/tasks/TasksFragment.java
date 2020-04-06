package com.example.changelevel.ui.uiMain.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.changelevel.LoginAndRegistration.FragmentLogin;
import com.example.changelevel.LoginAndRegistration.FragmentRegistration;
import com.example.changelevel.MainActivity;
import com.example.changelevel.R;
import com.example.changelevel.User.User;
import com.example.changelevel.ui.uiTaskList.accomplished.AccomplishedFragment;
import com.example.changelevel.ui.uiTaskList.active.ActiveFragment;
import com.example.changelevel.ui.uiTaskList.notActive.NotActiveFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TasksFragment extends Fragment implements View.OnClickListener {
    private TextView name_tasksList, email_tasksList;
    private TasksViewModel homeViewModel;
    private FragmentManager fragmentManager;
    Fragment fragment;
    private Button A,B,C;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(TasksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        name_tasksList = root.findViewById(R.id.name_tasksList);
        email_tasksList = root.findViewById(R.id.email_tasksList);

        A = root.findViewById(R.id.buttonGoToNotActivity);
        B = root.findViewById(R.id.buttonGoToActivity);
        C = root.findViewById(R.id.buttonGoToAccomplished);

        fragmentManager= getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragment = new NotActiveFragment();
        fragmentTransaction.add(R.id.containerTaskList,fragment);
        fragmentTransaction.commit();

        A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new NotActiveFragment();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerTaskList,fragment);
                fragmentTransaction.commit();
            }
        });
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ActiveFragment();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerTaskList,fragment);
                fragmentTransaction.commit();
            }
        });
        C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new AccomplishedFragment();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerTaskList,fragment);
                fragmentTransaction.commit();
            }
        });


        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                name_tasksList.setText(((User) getActivity().getIntent().getSerializableExtra("user")).getUserName());
                email_tasksList.setText(((User) getActivity().getIntent().getSerializableExtra("user")).getMail());
            }
        });

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.buttonGoToNotActivity:
                fragment = new NotActiveFragment();

                break;
            case R.id.buttonGoToActivity: fragment = new ActiveFragment();

                break;
            case R.id.buttonGoToAccomplished: fragment = new AccomplishedFragment();

                break;
        }
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerTaskList,fragment);
        fragmentTransaction.commit();
    }
}
