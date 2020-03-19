package com.example.changelevel;

public class Task {
    private String overview;
    private String iconView;

    private boolean completed = false;

    private byte complexity;

    private int sportXP;
    private int mindXP;
    private int creativityXP;


    private void setTasks(String overview, String iconView, byte complexity, int sportXP, int mindXP, int creativityXP){
        this.overview = overview;
        this.iconView = iconView;
        this.complexity = complexity;
        this.sportXP = sportXP;
        this.mindXP = mindXP;
        this.creativityXP = creativityXP;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getOverview() {
        return overview;
    }

    public String getIconView() {
        return iconView;
    }

    public byte getComplexity() {
        return complexity;
    }

    public int getSportXP() {
        return sportXP;
    }

    public int getMindXP() {
        return mindXP;
    }

    public int getCreativityXP() {
        return creativityXP;
    }
}
