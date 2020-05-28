package com.example.changelevel.ui.home.Act.listAct;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

public class EmailNewActivity extends AppCompatActivity {
    private FirebaseUser userF;
    private Button bChangeEmail;
    private TextInputLayout email;
    private User user;
    private ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_new);
        init();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmptyString()){
                    userF.updateEmail(email.getEditText().getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                        finish();
                                    else
                                    Toast.makeText(EmailNewActivity.this, "Неполучилось изменить e-mail",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private void init(){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences(user.APP_PREFERENCES_USER, MODE_PRIVATE);
        user = gson.fromJson(sharedPreferences.getString(user.APP_PREFERENCES_USER,""), User.class);
        userF = FirebaseAuth.getInstance().getCurrentUser();
        email = findViewById(R.id.til_email_activity_email_new);
        email.getEditText().setText(user.getEmail());
        bChangeEmail = findViewById(R.id.b_activity_email_new);
        ibBack = findViewById(R.id.ib_back_toolbar_activity_email_new);
        clearErrorEditText();
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
    }

    private boolean checkEmptyString(){
        if (email.getEditText().getText().toString().trim().isEmpty()) email.setError("Пустое поле");
        return !email.getEditText().getText().toString().trim().isEmpty();
    }

}
