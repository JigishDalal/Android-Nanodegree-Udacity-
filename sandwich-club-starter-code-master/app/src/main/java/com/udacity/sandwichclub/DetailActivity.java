package com.udacity.sandwichclub;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.also_known_tv)
    TextView alsoKnownAsTextView;
    @BindView(R.id.origin_tv)
    TextView originTextView;
    @BindView(R.id.description_tv)
    TextView descriptionTextView;
    @BindView(R.id.ingredients_tv)
    TextView ingredientsTextView;
    @BindView(R.id.image_iv)
    ImageView ingredientsIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = findViewById(R.id.main_toolbar);
        ButterKnife.bind(this);
        //backPress arrow
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getDataFromIntent();

    }

    private void getDataFromIntent() {

        Intent intentMainact = getIntent();
        int position = intentMainact.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (intentMainact == null) {
            closeOnError();
        } else if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            Log.e("extra position", " " + position);
            closeOnError();
            return;
        } else {
            String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
            String json = sandwiches[position];
            Sandwich sandwich = null;
            try {
//                JsonUtils Class method
                sandwich = JsonUtils.parseSandwichJson(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (sandwich == null) {
                // Sandwich data unavailable
                closeOnError();
                return;
            }

            populateUI(sandwich);
            setTitle(sandwich.getMainName());
//
//            setTitle(replaceMissingString(sandwich.getMainName()));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        String alsoKnownAsFormattedString = "";
        String ingredientsFormattedString = "";
        /* alsoKnownAsTextView = findViewById(R.id.also_known_tv);
         originTextView = findViewById(R.id.origin_tv);
         descriptionTextView = findViewById(R.id.description_tv);
         ingredientsTextView = findViewById(R.id.ingredients_tv);
         ingredientsIv = findViewById(R.id.image_iv);*/

        toolbar.setTitle(sandwich.getMainName().toString());
        Picasso.with(this)
                .load(sandwich.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(ingredientsIv);

//also know as
        if (sandwich.getAlsoKnownAs() !=null && !sandwich.getAlsoKnownAs().isEmpty()){
            for (String string : sandwich.getAlsoKnownAs()) {
                alsoKnownAsFormattedString += string + "\n";
            }
            alsoKnownAsTextView.setText(alsoKnownAsFormattedString);
        } else {
            alsoKnownAsTextView.setText(R.string.no_data_exists);
        }


//        originPlace
        if (!sandwich.getPlaceOfOrigin().isEmpty() && sandwich.getPlaceOfOrigin().length() < 0) {
            originTextView.setText(R.string.no_data_exists);
        } else {
            originTextView.setText(sandwich.getPlaceOfOrigin());
        }

//     description
        if (!sandwich.getDescription().isEmpty() && sandwich.getDescription().length() < 0) {
            descriptionTextView.setText(R.string.no_data_exists);
        } else {
            descriptionTextView.setText(sandwich.getDescription());
        }


//       ingredients

        if (!sandwich.getIngredients().toString().isEmpty() && sandwich.getIngredients()!=null) {

            for (String string : sandwich.getIngredients()) {
                ingredientsFormattedString += string + "\n";
            }

            ingredientsTextView.setText(ingredientsFormattedString);

        } else{

            ingredientsTextView.setText(R.string.no_data_exists);
    }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent backMainAct=new Intent(DetailActivity.this,MainActivity.class);
            startActivity(backMainAct);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}



