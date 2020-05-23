package com.example.changelevel.LoginAndRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.API.Firebase.Firestor.UserFS;
import com.example.changelevel.API.Firebase.Firestor.UserTasksCompletedFS;
import com.example.changelevel.MainActivity;
import com.example.changelevel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private TextInputLayout name, email, password, rePassword;
    private ProgressBar progressBar;
    private boolean checkPasswords, checkEmptyString;

    private String sName;
    private String sEmail;
    private String sPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.til_name_activity_registration);
        email = findViewById(R.id.til_email_activity_registration);
        password = findViewById(R.id.til_password_activity_registration);
        rePassword = findViewById(R.id.til_re_password_activity_registration);

        progressBar = findViewById(R.id.pb_activity_registration);

        findViewById(R.id.ib_back_activity_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clearErrorEditText();
        findViewById(R.id.button_registration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmptyString = false;
                checkPasswords = false;
                progressBar.setVisibility(View.VISIBLE);
                isCheckEmptyString();
                if (password.getEditText().getText().toString().trim()
                        .equals(rePassword.getEditText().getText().toString().trim())) {
                    checkPasswords = true;
                } else rePassword.setError("Пароли не совпадают");

                if (checkPasswords && checkEmptyString) {
                    sName = name.getEditText().getText().toString().trim();
                    sEmail = email.getEditText().getText().toString().trim();
                    sPassword = password.getEditText().getText().toString().trim();

                    mAuth.createUserWithEmailAndPassword(
                            sEmail,
                            sPassword)
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        rePassword.setError(null);
                                        final UserFS userFS = new UserFS(
                                                null,
                                                sName, 0, false,
                                                new ArrayList<String>());
                                        firestore.collection("users").document(sEmail).set(userFS)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        User user = new User(sEmail, userFS);
                                                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                                        Gson gson = new Gson();
                                                        SharedPreferences sharedPreferences = getSharedPreferences(user.APP_PREFERENCES_USER, MODE_PRIVATE);
                                                        SharedPreferences.Editor editorUser = sharedPreferences.edit();
                                                        editorUser.putString(user.APP_PREFERENCES_USER, gson.toJson(user));
                                                        editorUser.apply();
                                                        startActivity(intent);
                                                    }
                                                });
                                    } else {
                                        rePassword.setError("Ошибка регистрации");
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                } else progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void CreateUser(final String email){

    }


    private void isCheckEmptyString(){
        if (name.getEditText().getText().toString().trim().isEmpty()) name.setError("Пустое поле");
            else name.setError(null);
        if (email.getEditText().getText().toString().trim().isEmpty()) email.setError("Пустое поле");
            else email.setError(null);
        if (password.getEditText().getText().toString().trim().isEmpty()) password.setError("Пустое поле");
            else password.setError(null);
        if (rePassword.getEditText().getText().toString().trim().isEmpty()) rePassword.setError("Пустое поле");
            else rePassword.setError(null);
        checkEmptyString = (!name.getEditText().getText().toString().trim().isEmpty()
                && !email.getEditText().getText().toString().trim().isEmpty()
                    && !password.getEditText().getText().toString().trim().isEmpty()
                        && !rePassword.getEditText().getText().toString().trim().isEmpty());


    }

    private void clearErrorEditText(){
        name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                name.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                email.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                password.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rePassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                rePassword.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
