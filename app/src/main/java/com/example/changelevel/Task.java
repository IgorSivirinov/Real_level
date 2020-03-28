package com.example.changelevel;

public class Task {
    private String overview="";

    private boolean performed = false;
    private boolean completed = false;

    private int sportXP=0;
    private int mindXP=0;
    private int creativityXP=0;
    Task(String overview, int sportXP,int mindXP, int creativityXP){
        this.overview = overview;
        this.sportXP = sportXP;
        this.mindXP = mindXP;
        this.creativityXP = creativityXP;

    }
    public void setPerformed(boolean performed) {
        this.performed = performed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getOverview() {
        return overview;
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
    public boolean isPerformed() {
        return performed;
    }

    public boolean isCompleted() {
        return completed;
    }
}
