package com.example.changelevel.LoginAndRegistration;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.API.Firebase.Firestor.UserFS;
import com.example.changelevel.MainActivity;
import com.example.changelevel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextInputLayout email, password;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        email = findViewById(R.id.til_email_activity_login);
        password = findViewById(R.id.til_password_activity_login);
        progressBar = findViewById(R.id.pb_activity_login);

        clearErrorEditText();
            findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBar.setVisibility(View.VISIBLE);
                    if(checkEmptyString())
                        mAuth.signInWithEmailAndPassword(email.getEditText().getText().toString().trim(),
                                password.getEditText().getText().toString().trim())
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            password.setError(null);
                                            final FirebaseUser currentUser = mAuth.getCurrentUser();
                                            if(currentUser!=null){
                                                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                                firestore.collection("users").document(currentUser.getUid()).get()
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                User user = new User(currentUser.getUid(), currentUser.getEmail(), documentSnapshot.toObject(UserFS.class));
                                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                Gson gson = new Gson();
                                                                SharedPreferences sharedPreferences = getSharedPreferences(user.APP_PREFERENCES_USER, MODE_PRIVATE);
                                                                SharedPreferences.Editor editorUser = sharedPreferences.edit();
                                                                editorUser.putString(user.APP_PREFERENCES_USER, gson.toJson(user));
                                                                editorUser.apply();
                                                                startActivity(intent);

                                                            }
                                                        });

                                            }

                                        } else {
                                            password.setError("Не верный пароль или почта");
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
                    else progressBar.setVisibility(View.INVISIBLE);
                }
            });
            findViewById(R.id.button_go_to_registration).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                }
            });
        findViewById(R.id.b_restart_password_activity_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialogRestartPassword();
            }
        });

        }
    Dialog dialogRestartPassword;
    private void startDialogRestartPassword(){
        dialogRestartPassword = new Dialog(this);
        dialogRestartPassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogRestartPassword.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogRestartPassword.setContentView(R.layout.dialog_restart_password);
        Button ok = dialogRestartPassword.findViewById(R.id.b_ok_dialog_restart_password);
        Button cancel = dialogRestartPassword.findViewById(R.id.b_cancel_dialog_restart_password);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRestartPassword.dismiss();
            }});
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getEditText().getText().toString().trim().isEmpty()){
                    email.setError("Для получения письма заполните поле");
                    dialogRestartPassword.dismiss();
                }
                else {
                    mAuth.sendPasswordResetEmail(email.getEditText().getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(LoginActivity.this, "Письмо отправлено", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(LoginActivity.this, "Письмо не получилось отправить", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        dialogRestartPassword.show();
    }



    private void clearErrorEditText(){
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
    }
    private boolean checkEmptyString(){
        if (email.getEditText().getText().toString().trim().isEmpty()) email.setError("Пустое поле");
            else email.setError(null);
        if(password.getEditText().getText().toString().trim().isEmpty()) password.setError("Пустое поле");
            else password.setError(null);
        return !email.getEditText().getText().toString().trim().isEmpty() && !password.getEditText().getText().toString().trim().isEmpty();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }


}
