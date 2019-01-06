package com.example.jigis.cookingapp.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jigis.cookingapp.Adapter.HomeAdapter;
import com.example.jigis.cookingapp.MainActivity;
import com.example.jigis.cookingapp.Model.Recipes;
import com.example.jigis.cookingapp.Network.ApiClient;
import com.example.jigis.cookingapp.Network.ApiInterface;
import com.example.jigis.cookingapp.R;
import com.example.jigis.cookingapp.RecipeDetailsActivity;
import com.example.jigis.cookingapp.Utils.InternetConnection;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeAdapter.RecipesItemOnClickListener {

    @BindView(R.id.ry_home_recipe)
    RecyclerView ryRecipeList;
    HomeAdapter homeAdapter;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    List<Recipes> recipesList ;
    ApiInterface apiInterface;



    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String itemnumber = "itemKey";
    public static final String ingredientsJson="ingredient";
    public static final String stepsJson="stepsJson";
    //RecipeList Instance
    private static final String RecipelistInstance = "RecipeListInstance";

    CoordinatorLayout coordinatorLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.coordinatorLayout);



        if(savedInstanceState!=null)
        {
            recipesList= savedInstanceState.getParcelableArrayList(RecipelistInstance);
        }

        //network connection checking
        if(InternetConnection.checkConnection(getActivity()))
        {
            LoadRecipeData();
        }else {

            InternetConnectionError();
//            Toast.makeText(getActivity(), "Check the Internet Connection.", Toast.LENGTH_SHORT).show();
        }
        // Inflate the layout for this fragment
        return view;
    }

    private void InternetConnectionError() {

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent= new Intent(getContext(),MainActivity.class);
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                    }
                });

// Changing message text color
        snackbar.setActionTextColor(Color.RED);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();

    }

    //load  Recipe list
    private void LoadRecipeData() {
        progressBar.setVisibility(View.VISIBLE);
        apiInterface=ApiClient.getRetrofit().create(ApiInterface.class);
            Call<List<Recipes>> recipeCall=apiInterface.getRecipe();
            Log.e("url",""+recipeCall.request().url().toString());

            try {
            recipeCall.enqueue(new Callback<List<Recipes>>() {
                @Override
                public void onResponse(Call<List<Recipes>> call, Response<List<Recipes>> response) {
                    recipesList=response.body();
                    progressBar.setVisibility(View.GONE);
                    Log.v("checkResponseProfile",response.body().toString()+"code"+response.code());
                   recipeDataview(recipesList);
                }

                @Override
                public void onFailure(Call<List<Recipes>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("checkResponseProfile",""+call.request().url().toString());
                }
            });
        }catch (Exception e){

        }

    }

    private void recipeDataview(List<Recipes> recipesList) {
        ryRecipeList.setHasFixedSize(true);
        ryRecipeList.setLayoutManager(new GridLayoutManager(getActivity(),2));
        homeAdapter = new HomeAdapter(getContext(), recipesList,this);
        ryRecipeList.setAdapter(homeAdapter);
    }

    // On item Click  object
    @Override
    public void onItemClick(int clickedPosition) {
        Intent intentToStartRecipeInfoActivity = new Intent(getActivity(), RecipeDetailsActivity.class);
        //pass recipedata
        intentToStartRecipeInfoActivity.putExtra("RecipeData", recipesList.get(clickedPosition));
        // pass RecipeIngredients List array list
        intentToStartRecipeInfoActivity.putParcelableArrayListExtra("RecipeIngredientList", (ArrayList<? extends Parcelable>) recipesList.get(clickedPosition).getIngredients());
        //pass steps List
        intentToStartRecipeInfoActivity.putParcelableArrayListExtra("RecipeSteps",(ArrayList<? extends Parcelable>) recipesList.get(clickedPosition).getSteps());
        Log.e( "onItemClick: ","position "+recipesList.get(clickedPosition).getId());


       SharedPreferences sharedPreferences = getContext().getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String ingredientJson = gson.toJson(recipesList.get(clickedPosition).getIngredients());
        String stepJson = gson.toJson(recipesList.get(clickedPosition).getSteps());
        intentToStartRecipeInfoActivity.putExtra(ingredientsJson, ingredientJson);
        intentToStartRecipeInfoActivity.putExtra(stepsJson, stepJson);
        String resultJson = gson.toJson(recipesList.get(clickedPosition));
        sharedPreferences.edit().putString(itemnumber, resultJson).apply();


        startActivity(intentToStartRecipeInfoActivity);
    }


    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(RecipelistInstance, (ArrayList<? extends Parcelable>) recipesList);
    }


}
