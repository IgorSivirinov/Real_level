package com.example.changelevel.models.DataModels;

public class DataModelTask {

    private String id;

    private String name;
    private String overview;
    private String type;

    private int xp;
    private int minLevel;

    private boolean completed;
    private boolean blocked;

    public DataModelTask(String id, String name, String overview, String type, int xp, int minLevel, boolean completed, boolean blocked) {
        this.id = id;
        this.name = name;
        this.overview = overview;
        this.type = type;
        this.xp = xp;
        this.minLevel = minLevel;
        this.completed = completed;
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

    public boolean isCompleted() {
        return completed;
    }

    public boolean isBlocked() {
        return blocked;
    }
}
