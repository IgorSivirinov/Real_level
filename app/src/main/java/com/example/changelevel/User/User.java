package com.example.changelevel.User;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("email")
    private String mail;

    private String password="";

    @SerializedName("name")
    private String userName;

    private int sportLevel=0;
    private int mindLevel=0;
    private int creativityLevel=0;

    public User(String mail, String userName, String password){
        this.mail=mail;
        this.userName=userName;
        this.password=password;

    }

    public User(String name,String email) {
        this.mail = email;
        this.userName = name;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public int getSportLevel() {
        return sportLevel;
    }

    public int getMindLevel() {
        return mindLevel;
    }

    public int getCreativityLevel() {
        return creativityLevel;
    }
}
