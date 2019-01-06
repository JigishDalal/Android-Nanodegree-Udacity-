package com.example.jigis.cookingapp.Fragment;


import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jigis.cookingapp.Adapter.IngredientAdapter;
import com.example.jigis.cookingapp.Adapter.StepsAdapter;
import com.example.jigis.cookingapp.MainActivity;
import com.example.jigis.cookingapp.Model.Ingredient;
import com.example.jigis.cookingapp.Model.Recipes;
import com.example.jigis.cookingapp.Model.Steps;
import com.example.jigis.cookingapp.R;
import com.example.jigis.cookingapp.RecipeDetailsActivity;
import com.example.jigis.cookingapp.StepActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.jigis.cookingapp.RecipeDetailsActivity.mTwoPane;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsFragment extends Fragment implements StepsAdapter.stepsItemOnClickListener {

    int mRecipeID;
    @BindView(R.id.recycler_ingredient)
    RecyclerView recyclerViewIngredient;
    IngredientAdapter ingredientAdapter;

    @BindView(R.id.recyclerSteps)
    RecyclerView recyclerSteps;
    StepsAdapter stepsAdapter;
    String recipesname;
    List<Ingredient> ingredientList;
    List<Steps> stepsList;
    Recipes recipesModel;

    private SendStepData stepInterface;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_recipe_name)
    TextView  tvRecipeName;

    @BindView(R.id.tv_title)
    TextView tvTitle;



    private final String TAG = MainActivity.class.getSimpleName();


    public RecipeDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate MainActivity layout and bind ButterKnife using rootView
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, rootView);
        stepInterface = (SendStepData) getActivity();


        //back Button in toolbar BAck button and color code change
        final Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        backArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(backArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        //Ingredients List
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewIngredient.setLayoutManager(layoutManager);
        recyclerViewIngredient.setHasFixedSize(true);

        //steps
        RecyclerView.LayoutManager layoutManagerSteps = new LinearLayoutManager(getContext());
        recyclerSteps.setLayoutManager(layoutManagerSteps);
        recyclerSteps.setHasFixedSize(true);


        if (getArguments() != null) {
            //get RecipeId,recipename from RecipeDetails Activity
            mRecipeID = getArguments().getInt("recipe_id");
            recipesname=getArguments().getString("recipename");

            //get The recipeModel From RecipeDetails Activity
            recipesModel =getArguments().getParcelable("recipemodel");


            //title name and recipe name
            toolbar.setTitle(recipesname.toString());
            tvRecipeName.setText(recipesname.toString());

            //get the ingredientList data from RecipeDetails Activity
            ingredientList = getArguments().getParcelableArrayList("RecipeIngredientList");
            ingredientAdapter = new IngredientAdapter(getActivity(), ingredientList);
            recyclerViewIngredient.setAdapter(ingredientAdapter);
            ingredientAdapter.notifyDataSetChanged();

            //get steps data
            stepsList = getArguments().getParcelableArrayList("steplist");
            stepsAdapter = new StepsAdapter(getActivity(), stepsList, this);
            recyclerSteps.setAdapter(stepsAdapter);
            stepsAdapter.notifyDataSetChanged();

        } else {
            Log.v(TAG, "Error Receiving bundle from RecipeInfoActivity. Bundle may be null.");
        }
        return rootView;

    }


    @Override
    public void onItemClick(Steps steps) {
        if(mTwoPane == false) {
//  phone layout pass data to the step Activity
            Intent stepActivity = new Intent(getContext(), StepActivity.class);
            stepActivity.putExtra("steps", steps);
            stepActivity.putParcelableArrayListExtra("steplist", (ArrayList<? extends Parcelable>) stepsList);
            stepActivity.putExtra("recipemodel", recipesModel);
            startActivity(stepActivity);
        }else {
//tablet layout
            //pass the data to steps RecipeDetailsActivity
            Log.e("tagname steps ",""+steps);
            stepInterface.sendSteps(steps);

        }
    }
    public interface SendStepData {
        void sendSteps(Steps step);
    }
}
