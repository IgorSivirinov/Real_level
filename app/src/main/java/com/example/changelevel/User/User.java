package com.example.changelevel.User;

import com.example.changelevel.Task;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String mail;
    private String password;
    private String userName;

    private ArrayList<Task> tasks = new <Task> ArrayList();
    private int sportLevel;
    private int mindLevel;
    private int creativityLevel;
    public User(String mail, String userName, String password){
        this.mail=mail;
        this.userName=userName;
        this.password=password;

    }

    public User(String mail, String userName) {
        this.mail = mail;
        this.userName = userName;
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
