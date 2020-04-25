package com.example.changelevel.models.DataModels;

public class DataModelListSettings {

    private int image;
    private String name;
    private int id;

    public DataModelListSettings(int id, String name, int image){
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
