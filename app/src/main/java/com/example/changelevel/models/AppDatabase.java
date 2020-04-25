package com.example.changelevel.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.changelevel.models.DataModels.DataModelTask;

@Database(entities = {DataModelTask.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoModel ModelDao();
}
