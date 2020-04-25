package com.example.changelevel.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.changelevel.models.DataModels.DataModelTask;

import java.util.List;

@Dao
public interface DaoModel {
    @Query("SELECT * FROM DataModelTask")
    List<DataModelTask> getAll();

    @Insert
    void insert(DataModelTask task);

    @Delete
    void delete(DataModelTask task);

    @Update
    void update(DataModelTask task);
}
