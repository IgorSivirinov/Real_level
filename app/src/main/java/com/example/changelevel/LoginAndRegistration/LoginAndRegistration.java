package com.example.changelevel.LoginAndRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.changelevel.MainActivity;
import com.example.changelevel.R;
import com.example.changelevel.helper.SessionManager;

public class LoginAndRegistration extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_registration);
        if (SessionManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("user", SessionManager.getInstance(this).getUser());
            startActivity(intent);
        }
        findViewById(R.id.buttonGoToLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAndRegistration.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.buttonGoToRegistration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAndRegistration.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }

}
