package com.example.changelevel.LoginAndRegistration;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.changelevel.API.Api;
import com.example.changelevel.API.Constantes;
import com.example.changelevel.MainActivity;
import com.example.changelevel.R;
import com.example.changelevel.User.User;
import com.example.changelevel.models.DefaultResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRegistration extends Fragment {
    private static final int REQUEST=1;
    public static final String ACCESS_MESSAGE="message";
    private Button buttonRegistration;
    private EditText registrationMail, registrationUserName, registrationPassword, registrationRepeatPassword;

    public FragmentRegistration() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        registrationMail = view.findViewById(R.id.registrationMail);
        registrationUserName = view.findViewById(R.id.registrationUserName);
        registrationPassword = view.findViewById(R.id.registrationPassword);
        registrationRepeatPassword = view.findViewById(R.id.registrationRepeatPassword);
        buttonRegistration = view.findViewById(R.id.buttonRegistration);
        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registrationPassword.getText().toString().equals(registrationRepeatPassword.getText().toString())){
                    Retrofit retrofit=new Retrofit.Builder().baseUrl(Constantes.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                    Api service= retrofit.create(Api.class);
                    Call<DefaultResponse> call=service.Register(registrationUserName.getText().toString(),
                            registrationMail.getText().toString(),
                            registrationPassword.getText().toString());
                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            if(!response.body().isErr())
                            {
                                User user = response.body().getUser();
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

            }
        });
        return view;
    }

}
