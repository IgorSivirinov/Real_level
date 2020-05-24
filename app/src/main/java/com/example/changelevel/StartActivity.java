package com.example.changelevel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.API.Firebase.Firestor.UserFS;
import com.example.changelevel.LoginAndRegistration.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.Objects;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }
    Intent intent;
    @Override
    public void onStart() {
        super.onStart();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null){
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = new User(currentUser.getUid(), currentUser.getEmail(),
                                    documentSnapshot.toObject(UserFS.class));
                            intent = new Intent(StartActivity.this, MainActivity.class);
                            Gson gson = new Gson();
                            SharedPreferences sharedPreferences = getSharedPreferences(user.APP_PREFERENCES_USER, MODE_PRIVATE);
                            SharedPreferences.Editor editorUser = sharedPreferences.edit();
                            editorUser.putString(user.APP_PREFERENCES_USER, gson.toJson(user));
                            editorUser.apply();
                            startActivity(intent);
                        }
                    });

        }else{
            intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
