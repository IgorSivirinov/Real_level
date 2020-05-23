package com.example.changelevel.models.DataModels;

public class DataModelTask {

    private String id;

    private String name;
    private String overview;
    private String type;
    private int xp;

    private boolean completed;

    public DataModelTask(String id, String name, String overview, String type, int xp, boolean completed) {
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.type = type;
        this.xp = xp;
        this.completed = completed;
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

    public boolean isCompleted() {
        return completed;
    }
}
