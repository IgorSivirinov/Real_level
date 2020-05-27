package com.example.changelevel.API.Firebase.Firestor.ClientObjects;

import com.example.changelevel.API.Firebase.Firestor.UserFS;

public class User extends UserFS {
    public static final String APP_PREFERENCES_USER = "User";
    private String email;
    private String idUser;



    public User(String idUser, String email, UserFS userFS) {
        super(userFS.getUserAvatar(),userFS.getName(), userFS.getXp(),
                userFS.isAdmin(), userFS.isWriter(),
                userFS.getTasksCompleted(), userFS.getCreatedTasks());
        this.email = email;
        this.idUser = idUser;
    }

    public String getIdUser() {
        return idUser;
    }
    public String getEmail() {
        return email;
    }
}
