package com.example.jigis.moviestage2.ApiCall;

import com.example.jigis.moviestage2.Model.CastModel;
import com.example.jigis.moviestage2.Model.CastResponse;
import com.example.jigis.moviestage2.Model.MovieResponse;
import com.example.jigis.moviestage2.Model.ReviewsResponse;
import com.example.jigis.moviestage2.Model.VideoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    //popular movie call
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key")String apiKey, @Query("language") String language,
                                         @Query("page") int pageIndex);
    //top rated movie
    @GET("movie/top_rated")
    Call<MovieResponse> getTopRated(@Query("api_key")String apiKey);
    //Now movies
    @GET("movie/now_playing")
    Call<MovieResponse> getNowMovie(@Query("api_key")String apiKey,@Query("page") int pageIndex);
    //review
    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getReview(@Path("id") String id, @Query("api_key")String apiKey);
    //more video

    @GET("movie/{id}/videos")
    Call<VideoResponse> getVideo(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("movie/{id}/casts")
    Call<CastResponse> getCastChar(@Path("id") String id, @Query("api_key") String apiKey);




}
