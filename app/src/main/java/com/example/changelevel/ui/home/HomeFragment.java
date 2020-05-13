package com.example.changelevel.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.CustomAdapters.CustomAdapterListAct;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelListAct;
import com.example.changelevel.ui.home.Act.DataListAct;
import com.example.changelevel.ui.home.Act.listAct.NewTaskActivity;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModelListAct> data;
    public static View.OnClickListener myOnClickListener;
    private TextView name_toolbar_home;
    private BroadcastReceiver broadcastReceiver;
    private ImageButton imageButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        name_toolbar_home = root.findViewById(R.id.name_toolbar_home);
        imageButton = root.findViewById(R.id.ib_icon_user_toolbar_home);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

}
