package com.example.changelevel.API.Firebase.Firestor;

public class TaskCompletedTapeFS {

    private String taskName;
    private String userComment;
    private long taskXP;
    private UserFS user;

    public TaskCompletedTapeFS() { }

    public TaskCompletedTapeFS(String taskName, String userComment, long taskXP) {
        this.taskName = taskName;
        this.userComment = userComment;
        this.taskXP = taskXP;
    }
    public String getTaskName() {
        return taskName;
    }

    public String getUserComment() {
        return userComment;
    }

    public long getTaskXP() {
        return taskXP;
    }

    public UserFS getUser() {
        return user;
    }


}
