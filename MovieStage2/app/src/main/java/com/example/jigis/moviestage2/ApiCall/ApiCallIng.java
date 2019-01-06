package com.example.jigis.moviestage2.ApiCall;

import com.example.jigis.moviestage2.BuildConfig;

public class ApiCallIng {

    static public String API_URL = "http://api.themoviedb.org/3/";
   static public String API_KEY = BuildConfig.THE_GUARDIAN_API_KEY;
    static public String IMAGE_URL = "http://image.tmdb.org/t/p/";
    static public String IMAGE_SIZE_185 = "w185";
    static public String BACK_POSTER_IMAGE_SIZE_500="w500";
    public static  String VIDEOS_PATH = "https://www.youtube.com/watch?v=";
    public static  String VIDEO_THUMBNAIL = "https://img.youtube.com/vi";
    // Get the custom thumbnail in 320 x 180 small image resolution then "/mqdefault.jpg"
    // Get the custom thumbnail in 480 x 360 standard image resolution "/0.jpg"
    // Get the custom thumbnail in 720p or 1080p HD image resolution "/maxresdefault.jpg"
    public static  String VIDEO_THUMBNAIL_RESOLUTION = "mqdefault.jpg";

}
