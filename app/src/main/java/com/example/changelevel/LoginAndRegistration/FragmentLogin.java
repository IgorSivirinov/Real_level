package com.example.changelevel.LoginAndRegistration;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLogin extends Fragment {
    private EditText loginMail, loginPassword;
    private Button buttonLogin;

    public FragmentLogin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginMail = view.findViewById(R.id.loginMail);
        loginPassword = view.findViewById(R.id.loginPassword);
        buttonLogin = view.findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(Constantes.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                Api service = retrofit.create(Api.class);
                Call<DefaultResponse> call = service.Login(
                          loginMail.getText().toString()
                        , loginPassword.getText().toString());
                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if(!response.body().isErr()){
                            User user = response.body().getUser();
                            SessionManager.getInstance(getActivity().getApplicationContext()).saveUser(user);
                            Intent intent = new Intent(getActivity(), MainActivity.class);
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
        return  view;
    }
}
