package com.pbarnhardt.abm2task1.Adapters;

import static com.pbarnhardt.abm2task1.Utils.Constants.ASSESSMENT_KEY;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Formatting;
import com.pbarnhardt.abm2task1.Views.AssessmentDetailsActivity;
import com.pbarnhardt.abm2task1.Views.AssessmentEditActivity;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.ViewHolder>  {
    /**
     * Variables
     */
    private final List<Assessments> assessmentList;
    private final Context theContext;
    private final RecyclerAdapter recycler;

    private AssessmentSelection assessmentSelected;

    public AssessmentAdapter(List<Assessments> assessmentList, Context context, RecyclerAdapter recycler, AssessmentSelection assessmentSelected) {
        this.assessmentList = assessmentList;
        this.theContext = context;
        this.recycler = recycler;
        this.assessmentSelected = assessmentSelected;
    }

    public interface AssessmentSelection {
        void onAssessmentSelected(int position, Assessments assessment);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //bind views here
        TextView assessmentTitleView = itemView.findViewById(R.id.assessment_title);
        TextView assessmentDatesView = itemView.findViewById(R.id.assessment_dates);
        //bind button
        FloatingActionButton assessmentFloatingActionButton = itemView.findViewById(R.id.assessment_floatingEditButton);
        ImageButton assessmentDetailsButton = itemView.findViewById(R.id.assessmentDetails);
        AssessmentSelection assessmentSelected;

        public ViewHolder(@NonNull View itemView, AssessmentSelection assessmentSelected) {
            super(itemView);
            this.assessmentSelected = assessmentSelected;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            assessmentSelected.onAssessmentSelected(getAdapterPosition(), assessmentList.get(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_list_assessments, parent, false);
        return new ViewHolder(view, assessmentSelected);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.ViewHolder holder, int position) {
        final Assessments assessment = assessmentList.get(position);
        holder.assessmentTitleView.setText(assessment.getAssessmentName());
        holder.assessmentDatesView.setText(Formatting.dateFormat.format(assessment.getAssessmentDueDate()));
        switch(recycler) {
            case MAIN:
                holder.assessmentFloatingActionButton.setImageDrawable(AppCompatResources.getDrawable(theContext, R.drawable.ic_action_edit));
                holder.assessmentDetailsButton.setOnClickListener(v -> {
                    Intent intent = new Intent(theContext, AssessmentDetailsActivity.class);
                    intent.putExtra(ASSESSMENT_KEY, assessment.getAssessmentId());
                    theContext.startActivity(intent);
                });
                holder.assessmentFloatingActionButton.setOnClickListener(v -> {
                    Intent intent = new Intent(theContext, AssessmentEditActivity.class);
                    intent.putExtra(ASSESSMENT_KEY, assessment.getAssessmentId());
                    theContext.startActivity(intent);
                });
                break;
            case CHILD:
                holder.assessmentFloatingActionButton.setImageDrawable(AppCompatResources.getDrawable(theContext, R.drawable.ic_action_delete));
                holder.assessmentFloatingActionButton.setOnClickListener(v -> {
                    if(assessmentSelected != null) {
                        assessmentSelected.onAssessmentSelected(position, assessment);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return assessmentList.size();
    }
}
