package com.example.changelevel.ui.home.Act.listAct;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.changelevel.MainActivity;
import com.example.changelevel.R;
import com.example.changelevel.ui.home.SettingsActivity;

public class PasswordNewActivity extends AppCompatActivity {
    Button button;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_new);
        button = findViewById(R.id.b_activity_password_new);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogGivePassword();
            }
        });
    }


    private void DialogGivePassword(){
        Dialog dialog = new Dialog(this);
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
                }
            }
        });
        dialog.show();
    }

    private void DialogRestartPassword(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_restart_password);
        dialog.show();
    }
}
