package com.example.changelevel.models.DataModels;

public class DataModelTask {

    private String name;
    private int xp;


    public DataModelTask(String name, int xp){
        this.name = name;
        this.xp = xp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public String getName() {
        return name;
    }

    public int getXp() {
        return xp;
    }
}
