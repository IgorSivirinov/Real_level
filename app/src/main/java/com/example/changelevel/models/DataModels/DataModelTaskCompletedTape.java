package com.example.changelevel.models.DataModels;

import com.example.changelevel.API.Firebase.Firestor.TaskCompletedTapeFS;

public class DataModelTaskCompletedTape extends TaskCompletedTapeFS {
    private String id;


    public DataModelTaskCompletedTape(String id, TaskCompletedTapeFS taskCompletedTapeFS) {
        super(taskCompletedTapeFS.getTask(), taskCompletedTapeFS.getUser(), taskCompletedTapeFS.getComment(),taskCompletedTapeFS.getTime());
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
