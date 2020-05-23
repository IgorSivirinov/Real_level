package com.example.changelevel.API.Firebase.Firestor;

public class TaskFS{
    private String taskName;
    private String taskOverview;
    private String taskType;
    private long taskXP;
    private long minLevel;

    public TaskFS() {}

    public TaskFS(String taskName, String taskOverview, String taskType, long taskXP, long minLevel) {
        this.taskName = taskName;
        this.taskOverview = taskOverview;
        this.taskType = taskType;
        this.taskXP = taskXP;
        this.minLevel = minLevel;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskOverview() {
        return taskOverview;
    }

    public String getTaskType() {
        return taskType;
    }

    public long getTaskXP() {
        return taskXP;
    }

    public long getMinLevel() {
        return minLevel;
    }
}
