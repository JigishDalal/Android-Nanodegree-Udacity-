package com.example.jigis.cookingapp.Network;

import com.example.jigis.cookingapp.Model.Recipes;
import com.example.jigis.cookingapp.Webservies.WebservicesAPI;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ApiInterface {
    @GET("topher/2017/May/59121517_baking/baking.json")
   Call<List<Recipes>> getRecipe();
}
