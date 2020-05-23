package com.example.changelevel.API.Firebase.Firestor.ClientObjects;

import com.example.changelevel.API.Firebase.Firestor.UserFS;

public class User extends UserFS{
    public static final String APP_PREFERENCES_USER = "User";
    private String email;

    public User(String email, UserFS userFS) {
        super(userFS.getUserAvatar(),userFS.getName(), userFS.getXp(),userFS.isAdmin(), userFS.getTasksCompleted());
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
