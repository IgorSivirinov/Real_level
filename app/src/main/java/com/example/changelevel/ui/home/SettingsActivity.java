package com.example.changelevel.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.changelevel.CustomAdapters.CustomAdapterListAct;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelListAct;
import com.example.changelevel.ui.home.Act.DataListAct;
import com.example.changelevel.ui.home.Act.listAct.NewTaskActivity;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModelListAct> data;
    public static View.OnClickListener myOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ImageButton imageButton = findViewById(R.id.imageButton_toolbar_back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        myOnClickListener=new MyOnClickListener(this);
        recyclerView=findViewById(R.id.recycler_view_act);
        recyclerView.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data=new ArrayList<DataModelListAct>();

        for (int i = 0; i< DataListAct.id.length; i++)
        {
            data.add(new DataModelListAct(DataListAct.id[i],
                    DataListAct.nameArray[i],
                    DataListAct.iconArray[i]));
        }
        adapter=new CustomAdapterListAct(data);
        recyclerView.setAdapter(adapter);
    }

    private static class MyOnClickListener implements View.OnClickListener{
        private final Context context;
        private Intent intent;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            removeItem(v);
        }
        private void removeItem(View view)
        {
            int selectedItemPosition=recyclerView.getChildPosition(view);
            RecyclerView.ViewHolder viewHolder=recyclerView.findViewHolderForPosition(selectedItemPosition);

            switch (viewHolder.getAdapterPosition()){
                case 0:
                    intent = new Intent(context, NewTaskActivity.class);
                    break;
            }
            context.startActivity(intent);
        }
    }
}
