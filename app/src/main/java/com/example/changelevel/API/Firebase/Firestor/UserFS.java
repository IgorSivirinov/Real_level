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
        if (this.xp<=3000) return 6;
        if (this.xp<=6000) return 7;
        if (this.xp<=10000) return 8;
        if (this.xp<=15000) return 9;
        if (this.xp<=20000) return 10;
        if (this.xp<=35000) return 11;
        if (this.xp<=70000) return 12;
        if (this.xp<=100000) return 13;
        if (this.xp<=150000) return 14;
        if (this.xp<=200000) return 15;
        if (this.xp<=400000) return 16;
        if (this.xp<=800000) return 17;
        if (this.xp<=1400000) return 18;
        if (this.xp<=2000000) return 19;
        return 20;
    }

    public int getMaxXp(int level){
        switch (level){
            case 0: return 20;
            case 1: return 100;
            case 2: return 200;
            case 3: return 500;
            case 4: return 1000;
            case 5: return 1800;
            case 6: return 3000;
            case 7: return 6000;
            case 8: return 10000;
            case 9: return 15000;
            case 10: return 20000;
            case 11: return 35000;
            case 12: return 70000;
            case 13: return 100000;
            case 14: return 150000;
            case 15: return 200000;
            case 16: return 400000;
            case 17: return 800000;
            case 18: return 1400000;
            case 19: return 2000000;
            case 20: return 3000000;
        }
        return 3000;
    }
}
