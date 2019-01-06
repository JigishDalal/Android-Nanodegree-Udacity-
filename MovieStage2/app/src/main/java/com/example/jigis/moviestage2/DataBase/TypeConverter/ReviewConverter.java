package com.example.jigis.moviestage2.DataBase.TypeConverter;

import android.arch.persistence.room.TypeConverter;

import com.example.jigis.moviestage2.Model.Reviews;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ReviewConverter {
    @TypeConverter
    public static String encodeGenre(List<Reviews> reviewModelList) {

        Gson gson = new Gson();
        return gson.toJson(reviewModelList);
    }

    @TypeConverter
    public static List<Reviews> decodeGenre(String value) {

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Reviews>>() {
        }.getType();
        return gson.fromJson(value, listType);
    }
}
