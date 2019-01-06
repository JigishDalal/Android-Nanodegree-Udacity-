package com.example.jigis.moviestage2.DataBase.TypeConverter;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import com.example.jigis.moviestage2.Model.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class VideoConverter {
    @TypeConverter
    public static String encodeGenre(List<Video> videoModelList) {

        Gson gson = new Gson();
        return gson.toJson(videoModelList);
    }

    @TypeConverter
    public static List<Video> decodeGenre(String value) {

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Video>>() {
        }.getType();
        Log.e( "decodeGenre: ",""+listType.toString() );
        return gson.fromJson(value, listType);
    }
}
