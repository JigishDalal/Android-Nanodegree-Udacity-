package com.example.jigis.cookingapp;

import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.jigis.cookingapp.Fragment.RecipeDetailsFragment;
import com.example.jigis.cookingapp.Fragment.StepsFragment;
import com.example.jigis.cookingapp.Model.Ingredient;
import com.example.jigis.cookingapp.Model.Recipes;
import com.example.jigis.cookingapp.Model.Steps;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailsFragment.SendStepData  {

    int mRecipeID;
    Recipes selectedRecipes;
    List<Ingredient> ingredientList;
    List<Steps> stepsList;
    String recipename;
    public static boolean mTwoPane;


    StepsFragment stepsFragment;
    Bundle bundle = new Bundle();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        if (savedInstanceState == null) {

            //create FragmentManager and new reference to Fragment.
            FragmentManager fragmentManager = getSupportFragmentManager();
            RecipeDetailsFragment RecipeDetailsFragment = new RecipeDetailsFragment();


            // Get the data from activity
            //TODO: Pass recipeID to Fragment.
            //get selected recipe data from MainActivity
            selectedRecipes = getIntent().getParcelableExtra("RecipeData");
            //set recipe id ,name
            mRecipeID = selectedRecipes.getId();
            recipename = selectedRecipes.getName();
            //pass data in  RecipeDetailsfragment name , Recipe id
            bundle.putInt("recipe_id", mRecipeID);
            bundle.putString("recipename", recipename);


            //get selected ingredientlist data from MainActivity
            ingredientList = getIntent().getExtras().getParcelableArrayList("RecipeIngredientList");
//        pass Data in RecipeDetailsFragment ingredientlist
            bundle.putParcelableArrayList("RecipeIngredientList", (ArrayList<? extends Parcelable>) ingredientList);


            //get steps data list from MainActivity
            stepsList = getIntent().getExtras().getParcelableArrayList("RecipeSteps");
            //pass steps data
            bundle.putParcelableArrayList("steplist", (ArrayList<? extends Parcelable>) stepsList);
            //set bundle as argument on fragment obj
            RecipeDetailsFragment.setArguments(bundle);

            //pass recipe data to RecipDetailsFragment
            bundle.putParcelable("recipemodel", selectedRecipes);

            //phone layout load RecipeDetailsFragment
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_recipe_info, RecipeDetailsFragment)
                    .commit();

            if (findViewById(R.id.player_fragment) != null) {
                //tablet layout
                mTwoPane = true;
                Steps step = getIntent().getParcelableExtra("steps");

                bundle.putParcelable("steps", stepsList.get(0));

                //Create a new StepDetailFragment
                stepsFragment = new StepsFragment();
                //Insert the bundle as an argument to the above Fragment
                stepsFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.player_fragment, stepsFragment)
                        .commit();

            } else {
//                phone layout
                mTwoPane = false;

            }
        }
        else{

        }

    }

//    Its crate interFaceMethod to exchange data betweeen the Fragment two RecipDetails and stepsFragment

    @Override
    public void sendSteps(Steps step) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        stepsFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.player_fragment, stepsFragment)
                .commit();

        stepsFragment.receiveStepInterface(this, step);

    }




}
