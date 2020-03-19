package com.example.changelevel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.changelevel.LoginAndRegistration.FragmentRegistration;
import com.example.changelevel.LoginAndRegistration.LoginAndRegistration;
import com.example.changelevel.User.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
       private User user = null;
       private Thread update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        update = new Thread(new AnotherRunnable());
        update.start();
    }

    class AnotherRunnable implements Runnable{

        @Override
        public void run() {

        }
    }

}
