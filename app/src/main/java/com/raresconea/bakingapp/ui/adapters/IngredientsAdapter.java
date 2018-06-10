package com.raresconea.bakingapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.raresconea.bakingapp.R;
import com.raresconea.bakingapp.data.models.Ingredient;
import com.raresconea.bakingapp.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rares on 5/10/2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsHolder> {

    private Context context;
    private List<Ingredient> ingredients;

    public IngredientsAdapter(Context context) {
        this.context = context;
        ingredients = new ArrayList<>();
    }

    @Override
    public IngredientsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_row, parent, false);

        return new IngredientsAdapter.IngredientsHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsHolder holder, int position) {

        Ingredient ingredient = ingredients.get(position);

        Glide.with(context)
                .load(ImageUtil.getImageUrl(ingredient.getIngredient()))
                .placeholder(R.drawable.no_ingredient_image)
                .into(holder.ingredientPoster);

        holder.ingredientName.setText(ingredient.getIngredient());
        holder.ingredientsQuantity.setText(String.valueOf(ingredient.getQuantity()));
        holder.ingredientsMeasure.setText(ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    public void clearIngredients() {
        this.ingredients.clear();
        notifyDataSetChanged();
    }

    class IngredientsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredientPoster_iv)
        ImageView ingredientPoster;

        @BindView(R.id.ingredientName_tv)
        TextView ingredientName;

        @BindView(R.id.ingredients_measure_tv)
        TextView ingredientsMeasure;

        @BindView(R.id.ingredient_quantity_tv)
        TextView ingredientsQuantity;

        public IngredientsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
