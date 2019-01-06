package com.example.jigis.moviestage2.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.jigis.moviestage2.DataBase.MovieDatabase;
import com.example.jigis.moviestage2.DataBase.MovieModelDatabase;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    LiveData<List<MovieModelDatabase>> moviedatabase;


    public MovieViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase movieDatabase=MovieDatabase.getInstance(this.getApplication());
        moviedatabase=movieDatabase.moviesDao().getAllList();
    }

    public LiveData<List<MovieModelDatabase>> getMoviedatabase() {
        return moviedatabase;
    }
}
