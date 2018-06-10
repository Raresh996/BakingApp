package com.raresconea.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.raresconea.bakingapp.assertion.RecyclerViewItemCountAssertion;
import com.raresconea.bakingapp.ui.activities.RecipeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeActivityUITest {

    @Rule
    public ActivityTestRule<RecipeActivity> activityTestRule =
            new ActivityTestRule<>(RecipeActivity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        idlingResource = activityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    /* Test for phone and tablet */
    @Test
    public void clickOnRecipeRecyclerViewItem_opensStepsActivity() {

        /*
            Check that when there is a click event on a recipe
            the app goes to another activity where a new recyclerview
            is displayed
         */

        onView(withId(R.id.recipesRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.stepsDescriptionRecyclerView))
                .check(matches(isDisplayed()));
    }

    /* Test for phone and tablet */
    @Test
    public void onRecipeRecyclerView_countNumberOfItems() {
        /* Check if 4 recipes are displayed */
        onView(withId(R.id.recipesRecyclerView)).check(new RecyclerViewItemCountAssertion(4));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

}
