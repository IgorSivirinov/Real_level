package com.example.changelevel.API;

import com.example.changelevel.models.DefaultResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("register.php")
    Call<DefaultResponse> Register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<DefaultResponse> Login(
            @Field("email") String email,
            @Field("password") String password
    );
}
