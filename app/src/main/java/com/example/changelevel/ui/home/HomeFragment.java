package com.example.changelevel.ui.home;

import android.content.BroadcastReceiver;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.CustomAdapters.CustomAdapterListAct;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelListAct;
import com.example.changelevel.ui.home.Act.DataListAct;
import com.example.changelevel.ui.home.Act.listAct.NewTaskActivity;
import com.google.gson.Gson;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private TextView name_toolbar_home, tv_xp, tv_level;
    private ProgressBar progressBarXP;
    private ImageButton imageButton;
    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getContext()
                .getSharedPreferences(user.APP_PREFERENCES_USER, getContext().MODE_PRIVATE);
        user = gson.fromJson(sharedPreferences.getString(user.APP_PREFERENCES_USER,""),User.class);

        name_toolbar_home = root.findViewById(R.id.tv_name_toolbar_fragment_home);
        tv_xp = root.findViewById(R.id.tv_xp_fragment_home);
        tv_level = root.findViewById(R.id.tv_level_fragment_home);
        progressBarXP = root.findViewById(R.id.pb_xp_fragment_home);
        imageButton = root.findViewById(R.id.ib_icon_user_toolbar_home);

        name_toolbar_home.setText(user.getName());
        tv_level.setText("LEVEL "+user.checkLevel());
        tv_xp.setText(user.getXp()+"/"+user.getMaxXp(user.checkLevel()));

        progressBarXP.setMax(user.getMaxXp(user.checkLevel()));
        progressBarXP.setProgress((int) user.getXp());

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
