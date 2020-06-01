package com.example.changelevel.API.Firebase.Firestor;

import com.google.firebase.Timestamp;


public class TaskCompletedTapeFS {

    private TaskFS task;
    private UserFS user;
    private String comment;
    private Timestamp time;

    public TaskCompletedTapeFS() { }

    public TaskCompletedTapeFS(TaskFS task, UserFS user, String comment, Timestamp time) {
        this.task = task;
        this.user = user;
        this.comment = comment;
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public TaskFS getTask() {
        return task;
    }

    public UserFS getUser() {
        return user;
    }

    public Timestamp getTime() {
        return time;
    }
}
