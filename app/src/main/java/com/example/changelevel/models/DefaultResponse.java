package com.example.changelevel.models;

import com.google.gson.annotations.SerializedName;

public class DefaultResponse
{
    @SerializedName("error")
    private boolean err;

    @SerializedName("uid")
    private String uid;

    public DefaultResponse(boolean err, String uid) {
        this.err = err;
        this.uid = uid;
    }

    public boolean isErr() {
        return err;
    }

    public String getUid() {
        return uid;
    }
}
