package com.raresconea.bakingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.raresconea.bakingapp.data.models.Recipe;

/*
    Here I store the recipe selected as favorite
    so that will be the one displayed in the widget
 */
public class PrefsUtils {

    private static final String PREFS_NAME = "prefs_name";
    private static final String RECIPE_KEY = "recipe_key";

    public static void saveRecipe(Context context, Recipe recipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        prefs.putString(RECIPE_KEY, Recipe.toBase64String(recipe));

        prefs.apply();
    }

    public static Recipe loadRecipe(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String recipeBase64 = prefs.getString(RECIPE_KEY, "");

        return recipeBase64.isEmpty() ? null : Recipe.fromBase64(recipeBase64);
    }

}
