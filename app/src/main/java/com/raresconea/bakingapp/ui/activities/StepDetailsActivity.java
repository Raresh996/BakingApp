package com.raresconea.bakingapp.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.raresconea.bakingapp.R;
import com.raresconea.bakingapp.data.models.Step;
import com.raresconea.bakingapp.ui.fragments.StepsDetailsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
    This activity is reachable only on a phone and it displays
    the fragment which contains the video describing the step
    and it's details
 */
public class StepDetailsActivity extends AppCompatActivity implements StepsDetailsFragment.Callback {

    public static final String STEP_CLICKED = "step_clicked_step_details_activity";
    public static final String STEPS_SENT = "steps_sent_step_details_activity";
    private static final String STEP_DETAILS_FRAGMENT = "step_details_fragment";

    private StepsDetailsFragment stepsDetailsFragment;

    List<Step> steps;

    Integer position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        ButterKnife.bind(this);

        if (getIntent().hasExtra(STEPS_SENT)) {
            steps = getIntent().getParcelableArrayListExtra(STEPS_SENT);
            position = getIntent().getIntExtra(STEP_CLICKED, 0);

            Bundle bundle = new Bundle();
            bundle.putParcelable(StepsDetailsFragment.STEP_SENT, steps.get(position));
            bundle.putInt(StepsDetailsFragment.STEP_CLICKED, position);
            bundle.putInt(StepsDetailsFragment.NUMBER_OF_STEPS, steps.size());
            bundle.putBoolean(StepsDetailsFragment.IS_TABLET, false);

            stepsDetailsFragment = new StepsDetailsFragment();
            stepsDetailsFragment.setArguments(bundle);

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipeStepsDetailsFragmentLayout, stepsDetailsFragment)
                        .commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState,STEP_DETAILS_FRAGMENT, stepsDetailsFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        stepsDetailsFragment = (StepsDetailsFragment) getSupportFragmentManager().getFragment(savedInstanceState,STEP_DETAILS_FRAGMENT);

        if(stepsDetailsFragment.isAdded()) {
            return;
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipeStepsDetailsFragmentLayout, stepsDetailsFragment)
                .commit();
    }

    @Override
    public void onNewStepSelected(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(StepsDetailsFragment.STEP_SENT, steps.get(position));
        bundle.putInt(StepsDetailsFragment.STEP_CLICKED, position);
        bundle.putInt(StepsDetailsFragment.NUMBER_OF_STEPS, steps.size());
        bundle.putBoolean(StepsDetailsFragment.IS_TABLET, false);

        stepsDetailsFragment = new StepsDetailsFragment();
        stepsDetailsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipeStepsDetailsFragmentLayout, stepsDetailsFragment)
                .commit();
    }
}
