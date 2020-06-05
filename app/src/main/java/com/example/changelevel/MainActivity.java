package com.example.changelevel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.API.Firebase.Firestor.UserFS;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity{

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);


//        updateUser();
//        firestore.collection("users").document(mUser.getIdUser())
//                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                        if(documentSnapshot != null && documentSnapshot.exists()) {
//                            User user = new User(mUser.getIdUser(), mUser.getEmail(), documentSnapshot.toObject(UserFS.class));
//                            Gson gson = new Gson();
//                            SharedPreferences sharedPreferences = getSharedPreferences(user.APP_PREFERENCES_USER, MODE_PRIVATE);
//                            SharedPreferences.Editor editorUser = sharedPreferences.edit();
//                            editorUser.putString(user.APP_PREFERENCES_USER, gson.toJson(user));
//                            editorUser.apply();
//                            Log.d("MainActivity", "User update");
//                        }
//                    }
//                });
//
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);


    }
//    private void updateUser(){
//        Gson gson = new Gson();
//        SharedPreferences sharedPreferences = getSharedPreferences(mUser.APP_PREFERENCES_USER, MODE_PRIVATE);
//        mUser = gson.fromJson(sharedPreferences.getString(mUser.APP_PREFERENCES_USER,""), User.class);
//    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
