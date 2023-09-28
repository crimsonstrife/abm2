package com.pbarnhardt.abm2task1.Popups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pbarnhardt.abm2task1.Adapters.AssessmentPopperAdapter;
import com.pbarnhardt.abm2task1.Entity.Assessments;
import com.pbarnhardt.abm2task1.R;

import java.util.List;

public class DropdownAssessments extends PopupWindow {
    /**
     * Variables
     */
    private Context theContext;
    private List<Assessments> assessmentsList;
    private AssessmentPopperAdapter popperAdapter;

    /**
     * Constructor
     * @param theContext Context
     * @param assessmentsList List of assessments
     */
    public DropdownAssessments(Context theContext, List<Assessments> assessmentsList) {
        super(theContext);
        this.theContext = theContext;
        this.assessmentsList = assessmentsList;
        setupView();
    }

    private void setupView() {
        View view = LayoutInflater.from(theContext).inflate(R.layout.menu_popup, null);
        RecyclerView popupRecyclerView = view.findViewById(R.id.popupRecyclerView);
        popupRecyclerView.setHasFixedSize(true);
        popupRecyclerView.setLayoutManager(new LinearLayoutManager(theContext, LinearLayoutManager.VERTICAL, false));
        popupRecyclerView.addItemDecoration(new DividerItemDecoration(theContext, LinearLayoutManager.VERTICAL));
        popperAdapter = new AssessmentPopperAdapter(assessmentsList);
        popupRecyclerView.setAdapter(popperAdapter);
        setContentView(view);
    }

    public void setSelectedAssessmentListener(AssessmentPopperAdapter.AssessmentListener setAssessmentListener) {
        popperAdapter.setAssessmentListener(setAssessmentListener);
    }
}
