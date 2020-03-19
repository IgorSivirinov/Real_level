package com.example.changelevel.LoginAndRegistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.changelevel.R;

public class LoginAndRegistration extends AppCompatActivity {
    private FragmentManager fragmentManager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_registration);
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        FragmentLogin fragment=new FragmentLogin();
        fragmentTransaction.add(R.id.containerLoginAndRegistration,fragment);
        fragmentTransaction.commit();
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
