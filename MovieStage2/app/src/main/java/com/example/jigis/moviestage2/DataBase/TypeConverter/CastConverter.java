package com.example.jigis.moviestage2.DataBase.TypeConverter;

import android.arch.persistence.room.TypeConverter;

import com.example.jigis.moviestage2.Model.CastModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CastConverter {

    @TypeConverter
    public static String encodeGenre(List<CastModel> castModelList) {

        Gson gson = new Gson();
        return gson.toJson(castModelList);
    }

    @TypeConverter
    public static List<CastModel> decodeGenre(String value) {

        Gson gson = new Gson();
        Type listType = new TypeToken<List<CastModel>>() {
        }.getType();
        return gson.fromJson(value, listType);
    }

}