package com.example.changelevel.LoginAndRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.changelevel.API.Api;
import com.example.changelevel.API.Constantes;
import com.example.changelevel.MainActivity;
import com.example.changelevel.R;
import com.example.changelevel.User.User;
import com.example.changelevel.helper.SessionManager;
import com.example.changelevel.models.DefaultResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputLayout email, password;
    private TextInputEditText emailTIET, passwordTITE;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

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
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
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

        }
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser!=null){
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//        }
//    }
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


}
