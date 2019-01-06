package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class JsonUtils {
    private static final String SANDWHICH_MAIN_NAME="mainName";
    private static final String SANDWHICH_ALSO_KNOWN_AS="alsoKnownAs";
    private static final String SANDWHICH_PLACE_OF_ORIGIN="placeOfOrigin";
    private static final String SANDWHICH_DESCRIPTION="description";
    private static final String SANDWHICH_IMAGE="image";
    private static final String SANDWHICH_INGREDIENTS="ingredients";

    public static Sandwich parseSandwichJson(String json) throws JSONException {
        String placeOfOrigin;
        String image;
        Sandwich sandwichDetail;
        ArrayList stringList = new ArrayList<>();
        JSONObject sandwichJSONObject = new JSONObject(json);

        /* Is there an error ? */
        if (sandwichJSONObject.has("cod")) {
            int errorCode = sandwichJSONObject.getInt("cod");

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }
        sandwichDetail = new Sandwich();
//        JsonObject
        JSONObject nameJSONObject = sandwichJSONObject.getJSONObject("name");
        String mainName = nameJSONObject.getString(SANDWHICH_MAIN_NAME);
//        JsonArray
        JSONArray alsoJSONArray = nameJSONObject.getJSONArray(SANDWHICH_ALSO_KNOWN_AS);
        placeOfOrigin = sandwichJSONObject.getString(SANDWHICH_PLACE_OF_ORIGIN);
        image = sandwichJSONObject.getString(SANDWHICH_IMAGE);
        String description = sandwichJSONObject.getString(SANDWHICH_DESCRIPTION);
//        JsonArray
        JSONArray ingredientsJSONArray = sandwichJSONObject.getJSONArray(SANDWHICH_INGREDIENTS);

        sandwichDetail.setMainName(mainName);
        sandwichDetail.setImage(image);

        for (int i = 0; i< alsoJSONArray.length(); i++) {
            stringList.add(alsoJSONArray.getString(i));
        }
        sandwichDetail.setAlsoKnownAs(stringList);
        sandwichDetail.setPlaceOfOrigin(placeOfOrigin);
        stringList = new ArrayList();
        for (int i = 0; i< ingredientsJSONArray.length(); i++) {
            stringList.add(ingredientsJSONArray.getString(i));
        }
        sandwichDetail.setIngredients(stringList);
        sandwichDetail.setDescription(description);
        return sandwichDetail;
    }

    }

