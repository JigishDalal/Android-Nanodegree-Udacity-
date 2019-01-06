package com.example.jigis.moviestage2.DataBase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.content.Intent;


import java.util.List;

@Dao
public interface MovieDao {

    //item list
    @Query("SELECT * FROM  moviesTable")
    LiveData<List<MovieModelDatabase>> getAllList();

    //delete item
    @Delete
    void delete(MovieModelDatabase moviedatabase);

    //insert item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieModelDatabase moviedatabase);

    //if item selected then
    @Query("SELECT movieId FROM moviesTable where movieId = :id")
    Integer getID(Integer id);

}
