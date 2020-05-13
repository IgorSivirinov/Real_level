package com.example.changelevel.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.changelevel.CustomAdapters.CustomAdapterListAct;
import com.example.changelevel.MainActivity;
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
        ImageButton imageButton = findViewById(R.id.imageButton_back_toolbar_activity_settings);
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

    private static class MyOnClickListener implements View.OnClickListener {
        private Boolean passwordCheck = false;
        private final Context context;
        private Intent intent;
        RecyclerView.ViewHolder viewHolder;
        EditText password;
        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            removeItem(v);
        }

        private void removeItem(View view) {
            intent = null;
            int selectedItemPosition = recyclerView.getChildPosition(view);
            viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);

            if
            (viewHolder.getAdapterPosition() == 3 ||viewHolder.getAdapterPosition() == 0) {
                intent = new Intent(context, NewTaskActivity.class);
                context.startActivity(intent);
            }
            else
                DialogGivePassword();

            if(intent!=null)
                context.startActivity(intent);
        }

        private void DialogGivePassword(){
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_give_password);

            password = dialog.findViewById(R.id.et_password_dialog_give_password);
            Button restartPassword = dialog.findViewById(R.id.b_forgot_password_dialog_give_password);
            restartPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { DialogRestartPassword(); }});
            Button ok = dialog.findViewById(R.id.b_ok_dialog_give_password);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(password.getText().toString().equals("1234")) {
                        switch (viewHolder.getAdapterPosition()) {
                            case 0:
                                intent = new Intent(context, MainActivity.class);
                                break;
                            case 1:
                                intent = new Intent(context, MainActivity.class);
                                break;
                            case 2:
                                intent = new Intent(context, MainActivity.class);
                                break;
                        }
                        context.startActivity(intent);
                    }
                }
            });
            dialog.show();
        }

        private void DialogRestartPassword(){
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_restart_password);
            dialog.show();
        }

    }

}
