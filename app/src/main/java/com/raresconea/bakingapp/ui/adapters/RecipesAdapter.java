package com.raresconea.bakingapp.ui.adapters;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.raresconea.bakingapp.R;
import com.raresconea.bakingapp.data.models.Recipe;
import com.raresconea.bakingapp.ui.activities.RecipeActivity;
import com.raresconea.bakingapp.ui.activities.StepsActivity;
import com.raresconea.bakingapp.utils.ImageUtil;
import com.raresconea.bakingapp.utils.PrefsUtils;
import com.raresconea.bakingapp.widget.RecipeWidgetProvider;
import com.raresconea.bakingapp.widget.RecipeWidgetService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rares on 5/6/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeHolder> {

    private Context context;
    private List<Recipe> recipes;

    public RecipesAdapter(Context context) {
        this.context = context;
        recipes = new ArrayList<>();
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_row, parent, false);

        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {

        Glide.with(context)
                .load(recipes.get(position).getImage())
                .placeholder(ImageUtil.recipeImages[position])
                .into(holder.recipePoster);

        holder.recipeName.setText(recipes.get(position).getName());
        holder.recipeIngredients.setText(String.valueOf(recipes.get(position).getIngredients().size()));
        holder.recipeSteps.setText(String.valueOf(recipes.get(position).getSteps().size()));

        Recipe recipe = PrefsUtils.loadRecipe(context);

        if (recipe != null) {

            if (isFavorite(recipe, recipes.get(position).getId())) {
                holder.favorite.setImageResource(R.drawable.goldstar);
            } else {
                holder.favorite.setImageResource(R.drawable.blackstar);
            }
        } else {
            holder.favorite.setImageResource(R.drawable.blackstar);
        }

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavorite(recipe, recipes.get(position).getId())) {
                    PrefsUtils.saveRecipe(context, recipes.get(position));
                    holder.favorite.setImageResource(R.drawable.goldstar);
                    Toast.makeText(context, recipes.get(position).getName() + " " + context.getResources().getString(R.string.set_as_favorite), Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                    updateWidget();
                }
            }
        });

        holder.recipeRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StepsActivity.class);
                intent.putExtra(RecipeActivity.RECIPE_SENT, recipes.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void clearRecipes() {
        this.recipes.clear();
        notifyDataSetChanged();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    private boolean isFavorite(Recipe recipe, Integer id) {
        if (recipe == null) {
            return false;
        }

        return recipe.getId().equals(id);
    }

    private void updateWidget() {
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));
        RecipeWidgetProvider myWidget = new RecipeWidgetProvider();
        myWidget.onUpdate(context, AppWidgetManager.getInstance(context),ids);
    }

    class RecipeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipePoster_iv)
        ImageView recipePoster;

        @BindView(R.id.recipeName_tv)
        TextView recipeName;

        @BindView(R.id.recipe_ingredients_tv)
        TextView recipeIngredients;

        @BindView(R.id.recipe_steps_tv)
        TextView recipeSteps;

        @BindView(R.id.recipe_row)
        RelativeLayout recipeRow;

        @BindView(R.id.favorite_iv)
        ImageView favorite;

        public RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
