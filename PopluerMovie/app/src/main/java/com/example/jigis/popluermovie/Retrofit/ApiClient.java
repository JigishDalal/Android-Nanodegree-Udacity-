package com.example.jigis.popluermovie.Retrofit;

import com.example.jigis.popluermovie.ApiCall.ApiCall;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public  static Retrofit retrofit= null;

    public static Retrofit getRetrofit()

    {
        if (retrofit==null)
        {
            //time out connection
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
            //calling base ur
             retrofit = new Retrofit.Builder().client(okHttpClient)
                    .baseUrl(ApiCall.API_URL)
            .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
