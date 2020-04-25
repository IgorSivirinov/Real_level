package com.example.changelevel.models.DataModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class DataModelTask implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name = "";

    @ColumnInfo(name = "overview")
    private String overview = "";

    @ColumnInfo(name = "sportXP")
    private int sportXP=0;

    @ColumnInfo(name = "mindXP")
    private int mindXP=0;

    @ColumnInfo(name = "creativityXP")
    private int creativityXP=0;

    public DataModelTask(String name, String overview, int sportXP, int mindXP, int creativityXP){
        this.name = name;
        this.overview = overview;
        this.sportXP = sportXP;
        this.mindXP = mindXP;
        this.creativityXP = creativityXP;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getSportXP() {
        return sportXP;
    }
    public void setSportXP(int sportXP) {
        this.sportXP = sportXP;
    }

    public int getMindXP() {
        return mindXP;
    }
    public void setMindXP(int mindXP) {
        this.mindXP = mindXP;
    }

    public int getCreativityXP() {
        return creativityXP;
    }
    public void setCreativityXP(int creativityXP) {
        this.creativityXP = creativityXP;
    }
}
