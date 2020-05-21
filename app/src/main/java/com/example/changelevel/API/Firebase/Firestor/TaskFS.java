package com.example.changelevel.API.Firebase.Firestor;

public class TaskFS {
    private String taskName;
    private String taskOverview;
    private TaskType taskType;
    private long taskXP;

    public static class TaskType{
        private String name;
        private long minRank;

        TaskType(){}

        public TaskType(String name, long minRank) {
            this.name = name;
            this.minRank = minRank;
        }

        public String getName() {
            return name;
        }

        public long getMinRank() {
            return minRank;
        }

    }

    public TaskFS(){}

    public TaskFS(String taskName, String taskOverview, TaskType taskType, long taskXP) {
        this.taskName = taskName;
        this.taskOverview = taskOverview;
        this.taskType = taskType;
        this.taskXP = taskXP;
    }


    public String getTaskName() {
        return taskName;
    }

    public String getTaskOverview() {
        return taskOverview;
    }


    public TaskType getTaskType() {
        return taskType;
    }

    public long getTaskXP() {
        return taskXP;
    }

}
