package com.example.changelevel.API.Firebase.Firestor;

import java.util.ArrayList;
import java.util.List;

public class UserFS {
    private String userAvatar;
    private String name;
    private long xp;
    private boolean isAdmin = false;
    private ArrayList<String> tasksCompleted;

    public UserFS() {}

    public UserFS(String userAvatar, String name, long xp, boolean isAdmin, ArrayList<String> tasksCompleted) {
        this.userAvatar = userAvatar;
        this.name = name;
        this.xp = xp;
        this.isAdmin = isAdmin;
        this.tasksCompleted = tasksCompleted;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void addTaskCompleted(String tasksCompleted) {
        this.tasksCompleted.add(tasksCompleted);
    }

    public String getName() {
        return name;
    }

    public long getXp() {
        return xp;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public ArrayList<String> getTasksCompleted() {
        return tasksCompleted;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setXp(long xp) {
        this.xp = xp;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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
