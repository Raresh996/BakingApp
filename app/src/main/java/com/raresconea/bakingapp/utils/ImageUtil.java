package com.raresconea.bakingapp.utils;

import com.raresconea.bakingapp.R;

import java.util.HashMap;

/**
 * Created by Rares on 5/6/2018.
 */

public class ImageUtil {

    public static final Integer[] recipeImages = {R.drawable.nutella_pie, R.drawable.brownies, R.drawable.yellowcake, R.drawable.cheesecake};

    public static HashMap<String, String> ingredientLinks;

    /*
        I uploaded some images with ingredients on imgur and I
        take them from there using as key the ingredient's name
     */
    static {
        ingredientLinks = new HashMap<>();
        ingredientLinks.put("Graham Cracker crumbs", "https://i.imgur.com/8yUntWo.jpg");
        ingredientLinks.put("unsalted butter, melted", "https://i.imgur.com/UmlVLWT.jpg");
        ingredientLinks.put("granulated sugar", "https://i.imgur.com/69qXjqD.jpg");
        ingredientLinks.put("salt", "https://i.imgur.com/17VXg4O.jpg");
        ingredientLinks.put("vanilla", "https://i.imgur.com/CVM7AAG.jpg");
        ingredientLinks.put("vanilla extract", "https://i.imgur.com/CVM7AAG.jpg");
        ingredientLinks.put("vanilla extract, divided", "https://i.imgur.com/CVM7AAG.jpg");
        ingredientLinks.put("Nutella or other chocolate-hazelnut spread", "https://i.imgur.com/U39UcoO.jpg");
        ingredientLinks.put("Mascapone Cheese(room temperature)", "https://i.imgur.com/esIMRu5.jpg");
        ingredientLinks.put("heavy cream(cold)", "https://i.imgur.com/gvh3oKE.jpg");
        ingredientLinks.put("heavy cream", "https://i.imgur.com/gvh3oKE.jpg");
        ingredientLinks.put("cream cheese(softened)", "https://i.imgur.com/1TA8SID.jpg");
        ingredientLinks.put("cream cheese, softened", "https://i.imgur.com/1TA8SID.jpg");
        ingredientLinks.put("Bittersweet chocolate (60-70% cacao)", "https://i.imgur.com/xj8gSgZ.jpg");
        ingredientLinks.put("unsalted butter", "https://i.imgur.com/Mn20XFc.jpg");
        ingredientLinks.put("light brown sugar", "https://i.imgur.com/ZiplZfx.jpg");
        ingredientLinks.put("vanilla,divided", "https://cdn.cpnscdn.com/static.coupons.com/ext/kitchme/images/recipes/600x400/homemade-vanilla-pudding_6101.jpg");
        ingredientLinks.put("large eggs", "https://i.imgur.com/YX82WI9.jpg");
        ingredientLinks.put("large whole eggs", "https://i.imgur.com/YX82WI9.jpg");
        ingredientLinks.put("all purpose flour", "https://i.imgur.com/8k91NjA.jpg");
        ingredientLinks.put("cocoa powder", "https://i.imgur.com/KbpU3Om.jpg");
        ingredientLinks.put("semisweet chocolate chips", "https://i.imgur.com/CqC6CHa.jpg");
        ingredientLinks.put("sifted cake flour", "https://i.imgur.com/s6fowr4.jpg");
        ingredientLinks.put("baking powder", "https://i.imgur.com/F9OI2wH.jpg");
        ingredientLinks.put("egg yolks", "https://i.imgur.com/szqKQHf.jpg");
        ingredientLinks.put("large egg yolks", "https://i.imgur.com/szqKQHf.jpg");
        ingredientLinks.put("whole milk", "https://i.imgur.com/M2bvz2e.jpg");
        ingredientLinks.put("unsalted butter, softened and cut into 1 in. cubes", "https://i.imgur.com/Fz3AbBJ.jpg");
        ingredientLinks.put("egg whites", "https://i.imgur.com/QhnidPn.jpg");
        ingredientLinks.put("melted and cooled bittersweet or semisweet chocolate", "http://countrycupboardcookies.com/wp-content/uploads/2015/07/melted-chocolate-5.jpg");
    }

    public static String getImageUrl(String key) {
        return ingredientLinks.get(key);
    }
}
