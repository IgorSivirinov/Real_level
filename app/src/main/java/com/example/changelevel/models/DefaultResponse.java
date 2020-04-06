package com.example.changelevel.models;

import com.example.changelevel.User.User;
import com.google.gson.annotations.SerializedName;

public class DefaultResponse
{
    @SerializedName("error")
    private boolean err;

    @SerializedName("uid")
    private String uid;

    @SerializedName("user")
    private User user;

    public DefaultResponse(boolean err, String uid,User user) {
        this.err = err;
        this.uid = uid;
        this.user=user;
    }

    public boolean isErr() {
        return err;
    }

    public String getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }
}
