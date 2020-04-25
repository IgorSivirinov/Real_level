package com.example.changelevel.ui.uiMain.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.changelevel.PageAdapter;
import com.example.changelevel.R;
import com.example.changelevel.User.User;

import com.example.changelevel.ui.uiMain.tasks.Settings.SettingsActivity;
import com.google.android.material.tabs.TabLayout;

public class TasksFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ImageButton imageButtonGoToSettingsActivity;
    private TextView name_tasksList, email_tasksList;


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        email_tasksList = root.findViewById(R.id.name_tasksList);
        name_tasksList = root.findViewById(R.id.email_tasksList);
        tabLayout = root.findViewById(R.id.tabLayoutTasks);
        imageButtonGoToSettingsActivity = root.findViewById(R.id.imageButtonGoToSettingsActivity);
        viewPager= root.findViewById(R.id.viewPagerTasks);

        name_tasksList.setText(((User) getActivity().getIntent().getSerializableExtra("user")).getUserName());//<
        // Странно, Email и Name меняются местами.(Поменял мистами чтоб хотябы работоло)              РАЗОБРАТЬСЯ!!!!!
        email_tasksList.setText(((User) getActivity().getIntent().getSerializableExtra("user")).getMail());//<
        
        pagerAdapter = new PageAdapter(getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        imageButtonGoToSettingsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0){
                    pagerAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 1){
                    pagerAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 2){
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return root;
    }
}
