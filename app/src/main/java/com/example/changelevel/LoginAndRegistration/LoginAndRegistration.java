package com.example.changelevel.LoginAndRegistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.changelevel.MainActivity;
import com.example.changelevel.R;
import com.example.changelevel.helper.SessionManager;

public class LoginAndRegistration extends AppCompatActivity {
    private FragmentManager fragmentManager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_registration);
        if(SessionManager.getInstance(this).isLoggedIn())
        {
            Intent intent=new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            FragmentLogin fragment = new FragmentLogin();
            fragmentTransaction.add(R.id.containerLoginAndRegistration, fragment);
            fragmentTransaction.commit();
        }
    }

    public void Change(View view){
        Fragment fragment = null;
        switch (view.getId()){

            case R.id.buttonGoToLogin:
               fragment = new FragmentLogin();

                break;
            case R.id.buttonGoToRegistration: fragment = new FragmentRegistration();

                break;
        }
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerLoginAndRegistration,fragment);
        fragmentTransaction.commit();
    }
}
