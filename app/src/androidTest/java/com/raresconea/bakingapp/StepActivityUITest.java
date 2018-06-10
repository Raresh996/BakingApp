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
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class StepActivityUITest {

    @Rule
    public ActivityTestRule<RecipeActivity> activityTestRule =
            new ActivityTestRule<>(RecipeActivity.class);

    private IdlingResource idlingResource;

    @Before
    public void registerIdlingResource() {
        idlingResource = activityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    /* Test for both phone an tablet */
    @Test
    public void stepsDescriptionRecyclerView_countNumberOfSteps() {

        /*
            Check if the second recipe has 10 steps
        */
        onView(withId(R.id.recipesRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.stepsDescriptionRecyclerView)).check(new RecyclerViewItemCountAssertion(10));
    }

    /* Test for phone */
    @Test
    public void clickOnStepsDescriptionRecyclerView_opensStepDetailsActivity() {
        /*
            Check if after a click on the second recipe and another on the
            second step the textview that displays the number of step is displayed
        */
        onView(withId(R.id.recipesRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.stepsDescriptionRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.currentStep))
                .check(matches(isDisplayed()));
    }

    /* Test for tablet */
    @Test
    public void clickOnStepsDescriptionRecyclerView_stepDetailsFragmentInfoChanges() {
        /*
            Check if after a click on the second recipe and another on the
            third step the string in  the textview that displays the number of step
            starts with 3
        */
        onView(withId(R.id.recipesRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.stepsDescriptionRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(withId(R.id.currentStep))
                .check(matches(withText(containsString("3"))));
    }

    /* Test for phone and tablet */
    @Test
    public void clickOnStepsDescriptionRecyclerViewOnStepWithVideo_exoPlayerIsDisplayed() {
        /*
            Check that after a click on the second recipe and then a click on a step
            that has a video tutorial the exoplayer that loads this video is diplayed
         */
        onView(withId(R.id.recipesRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.stepsDescriptionRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(withId(R.id.exoPlayerView))
                .check(matches(isDisplayed()));
    }

    /* Test for phone and tablet */
    @Test
    public void clickOnStepsDescriptionRecyclerViewOnStepWithoutVideo_noVideoImageViewIsDisplayed() {
        /*
            Check that after a click on the second recipe and then a click on a step
            that has not a video tutorial the imageview which tells that there
            is no video tutorial is diplayed
         */
        onView(withId(R.id.recipesRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.stepsDescriptionRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.noVideoIv))
                .check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

}
