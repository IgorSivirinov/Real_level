package com.example.changelevel.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
//        myOnClickListener=new MyOnClickListener(getContext());
//        recyclerView=root.findViewById(R.id.recycler_view_act);
//        recyclerView.setHasFixedSize(true);
//        layoutManager=new GridLayoutManager(getContext(),1);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        data=new ArrayList<DataModelListAct>();
        name_toolbar_home = root.findViewById(R.id.name_toolbar_home);
        imageButton = root.findViewById(R.id.imageButtonGoToSettingsActivity);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });
//        Bundle bundle = this.getArguments();
//        if (bundle != null){
//            name_toolbar_home.setText(((User) bundle.getSerializable(MainActivity.KEY_ACTIVITY_TO_HOME)).getUserName());
//        }

//        for (int i = 0; i< DataListAct.id.length; i++)
//        {
//            data.add(new DataModelListAct(DataListAct.id[i],
//                    DataListAct.nameArray[i],
//                    DataListAct.iconArray[i]));
//        }
//        adapter=new CustomAdapterListAct(data);
//        recyclerView.setAdapter(adapter);
//
        return root;
    }

//    private static class MyOnClickListener implements View.OnClickListener{
//        private final Context context;
//        private Intent intent;
//
//        private MyOnClickListener(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public void onClick(View v) {
//            removeItem(v);
//        }
//        private void removeItem(View view)
//        {
//            int selectedItemPosition=recyclerView.getChildPosition(view);
//            RecyclerView.ViewHolder viewHolder=recyclerView.findViewHolderForPosition(selectedItemPosition);
//
//            switch (viewHolder.getAdapterPosition()){
//                case 3:
//                    intent = new Intent(context, NewTaskActivity.class);
//                    break;
//            }
//            context.startActivity(intent);
//        }
//    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        getActivity().getApplicationContext().unregisterReceiver(this.broadcastReceiver);
//    }
}
