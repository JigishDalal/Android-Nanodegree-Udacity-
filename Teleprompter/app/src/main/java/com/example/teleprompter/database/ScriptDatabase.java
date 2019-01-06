package com.example.teleprompter.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.teleprompter.database.dao.ScriptDAO;
import com.example.teleprompter.database.entity.Script;

@Database(entities = {Script.class}, version = 1, exportSchema = false)
public abstract class ScriptDatabase extends RoomDatabase {

    public abstract ScriptDAO mScriptDAO();

    private static ScriptDatabase INSTANCE;

    public static ScriptDatabase getScriptDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (ScriptDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ScriptDatabase.class, "script_database").build();
            }
        }
        return INSTANCE;
    }
}
