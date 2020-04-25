package com.example.changelevel.LoginAndRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.changelevel.API.Api;
import com.example.changelevel.API.Constantes;
import com.example.changelevel.MainActivity;
import com.example.changelevel.R;
import com.example.changelevel.User.User;
import com.example.changelevel.helper.SessionManager;
import com.example.changelevel.models.DefaultResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private EditText loginMail, loginPassword;
    private Button buttonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginMail = findViewById(R.id.loginMail);
        loginPassword = findViewById(R.id.loginPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(Constantes.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                Api service = retrofit.create(Api.class);
                Call<DefaultResponse> call = service.Login(
                        loginMail.getText().toString()      ,
                        loginPassword.getText().toString());
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if(!response.body().isErr()){
                            User user = response.body().getUser();
                            SessionManager.getInstance(getApplicationContext()).saveUser(user);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {

                    }
                });

            }
        });
    }
}
