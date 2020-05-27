package com.example.changelevel.API.Firebase.Firestor;

import java.util.ArrayList;

public class UserFS {
    private String userAvatar;
    private String name;
    private long xp;
    private boolean isAdmin = false;
    private boolean isWriter = false;
    private ArrayList<String> tasksCompleted;
    private ArrayList<String> createdTasks;

    public UserFS() {}

    public UserFS(String userAvatar, String name, long xp, boolean isAdmin, boolean isWriter,
                  ArrayList<String> tasksCompleted, ArrayList<String> createdTasks) {
        this.userAvatar = userAvatar;
        this.name = name;
        this.xp = xp;
        this.isAdmin = isAdmin;
        this.isWriter = isWriter;
        this.tasksCompleted = tasksCompleted;
        this.createdTasks = createdTasks;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getXp() {
        return xp;
    }

    public void setXp(long xp) {
        this.xp = xp;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isWriter() {
        return isWriter;
    }

    public void setWriter(boolean writer) {
        isWriter = writer;
    }

    public ArrayList<String> getTasksCompleted() {
        return tasksCompleted;
    }
    public void addTaskCompleted(String tasksCompleted) {
        this.tasksCompleted.add(tasksCompleted);
    }

    public ArrayList<String> getCreatedTasks() {
        return createdTasks;
    }

    public void addCreatedTasks(String createdTask){
        this.createdTasks.add(createdTask);
    }

    public void setCreatedTasks(ArrayList<String> createdTasks) {
        this.createdTasks = createdTasks;
    }


    public void setTasksCompleted(ArrayList<String> tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    public int checkLevel(){
        if (this.xp<=20) return 0;
        if (this.xp<=100) return 1;
        if (this.xp<=200) return 2;
        if (this.xp<=500) return 3;
        if (this.xp<=1000) return 4;
        if (this.xp<=1800) return 5;
        return 6;
    }

    public int getMaxXp(int level){
        switch (level){
            case 0: return 20;
            case 1: return 100;
            case 2: return 200;
            case 3: return 500;
            case 4: return 1000;
            case 5: return 1800;
        }
        return 3000;
    }
}
