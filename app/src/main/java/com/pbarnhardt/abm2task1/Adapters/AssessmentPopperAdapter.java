package com.pbarnhardt.abm2task1.Adapters;

import static com.pbarnhardt.abm2task1.Enums.Types.OBJECTIVE;
import static com.pbarnhardt.abm2task1.Enums.Types.PERFOREMANCE;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.R;

import org.w3c.dom.Entity;

import java.util.List;

public class AssessmentPopperAdapter extends RecyclerView.Adapter<AssessmentPopperAdapter.ViewHolder>{
    private List<Assessments> assessmentsList;
    private AssessmentListener assessmentListener;

    public AssessmentPopperAdapter(List<Assessments> assessmentsList) {
        super();
        this.assessmentsList = assessmentsList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        TextView itemType;
        ImageView assessmentItemAddIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemType = itemView.findViewById(R.id.itemType);
            assessmentItemAddIcon = itemView.findViewById(R.id.assessmentItemAddIcon);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_assessment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Assessments assessment = assessmentsList.get(position);
        String assessmentType = "";
        //set assessment type variable based on the enum result of assessment.getAssessmentType()
        switch(assessment.getAssessmentType()) {
            case OBJECTIVE:
                assessmentType = "OA";
                break;
            case PERFOREMANCE:
                assessmentType = "PA";
                break;
        }
        holder.itemTitle.setText(assessment.getAssessmentName());
        holder.itemType.setText(assessmentType);
        holder.itemView.setOnClickListener(view -> {
            if(assessmentListener != null) {
                assessmentListener.onAssessmentSelected(position, assessment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assessmentsList.size();
    }

    public interface AssessmentListener {
        void onAssessmentSelected(int position, Assessments assessment);
    }
}
