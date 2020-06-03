package com.example.changelevel.models.DataModels;

import java.util.ArrayList;

public class DataModelTask {

    private String id;

    private String name;
    private String overview;
    private String type;

    private int xp;
    private int minLevel;

    private ArrayList<String> tasksCompleted;

    private boolean blocked;

    public DataModelTask(String id, String name, String overview, String type, int xp, int minLevel, ArrayList<String> tasksCompleted, boolean blocked) {
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.type = type;
        this.xp = xp;
        this.minLevel = minLevel;
        this.tasksCompleted = tasksCompleted;
        this.blocked = blocked;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getType() {
        return type;
    }

    public int getXp() {
        return xp;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public ArrayList<String> getTasksCompleted() {
        return tasksCompleted;
    }

    public boolean isBlocked() {
        return blocked;
    }


}
