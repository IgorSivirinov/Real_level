package com.example.changelevel.ui.uiMain.tasks.Settings;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changelevel.CustomAdapters.CustomAdapterListSettings;
import com.example.changelevel.R;
import com.example.changelevel.models.DataModels.DataModelListSettings;
import com.example.changelevel.ui.uiMain.tasks.Settings.listSettings.DataListSettings;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModelListSettings> data;
    public static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        myOnClickListener=new MyOnClickListener(this);
        recyclerView=findViewById(R.id.recyclerViewSettings);
        recyclerView.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        data=new ArrayList<DataModelListSettings>();
        for (int i=0;i<DataListSettings.id.length;i++)
        {
            data.add(new DataModelListSettings(DataListSettings.id[i],
                    DataListSettings.nameArray[i],
                    DataListSettings.iconArray[i]));
        }
        removedItems=new ArrayList<>();
        adapter=new CustomAdapterListSettings(data);
        recyclerView.setAdapter(adapter);

    }
    private static  class MyOnClickListener implements View.OnClickListener{
        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            removeItem(v);
        }
        private void removeItem(View view)
        {
            int selectemItemPosition=recyclerView.getChildPosition(view);
            RecyclerView.ViewHolder viewHolder=recyclerView.findViewHolderForPosition(selectemItemPosition);
            TextView textViewName=viewHolder.itemView.findViewById(R.id.nameListSettings);
            String selectedName=textViewName.getText().toString();
            int selectedItemId=-1;
            for(int i=0;i<DataListSettings.nameArray.length;i++)
            { if(selectedName.equals(DataListSettings.nameArray[i]))
                    selectedItemId=DataListSettings.id[i];
            }
            removedItems.add(selectedItemId);
            data.remove(selectemItemPosition);
            adapter.notifyItemRemoved(selectemItemPosition);
        }
    }
}
