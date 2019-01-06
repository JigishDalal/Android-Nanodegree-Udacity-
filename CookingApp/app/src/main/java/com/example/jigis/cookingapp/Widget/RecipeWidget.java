package com.example.jigis.cookingapp.Widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.jigis.cookingapp.MainActivity;
import com.example.jigis.cookingapp.Model.Ingredient;
import com.example.jigis.cookingapp.Model.Recipes;
import com.example.jigis.cookingapp.Model.Steps;
import com.example.jigis.cookingapp.Network.ApiClient;
import com.example.jigis.cookingapp.Network.ApiInterface;
import com.example.jigis.cookingapp.R;
import com.google.gson.Gson;


import java.util.List;

import static com.example.jigis.cookingapp.Fragment.HomeFragment.MyPREFERENCES;
import static com.example.jigis.cookingapp.Fragment.HomeFragment.itemnumber;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {
    SharedPreferences prefs;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName, List<Ingredient> ingredients) {

// Construct the RemoteViews object
        RemoteViews  views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.title_recipe, recipeName);
        views.removeAllViews(R.id.widget_ingeredients_container);
        for (Ingredient ingredient : ingredients) {

            RemoteViews ingredientView = new RemoteViews(context.getPackageName(),
                    R.layout.item_widget_list);
            ingredientView.setTextViewText(R.id.ingredient_name_text_view,
                    String.valueOf(ingredient.getIngredient()) + " " +
                            String.valueOf(ingredient.getMeasure()));
            views.addView(R.id.widget_ingeredients_container, ingredientView);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);















/*        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        // Set up the collection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setRemoteAdapter(context, views);
        } else {
            setRemoteAdapterV11(context, views);
        }

//        //Create a new Intent to Launch the DetailActivity when Clicked
//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        // views.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);
//
//        // Instruct the widget manager to update the widget

        appWidgetManager.updateAppWidget(appWidgetId, views);
       appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_stack_view);*/

    }

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
         prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(itemnumber, null);
        Recipes recipes= gson.fromJson(json, Recipes.class);
        String RecipeName=recipes.getName();

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,RecipeName,recipes.getIngredients());
        }
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        super.onReceive(context, intent);
//
//
//    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
/*
//    /**
//     * Sets the remote adapter used to fill in the list items
//     *
//     * @param views RemoteViews to set the RemoteAdapter
//     */
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
//        views.setRemoteAdapter(R.id.widget_stack_view,
//                new Intent(context, ListIngredientWidgetService.class));
//    }
//
//    /**
//     * Sets the remote adapter used to fill in the list items
//     *
//     * @param views RemoteViews to set the RemoteAdapter
//     */
//    @SuppressWarnings("deprecation")
//    private static void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
//        views.setRemoteAdapter(0, R.id.widget_stack_view,
//                new Intent(context, ListIngredientWidgetService.class));
//    }
//


//    private Recipes getRecipe(Context context) {
//        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = prefs.getString(itemnumber, "");
//        return gson.fromJson(json, Recipes.class);
//    }
//


}

