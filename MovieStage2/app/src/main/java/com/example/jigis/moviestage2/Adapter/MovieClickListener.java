package com.example.jigis.moviestage2.Adapter;

import android.widget.ImageView;

import com.example.jigis.moviestage2.Model.Movie;

public interface MovieClickListener {
    void onMoviePosterClick(Movie movieModel, ImageView imageView, String transitionName);

}
