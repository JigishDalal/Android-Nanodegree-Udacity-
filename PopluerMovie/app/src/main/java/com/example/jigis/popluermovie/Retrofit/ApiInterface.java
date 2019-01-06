package com.example.jigis.popluermovie.Retrofit;

import com.example.jigis.popluermovie.Model.MovieResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

        //popular movie call
        @GET("movie/popular")
        Call<MovieResponse> getPopularMovies(@Query("api_key")String apiKey);
        //top rated movie
        @GET("movie/top_rated")
        Call<MovieResponse> getTopRated(@Query("api_key")String apiKey);

}
