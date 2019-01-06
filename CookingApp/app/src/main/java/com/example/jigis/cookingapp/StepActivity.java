package com.example.jigis.cookingapp;

import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.jigis.cookingapp.Fragment.StepsFragment;
import com.example.jigis.cookingapp.Model.Ingredient;
import com.example.jigis.cookingapp.Model.Recipes;
import com.example.jigis.cookingapp.Model.Steps;

import java.util.ArrayList;
import java.util.List;

public class StepActivity extends AppCompatActivity {
    List<Steps> stepsList;
    Recipes recipesModel;
    private final Bundle argsToPass = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        if (savedInstanceState == null) {
//            get the data RecipeDetails Fragment
            Steps step = getIntent().getParcelableExtra("steps");
             recipesModel = getIntent().getParcelableExtra("recipemodel");
             stepsList=getIntent().getParcelableArrayListExtra("steplist");
//         pass  data to stepFragment
            argsToPass.putParcelable("steps", step);
            argsToPass.putParcelableArrayList("steplist", (ArrayList<? extends Parcelable>) stepsList);
            argsToPass.putParcelable("recipemodel",recipesModel );

            //Create a new StepsFragment
            StepsFragment stepsFragment = new StepsFragment();
            //Insert the bundle as an argument to the above Fragment
            stepsFragment.setArguments(argsToPass);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_recipe_info_steps, stepsFragment)
                    .commit();
        }

    }
}
