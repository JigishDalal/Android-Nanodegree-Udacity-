package com.example.teleprompter.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.teleprompter.database.entity.Script;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ScriptDAO {

    @Insert(onConflict = REPLACE)
    void insert(Script script);

    @Query("DELETE FROM script")
    void deleteAll();

    @Query("DELETE FROM script WHERE id = :id")
    void deleteScript(int id);

    @Query("SELECT * FROM script ORDER BY date_created_milliseconds ASC")
    LiveData<List<Script>> getAllScripts();

    @Query("SELECT * FROM script WHERE id = :id")
    Script getScript(int id);
}
