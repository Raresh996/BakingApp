package com.raresconea.bakingapp.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.raresconea.bakingapp.R;
import com.raresconea.bakingapp.api.RecipeClient;
import com.raresconea.bakingapp.api.RecipeService;
import com.raresconea.bakingapp.data.idlingresource.RecipeIdlingResource;
import com.raresconea.bakingapp.data.models.Recipe;
import com.raresconea.bakingapp.ui.adapters.RecipesAdapter;
import com.raresconea.bakingapp.utils.NetworkUtil;
import com.raresconea.bakingapp.utils.PrefsUtils;
import com.raresconea.bakingapp.widget.RecipeWidgetService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/*
    This activity is the one seen when the app
    opens
 */
public class RecipeActivity extends AppCompatActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.recipesRecyclerView)
    RecyclerView recipesRecyclerView;

    @BindView(R.id.noWifiIv)
    ImageView noWifi;

    private RecipesAdapter recipesAdapter;

    private BroadcastReceiver receiver;
    IntentFilter intentFilter;

    public static final String RECIPE_SENT = "recipe_sent";
    final String TABLET = "tablet";
    boolean isConnected;

    private boolean isTablet;

    private RecipeIdlingResource recipeIdlingResource = new RecipeIdlingResource();

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        return recipeIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle(R.string.app_name);

        if (recipesRecyclerView.getTag().equals(TABLET)) {
            isTablet = true;
        }

        recipesAdapter = new RecipesAdapter(this);
        recipesRecyclerView.setAdapter(recipesAdapter);

        if (isTablet) {
            recipesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                recipesRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            } else {
                recipesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            }
        }

        registerInternetReceiver();
    }

    /*
        Receiver used to check if the connectivity
        state of the app changes and make specific changes
        to the UI
     */
    private void registerInternetReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isConnected = NetworkUtil.isNetworkConnection(context);

                if (isConnected) {
                    /*
                        If there is internet connection but the list
                        with recipes in the adapter does not have any
                        element then download the recipes
                    */
                    if (recipesAdapter.getItemCount() == 0) {
                        loadRecipes();
                    }
                } else {
                    /*
                        If there is not internet connection
                        and the list with recipes in the adapter
                        does not have any element then display
                        a photo suggesting the app is not connected
                        to internet or the connection is slow
                     */
                    if (recipesAdapter.getItemCount() == 0) {
                        setViewsForNoInternetConnection();
                    }
                }
            }
        };

        intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(receiver, intentFilter);
    }

    /*
        Method used to download the recipes
     */
    private void loadRecipes() {
        setViewsForInternetConnection();
        RecipeService recipeService = RecipeClient.getClient().create(RecipeService.class);



        recipeService.getRecipes()
                .doOnSubscribe(disposable -> {
                    progressBar.setVisibility(View.VISIBLE);
                    if (recipeIdlingResource != null) {
                        recipeIdlingResource.setIdleState(false);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recipes -> {
                    recipesAdapter.clearRecipes();
                    recipesAdapter.setRecipes(recipes);
                    progressBar.setVisibility(View.INVISIBLE);

                    if (recipeIdlingResource != null) {
                        recipeIdlingResource.setIdleState(true);
                    }
                });
    }

    /*
        When there is not internet connection
        an imageview with a suggesting image
        should be the only view displayed in the UI
     */
    private void setViewsForNoInternetConnection() {
        noWifi.setVisibility(View.VISIBLE);
        recipesRecyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    /*
        When there is internet connection the recyclerView
        and the progressbar will be visible
     */
    private void setViewsForInternetConnection() {
        noWifi.setVisibility(View.INVISIBLE);
        recipesRecyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (recipesAdapter.getItemCount() == 0 && isConnected) {
            setViewsForInternetConnection();
            loadRecipes();
        } else if (recipesAdapter.getItemCount() != 0){
            setViewsForInternetConnection();
        } else {
            setViewsForNoInternetConnection();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
