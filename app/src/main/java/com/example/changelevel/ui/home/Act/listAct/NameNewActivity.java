package com.example.changelevel.ui.home.Act.listAct;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.changelevel.API.Firebase.Firestor.ClientObjects.User;
import com.example.changelevel.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class NameNewActivity extends AppCompatActivity {

    private Gson gson;
    private SharedPreferences sharedPreferences;
    private User user;

    private ImageButton ibBack;

    private TextInputLayout newName;
    private Button bChangeName;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_new);
        init();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clearErrorEditText();
        bChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmptyString()){
                    firestore.collection("users").document(user.getIdUser())
                            .update("name", newName.getEditText().getText().toString().trim());
                    user.setName(newName.getEditText().getText().toString().trim());
                    SharedPreferences.Editor editorUser = sharedPreferences.edit();
                    editorUser.putString(user.APP_PREFERENCES_USER, gson.toJson(user));
                    editorUser.apply();
                    finish();
                }
            }
        });
    }
    private void  init(){
        gson = new Gson();
        firestore = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(user.APP_PREFERENCES_USER, MODE_PRIVATE);
        user = gson.fromJson(sharedPreferences.getString(user.APP_PREFERENCES_USER,""), User.class);
        newName = findViewById(R.id.til_name_activity_name_new);
        newName.getEditText().setText(user.getName());
        bChangeName = findViewById(R.id.b_change_activity_name_new);
        ibBack = findViewById(R.id.ib_back_toolbar_activity_name_new);
    }
    private void clearErrorEditText(){
        newName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                newName.setError(null);
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
        if (newName.getEditText().getText().toString().trim().isEmpty()) newName.setError("Пустое поле");
        return !newName.getEditText().getText().toString().trim().isEmpty();
    }

}
