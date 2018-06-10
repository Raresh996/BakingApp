package com.raresconea.bakingapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.raresconea.bakingapp.R;
import com.raresconea.bakingapp.data.models.Recipe;
import com.raresconea.bakingapp.data.models.Step;
import com.raresconea.bakingapp.ui.adapters.IngredientsAdapter;
import com.raresconea.bakingapp.ui.adapters.StepsDescriptionAdapter;
import com.raresconea.bakingapp.ui.fragments.StepsDetailsFragment;
import com.raresconea.bakingapp.widget.RecipeWidgetService;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;

/**
 * Created by Rares on 5/6/2018.
 */

/*
    Activity used for:
    - In case of phone :
        Displays a recyclerview with images of the ingredients
        and the name of every step.
    - In case of table:
        Displays in the left side what the phone does and in the right side the
        fragment which contains the video describing the selected step and it's details
 */
public class StepsActivity extends AppCompatActivity implements StepsDescriptionAdapter.CustomItemClickListener,
                                                                StepsDetailsFragment.Callback {

    private static final String STEP_DETAILS_FRAGMENT = "step_details_fragment";
    private static final String POSITION_SELECTED = "position_selected";

    @Nullable
    @BindView(R.id.ingredients_recyclerView)
    RecyclerView ingredientsRecyclerView;

    @Nullable
    @BindView(R.id.recyclerview_pager_indicator)
    IndefinitePagerIndicator pagerIndicator;

    @BindView(R.id.stepsDescriptionRecyclerView)
    RecyclerView stepsDescriptionRecyclerView;

    @Nullable
    @BindView(R.id.recipeStepsDetailsFragmentLayout)
    FrameLayout recipeStepsDetailsLayout;

    private StepsDetailsFragment stepsDetailsFragment;

    private IngredientsAdapter ingredientsAdapter;

    private Recipe recipe;

    private boolean isTablet;

    private List<Step> steps = new ArrayList<>();

    private StepsDescriptionAdapter stepsDescriptionAdapter;

    private int positionSelected;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        ButterKnife.bind(this);

        if (recipeStepsDetailsLayout != null) {
            isTablet = true;
        }

        if (getIntent().hasExtra(RecipeActivity.RECIPE_SENT)) {
            recipe = getIntent().getParcelableExtra(RecipeActivity.RECIPE_SENT);
            getSupportActionBar().setTitle(recipe.getName());
            steps = recipe.getSteps();
        }


        ingredientsAdapter = new IngredientsAdapter(getApplicationContext());
        ingredientsAdapter.setIngredients(recipe.getIngredients());
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);
        LinearLayoutManager linearLayoutManagerIngredients =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ingredientsRecyclerView.setLayoutManager(linearLayoutManagerIngredients);
        pagerIndicator.attachToRecyclerView(ingredientsRecyclerView);


        stepsDescriptionAdapter = new StepsDescriptionAdapter(this, this);
        stepsDescriptionRecyclerView.setAdapter(stepsDescriptionAdapter);
        LinearLayoutManager linearLayoutManagerSteps =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        stepsDescriptionRecyclerView.setLayoutManager(linearLayoutManagerSteps);
        stepsDescriptionAdapter.setDescriptions(steps);

        /*
            The first step of every recipe should be marked
            as selected when this activity is reach on a tablet
         */
        if (isTablet) {
            stepsDescriptionAdapter.setSelected(0);
        }

        if(savedInstanceState == null) {
            if (recipeStepsDetailsLayout != null) {
                isTablet = true;

                Bundle bundle = new Bundle();
                bundle.putParcelable(StepsDetailsFragment.STEP_SENT, steps.get(0));
                bundle.putInt(StepsDetailsFragment.STEP_CLICKED, 0);
                bundle.putInt(StepsDetailsFragment.NUMBER_OF_STEPS, steps.size());
                bundle.putBoolean(StepsDetailsFragment.IS_TABLET, true);
                stepsDetailsFragment = new StepsDetailsFragment();
                stepsDetailsFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.recipeStepsDetailsFragmentLayout, stepsDetailsFragment, STEP_DETAILS_FRAGMENT).commit();
            }
        }
    }


    @Override
    public void onItemClick(View v, int position) {
        if (isTablet) {
            positionSelected = position;

            Bundle bundle = new Bundle();
            bundle.putParcelable(StepsDetailsFragment.STEP_SENT, steps.get(position));
            bundle.putInt(StepsDetailsFragment.STEP_CLICKED, position);
            bundle.putInt(StepsDetailsFragment.NUMBER_OF_STEPS, steps.size());
            bundle.putBoolean(StepsDetailsFragment.IS_TABLET, true);
            stepsDescriptionAdapter.setSelected(position);
            stepsDetailsFragment = new StepsDetailsFragment();
            stepsDetailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipeStepsDetailsFragmentLayout, stepsDetailsFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putParcelableArrayListExtra(StepDetailsActivity.STEPS_SENT, new ArrayList<>(steps));
            intent.putExtra(StepDetailsActivity.STEP_CLICKED, position);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(POSITION_SELECTED, positionSelected);

        if (isTablet) {
            getSupportFragmentManager().putFragment(outState, STEP_DETAILS_FRAGMENT, stepsDetailsFragment);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (isTablet) {
            stepsDetailsFragment = (StepsDetailsFragment) getSupportFragmentManager().getFragment(savedInstanceState, STEP_DETAILS_FRAGMENT);

            positionSelected = savedInstanceState.getInt(POSITION_SELECTED);
            stepsDescriptionAdapter.setSelected(positionSelected);

            if (stepsDetailsFragment.isAdded()) {
                return;
            }

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipeStepsDetailsFragmentLayout, stepsDetailsFragment)
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (recipeStepsDetailsLayout != null) {
            isTablet = true;
        }
    }

    @Override
    public void onNewStepSelected(int position) {
        positionSelected = position;

        Bundle bundle = new Bundle();
        bundle.putParcelable(StepsDetailsFragment.STEP_SENT, steps.get(position));
        bundle.putInt(StepsDetailsFragment.STEP_CLICKED, position);
        bundle.putInt(StepsDetailsFragment.NUMBER_OF_STEPS, steps.size());
        bundle.putBoolean(StepsDetailsFragment.IS_TABLET, true);
        stepsDescriptionAdapter.setSelected(position);
        stepsDetailsFragment = new StepsDetailsFragment();
        stepsDetailsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipeStepsDetailsFragmentLayout, stepsDetailsFragment)
                .commit();
    }
}
