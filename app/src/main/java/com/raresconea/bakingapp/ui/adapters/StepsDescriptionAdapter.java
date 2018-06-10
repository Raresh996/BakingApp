package com.raresconea.bakingapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.raresconea.bakingapp.R;
import com.raresconea.bakingapp.data.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rares on 5/11/2018.
 */

public class StepsDescriptionAdapter extends RecyclerView.Adapter<StepsDescriptionAdapter.StepDescriptionHolder>{

    private Context context;
    private List<Step> steps;
    private int selected;

    CustomItemClickListener customItemClickListener;

    public StepsDescriptionAdapter(Context context, CustomItemClickListener customItemClickListener) {
        this.context = context;
        steps = new ArrayList<>();
        this.customItemClickListener = customItemClickListener;
        this.selected = -1;
    }

    @Override
    public StepDescriptionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.steps_description_row, parent, false);
        StepsDescriptionAdapter.StepDescriptionHolder holder = new StepDescriptionHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customItemClickListener.onItemClick(view, holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(StepDescriptionHolder holder, int position) {
        holder.stepDescription.setText(steps.get(position).getShortDescription());

        if (steps.get(position).getVideoURL().isEmpty()){
            holder.play.setVisibility(View.GONE);
        } else {
            holder.play.setVisibility(View.VISIBLE);
        }

        if (selected == position) {
            holder.stepDescription.setBackgroundColor(context.getResources().getColor(R.color.selected));
        } else {
            holder.stepDescription.setBackgroundColor(context.getResources().getColor(R.color.colorBackground));
        }
    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void setDescriptions(List<Step> descriptions) {
        this.steps = descriptions;
        notifyDataSetChanged();
    }

    public void clearDescriptions() {
        this.steps.clear();
        notifyDataSetChanged();
    }

    class StepDescriptionHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_step_description)
        TextView stepDescription;

        @BindView(R.id.playIv)
        ImageView play;

        public StepDescriptionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface CustomItemClickListener {
        public void onItemClick(View v, int position);
    }
}
